package com.gong.statchartview.statchartview

import android.graphics.PointF
import android.util.Log
import com.gong.statchartview.statchartview.animation.BaseAnimator
import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.utils.MathUtils


/**
 *
 *
 *              여기서 애니메이션을 한다라는 개념이 아니라
 *              뷰에다가 뿌릴 포인트를 계산한다라는 말이 맞을 듯 싶슴당
 *
 */
class StatChartRenderer (
    private val chartView: ChartViewContract ,
    private val chartConfig: ChartConfig ,
    private val statChartAnimation: StatChartAnimation
) {

    private var points: MutableList<StatChartViewPoints> = mutableListOf()
    private var animatedValue = 1f

    fun draw() {

        chartView.drawLine(
            points.map {
                StatChartViewPoints(
                    PointF(540 + it.point.x * animatedValue , 760 + it.point.y * animatedValue) ,
                    it.radius * animatedValue
                )
            }
        )



    }

    private fun pointReset(radius: Float , statDataList: List<StatData>){

    }

    fun setData(
        radius: Float ,
        statDataList: List<StatData>
    ) {
        Log.e("TAG", "invalidate before " )
        Log.e("TAG", "invalidate after " )

        points.clear()
        points.addAll(statDataList.toPoint(radius))

        statChartAnimation.animate(
            action = { value ->
                Log.e("TAG", "animtae   ${value}" )
                animatedValue = value
                chartView.invalidate()

            }

        )

    }
}

