package com.viewpoint.dangder.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.viewpoint.dangder.view.dialog.LoadingDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseFragment< B : ViewBinding> : Fragment() {

    protected val compositeDisposable = CompositeDisposable()
    protected lateinit var binding : B
    protected abstract val layoutId : Int
    protected lateinit var loadingDialog: LoadingDialog

    protected abstract fun initView()
    protected abstract fun subscribeModel()
    protected abstract fun initData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        loadingDialog = LoadingDialog.newInstance()

        initView()
        subscribeModel()
        initData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    protected fun showLoadingDialog(){
        loadingDialog.show(requireActivity().supportFragmentManager, loadingDialog.tag)
    }

    protected fun hideLoadingDialog(){
        if(loadingDialog.isAdded){
            loadingDialog.dismissAllowingStateLoss()
        }
    }


}