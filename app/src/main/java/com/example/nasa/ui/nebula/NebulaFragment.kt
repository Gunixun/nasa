package com.example.nasa.ui.nebula

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentNebulaPageBinding
import com.example.nasa.model.PictureByDayData
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.createMsgSnackBar
import com.example.nasa.utils.createErrSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.PictureByDayViewModel
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.*

class NebulaFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentNebulaPageBinding>
        (FragmentNebulaPageBinding::inflate) {

    private lateinit var currentDate: Date
    private var retryIter: Int = 0

    companion object {
        fun newInstance() = NebulaFragment()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }
        val localDate = LocalDate.of(2022, Month.MARCH, 14)
        currentDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
        viewModel.sendServerRequest(currentDate)

    }

    private fun renderData(pictureOfTheDayState: AppState) {
        binding.progress.isVisible = false
        when (pictureOfTheDayState) {
            is AppState.Loading -> {
                binding.progress.isVisible = true
            }
            is AppState.SuccessPBD -> {
                setPictureData(pictureOfTheDayState.serverResponseData)
                retryIter = 0
            }
            is AppState.Error -> {
                if (retryIter < 3) {
                    binding.root.createErrSnackBar(
                        text = pictureOfTheDayState.error.toString(),
                        actionText = R.string.retry,
                        { viewModel.sendServerRequest(currentDate) }
                    ).show()
                } else{
                    binding.root.createMsgSnackBar(
                        text = this.resources.getString(R.string.fall_load_data)
                    ).show()
                }
                retryIter++
            }
        }
    }

    private fun setPictureData(pictureByDayData: PictureByDayData) {
        binding.imageView.load(pictureByDayData.hdurl)
        binding.titleTextView.text = pictureByDayData.title
        binding.descriptionTextView.text = pictureByDayData.explanation
    }

}