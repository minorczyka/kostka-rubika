package pl.polsl.kostkarubika.solver.utils

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.algorithms.kociemba.CoordCubeForm
import pl.polsl.kostkarubika.solver.algorithms.kociemba.CoordTransformer
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.MoveAxis
import java.util.*

fun randomCube(): CubeForm {
    val cubeForm = CoordCubeForm()
    val gen = Random()
    cubeForm.flip = gen.nextInt(CoordTransformer.N_FLIP).toShort()
    cubeForm.twist = gen.nextInt(CoordTransformer.N_TWIST).toShort()
    do {
        cubeForm.urFtoDLB = gen.nextInt(CoordTransformer.N_URFtoDLB)
        cubeForm.uRtoBR = gen.nextInt(CoordTransformer.N_URtoBR)
    } while (cubeForm.edgeParity.toInt() xor cubeForm.cornerParity.toInt() != 0)
    return cubeForm
}

fun <T> rotateLeft(arr: Array<T>, l: Int, r: Int) {
    val temp = arr[l]
    for (i in l..r - 1) {
        arr[i] = arr[i + 1]
    }
    arr[r] = temp
}

fun <T> rotateRight(arr: Array<T>, l: Int, r: Int) {
    val temp = arr[r]
    for (i in r downTo l + 1) {
        arr[i] = arr[i - 1]
    }
    arr[l] = temp
}

fun transformMoves(moves: List<Move>, transformedFront: MoveAxis): List<Move> {
    val around = listOf(MoveAxis.F, MoveAxis.L, MoveAxis.B, MoveAxis.R)
    val frontIndex = around.indexOf(transformedFront)
    if (frontIndex == -1) {
        return moves
    }
    return moves.map { move ->
        if (move.axis == MoveAxis.U || move.axis == MoveAxis.D) {
            move
        } else {
            val axisIndex = around.indexOf(move.axis)
            val transformedAxisIndex = (axisIndex + frontIndex) % 4
            Move.moveFromAxisAndPower(around[transformedAxisIndex], move.power)
        }
    }
}