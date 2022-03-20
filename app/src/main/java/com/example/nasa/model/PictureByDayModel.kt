package com.example.nasa.model

data class PictureByDayModel(
    val date: String,
    val explanation: String,
    val hdurl: String,
    val mediaType: String,
    val serviceVersion: String,
    val title: String,
    val url: String
)