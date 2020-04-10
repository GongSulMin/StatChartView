package com.gong.statchartview

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gong.statchartview.statchartview.StatChartView
import com.gong.statchartview.statchartview.StatData
import com.gong.statchartview.statchartview.animation.AnimationType
import com.gong.statchartview.statchartview.data.Line
import com.gong.statchartview.statchartview.option.LineOption
import kotlinx.android.synthetic.main.activity_main.*

/**
 *
 *                      1. 사용자가 데이터를 입력할 떄
 *                             "민첩" 100 , 100 , 100 , 100
 *                             "지력" 100 , 100 , 100 , 0
 *                             =>  위와 같은 방식이면 사용자가 데이터를 입력하는게 넘 어려운거 같은데??
 *
 *
 *                      Issue
 *                          - 여러개 path 값을 넣었을 때 에러가 나는게 아니라 디폴트 값을 넣도록 유도 하자 => 옵션으로 디폴트 값이 필요 없다면 에러 던지게
 *
 *
 *                       BaseOption
 *                          Label Config
 *                          BasePath 존재 유무
 *                          BasePath 색깔
 *
 *                       Path 필요한거
 *                          Path 색깔
 *                          Path 데이터
 *
 *
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stat_chart_view.option =
            StatChartView.ChartViewOption.Builder()
                .setAnimationDuration(3000L)
                .setBaseLineOption(
                    LineOption
                        .build {
                            setPathColor("#B0BEC5")
                            setPathWidth(3F)
                        }
                )
                .setBaseChartShowStatus(true)
                .setAnimationType(AnimationType.SCALE_ANIMATION)
                .build()

        stat_chart_view.setBaseLinePointText(listOf("지능", "파워", "체력", "민첩", "운", "몰라"))


        val test1 = Line(
            listOf(
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build()
            ),
            LineOption
                .build {
                    setPathColor(Color.BLUE)
                    setPathWidth(5f)
                }
        )

        val test2 = Line(
            listOf(
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build(),
                StatData.Builder.value(generateNum()).build()
            ),
            LineOption
                .build {
                    setPathColor(Color.RED)
                    setPathWidth(5f)
                }
        )

        val data = mutableListOf<Line>()

        data.add(test1)
        data.add(test2)

        stat_chart_view.showChart(
            data
        )

    }
}

fun generateNum(): Double {
    return (50..100).random().toDouble()
}

