package com.example.nit3213app.data.api.models

import com.squareup.moshi.JsonClass

// Login request model
@JsonClass(generateAdapter = true)
data class LoginRequest(
    val username: String,
    val password: String
)
