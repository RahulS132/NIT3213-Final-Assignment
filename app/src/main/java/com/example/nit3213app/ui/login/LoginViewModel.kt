package com.example.nit3213app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nit3213app.data.repository.AppRepository
import com.example.nit3213app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>>(Resource.Idle)
    val loginState: StateFlow<Resource<String>> = _loginState.asStateFlow()

    val location: String = "sydney"

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Username and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = Resource.Loading
            try {
                val response = repository.login(location, username.trim(), password.trim())
                _loginState.value = Resource.Success(response.keypass)
            } catch (e: Exception) {
                _loginState.value = Resource.Error(
                    e.localizedMessage ?: "Login failed. Please check your credentials."
                )
            }
        }
    }

    fun resetState() {
        _loginState.value = Resource.Idle
    }
}
