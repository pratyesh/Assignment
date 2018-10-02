package com.assignment.model

import com.google.gson.annotations.SerializedName

data class ResponseDetails(
        @SerializedName("rows") val rows: ArrayList<Row>? = null,
        @SerializedName("title") val title: String = ""
)