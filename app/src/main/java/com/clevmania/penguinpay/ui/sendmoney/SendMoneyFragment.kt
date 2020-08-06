package com.clevmania.penguinpay.ui.sendmoney

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.penguinpay.R
import com.clevmania.penguinpay.model.Rates
import com.clevmania.penguinpay.ui.constants.Constants
import com.clevmania.penguinpay.ui.utils.ValidationType
import com.clevmania.penguinpay.ui.utils.validate
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.send_money_fragment.*
import java.lang.IllegalStateException
import kotlin.math.roundToInt

class SendMoneyFragment : Fragment() {
    private var currentExchangeRates : Rates? = null

    private lateinit var viewModel: SendMoneyViewModel

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

    }

    private fun validateFields(){
        try{
            if(getLocalExchangeRate() == Constants.RATE_CURRENTLY_UNAVAILABLE){
//                viewModel.retrieveRates()
            }
            val firstName = tilFirstName.validate(ValidationType.NAME,getString(R.string.first_name))
            val lastName = tilLastName.validate(ValidationType.NAME,getString(R.string.last_name))
            val mobile = tilMobile.validate(ValidationType.MOBILE,
                getString(R.string.mobile),picker = countryPicker)
            val amount = tilAmountToSend.validate(ValidationType.BINARY_AMOUNT,
                "Amount",editText = tieAmountToReceive,xChangeRate = getLocalExchangeRate())

            // We can now send this as params to some endpoint
            // Upon success, show notification

        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun getLocalExchangeRate(): Int{
        currentExchangeRates?.let {
            return when(countryPicker.selectedCountryNameCode){
                "NG" -> it.NGN.roundToInt()
                "UG" -> it.UGX.roundToInt()
                "TZ" -> it.TZS.roundToInt()
                "KE" -> it.KES.roundToInt()
                else -> throw IllegalStateException()
            }
        }
        return Constants.RATE_CURRENTLY_UNAVAILABLE
    }

    private fun onCountryChanged(){
        countryPicker.setOnCountryChangeListener {
            if(getLocalExchangeRate() == Constants.RATE_CURRENTLY_UNAVAILABLE){
//                viewModel.retrieveRates()
            }else{
                tilAmountToSend.validate(ValidationType.BINARY_AMOUNT,
                    "Amount",editText = tieAmountToReceive,xChangeRate = getLocalExchangeRate())
                tilMobile.validate(ValidationType.MOBILE,getString(R.string.mobile))
            }
        }
    }

}