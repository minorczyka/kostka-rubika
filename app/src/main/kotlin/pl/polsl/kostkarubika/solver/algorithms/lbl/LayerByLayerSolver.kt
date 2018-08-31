package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.Solution
import pl.polsl.kostkarubika.solver.algorithms.Solver
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.MovePower

class LayerByLayerSolver : Solver {

    private val phases = listOf(CrossPhase(),
            FirstLayerPhase(),
            SecondLayerPhase(),
            TopCrossPhase(),
            TopCrossPermutationPhase(),
            CornersPositionPhase(),
            CornersOrientationPhase())

    override fun prepare() { }

    override fun getSolution(faceForm: FaceForm): Solution? {
        val cubeForm = faceForm.toCubeForm()
        if (!cubeForm.isValid) {
            return null
        }

        val moves = phases.map { phase ->
            val moves = phase.solvePhase(cubeForm)
            moves.forEach { cubeForm.makeMove(it) }
            moves
        }
        val cleanMoves = moves.map { cleanDuplicates(it) }
        val phases = mutableListOf(0)
        for (i in 0..cleanMoves.size - 2) {
            phases += (phases[i] + cleanMoves[i].size)
        }

        return Solution(cleanMoves.flatMap { it }.toTypedArray(), phases.toTypedArray())
    }

    private fun cleanDuplicates(moves: List<Move>): List<Move> {
        if (moves.isEmpty()) {
            return moves
        }
        val result = mutableListOf<Move>()
        for (move in moves) {
            val lastMove = result.lastOrNull()
            if (lastMove != null && lastMove.axis == move.axis) {
                val turns = (lastMove.power.twist + move.power.twist) % 4
                when (turns) {
                    0 -> result.removeAt(result.lastIndex)
                    1 -> result[result.lastIndex] = Move.moveFromAxisAndPower(move.axis, MovePower.ONE)
                    2 -> result[result.lastIndex] = Move.moveFromAxisAndPower(move.axis, MovePower.TWO)
                    3 -> result[result.lastIndex] = Move.moveFromAxisAndPower(move.axis, MovePower.THREE)
                }
            } else {
                result += move
            }
        }
        return result
    }
}