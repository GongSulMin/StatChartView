package com.gong.statchartview.statchartview.utils

import android.graphics.Path
import android.util.Log
import com.gong.statchartview.statchartview.StatChartViewPoints
import com.gong.statchartview.statchartview.utils.MathUtils.degreeToRadians
import com.gong.statchartview.statchartview.utils.MathUtils.getAngle
import com.gong.statchartview.statchartview.utils.MathUtils.getCosX
import com.gong.statchartview.statchartview.utils.MathUtils.getSinY
import kotlin.math.cos
import kotlin.math.sin

object PathUtils  {

    fun getPolygonPath(
        path: Path,
        radius: Float,
        pointsCount: Int,
        sides: Int,
        centerX: Float,
        centerY: Float
    ): Path {

        val angle = getAngle(pointsCount)
        val startAngle = degreeToRadians(angle * sides)
        val cosX =   getCosX(centerX , radius , startAngle)
        val sinY =  getSinY(centerY , radius , startAngle)

        path.lineTo(
            cosX ,
            sinY
        )

        return path
    }

}

fun List<StatChartViewPoints>.toPath(): Path {
    val path = Path()

    path.moveTo(this.first().pointX , this.first().pointY)

    for (i in this.indices) {
        path.lineTo(this[i].pointX , this[i].pointY)
    }

    path.close()

    return path

}