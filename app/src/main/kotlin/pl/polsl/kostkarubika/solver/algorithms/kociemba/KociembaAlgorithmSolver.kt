package pl.polsl.kostkarubika.solver.algorithms.kociemba

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.Solution
import pl.polsl.kostkarubika.solver.algorithms.Solver
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*

class KociembaAlgorithmSolver(val transformer: CoordTransformer) : Solver {

    private val foundSolution = -1
    private val secondPhaseMoves = listOf(U1, U2, U3, R2, F2, D1, D2, D3, L2, B2)

    private var maxDepth = 24
    private var firstPhaseDepth = 0
    private var secondPhaseDepth = 0
    private val moveStack = Array(31) { D1 }
    private val stateStack = Array(31) { CubeState() }

    override fun prepare() {
        transformer.prepare()
    }

    override fun getSolution(faceForm: FaceForm): Solution? {
        val cubeForm = faceForm.toCubeForm()
        if (!cubeForm.isValid) {
            return null
        }

        val coordCubeForm = CoordCubeForm(cubeForm)

        stateStack[0].flip = coordCubeForm.flip.toInt()
        stateStack[0].twist = coordCubeForm.twist.toInt()
        stateStack[0].slice = coordCubeForm.fRtoBR / 24

        stateStack[0].urfToDlf = coordCubeForm.urFtoDLF.toInt()
        stateStack[0].frToBr = coordCubeForm.fRtoBR.toInt()
        stateStack[0].parity = coordCubeForm.cornerParity.toInt()
        stateStack[0].urToUl = coordCubeForm.uRtoUL.toInt()
        stateStack[0].ubToDf = coordCubeForm.uBtoDF.toInt()

        var bound = getFirstPhaseHeuristic(stateStack[0])
        while (true) {
            val res = firstPhaseSearch(0, bound)
            when (res) {
                foundSolution -> return Solution(moveStack.sliceArray(IntRange(0, secondPhaseDepth - 1)), arrayOf(0, firstPhaseDepth))
                else -> bound = res
            }
        }
    }

    private fun getFirstPhaseHeuristic(state: CubeState) = Math.max(
            transformer.getPruning(transformer.Slice_Flip_Prun, CoordTransformer.N_SLICE1 * state.flip + state.slice).toInt(),
            transformer.getPruning(transformer.Slice_Twist_Prun, CoordTransformer.N_SLICE1 * state.twist + state.slice).toInt())

    private fun firstPhaseSearch(depth: Int, bound: Int): Int {
        val state = stateStack[depth]
        val heuristic = getFirstPhaseHeuristic(state)
        val distance = depth + heuristic

        if (distance > bound) {
            return distance
        }
        if (heuristic == 0 && depth == bound) {
            val maxSecondPhaseDepth = Math.min(maxDepth - depth, 10)
            updateFirstPhase(depth)
            var secondPhaseBound = getSecondPhaseHeuristic(stateStack[depth])
            if (secondPhaseBound > maxSecondPhaseDepth) {
                return Int.MAX_VALUE
            }
            if (secondPhaseBound == 0) {
                firstPhaseDepth = depth
                secondPhaseDepth = depth
                return foundSolution
            }
            secondPhaseBound += depth
            while (true) {
                val res = secondPhaseSearch(depth, secondPhaseBound)
                when (res) {
                    foundSolution -> {
                        firstPhaseDepth = depth
                        return foundSolution
                    }
                    else -> {
                        if (res > maxSecondPhaseDepth + depth) {
                            return Int.MAX_VALUE
                        }
                        secondPhaseBound = res
                    }
                }
            }
        }

        var min = Int.MAX_VALUE
        val lastMove = moveStack.getOrNull(depth - 1)
        val nextState = stateStack[depth + 1]
        for (move in Move.values()) {
            if (lastMove == null || (move.axis != lastMove.axis && move.axis.ordinal != lastMove.axis.ordinal - 3)) {
                nextState.flip = transformer.flipMove[state.flip][move.ordinal].toInt()
                nextState.twist = transformer.twistMove[state.twist][move.ordinal].toInt()
                nextState.slice = transformer.FRtoBR_Move[state.slice * 24][move.ordinal] / 24
                moveStack[depth] = move
                val res = firstPhaseSearch(depth + 1, bound)
                when (res) {
                    foundSolution -> return foundSolution
                    else -> min = Math.min(min, res)
                }
            }
        }
        return min
    }

    private fun updateFirstPhase(depth: Int) {
        for (i in 0..depth - 1) {
            val move = moveStack[i]
            val state = stateStack[i]
            val nextState = stateStack[i + 1]
            nextState.urfToDlf = transformer.URFtoDLF_Move[state.urfToDlf][move.ordinal].toInt()
            nextState.frToBr = transformer.FRtoBR_Move[state.frToBr][move.ordinal].toInt()
            nextState.parity = transformer.parityMove[state.parity][move.ordinal].toInt()
            nextState.urToUl = transformer.URtoUL_Move[state.urToUl][move.ordinal].toInt()
            nextState.ubToDf = transformer.UBtoDF_Move[state.ubToDf][move.ordinal].toInt()
        }
        val state = stateStack[depth]
        state.urToDf = transformer.MergeURtoULandUBtoDF[state.urToUl][state.ubToDf].toInt()
    }

    private fun getSecondPhaseHeuristic(state: CubeState) = Math.max(
            transformer.getPruning(transformer.Slice_URtoDF_Parity_Prun, (CoordTransformer.N_SLICE2 * state.urToDf + state.frToBr) * 2 + state.parity).toInt(),
            transformer.getPruning(transformer.Slice_URFtoDLF_Parity_Prun, (CoordTransformer.N_SLICE2 * state.urfToDlf + state.frToBr) * 2 + state.parity).toInt())

    private fun secondPhaseSearch(depth: Int, bound: Int): Int {
        val state = stateStack[depth]
        val heuristic = getSecondPhaseHeuristic(state)
        val distance = depth + heuristic

        if (distance > bound) {
            return distance
        }
        if (heuristic == 0) {
            secondPhaseDepth = depth
            return foundSolution
        }

        var min = Int.MAX_VALUE
        val lastMove = moveStack.getOrNull(depth - 1)
        val nextState = stateStack[depth + 1]
        for (move in secondPhaseMoves) {
            if (lastMove == null || (move.axis != lastMove.axis && move.axis.ordinal != lastMove.axis.ordinal - 3)) {
                nextState.urfToDlf = transformer.URFtoDLF_Move[state.urfToDlf][move.ordinal].toInt()
                nextState.frToBr = transformer.FRtoBR_Move[state.frToBr][move.ordinal].toInt()
                nextState.parity = transformer.parityMove[state.parity][move.ordinal].toInt()
                nextState.urToDf = transformer.URtoDF_Move[state.urToDf][move.ordinal].toInt()
                moveStack[depth] = move
                val res = secondPhaseSearch(depth + 1, bound)
                when (res) {
                    foundSolution -> return foundSolution
                    else -> min = Math.min(min, res)
                }
            }
        }
        return min
    }
}
