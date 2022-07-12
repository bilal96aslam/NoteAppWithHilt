package com.example.noteappwithhilt.ui.auth

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappwithhilt.data.remote.response.UserRequest
import com.example.noteappwithhilt.data.remote.response.UserResponse
import com.example.noteappwithhilt.repository.UserRepository
import com.example.noteappwithhilt.utils.Helper
import com.example.noteappwithhilt.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (private val repository: UserRepository):ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = repository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            repository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            repository.loginUser(userRequest)
        }
    }

    fun validateCredentials(emailAddress: String, userName: String, password: String,
                            isLogin: Boolean) : Pair<Boolean, String> {

        var result = Pair(true, "")
        if(TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
        else if(!Helper.isValidEmail(emailAddress)){
            result = Pair(false, "Email is invalid")
        }
        else if(!TextUtils.isEmpty(password) && password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }
}