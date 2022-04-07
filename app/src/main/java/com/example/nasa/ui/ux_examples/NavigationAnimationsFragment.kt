package com.example.nasa.ui.ux_examples

import android.os.Bundle
import android.view.View
import com.example.nasa.databinding.FragmentNavigationViewBinding
import com.example.nasa.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator

class NavigationUxFragment:
    BaseFragment<FragmentNavigationViewBinding>(FragmentNavigationViewBinding::inflate) {

    companion object {
        fun newInstance() = NavigationUxFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = when (UxList.fromInt(position)) {
                UxList.TEXTVIEW -> "Textview"
                UxList.BUTTON -> "Button"
            }
        }.attach()
    }
}