package com.example.nit3213app.data.repository

import com.example.nit3213app.data.api.ApiService
import com.example.nit3213app.data.api.models.Entity
import com.example.nit3213app.data.api.models.LoginRequest
import com.example.nit3213app.data.api.models.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

// Data repository for the app
@Singleton
class AppRepository @Inject constructor(
    private val apiService: ApiService
) {

    // Perform login
    suspend fun login(location: String, username: String, password: String): LoginResponse {
        return apiService.login(location, LoginRequest(username, password))
    }

    // Fetch dashboard entities
    suspend fun getDashboard(keypass: String): List<Entity> {
        val response = apiService.getDashboard(keypass)
        return response.entities.map { Entity(LinkedHashMap(it)) }
    }
}
