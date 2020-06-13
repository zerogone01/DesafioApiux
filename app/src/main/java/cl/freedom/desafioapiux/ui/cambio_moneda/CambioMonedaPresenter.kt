package cl.freedom.desafioapiux.ui.cambio_moneda

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import cl.freedom.desafioapiux.R
import cl.freedom.desafioapiux.api.ApiClient
import cl.freedom.desafioapiux.model.ExchangeRates
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMoneda.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CambioMonedaPresenter(private val apiClient: ApiClient,
                            private val sensorManager : SensorManager,
                            private val rotationSensor : Sensor) : Presenter, SensorEventListener {
    private lateinit var view: CambioMoneda.View
    private var isRight = false

    override fun setView(view: CambioMoneda.View) {
        this.view = view
    }

     override fun addListenerSensor() {
        try {
            sensorManager.registerListener(this, rotationSensor, SENSOR_DELAY)
        } catch (e: Exception) {
            Log.d("TAG1","Hardware compatibility issue")
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor == rotationSensor) {
            if (event.values.size > 4) {
                val truncatedRotationVector = FloatArray(4)
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4)
                update(truncatedRotationVector)
            } else {
                update(event.values)
            }
        }
    }

    private fun update(vectors: FloatArray) {
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors)
        val worldAxisX = SensorManager.AXIS_X
        val worldAxisZ = SensorManager.AXIS_Z
        val adjustedRotationMatrix = FloatArray(9)
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix)
        val orientation = FloatArray(3)
        SensorManager.getOrientation(adjustedRotationMatrix, orientation)
        //val pitch = orientation[1] * FROM_RADS_TO_DEGS
        val roll = orientation[2] * FROM_RADS_TO_DEGS
        changeDirectionArrow(roll)
        Log.d("TAG2", "" + roll)
    }

    fun changeDirectionArrow(value: Float) {
        isRight = if (value <= 0) { //flecha hacia la derecha
            if (!isRight) view.actualizarDireccionFlecha(true)
            true
        } else { //flecha hacia la izquierda
            if (isRight) view.actualizarDireccionFlecha(false)
            false
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        private const val SENSOR_DELAY = 500 * 1000 // 500ms
        private const val FROM_RADS_TO_DEGS = -57
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