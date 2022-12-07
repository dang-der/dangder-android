package com.viewpoint.dangder.presenter.main

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentDetailBinding
import timber.log.Timber
import kotlin.math.abs


// 참고 : https://github.com/saulmm/CoordinatorBehaviorExample/blob/master/app/src/main/res/layout/activity_main.xml

class DetailFragment : BaseFragment<FragmentDetailBinding>(), AppBarLayout.OnOffsetChangedListener {
    override val layoutId: Int
        get() = R.layout.fragment_detail


    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200L

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    override fun initView() {
        initAppBar()
        initMenu()
        handleClickBackArrow()
    }

    override fun subscribeModel() {}

    override fun initData() {
        val dogId = arguments?.get("dogId").toString()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout!!.totalScrollRange
        val percentage = abs(verticalOffset).toFloat()/ maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun initMenu(){
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })
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

}