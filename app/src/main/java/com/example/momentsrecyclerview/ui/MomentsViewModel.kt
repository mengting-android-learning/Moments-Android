package com.example.momentsrecyclerview.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.momentsrecyclerview.data.LocalTweetsRepository
import com.example.momentsrecyclerview.data.LocalUserInfoRepository
import com.example.momentsrecyclerview.data.NetworkTweetsRepository
import com.example.momentsrecyclerview.data.NetworkUserInfoRepository
import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.data.source.local.MomentsDatabase
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }
class MomentsViewModel(
    application: Application,
    private val remoteUserInfoRepo: UserInfoRepository,
    private val remoteTweetsRepo: TweetsRepository,
    private val localUserInfoRepo: UserInfoRepository,
    private val localTweetsRepo: TweetsRepository
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

    private val _localImages = MutableLiveData<List<String>>(emptyList())
    val localImages: LiveData<List<String>>
        get() = _localImages

    fun setLocalImages(imageUris: List<String>) {
        _localImages.value = imageUris
    }

    private val _localContent = MutableLiveData<String>("")
    val localContent: LiveData<String>
        get() = _localContent

    fun setLocalContent(text: String) {
        _localContent.value = text
    }

    init {
        getData()
    }

    fun refreshData() {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            _status.value = STATUS.LOADING
            val tweetsVal: List<Tweet>?
            val remoteTweets: List<Tweet>? = fetchRemoteTweets()
            var localOnlyTweets: List<Tweet>? = emptyList()
            tweetsVal = if (!remoteTweets.isNullOrEmpty()) {
                localOnlyTweets = fetchOnlyLocalTweets()
                (localOnlyTweets ?: emptyList()) + remoteTweets
            } else {
                fetchLocalTweets()
            }
            val userInfoVal: UserInfo? = fetchLocalUserInfo() ?: fetchRemoteUserInfo()
            if (tweetsVal.isNullOrEmpty() && userInfoVal == null) {
                _status.value = STATUS.ERROR
            } else {
                userInfoVal?.let { _userInfo.value = it }
                tweetsVal?.let { _tweetsList.value = it }
                _status.value = STATUS.DONE
            }
            saveUserInfoToLocal()
            if (!remoteTweets.isNullOrEmpty()) {
                saveTweetsToLocal(remoteTweets, localOnlyTweets)
            }
        }
    }

    fun createAndSaveTweet() {
        viewModelScope.launch {
            if (_localImages.value != null || _localContent.value != null) {
                _userInfo.value?.let {
                    val tweet = Tweet(
                        content = if (_localContent.value?.isNotBlank() == true) {
                            _localContent.value
                        } else {
                            null
                        },
                        images = if (_localImages.value?.isNotEmpty() == true) {
                            _localImages.value?.map { image -> ImageUrl(image) }
                        } else {
                            null
                        },
                        sender = Sender(
                            userName = it.userName,
                            nick = it.nick,
                            avatarUrl = it.avatarUrl
                        ),
                    )
                    val currentList = _tweetsList.value ?: emptyList()
                    _tweetsList.value = listOf(tweet) + currentList
                    if (saveNewTweet(tweet)) {
                        setLocalContent("")
                        setLocalImages(emptyList())
                    }
                }
            }
        }
    }

    private suspend fun fetchOnlyLocalTweets(): List<Tweet>? {
        var tweets: List<Tweet>? = null
        if (localTweetsRepo is LocalTweetsRepository) {
            tweets = localTweetsRepo.getLocalTweets()
        }
        return tweets
    }

    private suspend fun fetchLocalTweets(): List<Tweet>? {
        var tweets: List<Tweet>? = null
        try {
            tweets = localTweetsRepo.getTweetsList()
        } catch (e: Exception) {
            Log.d("FetchLocalExp", e.toString())
        }
        return tweets
    }

    private suspend fun fetchLocalUserInfo(): UserInfo? {
        var userInfo: UserInfo? = null
        try {
            userInfo = localUserInfoRepo.getUserInfo()
        } catch (e: java.lang.Exception) {
            Log.d("FetchLocalExp", e.toString())
        }
        return userInfo
    }

    private suspend fun fetchRemoteTweets(): List<Tweet>? {
        var tweets: List<Tweet>? = null
        try {
            tweets = remoteTweetsRepo.getTweetsList()
        } catch (e: Exception) {
            Log.d("FetchRemoteExp", e.toString())
        }
        return tweets
    }

    private suspend fun fetchRemoteUserInfo(): UserInfo? {
        var userInfo: UserInfo? = null
        try {
            userInfo = remoteUserInfoRepo.getUserInfo()
        } catch (e: java.lang.Exception) {
            Log.d("FetchRemoteExp", e.toString())
        }
        return userInfo
    }

    private suspend fun saveTweetsToLocal(remoteTweets: List<Tweet>, localTweets: List<Tweet>?) {
        try {
            if (localTweetsRepo is LocalTweetsRepository) {
                _tweetsList.value?.let {
                    localTweetsRepo.saveTweets(remoteTweets, localTweets)
                }
            }
        } catch (e: Exception) {
            Log.w("SaveDataToLocalExp", e.toString())
        }
    }

    private suspend fun saveUserInfoToLocal() =
        try {
            _userInfo.value?.let { it1 -> localUserInfoRepo.saveUserInfo(it1) }
        } catch (e: Exception) {
            Log.w("SaveDataToLocal", e.toString())
        }

    private suspend fun saveNewTweet(tweet: Tweet): Boolean = try {
        remoteTweetsRepo.saveNewTweet(tweet)
        true
    } catch (e: Exception) {
        Log.w("AddTweetToRemoteExp", e.toString())
        try {
            localTweetsRepo.saveNewTweet(tweet)
            true
        } catch (e: Exception) {
            Log.w("AddTweetToLocalExp", e.toString())
            false
        }
    }
}

class MomentsViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dataSource = MomentsDatabase.getInstance(application).momentsDatabaseDao
        return MomentsViewModel(
            application,
            NetworkUserInfoRepository(UserInfoNetwork.userInfo),
            NetworkTweetsRepository(TweetsListNetwork.tweets),
            LocalUserInfoRepository(dataSource),
            LocalTweetsRepository(dataSource)
        ) as T
    }
}
