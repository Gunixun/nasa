package com.example.nasa.view_model

import com.example.nasa.model.MarsPictureModel
import com.example.nasa.model.PictureByDayModel


sealed class AppState {
    data class SuccessPBD(val serverResponseData: PictureByDayModel) : AppState()
    data class SuccessMarsPicture(val serverResponseData: MarsPictureModel) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}