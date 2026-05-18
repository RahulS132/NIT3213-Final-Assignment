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
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

// ViewModel for login screen logic
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<String>>(Resource.Idle)
    val loginState: StateFlow<Resource<String>> = _loginState.asStateFlow()

    // Process login request
    fun login(campus: String, username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = Resource.Error("Username and password cannot be empty")
            return
        }
        if (campus.isBlank()) {
            _loginState.value = Resource.Error("Please choose a campus.")
            return
        }

        viewModelScope.launch {
            _loginState.value = Resource.Loading
            try {
                val response = repository.login(campus, username.trim(), password.trim())
                _loginState.value = Resource.Success(response.keypass)
            } catch (e: HttpException) {
                _loginState.value = Resource.Error(
                    when (e.code()) {
                        401, 403 -> "Invalid username or password."
                        404 -> "Endpoint not found (404). Try a different campus."
                        in 500..599 -> "Server error (${e.code()}). Try again shortly."
                        else -> "Login failed: HTTP ${e.code()}"
                    }
                )
            } catch (e: IOException) {
                _loginState.value = Resource.Error(
                    "Network error: ${e.localizedMessage ?: "no connection"}"
                )
            } catch (e: Exception) {
                _loginState.value = Resource.Error(
                    e.localizedMessage ?: "Login failed. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _loginState.value = Resource.Idle
    }
}
