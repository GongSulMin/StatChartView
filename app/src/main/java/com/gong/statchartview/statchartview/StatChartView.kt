package com.gong.statchartview.statchartview

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.animation.addListener
import com.gong.statchartview.R
import com.gong.statchartview.statchartview.utils.MathUtils.degreeToRadians
import com.gong.statchartview.statchartview.utils.MathUtils.getAngle
import com.gong.statchartview.statchartview.utils.MathUtils.getCosX
import com.gong.statchartview.statchartview.utils.MathUtils.getSinY
import com.gong.statchartview.statchartview.utils.PathUtils.getPolygonPath
import javax.security.auth.login.LoginException
import kotlin.math.cos

/**
 *
 *                   Needed Options
 *                   - pointCount
 *                   - pointCountEachValue
 *                   - radius
 *                   - isPointCircleVisible
 *
 *                   ## Issue
 *                   - 인당 Path 관리하는게 나을까?? => 하이라이팅 기능을 위해서는 그러는게 나을수 도 있긴한데.. 흠
 *                   - data -> path 로 바꾸는 변환식이 여기 클래스에 있음 이거 분리해야댐
 *                   - baseChatview 클래스를 따로 만들어서 베이스 차트뷰를 여러개로 만들수 있게 만든다?
 *                   - Label Style도 있으면 좋을
 *
 *
 *                   ## data value 기준 반지름 구하는 방법
 *                   -  데이터 별로 나오는 반지름이 다
 *
 *                   ## 원 그리는 기준
 *                   - 맥스 반지름 값을 지정
 *                   - 사용자의 데이터를 기준으로 맥스 값에 나눠서 각각 반지름을 구함
 *                   - 반지름을 구한뒤 각도를 기준으로 x y 포인트 값을 구함
 *
 *
 *
 */


// TODO Width Height 값이 이상함
class StatChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) , ChartViewContract {

    val TAG = "StatChartView"

    var pointsCount: Int = 0

    private var radius: Float = 300f
    private val pointsRadius: Float = 13f

    private var centerX: Float = (width / 2).toFloat()
    private var centerY: Float = (height / 2).toFloat()

    lateinit var statChartRenderer: StatChartRenderer

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.GREEN
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
//    private val basePoint = mutableListOf<StatChartViewPoints>()

    private val statDataList = mutableListOf<StatData>()

    private val pointF = PointF()
    private val path = Path()
    private val basePath = Path()
    private val targetPath = Path()
    private val pathMeasure = PathMeasure()
    private var pathAnimateValue = 0f
    private var isPathAnimateEnd = true

    init {

        statChartRenderer = StatChartRenderer(
            this,
            ChartConfig(
                radius  ,
                centerX ,
                centerY
            )
        )

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

        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setWillNotDraw(false)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(Rect(0, 0, 100, 100), pathPaint)
        val startX = getCosX(centerX, statChartViewPointList[0].radius, 0.0)
        val startY = getSinY(centerY, statChartViewPointList[0].radius, 0.0)

        path.reset()
        basePath.reset()

        path.moveTo(
            getCosX(centerX, radius * pathAnimateValue, 0.0),
            getSinY(centerY, radius * pathAnimateValue, 0.0)
        )

        basePath.moveTo(
            startX,
            startY
        )

        for (i in 0 until statChartViewPointList.size) {

            path.addPath(
                getPolygonPath(
                    path,
                    statChartViewPointList[i].radius * pathAnimateValue,
                    pointsCount,
                    i,
                    centerX,
                    centerY
                )
            )

//            basePath.addPath(
//                getPolygonPath(
//                    basePath,
//                    basePoint[i].radius,
//                    pointsCount,
//                    i,
//                    centerX,
//                    centerY
//                )
//            )

            canvas.drawCircle(
                ( (statChartViewPointList[i].point.x ) * (pathAnimateValue)),
                ( (statChartViewPointList[i].point.y ) * (pathAnimateValue)),
                pointsRadius,
                circlePaint
            )

        }
//            path.lineTo(
//                startX ,
//                startY
//            )

        basePath.lineTo(
            startX,
            startY
        )

        path.close()

        pathMeasure.setPath(path, true)
        canvas.drawPath(path, pathPaint)
        canvas.drawPath(basePath, basePaint)

    }

    fun setStatData(list: List<StatData>) {
//        statChartRenderer.setData(list)

        statDataList.clear()
        statDataList.addAll(list)

        initStatList(list)
        setAnimation()
//        invalidate()
    }

    private fun initStatList(statList: List<StatData>) {
        statChartViewPointList.clear()

        val maxData: Double = statList.map { it.value }.max() ?: 100.0
//        basePoint.clear()

        pointsCount = statList.size


        // 사용자가 입력하는 맥스값을 해야댐
        // max 값 대비 반지름 값 구하기
        statList.forEach {
            statChartViewPointList.add(
                StatChartViewPoints(radius = (radius * ((it.value) / maxData)).toFloat()))
//            basePoint.add(StatChartViewPoints(radius = radius))
        }


        // 계산 어떻게 했는지 까먹엇네;; 한달전에 개발해서 그래........ ㅠㅠㅠㅠㅠㅠㅠ
        // 위에 반지름 값을 구했으니 각도를 구한뒤 포인트 위치 값을 구한다.
        for (i in 0 until statChartViewPointList.size) {

            Log.e("Radius", " ${statChartViewPointList[i].radius} " )

            val x = getCosX(centerX , statChartViewPointList[i].radius , degreeToRadians(getAngle(pointsCount) * i))
            val y = getSinY(centerY , statChartViewPointList[i].radius , degreeToRadians(getAngle(pointsCount) * i))

//            val baseX = getCosX(centerX , basePoint[i].radius , degreeToRadians(getAngle(pointsCount) * i))
//            val baseY = getSinY(centerY , basePoint[i].radius , degreeToRadians(getAngle(pointsCount) * i))
//
            statChartViewPointList[i].point = PointF(x,y)
//            basePoint[i].point = PointF(x,y)
        }

    }

    private fun setAnimation() {

        ValueAnimator.ofFloat(1f,  1f).apply {
            duration = 1500
            addUpdateListener {
                Log.e("ValueAnimato111r", "  ${it.animatedValue}" )
                pathAnimateValue = it.animatedValue as Float
                invalidate()
            }
            addListener(
                onEnd = {
                    isPathAnimateEnd = true
                } ,
                onStart =  {
                    isPathAnimateEnd = false
                }
            )
        }.start()

    }

    private fun getRadius(
        touchX: Float ,
        angle: Double
    ): Float {
        val radius = touchX / cos(angle).toFloat()
        return radius
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
                    }

                    statChartViewPointList[0].radius = 150f
                    invalidate()


                }
            }

            MotionEvent.ACTION_UP -> {
            }

            MotionEvent.ACTION_MOVE -> {
            }

        }
        return true
    }


    companion object {
        val STAT_MAX_POINT = 10
        val STAT_MIN_POINT = 3
    }

    override fun drawLine() {
    }


}