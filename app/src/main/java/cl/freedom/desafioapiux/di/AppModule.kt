package cl.freedom.desafioapiux.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import cl.freedom.desafioapiux.api.ApiClient
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMoneda.Presenter
import cl.freedom.desafioapiux.ui.cambio_moneda.CambioMonedaPresenter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, converterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(converterFactory)
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Provides
    @Singleton
    fun provideSensorManager(context: Context) : SensorManager
    {
        val sensorService = Activity.SENSOR_SERVICE;
        return context.getSystemService(sensorService) as SensorManager
    }

    @Provides
    @Singleton
    fun provideRotationSensor(sensorManager: SensorManager) : Sensor
    {
        return sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }


    @Provides
    @Singleton
    fun providesPresenterCambioMoneda(apiClient: ApiClient, sensorManager: SensorManager, rotationSensor: Sensor): Presenter {
        return CambioMonedaPresenter(apiClient, sensorManager, rotationSensor)
    }

    companion object {
        private const val BASE_URL = "https://api.exchangeratesapi.io"
    }

}