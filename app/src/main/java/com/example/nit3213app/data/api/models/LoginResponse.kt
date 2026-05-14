package com.example.nit3213app.data.api.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val keypass: String
)
