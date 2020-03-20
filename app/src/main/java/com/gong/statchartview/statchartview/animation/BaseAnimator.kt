package com.gong.statchartview.statchartview.animation

const val DEFAULT_DURATION = 1500

abstract class BaseAnimator<E>  {

    var animationDuration = DEFAULT_DURATION

    abstract fun animate(
        onStart: (() -> Unit) = {} ,
        onEnd: (() -> Unit) = {} ,
        data: List<E> ,
        action: (List<E>) -> Unit
    )

}
