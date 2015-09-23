package com.carlosmuvi.circledtimer.library.timer

import android.os.Handler
import org.jetbrains.anko.AnkoLogger
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by Carlos on 15/09/2015.
 */
public class Timer(val tick: Long, var times: Long, val body: (Long) -> Unit): AnkoLogger {

    var totalTimeMillis = 0L

    private val handler: Handler by Delegates.lazy {
        Handler()
    }

    private val myRunnable: Runnable by Delegates.lazy {
        Runnable {
            if(times > 0){
                //System.out.println("TICK: $times -- TOTALTIME: $totalTimeMillis")
                body(totalTimeMillis)
                totalTimeMillis += tick
                times--;
                handler.postDelayed(myRunnable, tick)
            }
        }
    }

    public fun startTimer(){
        totalTimeMillis += tick
        handler.postDelayed(myRunnable, tick)
    }

    public fun stopTimer(){
        handler.removeCallbacks(myRunnable)
    }

    fun resetTimer(){
        times = 0
        handler.removeCallbacks(myRunnable, tick)
    }


}