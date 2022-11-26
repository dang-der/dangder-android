package com.viewpoint.dangder.view.initdog

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile1Binding
import com.viewpoint.dangder.databinding.FragmentUserEmailBinding
import com.viewpoint.dangder.util.InputVerifyWatcher
import com.viewpoint.dangder.util.emailRegex
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.viewmodel.SignUpViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class DogProfile1Fragment : BaseFragment<FragmentDogProfile1Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_1

    private val signUpViewModel: SignUpViewModel by hiltNavGraphViewModels(R.id.signip_nav_graph)

    override fun initView() {
        binding.initdogNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_dogProfile1Fragment_to_dogProfile2Fragment)
        }

    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }
}