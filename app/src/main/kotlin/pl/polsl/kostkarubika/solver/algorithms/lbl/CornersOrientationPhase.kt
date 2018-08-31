package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*

class CornersOrientationPhase : AlgorithmPhase {

    private val orientMoves = listOf(R3, D3, R1, D1)

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        val state = CubeForm(cubeForm)
        val result = mutableListOf<Move>()
        for (i in 1..4) {
            while(cornerIsIncorrect(state)) {
                orientMoves.forEach { state.makeMove(it) }
                result += orientMoves
            }
            state.makeMove(U1)
            result += U1
        }
        return result
    }

    private fun cornerIsIncorrect(cubeForm: CubeForm): Boolean {
        return cubeForm.cornerOrientation[0] > 0
                || (cubeForm.cornerPermutation[0] != Corner.URF
                && cubeForm.cornerPermutation[0] != Corner.UFL
                && cubeForm.cornerPermutation[0] != Corner.ULB
                && cubeForm.cornerPermutation[0] != Corner.UBR)
    }
}