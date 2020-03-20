package com.gong.statchartview.statchartview

interface ChartViewContract {

    fun invalidate()
    fun postInvalidate()
    fun drawLine(points: List<StatChartViewPoints>)

}