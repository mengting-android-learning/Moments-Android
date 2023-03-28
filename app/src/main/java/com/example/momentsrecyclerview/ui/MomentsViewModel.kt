package com.example.momentsrecyclerview.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.momentsrecyclerview.data.NetworkTweetsRepository
import com.example.momentsrecyclerview.data.NetworkUserInfoRepository
import com.example.momentsrecyclerview.data.TweetsRepository
import com.example.momentsrecyclerview.data.UserInfoRepository
import com.example.momentsrecyclerview.data.domain.Tweet
import com.example.momentsrecyclerview.data.domain.UserInfo
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }

class MomentsViewModel(
    private val userInfoRepository: UserInfoRepository,
    private val tweetsRepository: TweetsRepository
) : ViewModel() {

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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MomentsViewModel(
                    NetworkUserInfoRepository(UserInfoNetwork.userInfo),
                    NetworkTweetsRepository(TweetsListNetwork.tweets)
                )
            }
        }
    }
}
