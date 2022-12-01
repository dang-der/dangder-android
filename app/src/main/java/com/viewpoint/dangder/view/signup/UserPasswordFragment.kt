package com.viewpoint.dangder.view.signup

import android.content.Intent
import androidx.core.widget.addTextChangedListener
import androidx.navigation.navGraphViewModels
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentUserPasswordBinding
import com.viewpoint.dangder.util.InputVerifyWatcher
import com.viewpoint.dangder.util.passwordRegex
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.view.InitDogActivity
import com.viewpoint.dangder.viewmodel.SignUpViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

class UserPasswordFragment : BaseFragment<FragmentUserPasswordBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_password

    private val signUpViewModel: SignUpViewModel by navGraphViewModels(R.id.signip_nav_graph)

    override fun initView() {
        binding.signupSignupButton.setOnClickListener {
            val pw = binding.signupCheckPasswordInput.text.toString()

            if (pw.isBlank() || binding.signupCheckPasswordInputLayout.isErrorEnabled || binding.signupPasswordInputLayout.isErrorEnabled) {
                showErrorSnackBar(binding.root, "비밀번호를 올바르게 입력해 주세요.")
                return@setOnClickListener
            }

            signUpViewModel.createUser(pw)
        }

        binding.signupPasswordInput.addTextChangedListener(
            InputVerifyWatcher(
                binding.signupPasswordInputLayout,
                "영문 + 숫자 조합으로 입력해주세요.",
                passwordRegex
            )
        )

        binding.signupCheckPasswordInput.addTextChangedListener {
            if (binding.signupPasswordInput.text.toString() != it.toString()) {
                binding.signupCheckPasswordInputLayout.error = "비밀번호가 일치하지 않습니다."
                binding.signupCheckPasswordInputLayout.isErrorEnabled = true
                return@addTextChangedListener
            }

            binding.signupCheckPasswordInputLayout.error = null
            binding.signupCheckPasswordInputLayout.isErrorEnabled = false
        }
    }

    override fun subscribeModel() {
        signUpViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.GoToInitDogPage -> {
                        startActivity(
                            Intent(activity, InitDogActivity::class.java).apply {
                                putExtra("userId", it.userId)
                            }
                        )
                    }
                    Actions.ShowLoadingDialog -> showLoadingDialog()
                    Actions.HideLoadingDialog -> hideLoadingDialog()
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
                            showErrorSnackBar(binding.root, it.message)
                        }
                    }
                }
            },
            onError = {

            }
        ).addTo(compositeDisposable)
    }

    override fun initData() {

    }
}