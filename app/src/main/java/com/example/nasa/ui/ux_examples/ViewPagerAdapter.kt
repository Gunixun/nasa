package com.example.nasa.ui.ux_examples

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

enum class UxList(val value: Int) {
    TEXTVIEW(0), BUTTON(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}


class ViewPagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(
        TextViewUxFragment.newInstance(),
        ButtonUxFragment.newInstance()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}