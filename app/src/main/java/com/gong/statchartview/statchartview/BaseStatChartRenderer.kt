package com.gong.statchartview.statchartview

import com.gong.statchartview.statchartview.data.Line
import com.gong.statchartview.statchartview.option.LineOption


const val BASE_CHART_COUNT = 4

class BaseStatChartRenderer(
    private val chartView: ChartViewContract
) : Renderer {

    private val baseChatList = mutableListOf<List<StatChartViewPoints>>()
    private val pathOption = LineOption.Builder.setPathColor("#B0BEC5").setPathWidth(5f).build()
    private lateinit var textRenderer: TextRenderer
    private var texts = mutableListOf<String>()

    override fun dataLoad(radius: Float, lines: List<Line>) {

        val list = mutableListOf<StatData>()
//        val maxValue: Double = statDataList.map { it.value }.max() ?: 100.0
        val maxValue: Double = 100.0

        for (i in lines[0].statData.indices) {
            list.add(StatData.Builder.value(maxValue).build())
        }

        val space = radius / BASE_CHART_COUNT

        for (i in 0..BASE_CHART_COUNT) {
            baseChatList.add(list.toPoint(100.0, space * i))
        }

        textRenderer = TextRenderer(chartView, baseChatList)
    }

    fun showText() {
        textRenderer.drawText(texts)
    }

    fun setText(list: List<String>) {
        texts.clear()
        texts.addAll(list)
    }

    override fun draw(
    ) {
        for (list in baseChatList) {
            chartView.drawLine(
                list.map {
                    StatChartViewPoints(
                        it.pointX + 540,
                        it.pointY + 760,
                        it.radius
                    )
                },
                pathOption
            )
        }
    }


}