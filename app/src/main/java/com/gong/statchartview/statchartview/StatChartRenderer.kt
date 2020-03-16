package com.gong.statchartview.statchartview

import android.graphics.PointF
import android.util.Log
import com.gong.statchartview.statchartview.utils.MathUtils

class StatChartRenderer (
    private val chartView: ChartViewContract ,
    private val chartConfig: ChartConfig
) {

    fun setData(
        radius: Float ,
        statDataList: List<StatData>
    ) {
//        statDataList.toPoint()

        chartView.drawLine()

//        statList.forEach {
//            statChartViewPointList.add(
//                StatChartViewPoints(radius = (radius * ((it.value) / maxData)).toFloat()))
//        }
//
//        for (i in 0 until statChartViewPointList.size) {
//
//            Log.e("Radius", " ${statChartViewPointList[i].radius} " )
//
//            val x = MathUtils.getCosX(
//                centerX,
//                statChartViewPointList[i].radius,
//                MathUtils.degreeToRadians(MathUtils.getAngle(pointsCount) * i)
//            )
//            val y = MathUtils.getSinY(
//                centerY,
//                statChartViewPointList[i].radius,
//                MathUtils.degreeToRadians(MathUtils.getAngle(pointsCount) * i)
//            )
//            statChartViewPointList[i].point = PointF(x,y)
//        }

    }

}