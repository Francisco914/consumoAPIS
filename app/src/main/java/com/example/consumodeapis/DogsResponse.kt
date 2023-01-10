package com.example.consumodeapis

import com.google.gson.annotations.SerializedName
//
data class DogsResponse(
    @SerializedName(value = "status") var status:String,
    @SerializedName(value = "messages") var images: List<String>
)