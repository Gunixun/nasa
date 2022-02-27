package com.example.nasa.ui.main

import android.content.Intent
import android.net.Uri
import coil.load
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPictureOfTheDayBinding
import com.example.nasa.model.PictureModel
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.utils.showSnackBar
import com.example.nasa.view_model.PictureByDayState
import com.example.nasa.view_model.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PictureOfTheDayFragment :
    BaseFragmentWithModel<PictureOfTheDayViewModel, FragmentPictureOfTheDayBinding>
        (FragmentPictureOfTheDayBinding::inflate) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    companion object{
        fun newInstance() = PictureOfTheDayFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner) {state -> renderData(state) }
        viewModel.sendServerRequest()

        initUi()
    }

    private fun renderData(pictureOfTheDayState: PictureByDayState) {
        binding.progress.isVisible = false
        when (pictureOfTheDayState) {
            is PictureByDayState.Loading -> {
                binding.progress.isVisible = true
            }
            is PictureByDayState.Success -> {
                setPictureModel(pictureOfTheDayState.serverResponseData)
            }
            is PictureByDayState.Error -> {
                binding.root.showSnackBar(
                    text = pictureOfTheDayState.error.toString(),
                    actionText = R.string.retry,
                    { viewModel.sendServerRequest() }
                )
            }
        }
    }

    private fun setPictureModel(pictureModel: PictureModel){
        binding.imageView.load(pictureModel.hdurl)
        binding.included.bottomSheetDescriptionHeader.text = pictureModel.title
        binding.included.bottomSheetDescription.text = pictureModel.explanation
    }

    private fun initUi(){
        binding.inputLayout.setEndIconOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        initBottomSheetBehavior()
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_SETTLING
    }
}