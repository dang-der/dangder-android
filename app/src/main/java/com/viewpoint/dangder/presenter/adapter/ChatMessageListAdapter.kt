package com.viewpoint.dangder.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viewpoint.dangder.databinding.ItemMessageListBinding
import com.viewpoint.dangder.domain.entity.ChatMessage

class ChatMessageListAdapter :
    ListAdapter<ChatMessage, ChatMessageListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemMessageListBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data : ChatMessage){

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMessageListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    fun addMessage(message:ChatMessage){
        val newList = currentList.toMutableList().apply { add(message) }
        submitList(newList)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
                return oldItem.equals(newItem)
            }


        }
    }

}