package com.example.nasa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasa.model.MarsPictureModel
import com.example.nasa.repository.IMarsPictureRepository
import com.example.nasa.repository.MarsRetrofitRepository
import com.example.nasa.utils.CallbackData
import java.util.*

class MarsViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IMarsPictureRepository = MarsRetrofitRepository()
) : BaseViewModel() {

    fun getLiveData(): LiveData<AppState> = liveData

    fun sendServerRequest(date: Date){
        liveData.postValue(AppState.Loading(null))
        repository.getMarsPicture(date,  object : CallbackData<MarsPictureModel> {
            override fun onSuccess(result: MarsPictureModel) {
                liveData.postValue(AppState.SuccessMarsPicture(result))
            }

            override fun onError(err: Throwable) {
                liveData.postValue(AppState.Error(err))
            }
        })
    }
}