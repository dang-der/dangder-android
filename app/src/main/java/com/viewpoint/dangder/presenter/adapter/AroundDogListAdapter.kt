package com.viewpoint.dangder.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viewpoint.dangder.databinding.ItemAroundDogListBinding
import com.viewpoint.dangder.presenter.uimodel.AroundDogItem

class AroundDogListAdapter(
    private val handleClickPassTicket : (dog : AroundDogItem) -> Unit,
    private val handleClickDogInfo : (dogId : String) -> Unit
) : ListAdapter<AroundDogItem, AroundDogListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemAroundDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AroundDogItem) {
            binding.dog = data
            binding.mainCardPassTicketBtn.setOnClickListener{
                handleClickPassTicket(data)
            }

            binding.mainCardInfoContainer.setOnClickListener {
                handleClickDogInfo(data.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAroundDogListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AroundDogItem>() {
            override fun areItemsTheSame(oldItem: AroundDogItem, newItem: AroundDogItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AroundDogItem, newItem: AroundDogItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}