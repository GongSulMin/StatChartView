package com.gong.statchartview.statchartview.option

import android.graphics.Color
import android.graphics.Paint


class PathOption(
    var pathColor: Int = Color.RED ,
    var pathWidth: Float = 10f
) {

    constructor(builder: Builder): this(builder.pathColor , builder.pathWidth)

    companion object {
        fun build(action:Builder.() -> Unit) = Builder.apply(action).build()
    }

    object Builder {
        var pathColor: Int = Color.RED
        var pathWidth: Float = 10f

        fun setPathColor(pathColor: Int): Builder {
            this.pathColor = pathColor
            return this
        }

        fun setPathColor(pathColor: String): Builder {
            this.pathColor = Color.parseColor(pathColor)
            return this
        }

        fun setPathWidth(pathWidth: Float): Builder {
            this.pathWidth = pathWidth
            return this
        }

        fun build() = PathOption(this)
    }
}

fun Paint.fromPathOption(pathOption: PathOption): Paint {
    return this.apply {
        color = pathOption.pathColor
        strokeWidth = pathOption.pathWidth
    }
}