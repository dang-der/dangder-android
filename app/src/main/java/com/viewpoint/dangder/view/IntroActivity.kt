package com.viewpoint.dangder.view

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityIntroBinding
import com.viewpoint.dangder.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_intro
    private val authViewModel: LoginViewModel by viewModels()

    override fun initView() {}

    override fun subscribe() {
        authViewModel.action.subscribe {

            Handler(Looper.getMainLooper()).postDelayed({
                when (it) {
                    Actions.GoToLoginPage -> {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                    is Actions.GoToInitDogPage -> {
                        startActivity(
                            Intent(this, InitDogActivity::class.java).apply {
                                putExtra("userId", it.userId)
                            }
                        )
                    }
                    Actions.ShowLoadingDialog -> showLoadingDialog()
                    Actions.HideLoadingDialog -> hideLoadingDialog()
                    else -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                finish()
            }, 3000)


        }.addTo(compositeDisposable)
    }

    override fun initData() {
        authViewModel.checkIsLogin()
    }

}