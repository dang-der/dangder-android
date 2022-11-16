package com.viewpoint.dangder.view

import androidx.activity.viewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityLoginBinding
import com.viewpoint.dangder.viewmodel.AuthViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val authViewModel : AuthViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initView() {

    }

    override fun subscribe() {

    }

    override fun initData() {

    }
}