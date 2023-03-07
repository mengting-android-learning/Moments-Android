package com.example.momentsrecyclerview.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.momentsrecyclerview.domain.UserInfo
import com.example.momentsrecyclerview.exceptions.UserInfoException
import com.example.momentsrecyclerview.network.UserInfoNetwork
import com.example.momentsrecyclerview.network.asDomainModel
import kotlinx.coroutines.launch

enum class STATUS { LOADING, ERROR, DONE }

class MomentsViewModel : ViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()

    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    private val _status = MutableLiveData<STATUS>()

    val status: LiveData<STATUS>
        get() = _status

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        _status.value = STATUS.LOADING
        viewModelScope.launch {
            try {
                _userInfo.value = UserInfoNetwork.userInfo.getUserInfo().asDomainModel()
                _status.value = STATUS.DONE
            } catch (e: UserInfoException) {
                Log.d("GetUserInfoError", e.toString())
                _status.value = STATUS.ERROR
            }
        }
    }
}
