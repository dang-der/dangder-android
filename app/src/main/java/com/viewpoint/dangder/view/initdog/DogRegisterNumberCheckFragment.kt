package com.viewpoint.dangder.view.initdog

import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogRegNumberBinding


class DogRegisterNumberCheckFragment : BaseFragment<FragmentDogRegNumberBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_reg_number


    override fun initView() {
        binding.initdogNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_dogRegisterNumberCheckFragment_to_dogProfile1Fragment)
        }
    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }
}