package com.gong.statchartview.statchartview.renderer

import com.gong.statchartview.statchartview.ChartViewContract
import com.gong.statchartview.statchartview.animation.AnimationType
import com.gong.statchartview.statchartview.animation.StatChartAnimation

class RendererFactory(
    private var chartView: ChartViewContract,
    private var animationType: AnimationType,
    private var statChartAnimation: StatChartAnimation
) : Factory {
    override fun create(): Renderer {
        return when (animationType) {
            AnimationType.NO_ANIMATION -> NoAnimRenderer(chartView, statChartAnimation)
            AnimationType.SCALE_ANIMATION -> ScaleStatChartRenderer(chartView, statChartAnimation)
            AnimationType.POINTS_ANIMATION -> PointStatChartRenderer(chartView, statChartAnimation)
        }
    }
}

interface Factory {
    fun create(): Renderer
}