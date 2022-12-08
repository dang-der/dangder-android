package com.viewpoint.dangder.presenter.main

import android.animation.ValueAnimator
import android.app.Notification.Action
import android.graphics.Color
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDetailBinding
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.presenter.dialog.BuyPassTicketDialog
import com.viewpoint.dangder.presenter.dialog.MatchedDialog
import com.viewpoint.dangder.util.showErrorSnackBar
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber
import kotlin.math.abs


// 참고 : https://github.com/saulmm/CoordinatorBehaviorExample/blob/master/app/src/main/res/layout/activity_main.xml

class DetailFragment : BaseFragment<FragmentDetailBinding>(), AppBarLayout.OnOffsetChangedListener {
    override val layoutId: Int
        get() = R.layout.fragment_detail

    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)

    private var buyPassTicketDialog: BuyPassTicketDialog? = null
    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun initView() {
        initAppBar()
        handleClickBackArrow()
        handleClickPassTicket()
        handleClickLike()
    }

    override fun subscribeModel() {
        mainViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.FetchOneDog -> {
                        binding.dog = it.data
                        initChips(it.data)
                    }

                    is Actions.Matched -> {
                        val matchedDialog =
                            MatchedDialog(pairDog = it.pairDog, mainViewModel = mainViewModel)
                        if (matchedDialog.isAdded.not()) {
                            matchedDialog.show(
                                requireActivity().supportFragmentManager,
                                matchedDialog.tag
                            )
                        }
                    }
                    is Actions.ShowBuyPassTicketDialog -> {
                        buyPassTicketDialog = BuyPassTicketDialog(mainViewModel, it.pairDogId)
                        if (buyPassTicketDialog?.isAdded == true) return@subscribeBy

                        buyPassTicketDialog?.show(
                            requireActivity().supportFragmentManager,
                            buyPassTicketDialog!!.tag
                        )
                    }

                    is Actions.GoToChatRoomPage->{
                        val b = bundleOf("chatRoomId" to it.roomId)
                        findNavController().navigate(R.id.action_mainFragment_to_chatRoomFragment, b)
                    }
//                    Actions.ShowLoadingDialog -> showLoadingDialog()
//                    Actions.HideLoadingDialog -> hideLoadingDialog()
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
                            Timber.tag("error").d(it.message)
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
        val dogId = arguments?.get("dogId").toString()
        mainViewModel.fetchOneDog(dogId)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat() / maxScroll.toFloat()

        handleAppbarTitleVisibility(percentage)
        handleToolbarTitleVisibility(percentage)
    }


    private fun handleClickBackArrow() {
        val finish = View.OnClickListener { findNavController().popBackStack() }

        binding.detailAppbarBackBtn.setOnClickListener(finish)
        binding.detailToolbarBackBtn.setOnClickListener(finish)
    }

    private fun handleClickPassTicket() {
        val dogId = arguments?.getString("dogId") ?: return
        binding.detailPassTicketBtn.setOnClickListener {
            mainViewModel.checkBuyPassTicket(dogId)
        }
    }

    private fun handleClickLike() {
        binding.detailLikeBtn.setOnClickListener {
            val dogId = arguments?.getString("dogId") ?: return@setOnClickListener
            mainViewModel.like(dogId)
        }
    }

    private fun handleClickReport() {
        // todo : 신고하기 페이지 이동 기능 구현
    }


    private fun initChips(dog: Dog) {
        val characters = dog.characters ?: kotlin.run {
            binding.detailCharactersContainer.isVisible = false
            return
        }

        characters.forEach {
            binding.detailCharactersGroup.addView(createChip(it))
        }

        val interests = dog.interests ?: run {
            binding.detailInterestsContainer.isVisible = false
            return
        }

        interests.forEach {
            binding.detailInterestsGroup.addView(createChip(it))
        }
    }

    private fun createChip(data: String): Chip {
        return Chip(requireContext()).apply {
            text = data
            setChipBackgroundColorResource(R.color.main_opacity_10)
        }
    }

    private fun initAppBar() {
        binding.detailToolbar.title = ""
        binding.detailAppbar.addOnOffsetChangedListener(this)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailToolbar)
        startAlphaAnimation(binding.detailToolbarContainer, 0, View.INVISIBLE)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(
                    binding.detailToolbarContainer,
                    ALPHA_ANIMATIONS_DURATION,
                    View.VISIBLE
                )

                startColorAnimation(ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(
                    binding.detailToolbarContainer,
                    ALPHA_ANIMATIONS_DURATION,
                    View.INVISIBLE
                )
                startColorAnimation(ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAppbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(
                    binding.detailAppbarContainer,
                    ALPHA_ANIMATIONS_DURATION,
                    View.INVISIBLE
                )
                mIsTheTitleContainerVisible = false
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(
                    binding.detailAppbarContainer,
                    ALPHA_ANIMATIONS_DURATION,
                    View.VISIBLE
                )
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation =
            if (visibility == View.VISIBLE) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }

    private fun startColorAnimation(duration: Long, visibility: Int) {
        val colorAnimation = if (visibility === View.VISIBLE)
            ValueAnimator.ofArgb(
                Color.TRANSPARENT,
                ContextCompat.getColor(requireContext(), R.color.main)
            ) else
            ValueAnimator.ofArgb(
                ContextCompat.getColor(requireContext(), R.color.main),
                Color.TRANSPARENT
            )

        colorAnimation.duration = duration
        colorAnimation.addUpdateListener {

            binding.detailToolbar.setBackgroundColor(it.animatedValue as Int)
        }
        colorAnimation.start()
    }

    companion object {
        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private const val ALPHA_ANIMATIONS_DURATION = 500L
    }

}