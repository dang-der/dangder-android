package com.viewpoint.dangder.view.signup

import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentEmailVerifyBinding

class VerifyEmailTokenFragment : BaseFragment<FragmentEmailVerifyBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_email_verify

    override fun initView() {
        binding.signupNextButton.setOnClickListener {
            val code =
                "${binding.signupCode1.text.toString()}${binding.signupCode2.text.toString()}${binding.signupCode3.text.toString()}${binding.signupCode4.text.toString()}"

            if (code.length != 4) {
                Snackbar.make(binding.root, "정확한 인증코드를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigate(R.id.action_emailVerifyFragment_to_userPasswordFragment)
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

    }

    override fun initData() {

    }
}