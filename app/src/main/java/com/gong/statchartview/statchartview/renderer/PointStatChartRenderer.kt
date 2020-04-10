package com.gong.statchartview.statchartview.renderer

import android.graphics.Path
import android.graphics.PathMeasure
import com.gong.statchartview.statchartview.ChartViewContract
import com.gong.statchartview.statchartview.StatChartViewPoints
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.data.Line
import com.gong.statchartview.statchartview.toPoint
import com.gong.statchartview.statchartview.utils.toPath


/**
 *
 *
 *          PathMeasure로 구현 하면 데이터 마다 있어야됨
 *          android dependency를 좀 없애고 싶은데 ??
 *
 *          TODO
 *          이 클래스에서  path에 대한 데이터가 들어가는거 자체가 매우 기분이 나쁘다;
 */
class PointStatChartRenderer(
    private val chartView: ChartViewContract,
    private var statChartAnimation: StatChartAnimation
) : Renderer {

    private var lines: MutableList<Line> = mutableListOf()
    private var maxStatValue = 100.0
    private var animateVar = 1f
    private var pathMeasures = mutableListOf<PathMeasure>()
    private var tempPaths = mutableListOf<Path>()

    override fun draw() {

        if (pathMeasures.isEmpty()) {
            lines.forEach { line ->
                val path = line.statData.toPoint(100.0, 300f)
                    .map { stat ->
                        StatChartViewPoints(
                            stat.pointX + 540,
                            stat.pointY + 760,
                            stat.radius
                        )
                    }
                    .toPath()

                val pathMeasure = PathMeasure().apply {
                    setPath(path, true)
                }

                tempPaths.add(Path())
                pathMeasures.add(pathMeasure)
            }
        } else {
            lines.forEachIndexed { index, line ->
                pathMeasures[index].getSegment(
                    0f,
                    pathMeasures[index].length * animateVar,
                    tempPaths[index],
                    true
                )
                chartView.drawLine(tempPaths[index], line.lineOption)
            }

        }
    }

    override fun anim(
        radius: Float,
        lines: List<Line>,
        animation: StatChartAnimation
    ) {
        this.lines = lines.toMutableList()
        this.maxStatValue = 100.0
        this.statChartAnimation = animation

        animation.animate {
            animateVar = it
            viewInvalidate()
        }
    }

    private fun viewInvalidate() {
        chartView.invalidate()
    }

}