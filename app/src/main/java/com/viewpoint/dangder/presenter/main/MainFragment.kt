package com.viewpoint.dangder.presenter.main

import android.app.Notification.Action
import android.content.Intent
import android.view.View
import androidx.core.os.bundleOf
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentMainBinding
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.presenter.adapter.AroundDogListAdapter
import com.viewpoint.dangder.presenter.chat.ChatRoomActivity
import com.viewpoint.dangder.presenter.dialog.BuyPassTicketDialog
import com.viewpoint.dangder.presenter.dialog.MatchedDialog
import com.viewpoint.dangder.util.showErrorSnackBar
import com.viewpoint.dangder.util.showSuccessSnackBar
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.StackFrom
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber.Forest.tag

class MainFragment : BaseFragment<FragmentMainBinding>(), CardStackListener {
    override val layoutId: Int
        get() = R.layout.fragment_main
    private val mainViewModel: MainViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)

    private var buyPassTicketDialog: BuyPassTicketDialog? = null

    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private val aroundDogListAdapter by lazy {
        AroundDogListAdapter(
            handleClickPassTicket = { mainViewModel.checkBuyPassTicket(it.id) },
            handleClickDogInfo = {
                val bundle = bundleOf("dogId" to it)
                findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
            }
        )
    }
    private var currentPage = 1

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
                    is Actions.FetchMoreAroundDogs -> {
                        val new = aroundDogListAdapter.currentList + it.data

                        aroundDogListAdapter.submitList(new)
                    }
                    is Actions.ShowSuccessMessage -> {
                        showSuccessSnackBar(binding.root, it.message)
                        buyPassTicketDialog?.dismiss()
                    }

                    is Actions.Matched -> {
                        val matchedDialog = MatchedDialog(pairDog = it.pairDog, mainViewModel = mainViewModel)

                        if (matchedDialog.isAdded.not()) {
                            matchedDialog.show(requireActivity().supportFragmentManager, matchedDialog.tag)
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
                        startActivity(
                            Intent(requireActivity(), ChatRoomActivity::class.java).apply {
                                putExtra("chatRoomId", it.roomId)
                            }
                        )
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
        mainViewModel.fetchAroundDogs()
    }

    override fun onCardSwiped(direction: Direction?) {
        if (direction == Direction.Right) {
            val current = aroundDogListAdapter.currentList[cardStackLayoutManager.topPosition - 1]
            mainViewModel.like(current.id)
        }

        if (cardStackLayoutManager.topPosition == aroundDogListAdapter.itemCount - 1) {
            mainViewModel.fetchMore(currentPage + 1)
            currentPage += 1
        }

    }

    private fun initAroundDogList() {
        cardStackLayoutManager = CardStackLayoutManager(requireActivity(), this)
        binding.mainAroundDogList.layoutManager = cardStackLayoutManager
        binding.mainAroundDogList.adapter = aroundDogListAdapter

        cardStackLayoutManager.setStackFrom(StackFrom.Top)
        cardStackLayoutManager.setScaleInterval(0.9f)
        cardStackLayoutManager.setDirections(Direction.HORIZONTAL)
    }

    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardDisappeared(view: View?, position: Int) {}
    override fun onCardDragging(direction: Direction?, ratio: Float) {}
    override fun onCardRewound() {}
    override fun onCardCanceled() {}
}