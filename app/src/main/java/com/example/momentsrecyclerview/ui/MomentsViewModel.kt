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

    init {
        getData()
    }

    fun refreshData() {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            var fetchRemoteDataSuccess = false
            _status.value = STATUS.LOADING
            try {
                fetchRemoteData()
                _status.value = STATUS.DONE
                fetchRemoteDataSuccess = true
            } catch (e: Exception) {
                Log.d("FetchRemoteDataExp", e.toString())
                try {
                    fetchLocalData()
                    _status.value = STATUS.DONE
                } catch (e: Exception) {
                    Log.d("FetchLocalDataExp", e.toString())
                    _status.value = STATUS.ERROR
                }
            }
            if (fetchRemoteDataSuccess) {
                try {
                    saveDataToLocal()
                } catch (e: Exception) {
                    Log.d("SaveDataToLocalExp", e.toString())
                }
            }
        }
    }

    private suspend fun saveDataToLocal() {
        _userInfo.value?.let {
            localUserInfoRepo.saveUserInfo(it)
        }
        if (localTweetsRepo is LocalTweetsRepository) {
            _tweetsList.value?.let {
                localTweetsRepo.saveTweets(it)
            }
        }
    }

    private suspend fun fetchRemoteData() {
        val userInfoVal = remoteUserInfoRepo.getUserInfo()
        val tweetsListVal = remoteTweetsRepo.getTweetsList()
        _userInfo.value = userInfoVal
        _tweetsList.value = tweetsListVal
    }

    private suspend fun fetchLocalData() {
        val userInfoVal = _userInfo.value ?: localUserInfoRepo.getUserInfo()
        val tweetsListVal = _tweetsList.value ?: localTweetsRepo.getTweetsList()
        _userInfo.value = userInfoVal
        _tweetsList.value = tweetsListVal
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
