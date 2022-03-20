package com.example.nasa.repository.dto

import com.google.gson.annotations.SerializedName

data class MarsPicturesResponseData (
    @field:SerializedName("photos") val photos: ArrayList<MarsServerResponseData>,
)