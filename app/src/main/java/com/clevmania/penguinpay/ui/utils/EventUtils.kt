package com.clevmania.penguinpay.ui.utils

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
open class EventUtils<out T>(private val content : T) {
    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent() : T = content
}