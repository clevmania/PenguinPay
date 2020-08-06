package com.clevmania.penguinpay.data

import com.clevmania.penguinpay.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author by Lawrence on 8/6/20.
 * for PenguinPay
 */
interface PenguinPayService {
    @GET("api/latest.json")
    suspend fun retrieveExchangeRates(
        @Query("symbols") symbols: String,
        @Query("app_id") appId: String
    ): ExchangeRateResponse
}

interface PenguinPayDataSource {
    suspend fun retrieveExchangeRate(symbols: String, id: String): ExchangeRateResponse
}

class PenguinPayRepository(
    private val apiService: PenguinPayService
) : PenguinPayDataSource {
    override suspend fun retrieveExchangeRate(symbols: String, id: String): ExchangeRateResponse {
        return apiService.retrieveExchangeRates(symbols, id)
    }
}