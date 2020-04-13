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
class ScaleStatChartRenderer(
    private val chartView: ChartViewContract,
    private var statChartAnimation: StatChartAnimation
) : Renderer {

    private var lines: MutableList<Line> = mutableListOf()
    private var maxStatValue = 100.0
    private var animatedValue = 1f

    override fun draw() {
        lines.forEach {
            chartView.drawLine(
                it.statData.toPoint(this.maxStatValue, 300F).map { stat ->
                    StatChartViewPoints(
                        stat.pointX * animatedValue + 540,
                        stat.pointY * animatedValue + 760,
                        stat.radius * animatedValue
                    )
                },
                it.lineOption
            )
        }
    }

    override fun anim(
        radius: Float,
        lines: List<Line>,
        animation: StatChartAnimation,
        animationDuration: Long
    ) {

        this.lines = lines.toMutableList()
        this.maxStatValue = 100.0
        this.statChartAnimation = animation

        statChartAnimation.animate(
            action = { value ->
                animatedValue = value
                chartView.invalidate()
            },
            animationDuration = animationDuration
        )
    }
}


