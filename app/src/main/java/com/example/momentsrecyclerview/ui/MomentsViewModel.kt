package com.example.momentsrecyclerview.ui

import android.app.Application
import android.net.Uri
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

    private val _localImages = MutableLiveData<List<String>?>(emptyList())
    val localImages: LiveData<List<String>?>
        get() = _localImages

    fun setLocalImages(imageUris: List<Uri>) {
        val uris = imageUris.map { uri -> uri.toString() }
        _localImages.value = if (_localImages.value.isNullOrEmpty()) {
            uris
        } else {
            _localImages.value!!.plus(uris)
        }
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
            val errorData: Pair<List<Tweet>, UserInfo?> = Pair(emptyList(), null)
            val localData = fetchLocalData()
            val remoteData = fetchRemoteData()
            if (localData == errorData && remoteData == errorData) {
                _status.value = STATUS.ERROR
            } else {
                _tweetsList.value = localData.first.union(remoteData.first).toList()
                _userInfo.value = localData.second ?: remoteData.second
                _status.value = STATUS.DONE
                saveDataToLocal()
            }
        }
    }

    fun saveNewTweet(text: String) {
        viewModelScope.launch {
            _userInfo.value?.let {
                val tweet = Tweet(
                    content = text,
                    images = null,
                    sender = Sender(
                        userName = it.userName,
                        nick = it.nick,
                        avatarUrl = it.avatarUrl
                    ),
                    comments = null
                )
                val currentList = _tweetsList.value ?: emptyList()
                _tweetsList.value = listOf(tweet) + currentList
                try {
                    remoteTweetsRepo.saveNewTweet(tweet)
                } catch (e: Exception) {
                    Log.w("AddTweetToRemoteExp", e.toString())
                    try {
                        localTweetsRepo.saveNewTweet(tweet)
                    } catch (e: Exception) {
                        Log.w("AddTweetToLocalExp", e.toString())
                    }
                }
            }
        }
    }

    private suspend fun fetchLocalData(): Pair<List<Tweet>, UserInfo?> {
        var localData: Pair<List<Tweet>, UserInfo?> = Pair(emptyList(), null)
        try {
            val userInfoVal = _userInfo.value ?: localUserInfoRepo.getUserInfo()
            val tweetsListVal =
                _tweetsList.value ?: localTweetsRepo.getTweetsList()
            localData = Pair(tweetsListVal, userInfoVal)
        } catch (e: Exception) {
            Log.w("FetchLocalExp", e.toString())
        }
        return localData
    }

    private suspend fun fetchRemoteData(): Pair<List<Tweet>, UserInfo?> {
        var remoteData: Pair<List<Tweet>, UserInfo?> = Pair(emptyList(), null)
        try {
            val userInfoVal = remoteUserInfoRepo.getUserInfo()
            val tweetsListVal = remoteTweetsRepo.getTweetsList()
            remoteData = Pair(tweetsListVal, userInfoVal)
        } catch (e: Exception) {
            Log.w("FetchRemoteExp", e.toString())
        }
        return remoteData
    }

    private suspend fun saveDataToLocal() {
        try {
            if (localTweetsRepo is LocalTweetsRepository) {
                _tweetsList.value?.let {
                    localTweetsRepo.saveTweets(it)
                }
            }
            _userInfo.value?.let {
                localUserInfoRepo.saveUserInfo(it)
            }
        } catch (e: Exception) {
            Log.w("SaveDataToLocalExp", e.toString())
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
