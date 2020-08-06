package com.clevmania.penguinpay.ui.utils

import android.text.TextUtils
import android.widget.EditText
import com.clevmania.penguinpay.ui.constants.Constants.BASE_TWO
import com.clevmania.penguinpay.ui.extension.afterTextChanged
import com.clevmania.penguinpay.ui.extension.showSnackBar
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker
import java.util.regex.Pattern
import kotlin.math.pow

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
class ValidationException (message:String)  : Exception(message)

enum class ValidationType {
    MOBILE, NAME, REQUIRED
}

@Throws(ValidationException::class)
fun TextInputLayout.validate(validationType: ValidationType,
                             label: String, length : Int?=null, picker: CountryCodePicker? = null,
                             editText: EditText?= null, localRate : Int?=null): String{
    val value = this.editText?.text.toString()

//     Validate empty field
    if (value.trim().isBlank()) {
        this.error = "$label is required"
        this.isErrorEnabled = true
        throw  ValidationException("$label is required")
    }

    when (validationType) {
        ValidationType.NAME -> {
            if(TextUtils.isDigitsOnly(value)){
                this.error = "$label cannot be a number"
                this.isErrorEnabled = true
                throw  ValidationException("$label cannot be a number")
            }
        }
        ValidationType.REQUIRED -> {
            if (value.trim().isBlank()) {
                this.error = "$label is required"
                this.isErrorEnabled = true
                throw  ValidationException("$label is required")
            }
        }
        ValidationType.MOBILE -> {
            picker?.let {
                if(!it.isValidFullNumber){
                    this.error = "$label Number is not valid"
                    this.isErrorEnabled = true
                    throw  ValidationException("$label Number is not valid")
                }else{
                    this.isErrorEnabled = false
                }
            }
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

fun String.validateBinary(): String{
    val binaryPattern = "^([0-1])$"
    return this.filter { !Pattern.compile(binaryPattern).matcher(it.toString()).find() }
}