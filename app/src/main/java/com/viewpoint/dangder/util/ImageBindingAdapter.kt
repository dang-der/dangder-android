package com.viewpoint.dangder.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("fullSizeBackground")
fun ImageView.fullSizeBackground(imageUrl: String?) {
    Glide.with(this.getContext())
        .load("https://storage.googleapis.com/$imageUrl")
        .into(this)
}