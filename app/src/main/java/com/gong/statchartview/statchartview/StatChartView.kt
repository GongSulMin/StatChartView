package com.gong.statchartview.statchartview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.transition.PatternPathMotion
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.PathInterpolator
import androidx.core.graphics.PathParser
import androidx.core.graphics.PathSegment
import androidx.core.view.animation.PathInterpolatorCompat
import com.gong.statchartview.R
import com.gong.statchartview.statchartview.utils.MathUtils.degreeToRadians
import com.gong.statchartview.statchartview.utils.MathUtils.getAngle
import com.gong.statchartview.statchartview.utils.MathUtils.getCosX
import com.gong.statchartview.statchartview.utils.MathUtils.getSinY
import com.gong.statchartview.statchartview.utils.PathUtils.getPolygonPath
import java.nio.file.PathMatcher
import kotlin.math.cos

/**
 *
 *                   Needed Options
 *                   - pointCount
 *                   - pointCountEachValue
 *                   - radius
 *                   - isPointCircleVisible
 *
 */


// TODO Width Height 값이 이상함
// TODO Animation
class StatChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val TAG = "StatChartView"

    var pointsCount: Int = 0

    private var radius: Float = 300f
    private val pointsRadius: Float = 13f


    private var centerX: Float = (width / 2).toFloat()
    private var centerY: Float = (height / 2).toFloat()

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10F
        style = Paint.Style.STROKE
        color = Color.RED
    }

    private val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5f
        style = Paint.Style.STROKE
        color = Color.BLACK
    }

    private val statChartViewPointList = mutableListOf<StatChartViewPoints>()
    private val basePoint = mutableListOf<StatChartViewPoints>()

    private val statChartView = StatChartViewPoints()
    private var selectStatChartView = StatChartViewPoints()

    private val statDataList = mutableListOf<StatData>()

    private val pointF = PointF()
    private val path = Path()
    private val basePath = Path()
    private val pathMeasure = PathMeasure()

    init {

        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.StatChartView)

        obtainStyledAttributes.let {
            it.getInt(R.styleable.StatChartView_polygon_point_count , 5).let { count ->
                pointsCount = when {
                    count > 10 -> {
                        STAT_MAX_POINT
                    }
                    count < 3 -> {
                        STAT_MIN_POINT
                    }
                    else -> {
                        count
                    }
                }
            }

            radius = it.getFloat(R.styleable.StatChartView_polygon_radius , 300f)
        }

        obtainStyledAttributes.recycle()

        centerX = 540.0F
        centerY = 768.0F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {

        if (statChartViewPointList.isNotEmpty()) {

            val startX =  getCosX(centerX , statChartViewPointList[0].radius , 0.0)
            val startY =  getSinY(centerY , statChartViewPointList[0].radius , 0.0)

            path.reset()
            basePath.reset()
            path.moveTo(
                startX ,
                startY
            )

            basePath.moveTo(
                startX ,
                startY
            )

            for (i in 0 until statChartViewPointList.size) {

                path.addPath(getPolygonPath(
                    path,
                    statChartViewPointList[i].radius ,
                    pointsCount ,
                    i ,
                    centerX ,
                    centerY
                ))

                basePath.addPath(getPolygonPath(
                    basePath,
                    basePoint[i].radius ,
                    pointsCount ,
                    i ,
                    centerX ,
                    centerY
                ))


                canvas.drawCircle(
                    statChartViewPointList[i].point.x,
                    statChartViewPointList[i].point.y,
                    pointsRadius,
                    circlePaint
                )

            }

            path.lineTo(
                startX ,
                startY
            )

            basePath.lineTo(
                startX ,
                startY
            )

            pathMeasure.setPath(path  , false)
            canvas.drawPath(path , pathPaint)
            canvas.drawPath(basePath , basePaint)

        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x
        val touchY = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                statChartViewPointList.forEach { list ->
                    list.takeIf {
                        it.rangeInPoint(
                            touchX ,
                            touchY ,
                            pointsRadius
                        )
                    }?.let { selectPoint ->
                        selectStatChartView = selectPoint
                    }

                    statChartViewPointList[0].radius = 150f
                    invalidate()

                    Log.e(TAG, "Radius:   ${getRadius(touchX , 0.0)}" )

                }
            }

            MotionEvent.ACTION_UP -> {
            }

            MotionEvent.ACTION_MOVE -> {
            }

        }
        return true
    }

    fun setStatData(list: List<StatData>) {
        statDataList.clear()
        statDataList.addAll(list)
        initStatList(list)
        invalidate()
    }

    private fun initStatList(statList: List<StatData>) {
        statChartViewPointList.clear()
        basePoint.clear()

        pointsCount = statList.size

        statList.forEach {
            statChartViewPointList.add(StatChartViewPoints(radius = (radius * ((it.value) / 100f)).toFloat()))
            basePoint.add(StatChartViewPoints(radius = radius))
        }


        for (i in 0 until statChartViewPointList.size) {
            val x = getCosX(centerX , statChartViewPointList[i].radius , degreeToRadians(getAngle(pointsCount) * i))
            val y = getSinY(centerY , statChartViewPointList[i].radius , degreeToRadians(getAngle(pointsCount) * i))

            val baseX = getCosX(centerX , basePoint[i].radius , degreeToRadians(getAngle(pointsCount) * i))
            val baseY = getSinY(centerY , basePoint[i].radius , degreeToRadians(getAngle(pointsCount) * i))

            statChartViewPointList[i].point = PointF(x,y)
            basePoint[i].point = PointF(x,y)
        }
    }

    private fun getRadius(
        touchX: Float ,
        angle: Double
    ): Float {
        val radius = touchX / cos(angle).toFloat()
        return radius
    }

    companion object {
        val STAT_MAX_POINT = 10
        val STAT_MIN_POINT = 3
    }


}