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
import androidx.core.view.doOnPreDraw
import com.gong.statchartview.R
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.utils.MathUtils.degreeToRadians
import com.gong.statchartview.statchartview.utils.MathUtils.getAngle
import com.gong.statchartview.statchartview.utils.MathUtils.getCosX
import com.gong.statchartview.statchartview.utils.MathUtils.getSinY
import com.gong.statchartview.statchartview.utils.PathUtils.getPolygonPath
import com.gong.statchartview.statchartview.utils.toPath
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
 *                      데이터 설정하고 onpredraw
 *                      animate invalidate
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

    lateinit var canvas: Canvas

    var animation = StatChartAnimation()

    init {
        statChartRenderer = StatChartRenderer(
            this,
            ChartConfig(
                radius  ,
                centerX ,
                centerY
            ) ,
            animation
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

        Log.e(TAG, "init" )
        invalidate()

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setWillNotDraw(false)
    }


    override fun onDraw(canvas: Canvas) {
        Log.e(TAG, "onDraw: " )
        this.canvas = canvas
        statChartRenderer.draw()
//        val startX = getCosX(centerX, statChartViewPointList[0].radius, 0.0)
//        val startY = getSinY(centerY, statChartViewPointList[0].radius, 0.0)
//
//        path.reset()
//        basePath.reset()
//
//        val pathTest = statChartViewPointList.toPath()
//
//
////        path.moveTo(
////            getCosX(centerX, radius , 0.0),
////            getSinY(centerY, radius , 0.0)
////        )
//
//        basePath.moveTo(
//            startX,
//            startY
//        )
//
//        // 여기까지 역할은 페인트를 포함해서 랜더링 클래스에서 보내준 데이터를 통해 패스값을 구하는게 뷰의 역할인데....
//        for (i in 0 until statChartViewPointList.size) {
//
////            path.lineTo(statChartViewPointList[i].point.x , statChartViewPointList[i].point.y)
//
////            path.addPath(
////                getPolygonPath(
////                    path,
////                    statChartViewPointList[i].radius ,
////                    pointsCount,
////                    i,
////                    centerX,
////                    centerY
////                )
////            )
//
////            basePath.addPath(
////                getPolygonPath(
////                    basePath,
////                    basePoint[i].radius,
////                    pointsCount,
////                    i,
////                    centerX,
////                    centerY
////                )
////            )
//
//            canvas.drawCircle(
//                ( (statChartViewPointList[i].point.x )),
//                ( (statChartViewPointList[i].point.y )),
//                pointsRadius,
//                circlePaint
//            )
//
//        }
//
////        path.lineTo(
////            startX ,
////            startY
////        )
//
//        basePath.lineTo(
//            startX,
//            startY
//        )
//
//        path.close()
//
//        pathMeasure.setPath(path, true)
//        canvas.drawPath(pathTest, pathPaint)
//        canvas.drawPath(basePath, basePaint)

    }

    fun anim(list: List<StatData>) {
//        statChartRenderer.setData(list)

        statDataList.clear()
        statDataList.addAll(list)
        doOnPreDraw {  statChartRenderer.setData(radius , list) }
//        invalidate()

//        initStatList(list)
//        setAnimation()
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

        invalidate()

    }

    private fun setAnimation() {

        ValueAnimator.ofFloat(1f,  1f).apply {
            duration = 1500
            addUpdateListener {
                Log.e("ValueAnimato111r", "  ${it.animatedValue}" )
//                pathAnimateValue = it.animatedValue as Float
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

    override fun drawLine( points: List<StatChartViewPoints> ) {
        Log.e("TAG", "${points[0].radius}: " )
        canvas.drawPath(points.toPath() , pathPaint)
    }


}