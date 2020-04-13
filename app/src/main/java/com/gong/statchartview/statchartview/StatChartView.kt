package com.gong.statchartview.statchartview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.text.StaticLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.doOnPreDraw
import com.gong.statchartview.R
import com.gong.statchartview.statchartview.animation.AnimationType
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.data.Line
import com.gong.statchartview.statchartview.option.LineOption
import com.gong.statchartview.statchartview.option.fromLineOption
import com.gong.statchartview.statchartview.renderer.NoAnimRenderer
import com.gong.statchartview.statchartview.renderer.Renderer
import com.gong.statchartview.statchartview.renderer.RendererFactory
import com.gong.statchartview.statchartview.utils.toPath

/**
 *
 *                  가장 중요한거 Padding 구하기!!
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
) : FrameLayout(context, attrs, defStyleAttr), ChartViewContract {

    val TAG = "StatChartView"

    private val pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10F
        style = Paint.Style.STROKE
        color = Color.RED
    }

    private val statChartViewPointList = mutableListOf<StatChartViewPoints>()

    // TODO 여기 수정 해야됨... 어떻게 바꿔야지 잘 바꾼거지 일단 지금 팩토리 클래스자체가 싱글톤 형태로 유지해도 가능하게 할 수 있을거같은데??
    var option: ChartViewOption = ChartViewOption(
        IS_VISIBLE_BASE_CHART,
        ANIMATION_DURATION,
        AnimationType.NO_ANIMATION,
        LineOption.Builder.setPathColor("#B0BEC5").setPathWidth(5f).build()
    )
        set(value) {
            field = value
            statChartRenderer = RendererFactory(this, option.animationType, animations).create()
            baseChartRenderer = BaseStatChartRenderer(
                this,
                field.baseLineOption
            )
        }

    private lateinit var canvas: Canvas
    private var pointsCount: Int = 0
    private var radius: Float = 300f
    private var centerX: Float = (width / 2).toFloat()
    private var centerY: Float = (height / 2).toFloat()
    private var statChartRenderer: Renderer
    private var baseChartRenderer: BaseStatChartRenderer
    private var animations = StatChartAnimation()
    private val pointsRadius: Float = 13f
    private val baseTextList = mutableListOf<String>()

    init {

        statChartRenderer =
            NoAnimRenderer(
                this,
                animations
            )


        baseChartRenderer = BaseStatChartRenderer(
            this,
            option.baseLineOption
        )

        val obtainStyledAttributes =
            context.obtainStyledAttributes(attrs, R.styleable.StatChartView)

        obtainStyledAttributes.let {
            it.getInt(R.styleable.StatChartView_polygon_point_count, 5).let { count ->
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

            radius = it.getFloat(R.styleable.StatChartView_polygon_radius, 300f)
        }

        obtainStyledAttributes.recycle()

        centerX = 540.0F
        centerY = 768.0F

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setWillNotDraw(false)
    }


    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        if (option.isVisibleBaseChartShow) baseChartRenderer.draw()
        baseChartRenderer.showText()
        statChartRenderer.draw()
    }

    fun showChart(list: List<Line>) {

        doOnPreDraw {
            baseChartRenderer.anim(
                radius,
                list,
                animations,
                option.animationDuration
            )
        }

        doOnPreDraw {
            statChartRenderer.anim(
                radius,
                list,
                animations,
                option.animationDuration
            )
        }
    }

    fun setBaseLinePointText(texts: List<String>) {
        baseTextList.clear()
        baseTextList.addAll(texts)
        baseChartRenderer.setText(texts)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                statChartViewPointList.forEach { list ->
                    list.takeIf {
                        it.rangeInPoint(
                            touchX,
                            touchY,
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

    override fun drawLine(points: List<StatChartViewPoints>, lineOption: LineOption) {
        canvas.drawPath(points.toPath(), pathPaint.fromLineOption(lineOption))
    }

    override fun drawLine(path: Path, lineOption: LineOption) {
        canvas.drawPath(path, pathPaint.fromLineOption(lineOption))
    }

    override fun drawText(
        staticLayout: StaticLayout,
        pointX: Float,
        pointY: Float
    ) {
        canvas.save()
        canvas.translate(pointX, pointY)
        staticLayout.draw(canvas)
        canvas.restore()
    }

    companion object {
        const val STAT_MAX_POINT = 10
        const val STAT_MIN_POINT = 3
        const val ANIMATION_DURATION = 1500L
        const val IS_VISIBLE_BASE_CHART = true
    }


    // 결국 1. 디폴트 밸류를 두개다 가지거나
    //     2. nullable 하거나 ??
    /**
     *
     *      https://medium.com/@vicidroiddev/using-builders-in-kotlin-data-class-e8a08797ed56
     */
    class ChartViewOption {
        var isVisibleBaseChartShow: Boolean = IS_VISIBLE_BASE_CHART
        var animationDuration: Long = ANIMATION_DURATION
        var animationType: AnimationType = AnimationType.NO_ANIMATION
        var baseLineOption: LineOption =
            LineOption.Builder.setPathColor("#B0BEC5").setPathWidth(5f).build()

        constructor(
            isVisible: Boolean,
            duration: Long,
            animationType: AnimationType,
            lineOption: LineOption
        ) {
            this.isVisibleBaseChartShow = isVisible
            this.animationDuration = duration
            this.animationType = animationType
            this.baseLineOption = lineOption
        }

        private constructor(builder: Builder) {
            with(builder) {
                isVisibleBaseChartShow?.let {
                    this@ChartViewOption.isVisibleBaseChartShow = it
                }

                animationDuration?.let {
                    this@ChartViewOption.animationDuration = it
                }

                animationType?.let {
                    this@ChartViewOption.animationType = it
                }

                baseLineOption?.let {
                    this@ChartViewOption.baseLineOption = it
                }
            }
        }

        class Builder {
            var isVisibleBaseChartShow: Boolean? = null
            var animationDuration: Long? = null
            var animationType: AnimationType? = null
            var baseLineOption: LineOption? = null

            fun setBaseChartShowStatus(value: Boolean): Builder {
                return apply {
                    this.isVisibleBaseChartShow = value
                }
            }

            fun setBaseLineOption(value: LineOption): Builder {
                return apply {
                    this.baseLineOption = value
                }
            }

            fun setAnimationDuration(duration: Long): Builder {
                return apply {
                    this.animationDuration = duration
                }
            }

            fun setAnimationType(animationType: AnimationType): Builder {
                return apply {
                    this.animationType = animationType
                }
            }

            fun build(): ChartViewOption {
                return ChartViewOption(this)
            }
        }
    }

}