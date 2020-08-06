package com.clevmania.penguinpay.ui.utils

import android.text.TextUtils
import android.widget.EditText
import com.clevmania.penguinpay.ui.constants.Constants.BASE_TWO
import com.clevmania.penguinpay.ui.extension.afterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import java.util.regex.Pattern
import kotlin.math.pow

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */

enum class ValidationType {
    MOBILE, NAME, BINARY_AMOUNT
}

fun TextInputLayout.validate(validationType: ValidationType,
                             label: String, length : Int?=null, picker: CountryCodePicker? = null,
                             editText: EditText?= null, xChangeRate : Int?=null): String{
    val value = this.editText?.text.toString()

    // Validate empty field
    if (value.trim().isBlank()) {
        this.error = "$label is required"
        this.isErrorEnabled = true
    }

    when (validationType) {
        ValidationType.NAME -> {
            if(TextUtils.isDigitsOnly(value)){
                this.error = "$label cannot be a number"
                this.isErrorEnabled = true
            }
        }
        ValidationType.BINARY_AMOUNT -> {
            val binaryPattern = "^([0-1])$"
            this.editText?.afterTextChanged { amount ->
                val isBinary = amount.filter {!Pattern.compile(binaryPattern).matcher(it.toString()).find()}
                if(isBinary.isEmpty()) {
                    this.isErrorEnabled = false
                    xChangeRate?.let {
                        val amountInLocalCurrency = amount.toDecimal() * xChangeRate
                        val amountInBinary = toBinary(amountInLocalCurrency)
                        editText?.setText(amountInBinary)
                    }

                }else{
                    this.error = "${isBinary.last()} is not a binary number"
                    this.isErrorEnabled = true
                    editText?.setText(null)
                }
            }
        }
        ValidationType.MOBILE -> {
            picker?.let {
                if(!it.isValidFullNumber){
                    this.error = "$label Number is not valid"
                    this.isErrorEnabled = true
                }else{
                    this.isErrorEnabled = false
                }
            }
        }
        else -> {
            //No type is provided, please proceed
        }
    }

    this.error = " "
    this.isErrorEnabled = false
    return value.trim()
}

fun String.toDecimal(): Int{
    var sum = 0
    this.reversed().forEachIndexed {
            k, v -> sum += v.toString().toInt() * BASE_TWO.pow(k).toInt()
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