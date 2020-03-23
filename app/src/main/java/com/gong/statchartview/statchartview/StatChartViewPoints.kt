package com.gong.statchartview.statchartview

import android.graphics.PointF
import android.util.Log

data class StatChartViewPoints(
    var pointX: Float = 0f ,
    var pointY: Float = 0f ,
    var radius: Float = 300f
) {

    fun setOptions(action: StatChartViewPoints.() -> Unit): StatChartViewPoints{
       return this.apply(action)
    }

    fun setPoint(x: Float , y: Float) {
        this.pointX = x
        this.pointY = y
    }

    fun rangeInPoint(
        touchX: Float ,
        touchY: Float ,
        radius: Float
    ): Boolean{
        return  touchX.contains((pointX - radius) ,(pointX + radius))
                && touchY.contains((pointY - radius) ,(pointY + radius))
    }

    private fun Float.contains(
        start: Float ,
        end: Float
    ): Boolean{
        return this >= start && this < end
    }
}