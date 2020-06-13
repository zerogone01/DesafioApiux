package cl.freedom.desafioapiux.di

import android.app.Application

class BaseApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        setUpGraph()
    }

    private fun setUpGraph() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

}