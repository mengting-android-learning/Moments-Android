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
import com.example.momentsrecyclerview.data.source.network.request.NewCommentRequest
import com.example.momentsrecyclerview.data.source.network.request.NewTweetRequest
import com.example.momentsrecyclerview.data.source.network.request.NewUserRequest
import com.example.momentsrecyclerview.domain.ImageUrl
import com.example.momentsrecyclerview.domain.Sender
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.github.network.GitNetwork
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
//        saveGitData()
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
                        content = _localContent.value.takeIf { !_localContent.value.isNullOrBlank() },
                        createdOn = System.currentTimeMillis(),
                        images = if (!_localImages.value.isNullOrEmpty()) {
                            _localImages.value?.map { image -> ImageUrl(image) }
                        } else {
                            null
                        },
                        sender = Sender(
                            id = it.id,
                            userName = it.userName,
                            nick = it.nick,
                            avatarUrl = it.avatarUrl,
                        ),
                    )
                    _tweetsList.value = listOf(tweet) + (_tweetsList.value ?: emptyList())
                    if (saveNewTweet(tweet)) {
                        setLocalContent("")
                        setLocalImages(emptyList())
                    }
                }
            }
        }
    }

    private suspend fun fetchOnlyLocalTweets(): List<Tweet>? =
        if (localTweetsRepo is LocalTweetsRepository) {
            localTweetsRepo.getLocalTweets()
        } else {
            null
        }

    private suspend fun fetchLocalTweets(): List<Tweet>? =
        try {
            localTweetsRepo.getTweetsList()
        } catch (e: Exception) {
            Log.d("FetchLocalExp", e.toString())
            null
        }

    private suspend fun fetchLocalUserInfo(): UserInfo? =
        try {
            localUserInfoRepo.getUserInfo()
        } catch (e: java.lang.Exception) {
            Log.d("FetchLocalExp", e.toString())
            null
        }

    private suspend fun fetchRemoteTweets(): List<Tweet>? =
        try {
            remoteTweetsRepo.getTweetsList()
        } catch (e: Exception) {
            Log.d("FetchRemoteExp", e.toString())
            null
        }

    private suspend fun fetchRemoteUserInfo(): UserInfo? =
        try {
            remoteUserInfoRepo.getUserInfo()
        } catch (e: java.lang.Exception) {
            Log.d("FetchRemoteExp", e.toString())
            null
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
            _userInfo.value?.let { localUserInfoRepo.saveUserInfo(it) }
        } catch (e: Exception) {
            Log.w("SaveDataToLocal", e.toString())
        }

    private suspend fun saveNewTweet(tweet: Tweet): Boolean = try {
        remoteTweetsRepo.saveNewTweet(tweet, _userInfo.value!!.id)
        true
    } catch (e: Exception) {
        Log.w("AddTweetToRemoteExp", e.toString())
        try {
            localTweetsRepo.saveNewTweet(tweet, _userInfo.value!!.id)
            true
        } catch (e: Exception) {
            Log.w("AddTweetToLocalExp", e.toString())
            false
        }
    }

    private fun saveGitData() {
        viewModelScope.launch {
            _status.value = STATUS.LOADING
            val user = GitNetwork.service.getUserInfo()
            UserInfoNetwork.userInfo.saveUser(
                NewUserRequest(
                    user.profileImageUrl,
                    user.avatarUrl,
                    user.nick,
                    user.userName
                )
            )
            val tweetsList = GitNetwork.service.getTweetsList()
                .filter { it.sender != null }.filter { !(it.images == null && it.content == null) }
                .asReversed()
            for (tweet in tweetsList) {
                val saveUser = UserInfoNetwork.userInfo.saveUser(
                    NewUserRequest(
                        null,
                        tweet.sender!!.avatarUrl,
                        tweet.sender.nick,
                        tweet.sender.userName
                    )
                )
                val saveNewTweet = TweetsListNetwork.tweets.saveNewTweet(
                    NewTweetRequest(
                        saveUser.id,
                        tweet.content,
                        System.currentTimeMillis(),
                        tweet.images
                    )
                )
                if (!tweet.comments.isNullOrEmpty()) {
                    for (comment in tweet.comments) {
                        val saveSender = UserInfoNetwork.userInfo.saveUser(
                            NewUserRequest(
                                null,
                                comment.sender.avatarUrl,
                                comment.sender.nick,
                                comment.sender.userName
                            )
                        )
                        TweetsListNetwork.tweets.saveNewComment(
                            saveNewTweet.id,
                            NewCommentRequest(
                                saveSender.id,
                                System.currentTimeMillis(),
                                comment.content
                            )
                        )
                    }
                }
            }
            getData()
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
