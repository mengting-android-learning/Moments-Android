package com.example.momentsrecyclerview.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.momentsrecyclerview.data.NetworkTweetsRepository
import com.example.momentsrecyclerview.data.NetworkUserInfoRepository
import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.data.source.local.MomentsDatabase
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.domain.mapper.local.asDomainTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalImage
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweet
import com.example.momentsrecyclerview.domain.mapper.local.toLocalTweetComment
import com.example.momentsrecyclerview.domain.mapper.local.toLocalUser
import com.example.momentsrecyclerview.domain.mapper.network.asDomainModel
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }

class MomentsViewModel(
    application: Application,
    private val userInfoRepository: UserInfoRepository,
    private val tweetsRepository: TweetsRepository
) : AndroidViewModel(application) {

    private val _userInfo = MutableLiveData<UserInfo>()

    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    private val _tweetsList = MutableLiveData<List<Tweet>>()
    val tweetsList: LiveData<List<Tweet>>
        get() = _tweetsList

    private val _status = MutableLiveData<STATUS>()

    val status: LiveData<STATUS>
        get() = _status

    private val db = MomentsDatabase.getInstance(application).momentsDatabaseDao

    init {
        getData()
    }

    fun refreshData() {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _status.value = STATUS.LOADING
            try {
                val userInfoVal = userInfoRepository.getNetworkUserInfo().asDomainModel()
                val tweetsListVal = tweetsRepository.getNetworkTweetsList().asDomainModel()
                _userInfo.value = userInfoVal
                _tweetsList.value = tweetsListVal
                _status.value = STATUS.DONE
                insertData(userInfoVal, tweetsListVal)
            } catch (e: Exception) {
                _status.value = STATUS.ERROR
            }
        }
    }

    private suspend fun insertData(userInfo: UserInfo, tweets: List<Tweet>) {
        db.insertUser(userInfo.toLocalUser())
        for (tweet in tweets) {
            val senderId = db.insertUser(tweet.sender.toLocalUser())
            val tweetId = db.insertTweet(tweet.toLocalTweet(senderId))
            if (tweet.comments?.isNotEmpty() == true) {
                for (comment in tweet.comments) {
                    val commentSenderId = db.insertUser(comment.sender.toLocalUser())
                    db.insertTweetComment(comment.toLocalTweetComment(commentSenderId, tweetId))
                }
            }
            if (tweet.images?.isNotEmpty() == true) {
                db.insertImage(tweet.images.map { it.toLocalImage(tweetId) })
            }
        }
//        _tweetsList.value = db.loadTweets().map { it.asDomainTweet() }
    }
}

class MomentsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MomentsViewModel(
            application,
            NetworkUserInfoRepository(UserInfoNetwork.userInfo),
            NetworkTweetsRepository(TweetsListNetwork.tweets)
        ) as T
    }
}
