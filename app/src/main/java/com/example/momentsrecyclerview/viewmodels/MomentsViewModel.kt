package com.example.momentsrecyclerview.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.momentsrecyclerview.domain.Tweet
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.exceptions.TweetException
import com.example.momentsrecyclerview.exceptions.UserInfoException
import com.example.momentsrecyclerview.network.TweetsListNetwork
import com.example.momentsrecyclerview.network.UserInfoNetwork
import com.example.momentsrecyclerview.network.asDomainModel
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }

class MomentsViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()

    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    private val _tweetsList = MutableLiveData<List<Tweet>>()
    val tweetsList: LiveData<List<Tweet>>
        get() = _tweetsList

    private val _userInfoStatus = MutableLiveData<STATUS>()

    val userInfoStatus: LiveData<STATUS>
        get() = _userInfoStatus

    private val _tweetStatus = MutableLiveData<STATUS>()

    val tweetStatus: LiveData<STATUS>
        get() = _tweetStatus

    init {
        getUserInfo()
        getTweetsList()
    }

    private fun getUserInfo() {
        _userInfoStatus.value = STATUS.LOADING
        viewModelScope.launch {
            try {
                _userInfo.value = UserInfoNetwork.userInfo.getUserInfo().asDomainModel()
                _userInfoStatus.value = STATUS.DONE
            } catch (e: UserInfoException) {
                Log.d("GetUserInfoError", e.toString())
                _userInfoStatus.value = STATUS.ERROR
            }
        }
    }

    private fun getTweetsList() {
        _tweetStatus.value = STATUS.LOADING
        viewModelScope.launch {
            try {
                _tweetsList.value = TweetsListNetwork.tweets.getTweetsList().asDomainModel()
                _tweetStatus.value = STATUS.DONE
            } catch (e: TweetException) {
                Log.d("GetTweetsError", e.toString())
                _tweetStatus.value = STATUS.ERROR
            }
        }
    }
}
