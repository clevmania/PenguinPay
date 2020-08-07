package com.clevmania.penguinpay.ui.sendmoney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.clevmania.penguinpay.R
import com.clevmania.penguinpay.model.Rates
import com.clevmania.penguinpay.ui.constants.Constants
import com.clevmania.penguinpay.ui.extension.afterTextChanged
import com.clevmania.penguinpay.ui.extension.showSnackBar
import com.clevmania.penguinpay.ui.extension.toggleProgress
import com.clevmania.penguinpay.ui.utils.*
import kotlinx.android.synthetic.main.send_money_fragment.*
import kotlin.math.roundToInt

class SendMoneyFragment : Fragment() {
    private var currentExchangeRates: Rates? = null

    private val viewModel by viewModels<SendMoneyViewModel> { InjectorUtils.provideViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.send_money_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryPicker.registerCarrierNumberEditText(tieMobile)
        onCountryChanged()
        btnSendMoney.setOnClickListener { validateFields() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(viewModel) {
            progress.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    progressBar.toggleProgress(it)
                }
            })

            error.observe(viewLifecycleOwner, Observer { uiEvent ->
                uiEvent.getContentIfNotHandled()?.let {
                    requireView().showSnackBar(it)
                }
            })

            xChangeRates.observe(viewLifecycleOwner, Observer { rateEvent ->
                rateEvent.getContentIfNotHandled()?.let {
                    currentExchangeRates = it
                    validateAndConvertToLocalCurrency()
                }
            })
        }
    }

    private fun validateFields() {
        try {
            if (getLocalExchangeRate() == Constants.RATE_CURRENTLY_UNAVAILABLE) {
                viewModel.retrieveRates()
            }
            tilFirstName.validate(ValidationType.NAME, getString(R.string.first_name))
            tilLastName.validate(ValidationType.NAME, getString(R.string.last_name))
            tilMobile.validate(ValidationType.MOBILE,
                getString(R.string.mobile), picker = countryPicker)
            tilAmountToSend.validate(ValidationType.REQUIRED, getString(R.string.amount))

            // All Field checks good,
            // Upon success, show notification
            requireContext().showSuccessDialog()

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun validateAndConvertToLocalCurrency() {
        if (getLocalExchangeRate() == Constants.RATE_CURRENTLY_UNAVAILABLE) {
            viewModel.retrieveRates()
            return
        }

        try {
            tieSendAmount?.afterTextChanged { amount ->
                binaryToLocalCurrencyConverter(amount)
            }
        } catch (ex: ValidationException) {
            ex.message?.let { requireView().showSnackBar(it) }
        }
    }

    private fun getLocalExchangeRate(): Int {
        if (currentExchangeRates == null) throw ValidationException("Missing Exchange Rates")
        currentExchangeRates?.let {
            return when (countryPicker.selectedCountryNameCode) {
                Constants.NIGERIA -> it.NGN.roundToInt()
                Constants.UGANDA -> it.UGX.roundToInt()
                Constants.TANZANIA -> it.TZS.roundToInt()
                Constants.KENYA -> it.KES.roundToInt()
                else -> throw IllegalStateException()
            }
        }

        return Constants.RATE_CURRENTLY_UNAVAILABLE
    }

    private fun binaryToLocalCurrencyConverter(amount : String){
        val didFindDecimal = amount.validateBinary()
        if (didFindDecimal.isEmpty()) {
            tilAmountToSend.isErrorEnabled = false
            getLocalExchangeRate().let {
                val amountInLocalCurrency = amount.toDecimal() * it
                val amountInBinary = toBinary(amountInLocalCurrency)
                tieAmountToReceive.setText(getString(R.string.amount_payable_to_recipient,
                    appendLocalCurrencyToAnAmount(amountInBinary)))
            }

        } else {
            tilAmountToSend.error = "${didFindDecimal.last()} is not a valid amount"
            tilAmountToSend.isErrorEnabled = true
            tieAmountToReceive?.text = null
        }
    }

    private fun onCountryChanged() {
        countryPicker.setOnCountryChangeListener {
            try {
                val amount = tieSendAmount.text.toString()
                if(amount.isNotEmpty()){
                    binaryToLocalCurrencyConverter(amount)
                }
            }catch (ex: ValidationException) {
                ex.message?.let { requireView().showSnackBar(it) }
            }
        }
    }

    private fun appendLocalCurrencyToAnAmount(amount: String): String{
        return when(countryPicker.selectedCountryNameCode){
            Constants.NIGERIA -> getString(R.string.nigerian_naira,amount)
            Constants.UGANDA -> getString(R.string.uganda_shilling, amount)
            Constants.TANZANIA -> getString(R.string.tanzania_shilling, amount)
            Constants.KENYA -> getString(R.string.kenyan_shilling, amount)
            else -> throw IllegalStateException()
        }
    }

}