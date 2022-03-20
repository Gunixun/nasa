package com.example.nasa.repository

import com.example.nasa.model.MarsPictureModel
import com.example.nasa.utils.CallbackData
import java.util.*

interface IMarsPictureRepository {
    fun getMarsPicture(date: Date, callback: CallbackData<MarsPictureModel>)
}