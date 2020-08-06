package com.clevmania.penguinpay.ui.sendmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.clevmania.penguinpay.data.PenguinPayDataSource

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
class SendMoneyViewModelFactory(private val dataSource: PenguinPayDataSource)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SendMoneyViewModel(dataSource) as T
    }
}