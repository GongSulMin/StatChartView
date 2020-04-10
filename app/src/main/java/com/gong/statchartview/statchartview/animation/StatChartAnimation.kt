package com.gong.statchartview.statchartview.animation

import android.animation.ValueAnimator
import androidx.core.animation.addListener
import com.gong.statchartview.statchartview.StatChartViewPoints

class StatChartAnimation(
    var animationDuration: Long
) {

    fun animate(
        onStart: (() -> Unit) = {},
        onEnd: (() -> Unit) = {},
        data: List<StatChartViewPoints> = listOf(),
        action: (Float) -> Unit
    ) {

        ValueAnimator.ofFloat(0f, 1f).apply {

            this.duration = animationDuration

            addUpdateListener { valueAnimator ->
                action.invoke(valueAnimator.animatedValue as Float)
            }

            addListener(
                onStart = {
                    onStart.invoke()
                },
                onEnd = {
                    onEnd.invoke()
                }
            )
        }.start()
    }
}