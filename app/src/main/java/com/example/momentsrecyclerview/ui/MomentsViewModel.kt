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
                val userInfoVal = userInfoRepository.getUserInfo()
                val tweetsListVal = tweetsRepository.getTweetsList()
                _userInfo.value = userInfoVal
                _tweetsList.value = tweetsListVal
                _status.value = STATUS.DONE
            } catch (e: Exception) {
                _status.value = STATUS.ERROR
            }
        }
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
