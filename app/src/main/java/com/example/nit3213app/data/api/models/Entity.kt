package com.example.nit3213app.data.api.models

import java.io.Serializable

// Domain model for Entity data
data class Entity(
    val fields: LinkedHashMap<String, String> = LinkedHashMap()
) : Serializable {

    val description: String?
        get() = fields["description"]

    val summaryFields: List<Pair<String, String>>
        get() = fields.entries
            .filter { it.key != "description" }
            .map { it.key to it.value }

    val primaryLabel: String
        get() = summaryFields.firstOrNull()?.second ?: "(unnamed)"
}
