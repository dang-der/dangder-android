package com.viewpoint.dangder.presenter.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDetailBinding
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.util.convertSPtoPX
import com.viewpoint.dangder.util.showErrorSnackBar
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber
import kotlin.math.abs


// 참고 : https://github.com/saulmm/CoordinatorBehaviorExample/blob/master/app/src/main/res/layout/activity_main.xml

class DetailFragment : BaseFragment<FragmentDetailBinding>(), AppBarLayout.OnOffsetChangedListener {
    override val layoutId: Int
        get() = R.layout.fragment_detail

    private val detailViewModel : DetailViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun initView() {
        initAppBar()
        initMenu()
        handleClickBackArrow()
    }

    override fun subscribeModel() {
        detailViewModel.action.subscribeBy(
            onNext = {
                     when(it){
                         is Actions.FetchOneDog -> {
                             binding.dog = it.data
                             initChips(it.data)
                         }
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
        val dogId = arguments?.get("dogId").toString()
        detailViewModel.fetchData(dogId)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat()/ maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun initChips(dog : Dog){
        val characters = dog.characters ?:return
        characters.forEach {
            binding.detailCharactersGroup.addView(createChip(it))
        }

        val interests = dog.interests ?:return
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

    private fun initMenu(){
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // todo : 메뉴 선택시 동작 구현하기 - 좋아요, 신고하기
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    private fun initAppBar(){
        binding.detailToolbar.title = ""
        binding.detailAppbar.addOnOffsetChangedListener(this)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.detailToolbar)
        startAlphaAnimation(binding.detailToolbarContainer, 0, View.INVISIBLE)
    }

    private fun handleClickBackArrow(){
        val finish = View.OnClickListener { findNavController().popBackStack() }

        binding.detailAppbarBackBtn.setOnClickListener(finish)
        binding.detailToolbarBackBtn.setOnClickListener(finish)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(binding.detailToolbarContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleVisible = true
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(binding.detailToolbarContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
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

    companion object{
        private const val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
        private const val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
        private const val ALPHA_ANIMATIONS_DURATION = 200L
    }

}