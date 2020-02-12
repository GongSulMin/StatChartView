package com.gong.statchartview.statchartview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gong.statchartview.R


class PolygonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var pointsCount: Int = 0

    var startX = Math.sin(10.0)
    var startY = Math.cos(10.0)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.RED
    }

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
        canvas.translate((width / 2).toFloat() , (height / 2).toFloat())
//        canvas.drawCircle(startX.toFloat(), startY.toFloat() , 10f , paint )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }
}