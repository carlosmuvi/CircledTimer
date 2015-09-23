package com.carlosmuvi.circledtimer.library.utils

import kotlin.properties.ReadWriteProperty

/**
 * Created by Carlos on 16/09/2015.
 */
object DelegatesExt {
    fun notNullSingleValue<T : Any>(): ReadWriteProperty<Any?, T> = NotNullSingleValueVar()
}

private class NotNullSingleValueVar<T : Any>() : ReadWriteProperty<Any?, T> {
    private var value: T? = null

    public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
        return value ?: throw IllegalStateException("${desc.name} not initialized")
    }

    public override fun set(thisRef: Any?, desc: PropertyMetadata, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${desc.name} already initialized")
    }
}