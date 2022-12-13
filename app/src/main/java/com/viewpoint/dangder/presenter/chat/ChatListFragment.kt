package com.viewpoint.dangder.presenter.chat

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.viewpoint.dangder.R
import com.viewpoint.dangder.base.BaseFragment
import com.viewpoint.dangder.databinding.FragmentChatListBinding
import com.viewpoint.dangder.presenter.action.Actions
import com.viewpoint.dangder.util.showErrorSnackBar
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber

class ChatListFragment : BaseFragment<FragmentChatListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_chat_list

    private val chatListViewMode: ChatListViewModel by hiltNavGraphViewModels(R.id.main_nav_graph)

    override fun initView() {

    }

    override fun subscribeModel() {
        chatListViewMode.action.subscribeBy(
            onNext = {
                when (it) {
                    is Actions.FetchChatRooms->{
                        Timber.d(it.rooms.toString())
                    }
                    else -> {
                        if (it is Actions.ShowErrorMessage) {
                            showErrorSnackBar(binding.root, it.message)
                        }
                    }
                }
            },
            onError = {}
        ).addTo(compositeDisposable)
    }

    override fun initData() {
        chatListViewMode.fetchChatRooms()
    }
}