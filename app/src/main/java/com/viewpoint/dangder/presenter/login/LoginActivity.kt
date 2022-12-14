package com.viewpoint.dangder.presenter.login


import android.content.Intent
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityLoginBinding
import com.viewpoint.dangder.presenter.MainActivity
import com.viewpoint.dangder.util.*
import com.viewpoint.dangder.presenter.initdog.InitDogActivity
import com.viewpoint.dangder.presenter.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_login
    private val authViewModel: LoginViewModel by viewModels()

    override fun initView() {

        binding.loginEmailInput.addTextChangedListener(
            InputVerifyWatcher(
                binding.loginEmailInputLayout,
                "이메일을 정확하게 입력해주세요.",
                emailRegex
            )
        )

        binding.loginPasswordInput.addTextChangedListener(
            InputVerifyWatcher(
                binding.loginPasswordInputLayout,
                "영문 + 숫자 조합으로 입력해주세요.",
                passwordRegex
            )
        )

        binding.loginSignupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }


        binding.loginLoginButton.setOnClickListener {
            hideKeyboard(this, currentFocus)

            val email = binding.loginEmailInput.text.toString()
            val pw = binding.loginPasswordInput.text.toString()

            if (email.isBlank() || pw.isBlank()) {
                showErrorSnackBar(binding.root, "이메일 또는 비밀번호를 입력해주세요.")
                return@setOnClickListener
            }

            if (binding.loginEmailInputLayout.isErrorEnabled || binding.loginPasswordInputLayout.isErrorEnabled) {
                showErrorSnackBar(binding.root, "이메일 또는 비밀번호를 정확하게 입력해주세요.")
                return@setOnClickListener
            }

            authViewModel.login(email, pw)
        }
    }

    override fun subscribe() {
        authViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    Actions.GoToMainPage -> {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                    is Actions.GoToInitDogPage -> {
                        startActivity(
                            Intent(this, InitDogActivity::class.java).apply {
                                putExtra("userId", it.userId)
                            }
                        )
                    }
                    Actions.ShowLoadingDialog -> {
                        showLoadingDialog()
                    }
                    Actions.HideLoadingDialog -> {
                        hideLoadingDialog()
                    }
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
                            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            onError = {
                Timber.e(it)
            }
        ).addTo(compositeDisposable)
    }

    override fun initData() {

    }
}