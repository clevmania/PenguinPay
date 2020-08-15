package com.clevmania.penguinpay.utils

import com.clevmania.penguinpay.constants.Constants
import kotlin.math.pow

/**
 * @author by Lawrence on 8/15/20.
 * for PenguinPay
 */

fun String.toDecimal(): Int{
    var sum = 0
    this.reversed().forEachIndexed {
            k, v -> sum += v.toString().toInt() * Constants.BASE_TWO.pow(k).toInt()
    }
    return sum
}

fun toBinary(decimalNumber: Int, binaryString: String = "") : String {
    while (decimalNumber > 0) {
        val temp = "${binaryString}${decimalNumber%2}"
        return toBinary(decimalNumber/2, temp)
    }
    return binaryString.reversed()
}