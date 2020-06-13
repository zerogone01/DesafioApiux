package cl.freedom.desafioapiux.ui.cambio_moneda

interface CambioMoneda {
    interface View {
        fun actualizarMonedas(cad: String, gbp: String, mxn: String)
    }

    interface Presenter {
        fun setView(view: View)
        fun solicitudWebService(moneda: String)
    }
}