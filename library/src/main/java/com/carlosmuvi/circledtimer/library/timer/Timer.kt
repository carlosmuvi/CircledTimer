package com.carlosmuvi.circledtimer.library.timer

import android.os.Handler
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by Carlos on 15/09/2015.
 */
public class Timer(val tick: Long, val times: Long, val onFinish: () -> Boolean, val body: (Long) -> Unit): AnkoLogger {

    var totalTimeMillis = 0L
    var remainingTimes = times

    private val handler: Handler by Delegates.lazy {
        Handler()
    }

    private val myRunnable: Runnable by Delegates.lazy {
        Runnable {
            if(remainingTimes > 0){
                //System.out.println("TICK: $times -- TOTALTIME: $totalTimeMillis")
                body(totalTimeMillis)
                totalTimeMillis += tick
                remainingTimes--;
                handler.postDelayed(myRunnable, tick)
            }else{
                onFinish()
            }
        }
    }

    public fun startTimer(){
        if (remainingTimes == times) totalTimeMillis += tick
        handler.postDelayed(myRunnable, tick)
    }

    public fun stopTimer(){
        handler.removeCallbacks(myRunnable)
    }

    fun resetTimer(){
        remainingTimes = times
        totalTimeMillis = 0L
        handler.removeCallbacks(myRunnable, tick)
    }


}