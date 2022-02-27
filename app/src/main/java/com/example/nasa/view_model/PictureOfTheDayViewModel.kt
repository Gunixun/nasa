package com.example.nasa.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasa.model.PictureModel
import com.example.nasa.repository.IPictureRepository
import com.example.nasa.repository.PictureRetrofitRepositoryImpl
import com.example.nasa.utils.CallbackData
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<PictureByDayState> = MutableLiveData(),
    private val repository: IPictureRepository = PictureRetrofitRepositoryImpl()
    ): BaseViewModel() {

    fun getLiveData(): LiveData<PictureByDayState> {
        return  liveData
    }

    fun sendServerRequest(){
        liveData.postValue(PictureByDayState.Loading(null))
        repository.getPictureByDate(Date(),  object : CallbackData<PictureModel> {
            override fun onSuccess(result: PictureModel) {
                liveData.postValue(PictureByDayState.Success(result))
            }

            override fun onError(err: Throwable) {
                liveData.postValue(PictureByDayState.Error(err))
            }
        })
    }
}