package com.clevmania.penguinpay.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.clevmania.penguinpay.R

/**
 * @author by Lawrence on 8/7/20.
 * for PenguinPay
 */

fun Context.showSuccessDialog(){
    val builder = AlertDialog.Builder(this)
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_transaction_success, null)

    builder.setView(view)

    val dialog = builder.create()
    dialog.window?.decorView?.setBackgroundResource(android.R.color.transparent)
    dialog.show()
}
