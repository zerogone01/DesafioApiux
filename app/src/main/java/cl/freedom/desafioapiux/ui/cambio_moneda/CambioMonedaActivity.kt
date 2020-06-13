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

class CambioMonedaActivity : AppCompatActivity(), CambioMoneda.View, View.OnClickListener, SensorEventListener {
    @JvmField
    @Inject
    var presenter: Presenter? = null
    private lateinit var radioButtonEuro: RadioButton
    private lateinit var radioButtonUsd: RadioButton
    private lateinit var textViewValueCad: TextView
    private lateinit var textViewValueGbp: TextView
    private lateinit var textViewValueMxn: TextView
    private lateinit var imageViewArrow: ImageView
    private lateinit var sensorManager: SensorManager
    private lateinit var rotationSensor: Sensor
    private var isRight = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio_moneda)
        (application as BaseApp).appComponent.inject(this)
        setViews()
        presenter!!.setView(this)
        configureSensorAndRotation()
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

    private fun configureSensorAndRotation() {
        try {
            sensorManager = getSystemService(Activity.SENSOR_SERVICE) as SensorManager
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            sensorManager.registerListener(this, rotationSensor, SENSOR_DELAY)
        } catch (e: Exception) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show()
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
            if (!isRight) imageViewArrow.setImageResource(R.drawable.arrow_right)
            true
        } else { //flecha hacia la izquierda
            if (isRight) imageViewArrow.setImageResource(R.drawable.arrow_left)
            false
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        private const val SENSOR_DELAY = 500 * 1000 // 500ms
        private const val FROM_RADS_TO_DEGS = -57
    }
}