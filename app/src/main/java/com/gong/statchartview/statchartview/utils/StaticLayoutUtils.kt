package com.gong.statchartview.statchartview.utils

import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint

object StaticLayoutUtils {

    fun createStaticLayout(
        text: String,
        paint: TextPaint
    ): StaticLayout {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                paint,
                getTextLength(paint, text)
            ).build()

        } else {

            StaticLayout(
                text,
                paint,
                100,
                Layout.Alignment.ALIGN_CENTER,
                0F,
                0F,
                false
            )

        }
    }

    private fun getTextLength(paint: Paint, text: String): Int {
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        return paint.measureText(text).toInt()
    }
}