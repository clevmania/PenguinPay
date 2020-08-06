package com.clevmania.penguinpay.ui.utils

import com.clevmania.penguinpay.PenguinPay
import com.clevmania.penguinpay.data.PenguinPayRepository
import com.clevmania.penguinpay.data.PenguinPayService
import com.clevmania.penguinpay.ui.sendmoney.SendMoneyViewModel

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
object InjectorUtils {
    private fun providePenguinService(): PenguinPayService{
        return PenguinPay.invoke().create(PenguinPayService::class.java)
    }

    private fun providePenguinRepository(): PenguinPayRepository {
        return PenguinPayRepository(providePenguinService())
    }

    fun provideViewModelFactory(): SendMoneyViewModel {
        return SendMoneyViewModel(providePenguinRepository())
    }
}