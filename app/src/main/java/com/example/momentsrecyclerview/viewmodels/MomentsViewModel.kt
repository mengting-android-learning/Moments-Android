package com.example.momentsrecyclerview.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.momentsrecyclerview.domain.UserInfo

class MomentsViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<UserInfo>()

    val userInfo: LiveData<UserInfo>
        get() = _userInfo

    init{
        getUserInfo()
    }

    private fun getUserInfo(){
        Log.d("test","getUserInfo")
    }

}