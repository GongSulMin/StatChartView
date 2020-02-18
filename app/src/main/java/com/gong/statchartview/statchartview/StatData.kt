package com.gong.statchartview.statchartview

data class StatData(
    var label: String? = null ,
    var value: Double
) {

    constructor(builder: Builder): this(
        builder.label ,
        builder.value
    )

    object Builder {
        var label: String? = null
            private set
        var value: Double = 0.0
            private set

        fun label(label: String?): Builder = apply {
            this.label = label
        }

        fun value(value: Double): Builder = apply {
            this.value = value
        }

        fun build(): StatData {
            return StatData(this)
        }

    }
}