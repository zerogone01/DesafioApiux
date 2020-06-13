package cl.freedom.desafioapiux.ui.cambio_moneda

import android.util.Log
import cl.freedom.desafioapiux.api.ApiClient
import cl.freedom.desafioapiux.model.ExchangeRates
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMoneda.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CambioMonedaPresenter(private val apiClient: ApiClient) : Presenter {
    private lateinit var view: CambioMoneda.View

    override fun setView(view: CambioMoneda.View) {
        this.view = view
    }

    //peticion al servidor y comprobacion de los datos
    override fun solicitudWebService(moneda: String) {

            val call = apiClient.getExchangeRates(moneda)
            call.enqueue(object : Callback<ExchangeRates> {
                override fun onResponse(call: Call<ExchangeRates>, response: Response<ExchangeRates>) {
                    if (response.isSuccessful) {
                        val exchangeRates = response.body()
                        val cad = exchangeRates?.rates!!.cad.toString()
                        val gbp = exchangeRates.rates!!.gbp.toString()
                        val mxn = exchangeRates.rates!!.mxn.toString()
                        view.actualizarMonedas(cad, gbp, mxn)
                        Log.d("TAG1", "Cad $cad")
                        Log.d("TAG1", "Gbp $gbp")
                        Log.d("TAG1", "Mxn $mxn")
                    }
                }

                override fun onFailure(call: Call<ExchangeRates?>, t: Throwable) {
                    Log.d("TAG1", "FALLO: " + t.message)
                }
            })

    }



}