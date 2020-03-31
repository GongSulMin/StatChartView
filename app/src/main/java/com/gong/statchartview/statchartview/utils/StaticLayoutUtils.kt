package com.gong.statchartview.statchartview.utils

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
                100
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
}