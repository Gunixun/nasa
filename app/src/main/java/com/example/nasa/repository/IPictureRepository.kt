package com.example.nasa.repository

import com.example.nasa.model.PictureModel
import com.example.nasa.utils.CallbackData
import java.util.*

interface IPictureRepository {

    fun getPictureByDate(date: Date, callback: CallbackData<PictureModel>)
}