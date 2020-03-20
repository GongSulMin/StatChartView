package com.gong.statchartview

import android.os.Bundle
import android.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import com.gong.statchartview.statchartview.StatData
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
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TransitionManager.beginDelayedTransition(parent_view)
//        child_tv.visibility = View.VISIBLE

        // 데이터를 좌표료 바꾸는게 있어야 한다.. ?

        stat_chart_view.anim(
            listOf(
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
