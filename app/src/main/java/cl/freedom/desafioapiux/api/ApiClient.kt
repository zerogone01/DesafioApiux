package cl.freedom.desafioapiux.api

import cl.freedom.desafioapiux.model.ExchangeRates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {
    @GET("/latest")
    fun getExchangeRates(@Query("base") moneda: String): Call<ExchangeRates>
}