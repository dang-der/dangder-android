package com.viewpoint.dangder.util

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showErrorSnackBar(view : View, message : String){
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    snackBar.setTextColor(Color.WHITE)
    snackBar.setBackgroundTint(Color.parseColor("#ED3F3F"))
    snackBar.show()
}