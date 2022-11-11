package com.viewpoint.dangder.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : ViewModel, B : ViewBinding> : Fragment() {

    protected abstract val viewModel : VM
    protected lateinit var binding : B
    protected abstract val layoutId : Int

    protected abstract fun initView()
    protected abstract fun subscribeModel()
    protected abstract fun fetch()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        initView()
        subscribeModel()
        fetch()

        return binding.root
    }
}