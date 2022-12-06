package com.viewpoint.dangder.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viewpoint.dangder.databinding.ItemAroundDogListBinding
import com.viewpoint.dangder.view.data.AroundDog

class AroundDogListAdapter(
    private val handleClickPassTicket : (dog : AroundDog) -> Unit
) : ListAdapter<AroundDog, AroundDogListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemAroundDogListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: AroundDog) {
            binding.dog = data
            binding.mainCardPassTicketBtn.setOnClickListener{
                handleClickPassTicket(data)
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
        val diffUtil = object : DiffUtil.ItemCallback<AroundDog>() {
            override fun areItemsTheSame(oldItem: AroundDog, newItem: AroundDog): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AroundDog, newItem: AroundDog): Boolean {
                return oldItem == newItem
            }

        }
    }
}