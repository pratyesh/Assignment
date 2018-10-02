package com.assignment.model


import com.google.gson.annotations.SerializedName

data class Row(
        @SerializedName("title") val title: String? = null,
        @SerializedName("imageHref") val imageHref: String? = null,
        @SerializedName("description") val description: String? = null
)