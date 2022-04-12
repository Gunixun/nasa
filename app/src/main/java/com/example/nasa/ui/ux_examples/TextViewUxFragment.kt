package com.example.nasa.ui.ux_examples

import com.example.nasa.databinding.FragmentUxTextViewBinding
import com.example.nasa.ui.BaseFragment

class TextViewUxFragment :
    BaseFragment<FragmentUxTextViewBinding>(FragmentUxTextViewBinding::inflate) {

    companion object {
        fun newInstance() = TextViewUxFragment()
    }
}