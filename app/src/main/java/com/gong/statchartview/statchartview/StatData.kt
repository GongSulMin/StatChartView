package com.gong.statchartview.statchartview

import com.gong.statchartview.statchartview.utils.MathUtils

const val DEFAULT_STAT_MAX_VALUE = 100.0
// 외부적으로 사람들이 보는 데이터
data class  StatData(
    var value: Double
) {

    constructor(builder: Builder): this(
        builder.value
    )

    object Builder {
        var value: Double = 0.0
            private set

        fun value(value: Double): Builder = apply {
            this.value = value
        }

        fun build(): StatData {
            return StatData(this)
        }

    }

 }

fun List<StatData>.toPoint(maxValue: Double , maxRadius: Float): List<StatChartViewPoints> {

    val maxData: Double = maxValue
    val pointsCount = this.size

    return this.mapIndexed { index , data ->

        val radius = maxRadius * (data.value / maxData).toFloat()
        val angle = MathUtils.degreeToRadians(MathUtils.getAngle(pointsCount) * index)

        StatChartViewPoints(
            MathUtils.getCosX(0f, radius, angle),
            MathUtils.getSinY(0f, radius, angle),
            radius
        )
    }
}
