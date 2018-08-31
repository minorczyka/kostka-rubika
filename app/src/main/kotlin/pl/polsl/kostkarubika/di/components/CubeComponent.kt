package pl.polsl.kostkarubika.di.components

import dagger.Component
import pl.polsl.kostkarubika.di.modules.CubeModule
import pl.polsl.kostkarubika.di.modules.OpenGLModule
import pl.polsl.kostkarubika.di.modules.SolverModule
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.views.solver.CubeSurfaceView
import pl.polsl.kostkarubika.views.solver.SolverActivity

@ActivityScope
@Component(modules = arrayOf(CubeModule::class, SolverModule::class, OpenGLModule::class), dependencies = arrayOf(AppComponent::class))
interface CubeComponent {

    fun inject(activity: SolverActivity)
    fun inject(surfaceView: CubeSurfaceView)
}