package com.viewpoint.dangder.presenter.initdog

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDogRegNumberBinding
import com.viewpoint.dangder.util.showErrorSnackBar
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.time.LocalDate


class DogRegisterNumberCheckFragment : BaseFragment<FragmentDogRegNumberBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_dog_reg_number

    private val initDogViewModel: InitDogViewModel by hiltNavGraphViewModels(R.id.init_dog_nav_graph)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        binding.initdogNextButton.setOnClickListener {
            val regNum = binding.initDogRegNumInput.text.toString()

            val y = binding.initdogBirthYearInput.text.toString()
            val m = binding.initdogBirthMonthInput.text.toString()
            val d = binding.initdogBirthDayInput.text.toString()
            val now = LocalDate.now().year

            if (regNum.isBlank() || y.isBlank() || m.isBlank() || d.isBlank()) {
                showErrorSnackBar(binding.root, "정보를 입력해 주세요.")
                return@setOnClickListener
            }

            if (y.toInt() !in 1900..now || m.toInt() !in 1..12 || d.toInt() !in 1..31) {
                showErrorSnackBar(binding.root, "올바른 생년월일을 입력해 주세요.")
                return@setOnClickListener
            }

            if (regNum.length > 15) {
                showErrorSnackBar(binding.root, "올바른 등록번호를 입력해 주세요.")
                return@setOnClickListener
            }

            val ownerBirth = (String.format("%04d", y.toInt()) + String.format(
                "%02d",
                m.toInt()
            ) + String.format("%02d", d.toInt())).substring(2)

            initDogViewModel.checkRegisteredDog(regNum, ownerBirth)
        }

    }

    override fun subscribeModel() {
        initDogViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    Actions.GoToNextPage -> findNavController().navigate(R.id.action_dogRegisterNumberCheckFragment_to_dogProfile1Fragment)
                    Actions.ShowLoadingDialog -> showLoadingDialog()
                    Actions.HideLoadingDialog -> hideLoadingDialog()
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
        val userId = requireActivity().intent.getStringExtra("userId")
        initDogViewModel._userId = userId
    }
}