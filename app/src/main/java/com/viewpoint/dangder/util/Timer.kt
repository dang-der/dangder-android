package com.viewpoint.dangder.util

import android.os.CountDownTimer
import android.widget.TextView

class Timer(
    start: Long, interval: Long, private val textView: TextView
) : CountDownTimer(start, interval) {
    override fun onTick(p0: Long) {
        val m = p0 / (1000 *60)
        val s = (p0 % (1000 * 60)) /1000
        textView.text = "${String.format("%02d", m)} : ${String.format("%02d", s)}"
    }

    override fun onFinish() {
        textView.text = "00 : 00"
    }
}