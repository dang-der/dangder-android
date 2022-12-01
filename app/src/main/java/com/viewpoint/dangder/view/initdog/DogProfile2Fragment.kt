package com.viewpoint.dangder.view.initdog

import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.chip.Chip
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogProfile2Binding
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.viewmodel.RegisterDogViewModel
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy


class DogProfile2Fragment : BaseFragment<FragmentDogProfile2Binding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_profile_2

    private val registerDogViewModel: RegisterDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)

    override fun initView() {}

    override fun subscribeModel() {
        registerDogViewModel.action.subscribeBy(
            onNext = {
                     when(it){
                         is Actions.FetchInterests-> addInterestsChips(it.data)
                         is Actions.FetchCharacters-> addCharacterChips(it.data)
                         else->{
                             if(it is Actions.ShowErrorMessage){
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
        registerDogViewModel.fetchCharacters()
        registerDogViewModel.fetchInterests()
    }

    private val characterChipCheckedChangeListener=
        OnCheckedChangeListener { p0, p1 ->
            val value = p0.text.toString()
            if(p1) registerDogViewModel.addCharacter(value) else registerDogViewModel.removeCharacter(value)
        }

    private val interestChipCheckedChangeListener=
        OnCheckedChangeListener { p0, p1 ->
            val value = p0.text.toString()
            if(p1) registerDogViewModel.addInterest(value) else registerDogViewModel.removeInterest(value)
        }

    private fun addCharacterChips(characters : Array<String>){
        val group = binding.initdogCharacterList

        characters.forEach {
            group.addView(createChip(it, characterChipCheckedChangeListener))
        }
    }

    private fun addInterestsChips(interests : Array<String>){
        val group = binding.initdogInterestList

        interests.forEach {
            group.addView(createChip(it, interestChipCheckedChangeListener))
        }
    }

    private fun createChip(data : String, checkedChangeListener: OnCheckedChangeListener): Chip {
        return Chip(requireContext()).apply {
            text = data
            isCheckable = true
            setChipBackgroundColorResource(R.color.main_opacity_10)
            setOnCheckedChangeListener(checkedChangeListener)
        }
    }



}