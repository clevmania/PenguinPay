package com.clevmania.penguinpay.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun View.showSnackBar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.makeVisible(){ this.visibility = View.VISIBLE
}

fun View.makeGone(){ this.visibility = View.GONE
}

fun View.makeInVisible() { this.visibility = View.INVISIBLE}

fun ProgressBar.toggleProgress(it: Boolean) {
    if(it){ this.makeVisible() }
    else{ this.makeGone() }
}