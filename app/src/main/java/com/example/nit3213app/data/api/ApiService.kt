package com.example.nit3213app.data.api

import com.example.nit3213app.data.api.models.DashboardResponse
import com.example.nit3213app.data.api.models.LoginRequest
import com.example.nit3213app.data.api.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit API service interface
interface ApiService {

    // Authenticate user
    @POST("{location}/auth")
    suspend fun login(
        @Path("location") location: String,
        @Body request: LoginRequest
    ): LoginResponse

    // Fetch dashboard data
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): DashboardResponse
}
