package cl.freedom.desafioapiux.ui.cambio_moneda

interface CambioMoneda {
    interface View {
        fun actualizarMonedas(cad: String, gbp: String, mxn: String)
        fun actualizarDireccionFlecha(isRight : Boolean)
    }

    interface Presenter {
        fun setView(view: View)
        fun solicitudWebService(moneda: String)
        fun addListenerSensor()
    }
}