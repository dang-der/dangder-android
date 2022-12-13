package com.viewpoint.dangder.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.viewpoint.dangder.databinding.ItemChatRoomListBinding
import com.viewpoint.dangder.presenter.uimodel.ChatRoomItem

class ChatRoomListAdapter(
    private val onItemClickListener: (ChatRoomItem) -> Unit
) : ListAdapter<ChatRoomItem, ChatRoomListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemChatRoomListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ChatRoomItem) {
            binding.room = data
            binding.root.setOnClickListener {
                onItemClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatRoomListBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<ChatRoomItem>() {
            override fun areItemsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChatRoomItem, newItem: ChatRoomItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


}