package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*

class TopCrossPhase : AlgorithmPhase {

    enum class CrossPattern {
        Dot, L, Line, Cross
    }

    private val dotOrLineMoves = listOf(F1, R1, U1, R3, U3, F3)
    private val lMoves = listOf(F1, U1, R1, U3, R3, F3)

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        val state = CubeForm(cubeForm)
        var result = emptyList<Move>()
        while (true) {
            val faceForm = state.toFaceForm()
            val pattern = matchFace(faceForm)
            val moves = when (pattern) {
                CrossPattern.Dot -> dotOrLineMoves
                CrossPattern.L -> {
                    val moves = mutableListOf<Move>()
                    val move = orientateL(faceForm)
                    if (move != null) {
                        moves += move
                    }
                    moves += lMoves
                    moves
                }
                CrossPattern.Line -> {
                    val moves = mutableListOf<Move>()
                    val move = orientateLine(faceForm)
                    if (move != null) {
                        moves += move
                    }
                    moves += dotOrLineMoves
                    moves
                }
                CrossPattern.Cross -> null
            }
            if (moves != null) {
                result += moves
                moves.forEach { state.makeMove(it) }
            } else {
                break
            }
        }
        return result
    }

    private fun matchFace(faceForm: FaceForm): CrossPattern {
        val count = listOf(faceForm.fields[1], faceForm.fields[3], faceForm.fields[5], faceForm.fields[7])
                .filter { it == Color.U }
                .size
        return when (count) {
            4 -> CrossPattern.Cross
            2 -> {
                if ((faceForm.fields[1] == Color.U && faceForm.fields[7] == Color.U) ||
                        (faceForm.fields[3] == Color.U && faceForm.fields[5] == Color.U)) {
                    CrossPattern.Line
                } else {
                    CrossPattern.L
                }
            }
            else -> CrossPattern.Dot
        }
    }

    private fun orientateL(faceForm: FaceForm): Move? {
        return if (faceForm.fields[3] == Color.U && faceForm.fields[7] == Color.U) {
            Move.U1
        } else if (faceForm.fields[5] == Color.U && faceForm.fields[7] == Color.U) {
            Move.U2
        } else if (faceForm.fields[1] == Color.U && faceForm.fields[5] == Color.U) {
            Move.U3
        } else {
            null
        }
    }

    private fun orientateLine(faceForm: FaceForm): Move? {
        return if (faceForm.fields[1] == Color.U) {
            Move.U1
        } else {
            null
        }
    }
}