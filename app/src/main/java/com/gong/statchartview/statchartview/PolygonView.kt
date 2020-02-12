package com.gong.statchartview.statchartview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gong.statchartview.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class PolygonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var pointsCount: Int = 0

    var startX = 20f * cos(0.0)
    var startY = 20f * sin(0.0)

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10F
        style = Paint.Style.STROKE
        color = Color.GREEN
    }

    val path = Path()

    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PolygonView)
        obtainStyledAttributes.let {
           pointsCount = it.getInt(R.styleable.PolygonView_polygon_point_count , 2)
        }
        obtainStyledAttributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        val centerX  = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()



        for (j in 1 until 5) {
            val angle = 360f / (pointsCount + j)
            path.reset()
            for (i in 0 until pointsCount + j) {
                val startAngle = degreeToRadians(angle * i)
                val x = centerX + (j * 100f) * cos(startAngle).toFloat()
                val y = centerY + (j * 100f) * sin(startAngle).toFloat()

                val startAngle1 = degreeToRadians(angle * (i + 1))
                val x1 = centerX + (j * 100f) * cos(startAngle1).toFloat()
                val y1 = centerY + (j * 100f) * sin(startAngle1).toFloat()

                path.moveTo(x , y)
                path.lineTo(x1 , y1)
                canvas.drawPath(path , pathPaint)

                canvas.drawCircle(
                    x,
                    y,
                    10f,
                    circlePaint
                )

            }

        }
    }

    fun degreeToRadians(angle: Float): Double {
        return (PI * angle) / 180
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}