package com.gong.statchartview.statchartview.utils

import kotlin.math.cos
import kotlin.math.sin

object MathUtils  {
    fun degreeToRadians(angle: Float): Double {
//        (PI * angle) / 180
        return  Math.toRadians(angle.toDouble())
    }

    fun getAngle(pointCount: Int): Float {
        return 360F / pointCount
    }

    fun getCosX(
        centerX:Float ,
        radius: Float ,
        angle: Double
    ): Float {
        return centerX + (radius) * cos(angle).toFloat()
    }

    fun getSinY(
        centerY:Float ,
        radius: Float ,
        angle: Double
    ): Float {
        return centerY + (radius) * sin(angle).toFloat()
    }
}