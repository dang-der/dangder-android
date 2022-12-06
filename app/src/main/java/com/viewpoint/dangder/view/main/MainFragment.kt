package com.viewpoint.dangder.view.main

import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.action.Actions
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentChatListBinding
import com.viewpoint.dangder.databinding.FragmentMainBinding
import com.viewpoint.dangder.databinding.FragmentMyProfileBinding
import com.viewpoint.dangder.databinding.FragmentTodayBinding
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.util.showSuccessSnackBar
import com.viewpoint.dangder.view.adapter.AroundDogListAdapter
import com.viewpoint.dangder.view.data.AroundDog
import com.viewpoint.dangder.view.dialog.BuyPassTicketDialog
import com.viewpoint.dangder.viewmodel.MainViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber

class MainFragment : BaseFragment<FragmentMainBinding>(), CardStackListener {
    override val layoutId: Int
        get() = R.layout.fragment_main
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)

    private val buyPassTicketDialog by lazy { BuyPassTicketDialog(mainViewModel) }
    private val cardStackLayoutManager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val aroundDogListAdapter by lazy { AroundDogListAdapter { mainViewModel.checkBuyPassTicket() } }


    override fun initView() {
        initAroundDogList()
    }

    override fun subscribeModel() {
        mainViewModel.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.FetchAroundDogs -> {
                        aroundDogListAdapter.submitList(it.data)
                    }
                    is Actions.ShowSuccessMessage->{
                        showSuccessSnackBar(binding.root, it.message)
                        buyPassTicketDialog.dismiss()
                    }
                    Actions.ShowBuyPassTicketDialog->{
                        if(buyPassTicketDialog.isAdded) return@subscribeBy
                        buyPassTicketDialog.show(requireActivity().supportFragmentManager, buyPassTicketDialog.tag)
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
        )
    }

    override fun initData() {
        mainViewModel.fetchData()
    }

    private fun initAroundDogList() {
        binding.mainAroundDogList.layoutManager = cardStackLayoutManager
        binding.mainAroundDogList.adapter = aroundDogListAdapter

        cardStackLayoutManager.setStackFrom(StackFrom.Top)
        cardStackLayoutManager.setScaleInterval(0.9f)
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL)
    }


    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            val current = aroundDogListAdapter.currentList[cardStackLayoutManager.topPosition]
            mainViewModel.like(current.id)
        }
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
    override fun onCardDragging(direction: Direction?, ratio: Float) {}
    override fun onCardRewound() {}
    override fun onCardCanceled() {}
}