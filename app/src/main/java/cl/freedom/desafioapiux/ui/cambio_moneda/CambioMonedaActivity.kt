package cl.freedom.desafioapiux.ui.cambio_moneda

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cl.freedom.desafioapiux.R
import cl.freedom.desafioapiux.di.BaseApp
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMoneda.Presenter
import javax.inject.Inject

class CambioMonedaActivity : AppCompatActivity(), CambioMoneda.View, View.OnClickListener {
    @JvmField
    @Inject
    var presenter: Presenter? = null
    private lateinit var radioButtonEuro: RadioButton
    private lateinit var radioButtonUsd: RadioButton
    private lateinit var textViewValueCad: TextView
    private lateinit var textViewValueGbp: TextView
    private lateinit var textViewValueMxn: TextView
    private lateinit var imageViewArrow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio_moneda)
        (application as BaseApp).appComponent.inject(this)
        setViews()
        presenter!!.setView(this)
        presenter!!.addListenerSensor()
    }



    private fun setViews() {
        radioButtonEuro = findViewById(R.id.radioButtonEuro)
        radioButtonUsd = findViewById(R.id.radioButtonUsd)
        textViewValueCad = findViewById(R.id.textViewValueCanada)
        textViewValueGbp = findViewById(R.id.textViewValueGreatBritain)
        textViewValueMxn = findViewById(R.id.textViewValueMexico)
        radioButtonEuro.setOnClickListener(this)
        radioButtonUsd.setOnClickListener(this)
        imageViewArrow = findViewById(R.id.imageViewArrow)
    }

    override fun onClick(view: View) {
        val checked = (view as RadioButton).isChecked
        when (view.getId()) {
            R.id.radioButtonUsd -> if (checked) {
                presenter!!.solicitudWebService("USD")
                Log.d("TAG1", "USD")
            }
            R.id.radioButtonEuro -> if (checked) {
                presenter!!.solicitudWebService("EUR")
                Log.d("TAG1", "EURO")
            }
        }
    }



    override fun actualizarMonedas(cad: String, gbp: String, mxn: String) {
        textViewValueCad.text = cad
        textViewValueGbp.text = gbp
        textViewValueMxn.text = mxn
    }

    override fun actualizarDireccionFlecha(isRight: Boolean) {
        val resource : Int = if(isRight) R.drawable.arrow_right else R.drawable.arrow_left;
        imageViewArrow.setImageResource(resource)
    }


}