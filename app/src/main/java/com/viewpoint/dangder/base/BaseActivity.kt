package com.viewpoint.dangder.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<B :ViewBinding> : AppCompatActivity(){
    protected lateinit var binding : B
    protected abstract val layoutId : Int
    protected val compositeDisposable = CompositeDisposable()

    protected abstract fun initView()
    protected abstract fun subscribe()
    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)

        initView()
        initData()
    }

    override fun onStart() {
        super.onStart()
        subscribe()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }


}