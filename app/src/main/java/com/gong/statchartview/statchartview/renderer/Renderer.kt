package com.gong.statchartview.statchartview.renderer

import com.gong.statchartview.statchartview.animation.StatChartAnimation
import com.gong.statchartview.statchartview.data.Line

interface Renderer {
    fun draw()
    fun anim(radius: Float, lines: List<Line>, animation: StatChartAnimation)
}
