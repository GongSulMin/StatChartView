package com.gong.statchartview.statchartview

import android.graphics.Path
import android.graphics.PathMeasure
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.data.Line
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
    private val chartConfig: ChartConfig,
    private val statChartAnimation: StatChartAnimation
) : Renderer {

    private var lines: MutableList<Line> = mutableListOf()
    private var maxStatValue = 100.0
    private var animateVar = 1f
    private var pathMeasures = mutableListOf<PathMeasure>()
    private var tempPaths = mutableListOf<Path>()

    override fun draw() {

        if (pathMeasures.isEmpty()) {
            lines.forEachIndexed { index, it ->
                val path = it.statData.toPoint(100.0, 300f)
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

    override fun dataLoad(radius: Float, list: List<Line>) {
        this.lines = list.toMutableList()
        this.maxStatValue = 100.0

        statChartAnimation.animate {
            animateVar = it
            viewInvalidate()
        }

    }

    private fun viewInvalidate() {
        chartView.invalidate()
    }

}