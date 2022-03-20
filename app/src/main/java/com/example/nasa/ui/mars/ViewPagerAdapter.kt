package com.example.nasa.ui.mars

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

enum class ViewList(val value: Int) {
    MARS(0), WEATHER(1);
}


class ViewPagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(MarsPictureFragment.newInstance(), MarsWeatherFragment.newInstance())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}