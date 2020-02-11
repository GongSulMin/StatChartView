package com.gong.statchartview.statchartview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gong.statchartview.R

class StatCharView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint

    private val pointPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }
    
    private val path = Path()
    var startX = 0f
    var startY = 0f

    val points = arrayListOf<PointF>()

    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.StatCharView)
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = obtainStyledAttributes.getColor(R.styleable.StatCharView_point_color , Color.RED )
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
        obtainStyledAttributes.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
                path.moveTo(startX , startY)
            }

            MotionEvent.ACTION_UP -> {
                points.add(PointF(x , y))
                performClick()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                path.quadTo(startX , startY , (startX + x)/2 ,(startY + y)/2 )
                startX = x
                startY = y
                invalidate()
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(path ,paint)
        points.forEach {
            canvas.drawCircle(it.x , it.y , 20f , pointPaint)
        }
//        canvas.drawRect(RectF(0f , 0f ,300f , 300f) , paint )
    }
}