package com.example.nasa.repository

import com.example.nasa.model.PictureByDayModel
import com.example.nasa.utils.CallbackData
import java.util.*

interface IPictureRepository {

    fun getPictureByDate(date: Date, callback: CallbackData<PictureByDayModel>)
}