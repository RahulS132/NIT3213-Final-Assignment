package com.example.nit3213app.data.api.models

import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass(generateAdapter = true)
data class Entity(
    val property1: String? = null,
    val property2: String? = null,
    val description: String? = null
) : Serializable
