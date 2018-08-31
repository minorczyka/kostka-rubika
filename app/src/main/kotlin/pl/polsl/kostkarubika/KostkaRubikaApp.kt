package pl.polsl.kostkarubika

import android.app.Application
import pl.polsl.kostkarubika.di.components.AppComponent
import pl.polsl.kostkarubika.di.components.DaggerAppComponent
import pl.polsl.kostkarubika.di.modules.AppModule
import pl.polsl.kostkarubika.di.modules.OpenGLModule

class KostkaRubikaApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}
