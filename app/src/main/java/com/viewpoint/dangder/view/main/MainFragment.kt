package com.viewpoint.dangder.view.main

import android.view.View
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentChatListBinding
import com.viewpoint.dangder.databinding.FragmentMainBinding
import com.viewpoint.dangder.databinding.FragmentMyProfileBinding
import com.viewpoint.dangder.databinding.FragmentTodayBinding
import com.viewpoint.dangder.view.adapter.AroundDogListAdapter
import com.viewpoint.dangder.view.data.AroundDog
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom

class MainFragment : BaseFragment<FragmentMainBinding>(), CardStackListener {
    override val layoutId: Int
        get() = R.layout.fragment_main
    private val cardStackLayoutManager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val aroundDogListAdapter by lazy {  AroundDogListAdapter()}


    override fun initView() {
        initAroundDogList()
    }

    override fun subscribeModel() {

    }

    override fun initData() {

    }

    private fun initAroundDogList(){
        binding.mainAroundDogList.layoutManager = cardStackLayoutManager
        binding.mainAroundDogList.adapter = aroundDogListAdapter

        cardStackLayoutManager.setStackFrom(StackFrom.Top)
        cardStackLayoutManager.setScaleInterval(0.9f)
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL)

        val fakeData = (1..10).map { AroundDog(it.toString(), "name ${it}") }
        aroundDogListAdapter.submitList(fakeData)
    }



    override fun onCardSwiped(direction: Direction?) {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardDisappeared(view: View?, position: Int) {}

    override fun onCardDragging(direction: Direction?, ratio: Float) {}
    override fun onCardRewound() {}
    override fun onCardCanceled() {}
}