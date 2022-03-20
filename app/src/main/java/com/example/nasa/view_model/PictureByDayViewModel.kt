package com.example.nasa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasa.model.PictureByDayModel
import com.example.nasa.repository.IPictureRepository
import com.example.nasa.repository.PictureRetrofitRepositoryImpl
import com.example.nasa.utils.CallbackData
import java.util.*

class PictureByDayViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: IPictureRepository = PictureRetrofitRepositoryImpl()
) : BaseViewModel() {

    fun getLiveData(): LiveData<AppState> = liveData

    fun sendServerRequest(date: Date) {
        liveData.postValue(AppState.Loading(null))
        repository.getPictureByDate(date, object : CallbackData<PictureByDayModel> {
            override fun onSuccess(result: PictureByDayModel) {
                liveData.postValue(AppState.SuccessPBD(result))
            }

            override fun onError(err: Throwable) {
                liveData.postValue(AppState.Error(err))
            }
        })
    }
}