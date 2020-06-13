package cl.freedom.desafioapiux.di

import cl.freedom.desafioapiux.di.AppModule
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMonedaActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(cambioMonedaActivity: CambioMonedaActivity)
}