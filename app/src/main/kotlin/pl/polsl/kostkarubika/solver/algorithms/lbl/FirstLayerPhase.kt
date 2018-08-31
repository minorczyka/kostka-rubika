package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Corner.*
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*
import pl.polsl.kostkarubika.solver.enums.MoveAxis
import pl.polsl.kostkarubika.solver.enums.MovePower
import pl.polsl.kostkarubika.solver.utils.transformMoves

class FirstLayerPhase : AlgorithmPhase {

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        val state = CubeForm(cubeForm)
        var result = emptyList<Move>()
        val corners = listOf(Pair(4, DFR), Pair(5, DLF), Pair(6, DBL), Pair(7, DRB))
        corners.forEach {
            val (index, corner) = it
            if (state.cornerPermutation[index] != corner || state.cornerOrientation[index] > 0) {
                result += solveCorner(state, corner)
            }
        }
        return result
    }

    private fun solveCorner(cubeForm: CubeForm, corner: Corner): List<Move> {
        var result = emptyList<Move>()
        var index = cubeForm.cornerPermutation.indexOf(corner)
        var orientation = cubeForm.cornerOrientation[index]

        if (index >= 4) {
            val moves = moveCornerFromBottom(index, orientation)
            result += moves
            moves.forEach { cubeForm.makeMove(it) }
            index = cubeForm.cornerPermutation.indexOf(corner)
            orientation = cubeForm.cornerOrientation[index]
        }

        val upMove = moveCornerOnUpper(corner, index)
        if (upMove != null) {
            result += upMove
            cubeForm.makeMove(upMove)
            index = cubeForm.cornerPermutation.indexOf(corner)
            orientation = cubeForm.cornerOrientation[index]
        }

        if (orientation == 0.toByte()) {
            val moves = rotateCornerOnUpper(corner)
            result += moves
            moves.forEach { cubeForm.makeMove(it) }
            index = cubeForm.cornerPermutation.indexOf(corner)
            orientation = cubeForm.cornerOrientation[index]
        }

        val moves = moveCornerToBottom(corner, orientation)
        result += moves
        moves.forEach { cubeForm.makeMove(it) }
        return result
    }

    private fun getTransformForCorner(corner: Corner): MoveAxis {
        return when (corner) {
            DLF -> MoveAxis.L
            DBL -> MoveAxis.B
            DRB -> MoveAxis.R
            else -> MoveAxis.F
        }
    }

    private fun moveCornerFromBottom(position: Int, orientation: Byte): List<Move> {
        val moves = if (orientation == 1.toByte()) {
            listOf(F3, U3, F1)
        } else {
            listOf(F3, U1, F1)
        }
        val transform = getTransformForCorner(Corner.values()[position])
        return transformMoves(moves, transform)
    }

    private fun moveCornerOnUpper(corner: Corner, cornerPosition: Int): Move? {
        val turns = (corner.ordinal - cornerPosition + 4) % 4
        return when (turns) {
            1 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.ONE)
            2 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.TWO)
            3 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.THREE)
            else -> null
        }
    }

    private fun rotateCornerOnUpper(corner: Corner): List<Move> {
        val moves = listOf(R1, U2, R3, U3)
        val transform = getTransformForCorner(corner)
        return transformMoves(moves, transform)
    }

    private fun moveCornerToBottom(corner: Corner, orientation: Byte): List<Move> {
        val moves = if (orientation == 1.toByte()) {
            listOf(R1, U1, R3)
        } else {
            listOf(F3, U3, F1)
        }
        val transform = getTransformForCorner(corner)
        return transformMoves(moves, transform)
    }
}