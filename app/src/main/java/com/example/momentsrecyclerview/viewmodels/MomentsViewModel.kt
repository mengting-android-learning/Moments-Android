package com.example.momentsrecyclerview.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.momentsrecyclerview.data.Tweet
import com.example.momentsrecyclerview.data.UserInfo
import com.example.momentsrecyclerview.data.source.network.TweetsListNetwork
import com.example.momentsrecyclerview.data.source.network.UserInfoNetwork
import com.example.momentsrecyclerview.data.source.network.asDomainModel
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }

class MomentsViewModel : ViewModel() {

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
                val userInfoVal = UserInfoNetwork.userInfo.getUserInfo().asDomainModel()
                val tweetsListVal = TweetsListNetwork.tweets.getTweetsList().asDomainModel()
                _userInfo.value = userInfoVal
                _tweetsList.value = tweetsListVal
                _status.value = STATUS.DONE
            } catch (e: Exception) {
                _status.value = STATUS.ERROR
            }
        }
    }
}
