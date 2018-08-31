package pl.polsl.kostkarubika.di.modules

import dagger.Module
import dagger.Provides
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.graphics.rubik.RubikCube
import pl.polsl.kostkarubika.graphics.utils.Arcball
import pl.polsl.kostkarubika.graphics.utils.Camera
import pl.polsl.kostkarubika.graphics.utils.Shader
import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.algorithms.kociemba.KociembaAlgorithmSolver
import pl.polsl.kostkarubika.solver.algorithms.lbl.LayerByLayerSolver
import pl.polsl.kostkarubika.views.solver.CubeRenderer
import pl.polsl.kostkarubika.views.solver.SolverViewModel

@Module
class CubeModule(val faceForm: FaceForm) {

    @Provides
    @ActivityScope
    fun provideRubikCube(shader: Shader, colorProvider: ColorProvider): RubikCube {
        return RubikCube(shader, faceForm, colorProvider)
    }

    @Provides
    @ActivityScope
    fun provideCubeRenderer(camera: Camera, rubikCube: RubikCube, arcball: Arcball): CubeRenderer {
        return CubeRenderer(camera, rubikCube, arcball, faceForm)
    }

    @Provides
    @ActivityScope
    fun provideSolverViewModel(kociemba: KociembaAlgorithmSolver, lbl: LayerByLayerSolver, rubikCube: RubikCube): SolverViewModel {
        return SolverViewModel(faceForm, kociemba, lbl, rubikCube)
    }
}