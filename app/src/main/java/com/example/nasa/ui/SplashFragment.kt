package com.example.nasa.ui

import android.os.Bundle
import android.view.View
import com.example.nasa.databinding.FragmentSplashBinding

class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewSplash.animate().rotationBy(4 * 360f).setDuration(10000L).start()

    }
}