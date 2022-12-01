package com.viewpoint.dangder.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.viewpoint.dangder.R
import com.viewpoint.dangder.databinding.ItemImageListBinding
import java.util.*

class ImageListAdapter : ListAdapter<Uri, ImageListAdapter.ViewHolder>(diffUtil) {

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: ImageListAdapter.ViewHolder)
    }

    var onStartDragListener: OnStartDragListener? = null
    private var editMode = false

    inner class ViewHolder(val binding: ItemImageListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(uri: Uri) {
            Glide.with(binding.root)
                .load(uri)
                .fitCenter()
                .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
                .into(binding.initdogImageItem)

            binding.initdogImageItem.setOnLongClickListener {
                toggleEditMode()
                return@setOnLongClickListener true
            }
        }

        fun startEditMode() {
            binding.initdogImageDeleteBtn.isVisible = true
            binding.initdogImageItem.animation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.shake)
            binding.initdogImageItem.animation.startNow()

            binding.initdogImageItem.setOnTouchListener { view, motionEvent ->
                if (motionEvent.actionMasked == MotionEvent.ACTION_DOWN) {
                    onStartDragListener?.onStartDrag(this)
                }

                return@setOnTouchListener false
            }
        }

        fun endEditMode() {
            binding.initdogImageDeleteBtn.isVisible = false
            binding.initdogImageItem.clearAnimation()
            binding.initdogImageItem.setOnTouchListener { view, motionEvent -> return@setOnTouchListener false }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemImageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.initdogImageDeleteBtn.setOnClickListener { deleteItem(holder.adapterPosition) }
        if (editMode) holder.startEditMode() else holder.endEditMode()
    }

    fun addItem(uri: Uri) {
        val new = this.currentList.toMutableList().apply {
            add(uri)
        }

        this.submitList(new)
    }

    fun swapItem(from: Int, to: Int) {
        val copy = this.currentList.toMutableList()
        Collections.swap(copy, from, to)
        this.submitList(copy)
    }

    private fun deleteItem(position: Int) {
        val copy = this.currentList.toMutableList().apply { removeAt(position) }
        if (copy.isEmpty()) editMode = false

        this.submitList(copy)
    }

    private fun toggleEditMode() {
        editMode = editMode.not()
        this.notifyDataSetChanged()
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem.lastPathSegment == newItem.lastPathSegment
            }

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
                return oldItem == newItem
            }

        }
    }
}