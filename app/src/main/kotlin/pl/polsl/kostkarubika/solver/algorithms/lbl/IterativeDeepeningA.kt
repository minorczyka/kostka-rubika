package pl.polsl.kostkarubika.solver.algorithms.lbl

import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.utils.Either
import pl.polsl.kostkarubika.solver.utils.Left
import pl.polsl.kostkarubika.solver.utils.Right

abstract class IterativeDeepeningA<T> {

    var count = 0

    abstract fun getHeuristic(state: T): Int

    abstract fun getNextState(state: T, move: Move): T

    fun getSolution(state: T): List<Move> {
        var bound = getHeuristic(state)
        while (true) {
            val res = search(state, null, 0, bound)
            when (res) {
                is Left -> bound = res.value
                is Right -> return res.value
            }
        }
    }

    protected fun search(state: T, lastMove: Move?, depth: Int, bound: Int): Either<Int, List<Move>> {
        ++count
        val heuristic = getHeuristic(state)
        val distance = depth + heuristic

        if (distance > bound) {
            return Left(distance)
        }
        if (heuristic == 0 && depth == bound) {
            return Right(emptyList())
        }
        var min = Int.MAX_VALUE
        for (move in Move.values()) {
            if (lastMove == null || (move.axis != lastMove.axis)) {
                val nextState = getNextState(state, move)
                val res = search(nextState, move, depth + 1, bound)
                when (res) {
                    is Left -> min = Math.min(min, res.value)
                    is Right -> return Right(listOf(move) + res.value)
                }
            }
        }
        return Left(min)
    }
}