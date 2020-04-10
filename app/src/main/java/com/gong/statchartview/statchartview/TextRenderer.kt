package com.gong.statchartview.statchartview

import android.graphics.Color
import android.text.TextPaint
import android.util.Log
import com.gong.statchartview.statchartview.utils.StaticLayoutUtils

class TextRenderer(
    private val chartView: ChartViewContract,
    private val lastPoint: List<StatChartViewPoints>,
    private val points: List<StatChartViewPoints>
//    List<List<StatChartViewPoints>>
) {
    fun drawText(texts: List<String>) {

        if (texts.size != points.size) {
            Log.w("StactChartView", " Text length not matched point length ")
        }

        for (i in points.indices) {
            val staticLayout =
                StaticLayoutUtils.createStaticLayout(
                    texts[i],
                    TextPaint().apply {
                        textSize = 50f
                        color = Color.RED
                    }
                )

            chartView.drawText(
                staticLayout,
                (lastPoint[i].pointX + 540) - staticLayout.width / 2,
                (lastPoint[i].pointY + 760) - staticLayout.height / 2
            )
        }
    }

    fun calculateTextPoint(list: MutableList<StatData>, radius: Float): Float {

        return 0f
    }

}