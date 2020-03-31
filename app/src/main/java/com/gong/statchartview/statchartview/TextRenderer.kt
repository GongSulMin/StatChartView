package com.gong.statchartview.statchartview

import android.graphics.Color
import android.text.TextPaint
import com.gong.statchartview.statchartview.utils.StaticLayoutUtils

class TextRenderer(
    private val chartView: ChartViewContract,
    private val points: List<List<StatChartViewPoints>>
) {

    fun drawText(list: List<String>) {
        for (i in list.indices) {
            chartView.drawText(
                StaticLayoutUtils.createStaticLayout(
                    list[i],
                    TextPaint().apply {
                        textSize = 20F
                        color = Color.RED
                    }
                ),
                points.last()[i].pointX + 540,
                points.last()[i].pointY + 760
            )
        }
    }
}