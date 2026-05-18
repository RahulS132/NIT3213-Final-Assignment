package com.example.nit3213app.data.api.models

import com.squareup.moshi.JsonClass

// Dashboard data response model
@JsonClass(generateAdapter = true)
data class DashboardResponse(
    val entities: List<Map<String, String>>,
    val entityTotal: Int
)
