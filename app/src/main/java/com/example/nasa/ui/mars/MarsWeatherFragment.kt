package com.example.nasa.ui.mars

import com.example.nasa.databinding.FragmentMarsWeatherBinding
import com.example.nasa.ui.BaseFragmentWithModel
import com.example.nasa.view_model.PictureByDayViewModel

class MarsWeatherFragment :
    BaseFragmentWithModel<PictureByDayViewModel, FragmentMarsWeatherBinding>
        (FragmentMarsWeatherBinding::inflate) {

    companion object {
        fun newInstance() = MarsWeatherFragment()
    }

}