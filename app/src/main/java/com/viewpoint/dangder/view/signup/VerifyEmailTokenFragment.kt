package com.viewpoint.dangder.view.signup

import androidx.core.widget.addTextChangedListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentEmailVerifyBinding
import com.viewpoint.dangder.viewmodel.SignUpViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

class VerifyEmailTokenFragment : BaseFragment<FragmentEmailVerifyBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_email_verify

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.signip_nav_graph)

    override fun initView() {
        binding.signupNextButton.setOnClickListener {
            val token =
                "${binding.signupCode1.text.toString()}${binding.signupCode2.text.toString()}${binding.signupCode3.text.toString()}${binding.signupCode4.text.toString()}"

            if (token.length != 4) {
                Snackbar.make(binding.root, "정확한 인증코드를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signUpViewModel.verifyEmailToken(token)
        }

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

    override fun subscribeModel() {
        signUpViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    Actions.GoToNextPage -> findNavController().navigate(R.id.action_emailVerifyFragment_to_userPasswordFragment)
                    else ->{
                        if(it is Actions.ShowErrorMessage){
                            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
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