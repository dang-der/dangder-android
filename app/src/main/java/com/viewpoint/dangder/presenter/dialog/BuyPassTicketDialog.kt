package com.viewpoint.dangder.presenter.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iamport.sdk.domain.core.Iamport
import com.viewpoint.dangder.databinding.DialogBuyPassticketBinding
import com.viewpoint.dangder.presenter.main.MainViewModel

class BuyPassTicketDialog(
    private val mainViewModel: MainViewModel
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBuyPassticketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Iamport.init(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBuyPassticketBinding.inflate(inflater, container, false)
        handleClickBuyPassTicket()
        return binding.root
    }

    private fun handleClickBuyPassTicket() {
        binding.paymentDialogBuyButton.setOnClickListener {
            mainViewModel.requestBuyPassTicket(Iamport)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Iamport.close()
    }
}