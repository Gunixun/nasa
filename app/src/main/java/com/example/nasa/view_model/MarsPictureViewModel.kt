package com.example.nasa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasa.model.MarsPictureModel
import com.example.nasa.repository.IMarsPictureRepository
import com.example.nasa.repository.MarsRetrofitRepository
import com.example.nasa.utils.CallbackData
import java.util.*

class MarsPictureViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IMarsPictureRepository = MarsRetrofitRepository()
) : BaseViewModel() {

    fun getLiveData(): LiveData<AppState> = liveData

    fun sendServerRequest(date: Date, cameraName: String, ){
        liveData.postValue(AppState.Loading(null))
        repository.getMarsPicture(date, cameraName, object : CallbackData<MarsPictureModel> {
            override fun onSuccess(result: MarsPictureModel) {
                liveData.postValue(AppState.SuccessMarsPicture(result))
            }

            override fun onError(err: Throwable) {
                liveData.postValue(AppState.Error(err))
            }
        })
    }
}