package com.gong.statchartview.statchartview

import com.gong.statchartview.statchartview.data.Line

interface Renderer {
    fun draw()
    fun dataLoad(radius: Float, list: List<Line>)
}
