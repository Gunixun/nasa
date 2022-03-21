package com.example.nasa.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import coil.load
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPictureByDayBinding
import com.example.nasa.model.PictureByDayModel
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.showErrSnackBar
import com.example.nasa.utils.showMsgSnackBar
import com.example.nasa.view_model.AppState
import com.example.nasa.view_model.PictureByDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*

class PictureByDayFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentPictureByDayBinding>
        (FragmentPictureByDayBinding::inflate) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var currentDate: Date
    private var retryIter: Int = 0

    companion object {
        fun newInstance() = PictureByDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) { state -> renderData(state) }
        currentDate = Date()
        initUi()
        viewModel.sendServerRequest(currentDate)
    }

    private fun renderData(pictureOfTheDayState: AppState) {
        binding.progress.isVisible = false
        when (pictureOfTheDayState) {
            is AppState.Loading -> {
                binding.progress.isVisible = true
            }
            is AppState.SuccessPBD -> {
                setPictureModel(pictureOfTheDayState.serverResponseData)
                retryIter = 0
            }
            is AppState.Error -> {
                if (retryIter < 3) {
                    binding.root.showErrSnackBar(
                        text = pictureOfTheDayState.error.toString(),
                        actionText = R.string.retry,
                        { viewModel.sendServerRequest(currentDate) }
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

    private fun setPictureModel(pictureByDayModel: PictureByDayModel) {
        binding.imageView.load(pictureByDayModel.hdurl)
        binding.included.bottomSheetDescriptionHeader.text = pictureByDayModel.title
        binding.included.bottomSheetDescription.text = pictureByDayModel.explanation
    }

    private fun initUi() {
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        connectChipGroup()
        initBottomSheetBehavior()
    }

    private fun connectChipGroup() {
        binding.chipToday.isChecked = true
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when {
                binding.chipToday.id == checkedId -> {
                    currentDate = Date()
                }
                binding.chipYesterday.id == checkedId -> {
                    currentDate = Date(Date().getTime() - (1000 * 60 * 60 * 24))
                }
                binding.chipTwoDaysAgo.id == checkedId -> {
                    currentDate = Date(Date().getTime() - (1000 * 60 * 60 * 48))
                }
            }
            viewModel.sendServerRequest(currentDate)
        }
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehavior.setHideable(false)
    }
}