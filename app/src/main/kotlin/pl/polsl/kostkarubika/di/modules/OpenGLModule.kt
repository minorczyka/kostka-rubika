package pl.polsl.kostkarubika.di.modules

import android.content.res.AssetManager

import dagger.Module
import dagger.Provides
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.graphics.utils.Arcball
import pl.polsl.kostkarubika.graphics.utils.Camera
import pl.polsl.kostkarubika.graphics.utils.Shader
import java.io.IOException

@Module
class OpenGLModule {

    @Provides
    @ActivityScope
    fun provideShader(assetManager: AssetManager): Shader {
        val shader = try {
            Shader(assetManager.open("shaders/cube.vert"), assetManager.open("shaders/cube.frag"))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
        return shader!!
    }

    @Provides
    @ActivityScope
    fun provideCamera(): Camera {
        return Camera()
    }

    @Provides
    @ActivityScope
    fun provideArcball(): Arcball {
        return Arcball()
    }
}
