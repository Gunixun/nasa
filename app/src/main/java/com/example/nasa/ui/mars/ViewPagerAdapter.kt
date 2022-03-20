package com.example.nasa.ui.mars

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

enum class ViewList(val value: Int) {
    MARSFHAZ(0), MARSRHAZ(1);
}


class ViewPagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(MarsPictureFHAZFragment.newInstance(), MarsPictureCHEMCAMFragment.newInstance())

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}