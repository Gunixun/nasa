package com.example.nasa.ui.ux_examples

import com.example.nasa.databinding.FragmentUxButtonBinding
import com.example.nasa.ui.BaseFragment

class ButtonUxFragment :
    BaseFragment<FragmentUxButtonBinding>(FragmentUxButtonBinding::inflate) {

    companion object {
        fun newInstance() = ButtonUxFragment()
    }
}