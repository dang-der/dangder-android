package com.viewpoint.dangder.view.signup

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentUserEmailBinding
import com.viewpoint.dangder.util.InputWatcher
import com.viewpoint.dangder.util.emailRegex
import com.viewpoint.dangder.viewmodel.SignUpViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class UserEmailFragment : BaseFragment<FragmentUserEmailBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_email

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.signip_nav_graph)

    override fun initView() {
        signUpViewModel._email?.let {
            binding.signupEmailInput.setText(it)
        }

        binding.signupNextButton.setOnClickListener {

            val email = binding.signupEmailInput.text.toString()

            if (email.isBlank()) {
                Snackbar.make(binding.root, "이메일을 입력해 주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.signupEmailInputLayout.isErrorEnabled) return@setOnClickListener

            signUpViewModel.createEmailTokenForSignUp(email)
        }

        binding.signupEmailInput.addTextChangedListener(
            InputWatcher(
                binding.signupEmailInputLayout,
                "이메일을 정확하게 입력해주세요.",
                emailRegex
            )
        )
    }

    override fun subscribeModel() {
        signUpViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    Actions.GoToNextPage -> findNavController().navigate(R.id.action_userEmailFragment_to_emailVerifyFragment)
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
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