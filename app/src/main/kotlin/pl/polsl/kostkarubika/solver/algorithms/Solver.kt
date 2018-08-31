package pl.polsl.kostkarubika.solver.algorithms

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.Solution

interface Solver {

    fun getSolution(faceForm: FaceForm): Solution?

    fun prepare()
}