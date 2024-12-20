package com.example.currencyconverterapp


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("latest") // Endpunkt der API, z.B. fixer.io/latest
    suspend fun getExchangeRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<ExchangeRateResponse>
}
