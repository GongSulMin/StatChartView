package com.gong.statchartview.statchartview.utils

import android.graphics.Path
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