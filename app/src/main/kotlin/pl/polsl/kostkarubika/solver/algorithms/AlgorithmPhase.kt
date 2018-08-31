package pl.polsl.kostkarubika.solver.algorithms

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.enums.Move

interface AlgorithmPhase {

    fun solvePhase(cubeForm: CubeForm): List<Move>
}