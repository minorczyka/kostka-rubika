package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.AlgorithmPhase
import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.enums.Edge.*
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*
import pl.polsl.kostkarubika.solver.enums.MoveAxis
import pl.polsl.kostkarubika.solver.enums.MovePower
import pl.polsl.kostkarubika.solver.utils.transformMoves

class SecondLayerPhase : AlgorithmPhase {

    override fun solvePhase(cubeForm: CubeForm): List<Move> {
        val state = CubeForm(cubeForm)
        var result = emptyList<Move>()
        val edges = listOf(Pair(8, FR), Pair(9, FL), Pair(10, BL), Pair(11, BR))
        edges.forEach {
            val (index, edge) = it
            if (state.edgePermutation[index] != edge || state.edgeOrientation[index] > 0) {
                result += solveEdge(state, edge)
            }
        }
        return result
    }

    private fun solveEdge(cubeForm: CubeForm, edge: Edge): List<Move> {
        var result = emptyList<Move>()
        var index = cubeForm.edgePermutation.indexOf(edge)
        var orientation = cubeForm.edgeOrientation[index]

        if (index >= 8) {
            val moves = moveEdgeFromMiddle(index)
            result += moves
            moves.forEach { cubeForm.makeMove(it) }
            index = cubeForm.edgePermutation.indexOf(edge)
            orientation = cubeForm.edgeOrientation[index]
        }

        val upMove = moveEdgeOnUpper(edge, orientation, index)
        if (upMove != null) {
            result += upMove
            cubeForm.makeMove(upMove)
            index = cubeForm.edgePermutation.indexOf(edge)
            orientation = cubeForm.edgeOrientation[index]
        }

        val moves = moveEdgeToMiddle(edge, index, orientation)
        result += moves
        moves.forEach { cubeForm.makeMove(it) }
        return result
    }

    private fun getTransformForEdge(edge: Edge): MoveAxis {
        return when (edge) {
            FL, UL -> MoveAxis.L
            BL, UB -> MoveAxis.B
            BR, UR -> MoveAxis.R
            else -> MoveAxis.F
        }
    }

    private fun moveEdgeFromMiddle(position: Int): List<Move> {
        val moves = listOf(U1, R1, U3, R3, U3, F3, U1, F1)
        val transformer = getTransformForEdge(Edge.values()[position])
        return transformMoves(moves, transformer)
    }

    private fun getPositionForEdge(edge: Edge, orientation: Byte): Edge {
        val edges = listOf(FR, FL, BL, BR)
        val positions = listOf(UR, UF, UL, UB)
        val o: Int = if (edge == FL || edge == BR) 1 - orientation else orientation.toInt()
        val index = (edges.indexOf(edge) + o) % positions.size
        return positions[index]
    }

    private fun moveEdgeOnUpper(edge: Edge, orientation: Byte, edgePosition: Int): Move? {
        val destination = getPositionForEdge(edge, orientation)
        val turns = (destination.ordinal - edgePosition + 4) % 4
        return when (turns) {
            1 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.ONE)
            2 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.TWO)
            3 -> Move.moveFromAxisAndPower(MoveAxis.U, MovePower.THREE)
            else -> null
        }
    }

    private fun moveEdgeToMiddle(edge:Edge, edgePosition: Int, orientation: Byte): List<Move> {
        val o: Int = if (edge == FL || edge == BR) 1 - orientation else orientation.toInt()
        val moves = if (o > 0) {
            listOf(U1, R1, U3, R3, U3, F3, U1, F1)
        } else {
            listOf(U3, L3, U1, L1, U1, F1, U3, F3)
        }
        val transform = getTransformForEdge(Edge.values()[edgePosition])
        return transformMoves(moves, transform)
    }
}