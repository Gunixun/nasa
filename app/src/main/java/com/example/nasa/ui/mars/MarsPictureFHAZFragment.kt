package com.example.nasa.ui.mars

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentMarsPictureBinding
import com.example.nasa.model.MarsPictureModel
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.showSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.MarsPictureViewModel
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

class MarsPictureFHAZFragment :
    BaseFragmentWithModel<MarsPictureViewModel, FragmentMarsPictureBinding>
        (FragmentMarsPictureBinding::inflate) {

    private lateinit var currentDate: Date
    private val cameraName = "FHAZ"
    private var retryIter: Int = 0

    companion object {
        fun newInstance() = MarsPictureFHAZFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }

        // INFO: чтобы не тратить время пока зашил конкретную дату, позже добавлю обработку текущей даты + календарь
        val localDate = LocalDate.of(2022, Month.MARCH, 3)
        currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        viewModel.sendServerRequest(currentDate, cameraName)
    }

    private fun renderData(pictureOfTheDayState: AppState) {
        binding.progress.isVisible = false
        when (pictureOfTheDayState) {
            is AppState.Loading -> {
                binding.progress.isVisible = true
            }
            is AppState.SuccessMarsPicture -> {
                setMarsPictureModel(pictureOfTheDayState.serverResponseData)
            }
            is AppState.Error -> {
                if (retryIter < 3) {
                    binding.root.showSnackBar(
                        text = pictureOfTheDayState.error.toString(),
                        actionText = R.string.retry,
                        { viewModel.sendServerRequest(currentDate, cameraName) }
                    )
                }
                retryIter++
            }
        }
    }

    private fun setMarsPictureModel(marsPictureModel: MarsPictureModel) {
        binding.imageView.load(marsPictureModel.url)
    }

}