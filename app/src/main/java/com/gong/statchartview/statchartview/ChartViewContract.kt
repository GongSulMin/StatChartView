package com.gong.statchartview.statchartview

import android.graphics.Path
import com.gong.statchartview.statchartview.option.LineOption

interface ChartViewContract {

    fun invalidate()
    fun postInvalidate()
    fun drawLine(points: List<StatChartViewPoints>, lineOption: LineOption)
    fun drawLine(
        path: Path,
        lineOption: LineOption
    )

}