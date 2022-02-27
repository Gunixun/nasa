package com.example.nasa.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.nasa.view_model.BaseViewModel
import java.lang.reflect.ParameterizedType

abstract class BaseFragmentWithModel<ViewModelType : BaseViewModel, VB : ViewBinding>(private val bindingInflater: (inflater: LayoutInflater) -> VB) :
    BaseFragment<VB>(bindingInflater) {
    lateinit var viewModel: ViewModelType

    private fun initViewModel() {
        val parameterizedType = javaClass.genericSuperclass as? ParameterizedType
        val vmClass = parameterizedType?.actualTypeArguments?.getOrNull(0) as? Class<ViewModelType>?
        if (vmClass != null)
            viewModel = ViewModelProvider(this).get(vmClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }
}