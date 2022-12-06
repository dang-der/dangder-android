package com.viewpoint.dangder.presenter.initdog

import android.content.Intent
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.gms.location.LocationServices
import com.google.android.material.chip.Chip
import com.viewpoint.dangder.R
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile2Binding
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.presenter.MainActivity
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class DogProfile2Fragment : BaseFragment<FragmentDogProfile2Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_2

    private val needPermissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private val initDogViewModel: InitDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)


    override fun initView() {
        requestPermissions(needPermissions)
        handleClickCreateDog()
    }

    override fun subscribeModel() {
        initDogViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.FetchInterests -> addInterestsChips(it.data)
                    is Actions.FetchCharacters -> addCharacterChips(it.data)
                    Actions.GoToMainPage ->{
                        startActivity(
                            Intent(requireActivity(), MainActivity::class.java)
                        )
                        requireActivity().finish()
                    }
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

    override fun initData() {
        initDogViewModel.fetchCharacters()
        initDogViewModel.fetchInterests()
    }

    private fun handleClickCreateDog(){
        binding.initdogRegisterBtn.setOnClickListener {

            if(checkPermissions(needPermissions).not()){
                showDialogGoToSetting(arrayOf("위치 정보"))
                return@setOnClickListener
            }

            val locationClient = LocationServices.getFusedLocationProviderClient(requireContext())

            locationClient.lastLocation.addOnSuccessListener {
                initDogViewModel.createDog(it)
            }
        }
    }

    private val characterChipCheckedChangeListener =
        OnCheckedChangeListener { p0, p1 ->
            val value = p0.text.toString()
            if (p1) initDogViewModel.addCharacter(value) else initDogViewModel.removeCharacter(
                value
            )
        }

    private val interestChipCheckedChangeListener =
        OnCheckedChangeListener { p0, p1 ->
            val value = p0.text.toString()
            if (p1) initDogViewModel.addInterest(value) else initDogViewModel.removeInterest(
                value
            )
        }

    private fun addCharacterChips(characters: Array<String>) {
        val group = binding.initdogCharacterList

        characters.forEach {
            group.addView(createChip(it, characterChipCheckedChangeListener))
        }
    }

    private fun addInterestsChips(interests: Array<String>) {
        val group = binding.initdogInterestList

        interests.forEach {
            group.addView(createChip(it, interestChipCheckedChangeListener))
        }
    }

    private fun createChip(data: String, checkedChangeListener: OnCheckedChangeListener): Chip {
        return Chip(requireContext()).apply {
            text = data
            isCheckable = true
            setChipBackgroundColorResource(R.color.main_opacity_10)
            setOnCheckedChangeListener(checkedChangeListener)
        }
    }


}