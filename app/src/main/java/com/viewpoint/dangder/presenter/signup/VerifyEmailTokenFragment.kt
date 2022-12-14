package com.viewpoint.dangder.presenter.signup

import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentEmailVerifyBinding
import com.viewpoint.dangder.util.Timer
import com.viewpoint.dangder.util.showErrorSnackBar
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

class VerifyEmailTokenFragment : BaseFragment<FragmentEmailVerifyBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_email_verify

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.signip_nav_graph)

    override fun initView() {
        signUpViewModel._token?.let {
            binding.signupCode1.setText(it[0].toString())
            binding.signupCode2.setText(it[1].toString())
            binding.signupCode3.setText(it[2].toString())
            binding.signupCode4.setText(it[3].toString())
        }

        signUpViewModel._token ?: let {
            Timer(5 * 1000 * 60, 1000, binding.signupTimerText).start()
        }

        binding.signupNextButton.setOnClickListener {
            val token =
                "${binding.signupCode1.text.toString()}${binding.signupCode2.text.toString()}${binding.signupCode3.text.toString()}${binding.signupCode4.text.toString()}"

            if (token.length != 4) {
                showErrorSnackBar(binding.root, "정확한 인증코드를 입력해주세요.")
                return@setOnClickListener
            }

            signUpViewModel.verifyEmailToken(token)
        }

        moveFocus()
    }

    override fun subscribeModel() {
        signUpViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    Actions.GoToNextPage -> findNavController().navigate(R.id.action_emailVerifyFragment_to_userPasswordFragment)
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

    override fun initData() {}

    private fun moveFocus(){
        binding.signupCode1.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.signupCode2.requestFocus()
            }
        }

        binding.signupCode2.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.signupCode3.requestFocus()
            }
        }

        binding.signupCode3.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                binding.signupCode4.requestFocus()
            }
        }
    }
}