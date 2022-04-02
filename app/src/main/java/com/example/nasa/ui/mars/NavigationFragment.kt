package com.example.nasa.ui.mars

import android.os.Bundle
import android.view.View
import com.example.nasa.databinding.FragmentNavigationViewBinding
import com.example.nasa.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class NavigationFragment :
    BaseFragment<FragmentNavigationViewBinding>(FragmentNavigationViewBinding::inflate) {

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = when (ViewList.fromInt(position)) {
                ViewList.MARSFHAZ -> "Camera FHAZ"
                ViewList.MARSRHAZ -> "Camera CHEMCAM"
            }
        }.attach()
    }
}