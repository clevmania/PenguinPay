package com.clevmania.penguinpay.model

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
data class ExchangeRateResponse(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Rates,
    val timestamp: Int
)