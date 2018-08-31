package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*
import pl.polsl.kostkarubika.solver.enums.MoveAxis
import pl.polsl.kostkarubika.solver.utils.transformMoves

class CornersPositionPhase : AlgorithmPhase {

    private val cornersOrientationMoves = listOf(U1, R1, U3, L3, U1, R3, U3, L1)

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        val state = CubeForm(cubeForm)
        val result = mutableListOf<Move>()
        when (correctCorners(state)) {
            0 -> {
                cornersOrientationMoves.forEach { state.makeMove(it) }
                result += cornersOrientationMoves
            }
            4 -> return result
        }
        val correctCorner = findCorrectCorner(state)
        val transform = getTransform(correctCorner)
        val moves = transformMoves(cornersOrientationMoves, transform)
        while (correctCorners(state) != 4) {
            moves.forEach { state.makeMove(it) }
            result += moves
        }
        return result
    }

    private fun correctCorners(cubeForm: CubeForm): Int {
        var result = 0
        if (cubeForm.cornerPermutation[0] == Corner.URF) {
            result += 1
        }
        if (cubeForm.cornerPermutation[1] == Corner.UFL) {
            result += 1
        }
        if (cubeForm.cornerPermutation[2] == Corner.ULB) {
            result += 1
        }
        if (cubeForm.cornerPermutation[3] == Corner.UBR) {
            result += 1
        }
        return result
    }

    private fun findCorrectCorner(cubeForm: CubeForm): Corner {
        if (cubeForm.cornerPermutation[0] == Corner.URF) {
            return Corner.URF
        }
        if (cubeForm.cornerPermutation[1] == Corner.UFL) {
            return Corner.UFL
        }
        if (cubeForm.cornerPermutation[2] == Corner.ULB) {
            return Corner.ULB
        }
        return Corner.UBR
    }

    private fun getTransform(corner: Corner): MoveAxis {
        return when (corner) {
            Corner.URF -> MoveAxis.F
            Corner.UFL -> MoveAxis.L
            Corner.ULB -> MoveAxis.B
            else -> MoveAxis.R
        }
    }
}