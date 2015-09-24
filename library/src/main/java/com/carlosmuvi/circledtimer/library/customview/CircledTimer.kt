package com.carlosmuvi.circledtimer.library.customview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.carlosmuvi.circledtimer.library.R
import com.carlosmuvi.circledtimer.library.model.Coord
import com.carlosmuvi.circledtimer.library.utils.DelegatesExt
import com.carlosmuvi.circledtimer.library.timer.Timer
import com.carlosmuvi.circledtimer.library.utils.toMitsSecs
import java.util.*
import kotlin.properties.Delegates

/**
 * Created by Carlos on 15/09/2015.
 */
public class CircledTimer : View {

    //state
    enum class State { NOT_STARTED, RUNNING, PAUSED, FINISHED }
    var currentState = State.NOT_STARTED

    //timing
    public var totalTimeSeconds: Int = 1

    private var timer: Timer by DelegatesExt
            .notNullSingleValue()

    //Paints
    private val channelPaint = Paint()
    private val circlePaint = Paint()
    private val flowPaint = Paint()
    private val textPaint = Paint()

    //Sizes
    private val center: Coord  by Delegates
            .lazy { Coord((getWidth() / 2).toFloat(), (getHeight() / 2).toFloat()) }

    public var channelWidth : Float = 7.5f
    public var circleRadius : Float = 15f
    public var textSize : Float = 150f
    val channelRadius: Float by Delegates.lazy {
        Math.min(getHeight() / 2.0F, getWidth() / 2.0F) - (circleRadius)
    }

    //Colors
    public var channelColor: Int = Color.BLACK
    public var flowColor: Int = Color.RED

    public var onFinishListener : () -> Boolean = { true }

    //Flow Path
    private val flowPath = Path()
    private val flowCoords: List<Coord> by Delegates
            .lazy { generateCirclePoints(center.x, center.y, channelRadius) }
    private var flowCoordsIndex = 0

    //Text
    private var textValue = "00:00:000"

    /**
     * CONSTRUCTORS AND INITIALIZATION
     */

    constructor(ctx: Context) : super(ctx) {
        getAttrs()
    }

    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs, 0) {
        getAttrs(attrs)
    }

    constructor(ctx: Context, attrs: AttributeSet, defStyle: Int) : super(ctx, attrs, defStyle) {
        getAttrs(attrs)
    }

    public fun build(){
        //setup timer (ticks every 10 milliseconds)
        timer = Timer(10, totalTimeSeconds * 100L, onFinishListener) {
            textValue = it.toMitsSecs()
            drawFlowPart()
            invalidate()
        }

        //flow init
        with(flowPaint) {
            setStrokeWidth(channelWidth + 1)
            setColor(flowColor)
            setStyle(Paint.Style.STROKE)
            setStrokeJoin(Paint.Join.ROUND)
        }

        //channel init
        with(channelPaint) {
            setStrokeWidth(channelWidth)
            setStyle(Paint.Style.STROKE)
            setColor(channelColor)

        }

        //circle init
        with(circlePaint) {
            setColor(flowColor)
        }

        //text init
        with(textPaint) {
            setTextSize(textSize)
            setColor(flowColor)
        }

    }
    private fun getAttrs(attrs: AttributeSet? = null) {

        //read styleable attributes
        if (attrs != null) {
            var attributes = getContext().getTheme()
                    .obtainStyledAttributes(attrs, R.styleable.CircledTimer, 0, 0);
            flowColor = attributes.getColor(R.styleable.CircledTimer_flowColor, flowColor)
            channelColor = attributes.getColor(R.styleable.CircledTimer_channelColor, channelColor)
            attributes.recycle()
        }

    }

    /**
     * PUBLIC FUNCTIONS
     */
    fun startTimer() {
        when (currentState) {
            State.RUNNING, State.FINISHED -> {
                //do nothing, already started
            }
            State.NOT_STARTED, State.PAUSED -> {
                currentState = State.RUNNING
                timer.startTimer()
            }
        }
    }

    fun stopTimer() {
        when (currentState) {
            State.RUNNING -> {
                timer.stopTimer()
                currentState = State.PAUSED
            }
            else -> {
                //do nothing, already stopped
            }
        }
    }


    fun resetTimer() {
        //TODO check state
        if(currentState != State.NOT_STARTED){
            currentState = State.NOT_STARTED
            flowPath.reset()
            textValue = "00:00:000"
            flowCoordsIndex = 0
            timer.resetTimer()
            invalidate()
        }
    }

    /**
     * VIEW EVENTS
     */

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(center.x, center.y, channelRadius, channelPaint)
        canvas.drawText(textValue, computeTimeXOffset(textValue, textPaint),
                computeTimeYOffset(textValue, textPaint), textPaint)
        canvas.drawPath(flowPath, flowPaint)
        canvas.drawCircle(flowCoords[flowCoordsIndex].x, flowCoords[flowCoordsIndex].y, circleRadius, circlePaint)
    }

    /**
     * DRAW FUNCTIONS
     */

    private fun drawFlowPart() {
        flowPath.moveTo(flowCoords[flowCoordsIndex].x, flowCoords[flowCoordsIndex].y)
        flowCoordsIndex = (flowCoordsIndex + 1) % flowCoords.size()
        flowPath.lineTo(flowCoords[flowCoordsIndex].x, flowCoords[flowCoordsIndex].y)
    }

    /**
     * COORDINATE GENERATION
     */

    /**
     * Calculates Y value to center text based on its content
     */
    private fun computeTimeYOffset(text: String, paint: Paint): Float {
        val textBounds = Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return center.y + (textBounds.height() / 2.0f);
    }

    /**
     * Calculates X value to center text based on its content
     */
    private fun computeTimeXOffset(text: String, paint: Paint)
            = center.x - (paint.measureText(text) / 2.0f)

    /**
     * Generates points of a circle based on its center and radius
     */
    private fun generateCirclePoints(centerX: Float, centerY: Float, radius: Float)
            : List<Coord> {
        val coords = ArrayList<Coord>()
        val stepSize = 360 / (totalTimeSeconds * 100.0)
        for (i in 270.00..630.00 step stepSize) {
            val a = centerX + radius * Math.cos(i * Math.PI / 180)
            val b = centerY + radius * Math.sin(i * Math.PI / 180)
            coords.add(Coord(a.toFloat(), b.toFloat()))
        }
        return coords
    }
}
