package com.example.nasa.ui.animations

import android.os.Bundle
import android.view.View
import com.example.nasa.databinding.FragmentNavigationViewBinding
import com.example.nasa.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class NavigationAnimationsFragment:
    BaseFragment<FragmentNavigationViewBinding>(FragmentNavigationViewBinding::inflate) {

    companion object {
        fun newInstance() = NavigationAnimationsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = when (AnimationsList.fromInt(position)) {
                AnimationsList.ROTATE -> "Animations Rotate"
                AnimationsList.CONSTRAINTS -> "Animations Constraints"
            }
        }.attach()
    }
}