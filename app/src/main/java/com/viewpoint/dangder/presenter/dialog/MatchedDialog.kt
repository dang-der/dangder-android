package com.viewpoint.dangder.presenter.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.viewpoint.dangder.databinding.DialogMatchedBinding
import com.viewpoint.dangder.domain.entity.Dog
import com.viewpoint.dangder.presenter.main.MainViewModel


class MatchedDialog(
    private val mainViewModel : MainViewModel,
    private val pairDog: Dog,
) : DialogFragment() {

    private lateinit var binding: DialogMatchedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogMatchedBinding.inflate(inflater)

        initView()

        return binding.root
    }

    private fun initView() {
        binding.dog = pairDog
        handleClickClose()
        handleClickGoChat()
    }

    private fun handleClickGoChat() {
        binding.matchChatBtn.setOnClickListener {
            mainViewModel.enterChatRoom(pairDogId = pairDog.id)
            this.dismiss()
        }
    }

    private fun handleClickClose() {
        binding.matchCloseBtn.setOnClickListener { this.dismiss() }
    }

}