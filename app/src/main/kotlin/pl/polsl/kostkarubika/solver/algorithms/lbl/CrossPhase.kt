package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Edge.*
import pl.polsl.kostkarubika.solver.enums.Move

internal class CrossPhase : IterativeDeepeningA<CubeForm>(), AlgorithmPhase {

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        return getSolution(cubeForm)
    }

    override fun getHeuristic(state: CubeForm): Int {
        var result = 0
        if (state.edgePermutation[4] != DR || state.edgeOrientation[4] > 0) {
            ++result
        }
        if (state.edgePermutation[5] != DF || state.edgeOrientation[5] > 0) {
            ++result
        }
        if (state.edgePermutation[6] != DL || state.edgeOrientation[6] > 0) {
            ++result
        }
        if (state.edgePermutation[7] != DB || state.edgeOrientation[7] > 0) {
            ++result
        }
        return result
    }

    override fun getNextState(state: CubeForm, move: Move): CubeForm {
        val nextState = CubeForm(state)
        nextState.makeMove(move)
        return nextState
    }
}