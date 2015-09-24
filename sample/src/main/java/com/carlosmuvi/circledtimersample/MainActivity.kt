package com.carlosmuvi.circledtimersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.activity_main.btn_start
import kotlinx.android.synthetic.activity_main.btn_stop
import kotlinx.android.synthetic.activity_main.btn_reset
import kotlinx.android.synthetic.activity_main.view_timer

public class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start.setOnClickListener { view_timer.startTimer() }
        btn_stop.setOnClickListener { view_timer.stopTimer() }
        btn_reset.setOnClickListener { view_timer.resetTimer() }

    }

}

