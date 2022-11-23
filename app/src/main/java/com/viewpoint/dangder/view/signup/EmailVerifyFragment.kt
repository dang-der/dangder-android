package com.viewpoint.dangder.view.signup

import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentEmailVerifyBinding
import com.viewpoint.dangder.databinding.FragmentUserEmailBinding

class EmailVerifyFragment : BaseFragment<FragmentEmailVerifyBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_email_verify

    override fun initView() {
        binding.signupNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_emailVerifyFragment_to_userPasswordFragment)
        }
    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }
}