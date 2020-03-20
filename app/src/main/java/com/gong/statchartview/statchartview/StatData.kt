package com.gong.statchartview.statchartview

import android.graphics.PointF
import com.gong.statchartview.statchartview.utils.MathUtils

const val DEFAULT_STAT_MAX_VALUE = 100.0
// 외부적으로 사람들이 보는 데이터
data class  StatData(
    var label: String? = null ,
    var value: Double
) {

    constructor(builder: Builder): this(
        builder.label ,
        builder.value
    )

    object Builder {
        var label: String? = null
            private set
        var value: Double = 0.0
            private set

        fun label(label: String?): Builder = apply {
            this.label = label
        }

        fun value(value: Double): Builder = apply {
            this.value = value
        }

        fun build(): StatData {
            return StatData(this)
        }

    }

 }

fun List<StatData>.toPoint(maxRadius: Float): List<StatChartViewPoints> {

    val maxData: Double = this.map { it.value }.max() ?: DEFAULT_STAT_MAX_VALUE
    val pointsCount = this.size

    return this.mapIndexed { index , data ->

        val radius = maxRadius * (data.value / maxData).toFloat()
        val angle = MathUtils.degreeToRadians(MathUtils.getAngle(pointsCount) * index)

        StatChartViewPoints(
            MathUtils.getPoint(radius , angle) ,
            radius
        )
    }
}
