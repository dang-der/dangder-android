package com.viewpoint.dangder.util

import android.content.Context
import android.util.TypedValue

fun convertSPtoPX(context: Context, sp : Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)
}