package com.gong.statchartview.statchartview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gong.statchartview.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class StatChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val TAG = "StatChartView"

    private var pointsCount: Int = 0
    private var radius: Float = 300f

    private val centerX by lazy {
        (width / 2).toFloat()
    }
    private val centerY  by lazy {
        (height / 2).toFloat()
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10F
        style = Paint.Style.STROKE
        color = Color.GREEN
    }

    private val statChartViewPointList = mutableListOf<StatChartViewPoints>()
    private val statChartViewPoints = StatChartViewPoints()
    private val pointF = PointF()

    private val path = Path()

    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.StatChartView)
        obtainStyledAttributes.let {
            pointsCount = it.getInt(R.styleable.StatChartView_polygon_point_count , 5)
            radius = it.getFloat(R.styleable.StatChartView_polygon_radius , 300f)
        }
        obtainStyledAttributes.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        for (i in 0 until pointsCount) {
            val x = getCosX(centerX , radius , degreeToRadians(getAngle(pointsCount) * i))
            val y = getSinY(centerY , radius , degreeToRadians(getAngle(pointsCount) * i))
            val statChartViewPoints = StatChartViewPoints(PointF(x , y))
            statChartViewPointList.add(statChartViewPoints)
        }
    }
    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(getPolygonPath(radius , pointsCount) , pathPaint)
        for (statChartViewPoints in statChartViewPointList) {
            canvas.drawCircle(
                statChartViewPoints.point.x,
                statChartViewPoints.point.y,
                10f,
                circlePaint
            )
        }
    }
    private fun getPolygonPath(radius: Float , pointsCount: Int): Path {
        val angle = getAngle(pointsCount)

        val path = Path()
        path.moveTo(
            getCosX(centerX , radius , 0.0) ,
            getSinY(centerY , radius , 0.0)
        )

        for (i in 0 until pointsCount) {
            val startAngle = degreeToRadians(angle * i)
            path.lineTo(
                getCosX(centerX , radius , startAngle) ,
                getSinY(centerY , radius , startAngle)
            )
        }
        path.close()
        return path
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x
        val touchY = event.y


        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }

            MotionEvent.ACTION_UP -> {
            }

            MotionEvent.ACTION_MOVE -> {

            }
        }
        return false
    }

    private fun degreeToRadians(angle: Float): Double {
        return (PI * angle) / 180
    }

    private fun getAngle(pointCount: Int): Float {
        return 360F / pointCount
    }

    private fun getCosX(
        centerX:Float ,
        radius: Float ,
        angle: Double
    ): Float {
        return centerX + (radius) * cos(angle).toFloat()
    }
    private fun getSinY(
        centerY:Float ,
        radius: Float ,
        angle: Double
    ): Float {
        return centerY + (radius) * sin(angle).toFloat()
    }

}