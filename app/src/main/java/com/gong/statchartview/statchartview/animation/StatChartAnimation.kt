package com.gong.statchartview.statchartview.animation

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import androidx.core.animation.addListener
import androidx.core.graphics.toPoint
import com.gong.statchartview.statchartview.StatChartViewPoints

class StatChartAnimation  {

    fun animate(
        onStart: (() -> Unit) = {} ,
        onEnd: (() -> Unit) = {} ,
        data: List<StatChartViewPoints> = listOf() ,
        action: (Float) -> Unit) {


        ValueAnimator.ofFloat(0f,  1f).apply {
            duration = 1500
            addUpdateListener { valueAnimator ->
                action.invoke(valueAnimator.animatedValue as Float)
            }
            addListener(
                onStart = {
                    onStart.invoke()
                } ,
                onEnd =  {
                    onEnd.invoke()
                }
            )
        }.start()

    }

}