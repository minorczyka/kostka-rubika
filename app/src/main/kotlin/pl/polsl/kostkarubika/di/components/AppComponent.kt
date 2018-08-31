package pl.polsl.kostkarubika.di.components

import android.app.Application
import android.content.res.AssetManager
import dagger.Component
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.di.modules.AppModule
import pl.polsl.kostkarubika.views.draw.DrawActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun application(): Application
    fun assetManager(): AssetManager
    fun colorProvider(): ColorProvider

    fun inject(activity: DrawActivity)
}