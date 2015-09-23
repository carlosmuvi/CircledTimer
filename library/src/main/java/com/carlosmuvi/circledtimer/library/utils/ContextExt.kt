package com.carlosmuvi.circledtimer.library.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Attributes

/**
 * Created by Carlos on 16/09/2015.
 */

fun Context.getAttrs(attrs: AttributeSet, resId: IntArray) : TypedArray{
    return getTheme().obtainStyledAttributes(attrs, resId, 0, 0)
}

fun Long.toMitsSecs(): String {
    val formatter = SimpleDateFormat("mm:ss:SSS");
    return formatter.format(Date(this));
}
fun Long.get(index: Int): Char = this.toString()[index]




