package pl.polsl.kostkarubika.di.modules

import android.app.Application
import android.content.SharedPreferences
import android.content.res.AssetManager
import android.preference.PreferenceManager

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import pl.polsl.kostkarubika.detector.ColorProvider

@Module
class AppModule(private var application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesAssetManager(): AssetManager {
        return application.assets
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    @Provides
    @Singleton
    fun provideColorProvider(sharedPreferences: SharedPreferences): ColorProvider {
        return ColorProvider(sharedPreferences)
    }
}
