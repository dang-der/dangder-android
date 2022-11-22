package com.viewpoint.dangder.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(context: Context, currentFocus : View?){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}