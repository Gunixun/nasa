package com.example.nasa.ui.mars

import android.os.Build
import android.os.Bundle
import android.transition.ChangeImageTransform
import android.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentMarsPictureBinding
import com.example.nasa.model.MarsPictureModel
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.showErrSnackBar
import com.example.nasa.utils.showMsgSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.MarsPictureViewModel
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

const val ARG_CAMERA_NAME = "arg_camera_name"

class MarsPictureFragment :
    BaseFragmentWithModel<MarsPictureViewModel, FragmentMarsPictureBinding>
        (FragmentMarsPictureBinding::inflate) {

    private lateinit var currentDate: Date
    private var cameraName: String = "CHEMCAM"
    private var retryIter: Int = 0

    companion object {
        fun newInstance(cameraName: String)  = MarsPictureFragment().apply {
            arguments = Bundle(2).apply {
                putString(ARG_CAMERA_NAME, cameraName)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { cameraName = it.getString(ARG_CAMERA_NAME, "CHEMCAM") }

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
                retryIter = 0
            }
            is AppState.Error -> {
                if (retryIter < 3) {
                    binding.root.showErrSnackBar(
                        text = pictureOfTheDayState.error.toString(),
                        actionText = R.string.retry,
                        { viewModel.sendServerRequest(currentDate, cameraName) }
                    )
                } else{
                    binding.root.showMsgSnackBar(
                        text = this.resources.getString(R.string.fall_load_data)
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