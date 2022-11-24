package com.viewpoint.dangder.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.viewpoint.dangder.view.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<B :ViewBinding> : AppCompatActivity(){
    protected lateinit var binding : B
    protected abstract val layoutId : Int
    protected lateinit var loadingDialog:LoadingDialog
    protected val compositeDisposable = CompositeDisposable()

    protected abstract fun initView()
    protected abstract fun subscribe()
    protected abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        loadingDialog = LoadingDialog.newInstance()
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

    protected fun showLoadingDialog(){
        loadingDialog.show(supportFragmentManager, loadingDialog.tag)
    }

    protected fun hideLoadingDialog(){
        if(loadingDialog.isAdded){
            loadingDialog.dismissAllowingStateLoss()
        }
    }


}