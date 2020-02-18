package com.gong.statchartview

import android.animation.ValueAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gong.statchartview.statchartview.StatCharViewListener
import com.gong.statchartview.statchartview.StatChartViewPoints
import com.gong.statchartview.statchartview.StatData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stat_chart_view.setStatData(

            listOf<StatData>(
                StatData.Builder.value(100.0).build(),
                StatData.Builder.value(100.0).build(),
                StatData.Builder.value(100.0).build(),
                StatData.Builder.value(70.0).build(),
                StatData.Builder.value(60.0).build(),
                StatData.Builder.value(50.0).build()

            )


        )


    }

    override fun onStart() {
        super.onStart()

    }
}
