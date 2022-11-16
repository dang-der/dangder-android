package com.viewpoint.dangder.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment< B : ViewBinding> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()
    protected lateinit var binding : B
    protected abstract val layoutId : Int

    protected abstract fun initView()
    protected abstract fun subscribeModel()
    protected abstract fun initData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        initView()
        subscribeModel()
        initData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }


}