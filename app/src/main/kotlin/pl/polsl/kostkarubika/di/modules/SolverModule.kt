package pl.polsl.kostkarubika.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.solver.algorithms.kociemba.CoordDataLoader
import pl.polsl.kostkarubika.solver.algorithms.kociemba.CoordTransformer
import pl.polsl.kostkarubika.solver.algorithms.kociemba.KociembaAlgorithmSolver
import pl.polsl.kostkarubika.solver.algorithms.lbl.LayerByLayerSolver
import java.io.File

@Module
class SolverModule {

    @Provides
    @ActivityScope
    fun provideCoordDataLoader(application: Application): CoordDataLoader {
        val dir = application.filesDir
        val file = File(dir, "rubik.data")
        return CoordDataLoader(file)
    }

    @Provides
    @ActivityScope
    fun provideCoordTransformer(coordDataLoader: CoordDataLoader): CoordTransformer {
        val coordTransformer = CoordTransformer(coordDataLoader)
        return coordTransformer
    }

    @Provides
    @ActivityScope
    fun provideKociembaAlgorithmSolver(coordTransformer: CoordTransformer): KociembaAlgorithmSolver {
        val solver = KociembaAlgorithmSolver(coordTransformer)
        return solver
    }

    @Provides
    @ActivityScope
    fun provideLayerByLayerSolver(): LayerByLayerSolver {
        return LayerByLayerSolver()
    }
}