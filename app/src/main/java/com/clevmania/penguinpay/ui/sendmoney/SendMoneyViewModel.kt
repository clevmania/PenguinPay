package com.clevmania.penguinpay.ui.sendmoney

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clevmania.penguinpay.data.PenguinPayDataSource
import com.clevmania.penguinpay.model.Rates
import com.clevmania.penguinpay.constants.Constants
import com.clevmania.penguinpay.extension.toDefaultErrorMessage
import com.clevmania.penguinpay.utils.EventUtils
import kotlinx.coroutines.launch

class SendMoneyViewModel(private val dataSource : PenguinPayDataSource) : ViewModel() {
    private val sym = "KES,NGN,TZS,UGX"

    private val _progress = MutableLiveData<EventUtils<Boolean>>()
    val progress : LiveData<EventUtils<Boolean>> = _progress

    private val _error = MutableLiveData<EventUtils<String>>()
    val error : LiveData<EventUtils<String>> = _error

    private val _rates = MutableLiveData<EventUtils<Rates>>()
    val xChangeRates : LiveData<EventUtils<Rates>> = _rates

    init { retrieveRates() }

    fun retrieveRates(){
        viewModelScope.launch {
            _progress.value = EventUtils(true)
            try {
                val transactionStatus = dataSource.retrieveExchangeRate(sym,Constants.APP_ID)
                _rates.value = EventUtils(transactionStatus.rates)
            }catch (ex: Exception){
                _error.value = EventUtils(ex.toDefaultErrorMessage())
            }finally {
                _progress.value = EventUtils(false)
            }
        }
    }
}