package com.carlosmuvi.circledtimersample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.carlosmuvi.circledtimer.library.customview.CircledTimer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class JavaActivity extends AppCompatActivity {

  CircledTimer timer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    timer = (CircledTimer) findViewById(R.id.view_timer);

    timer.setTotalTimeSeconds(5);
    timer.setTextSize(75);
    timer.setChannelWidth(10);
    timer.setCircleRadius(20);
    timer.setFlowColor(Color.RED);

    timer.setOnFinishListener(new Function0<Boolean>() {
      @Override public Boolean invoke() {
        Toast.makeText(JavaActivity.this, "Finished!", Toast.LENGTH_LONG).show();
        return true;
      }
    });

    timer.build();

    findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        timer.startTimer();
      }
    });

    findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        timer.stopTimer();
      }
    });

    findViewById(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        timer.resetTimer();
      }
    });
  }
}
