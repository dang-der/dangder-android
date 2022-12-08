package com.viewpoint.dangder.util

import android.media.Image
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

const val prefix = "https://storage.googleapis.com/"
@BindingAdapter("fullSizeBackground")
fun ImageView.fullSizeBackground(imageUrl: String?) {
    Glide.with(this.getContext())
        .load("$prefix$imageUrl")
        .into(this)
}

@BindingAdapter("profileImage")
fun ImageView.profileImage(imageUrl: String?){
    imageUrl?: return

    Glide.with(this.context)
        .load("$prefix$imageUrl")
        .circleCrop()
        .into(this)
}

@BindingAdapter("backgroundImage")
fun ImageView.backgroundImage(imageUrl : String?){
    imageUrl?:return

    Glide.with(this.context)
        .load("${prefix}${imageUrl}")
        .into(this)
}