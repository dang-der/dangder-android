package com.viewpoint.dangder.view.initdog

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile2Binding
import com.viewpoint.dangder.viewmodel.RegisterDogViewModel


class DogProfile2Fragment : BaseFragment<FragmentDogProfile2Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_2

    private val registerDogViewModel: RegisterDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)

    override fun initView() {

    }

    override fun subscribeModel() {

    }

    override fun initData() {
        registerDogViewModel.fetchCharacters()
        registerDogViewModel.fetchInterests()
    }

    private fun initListView(){

    }
}