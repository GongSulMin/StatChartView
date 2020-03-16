package com.gong.statchartview.statchartview

import android.graphics.PointF
import android.util.Log

// 내부적으로 쓰는 데이터
data class StatChartViewPoints(
    var point: PointF = PointF(0f , 0f) ,
    var radius: Float = 300f
) {

    fun setOptions(action: StatChartViewPoints.() -> Unit): StatChartViewPoints{
       return this.apply(action)
    }

    fun setPoint(x: Float , y: Float) {
        point.x = x
        point.y = y
    }

    fun rangeInPoint(
        touchX: Float ,
        touchY: Float ,
        radius: Float
    ): Boolean{
        return  touchX.contains((point.x - radius) ,(point.x + radius))
                && touchY.contains((point.y - radius) ,(point.y + radius))
    }

    private fun Float.contains(
        start: Float ,
        end: Float
    ): Boolean{
        return this >= start && this < end
    }
}