package com.example.nasa.ui.mars

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nasa.ui.animations.AnimationsList

enum class ViewList(val value: Int) {
    MARSFHAZ(0), MARSRHAZ(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}


class ViewPagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(
        MarsPictureFragment.newInstance("FHAZ"),
        MarsPictureFragment.newInstance("CHEMCAM")
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}