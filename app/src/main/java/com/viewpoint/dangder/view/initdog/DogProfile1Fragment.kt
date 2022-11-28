package com.viewpoint.dangder.view.initdog

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile1Binding
import com.viewpoint.dangder.view.InitDogActivity
import com.viewpoint.dangder.viewmodel.RegisterDogViewModel
import timber.log.Timber
import java.util.jar.Manifest


class DogProfile1Fragment : BaseFragment<FragmentDogProfile1Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_1

    private val registerDogViewModel: RegisterDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)

    override fun initView() {
        requestPermissions(arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ))

        binding.initdogNextButton.setOnClickListener {
            findNavController().navigate(R.id.action_dogProfile1Fragment_to_dogProfile2Fragment)
        }

        binding.initdogAddImageBtn.setOnClickListener {
            val permissionResult = checkPermissions(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
            )

            if(permissionResult.not()) {
                showDialogGoToSetting()
                return@setOnClickListener
            }

            Timber.tag("permissions").d(permissionResult.toString())
        }

    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }
}