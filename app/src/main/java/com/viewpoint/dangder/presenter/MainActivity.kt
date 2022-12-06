package com.viewpoint.dangder.presenter

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseActivity
import com.viewpoint.dangder.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int = R.layout.activity_main

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.mainBottomNavigation.setupWithNavController(navController)
    }

    override fun subscribe() {

    }

    override fun initData() {

    }
}