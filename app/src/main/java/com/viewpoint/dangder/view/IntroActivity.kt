package com.viewpoint.dangder.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityIntroBinding
import com.viewpoint.dangder.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.socket.client.On.Handle
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_intro
    private val authViewModel: AuthViewModel by viewModels()

    override fun initView() {}

    override fun subscribe() {
        authViewModel.isLogin.subscribe {
            Timber.d("isLogin : ${it}")

            Handler(Looper.getMainLooper()).postDelayed({
                if (it.not()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    return@postDelayed
                }
                startActivity(Intent(this, MainActivity::class.java))
            }, 3000)


        }.addTo(compositeDisposable)
    }

    override fun initData() {
        authViewModel.checkIsLogin()
    }
}