package com.example.nit3213app.data.repository

import com.example.nit3213app.data.api.ApiService
import com.example.nit3213app.data.api.models.DashboardResponse
import com.example.nit3213app.data.api.models.LoginRequest
import com.example.nit3213app.data.api.models.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun login(location: String, username: String, password: String): LoginResponse {
        return apiService.login(location, LoginRequest(username, password))
    }

    suspend fun getDashboard(keypass: String): DashboardResponse {
        return apiService.getDashboard(keypass)
    }
}
