package com.viewpoint.dangder.view.signup

import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentUserEmailBinding

class UserEmailFragment : BaseFragment<FragmentUserEmailBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_user_email

    override fun initView() {

        binding.signupNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_userEmailFragment_to_emailVerifyFragment)
        }

    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }
}