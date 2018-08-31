package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*

class TopCrossPermutationPhase : AlgorithmPhase {

    private val swapMoves = listOf(R1, U1, R3, U1, R1, U2, R3, U1)

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        if (checkCubeForm(cubeForm)) {
            return emptyList()
        }
        for (i in 0..3) {
            for (j in 0..3) {
                val state = CubeForm(cubeForm)
                val moves = movesWithOneSwap(i, j)
                moves.forEach { state.makeMove(it) }
                if (checkCubeForm(state)) {
                    return moves
                }
            }
        }
        for (i in 0..3) {
            for (j in 0..3) {
                for (k in 0..3) {
                    val state = CubeForm(cubeForm)
                    val moves = movesWithTwoSwaps(i, j, k)
                    moves.forEach { state.makeMove(it) }
                    if (checkCubeForm(state)) {
                        return moves
                    }
                }
            }
        }
        return emptyList()
    }

    private fun checkCubeForm(cubeForm: CubeForm): Boolean {
        return cubeForm.edgePermutation[0] == Edge.UR
                && cubeForm.edgePermutation[1] == Edge.UF
                && cubeForm.edgePermutation[2] == Edge.UL
                && cubeForm.edgePermutation[3] == Edge.UB
    }

    private fun movesWithOneSwap(firstTurn: Int, secondTurn: Int): List<Move> {
        val moves = mutableListOf<Move>()
        when (firstTurn) {
            1 -> moves += U1
            2 -> moves += U2
            3 -> moves += U3
        }
        moves += swapMoves
        when (secondTurn) {
            1 -> moves += U1
            2 -> moves += U2
            3 -> moves += U3
        }
        return moves
    }

    private fun movesWithTwoSwaps(firstTurn: Int, secondTurn: Int, thirdTurn: Int): List<Move> {
        val moves = mutableListOf<Move>()
        when (firstTurn) {
            1 -> moves += U1
            2 -> moves += U2
            3 -> moves += U3
        }
        moves += swapMoves
        moves += movesWithOneSwap(secondTurn, thirdTurn)
        return moves
    }
}