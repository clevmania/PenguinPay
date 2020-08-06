package com.clevmania.penguinpay.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.clevmania.penguinpay.R

class SendMoneyFragment : Fragment() {

    companion object {
        fun newInstance() = SendMoneyFragment()
    }

    private lateinit var viewModel: SendMoneyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.send_money_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SendMoneyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}