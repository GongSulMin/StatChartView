package com.gong.statchartview.statchartview.renderer

import com.gong.statchartview.statchartview.ChartViewContract
import com.gong.statchartview.statchartview.StatChartViewPoints
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.data.Line
import com.gong.statchartview.statchartview.toPoint


/**
 *
 *
 *              여기서 애니메이션을 한다라는 개념이 아니라
 *              뷰에다가 뿌릴 포인트를 계산한다라는 말이 맞을 듯 싶슴당
 *
 */
class NoAnimRenderer(
    private val chartView: ChartViewContract,
    private val startChartAnimation: StatChartAnimation
) : Renderer {

    private var lines: MutableList<Line> = mutableListOf()
    private var maxStatValue = 100.0

    override fun draw() {
        lines.forEach {
            chartView.drawLine(
                it.statData.toPoint(this.maxStatValue, 300F).map { stat ->
                    StatChartViewPoints(
                        stat.pointX + 540,
                        stat.pointY + 760,
                        stat.radius
                    )
                },
                it.lineOption
            )
        }
    }

    override fun anim(radius: Float, lines: List<Line>, animation: StatChartAnimation) {
        this.lines = lines.toMutableList()
        this.maxStatValue = 100.0
        chartView.invalidate()
    }
}


