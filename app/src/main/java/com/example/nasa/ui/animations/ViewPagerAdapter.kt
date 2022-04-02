package com.example.nasa.ui.animations

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

enum class AnimationsList(val value: Int) {
    ROTATE(0), CONSTRAINTS(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}


class ViewPagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {

    private val fragments = arrayOf(
        AnimationsRotateFragment.newInstance(),
        AnimationsConstraintSetFragment.newInstance()
    )

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}