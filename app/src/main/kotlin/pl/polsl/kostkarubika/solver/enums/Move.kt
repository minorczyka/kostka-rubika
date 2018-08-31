package pl.polsl.kostkarubika.solver.enums

import pl.polsl.kostkarubika.graphics.utils.Float3
import pl.polsl.kostkarubika.solver.enums.MoveAxis.*
import pl.polsl.kostkarubika.solver.enums.MovePower.*
import pl.polsl.kostkarubika.solver.utils.ANY
import pl.polsl.kostkarubika.solver.utils.equalFloat

enum class Move {

    U1, U2, U3,  R1, R2, R3, F1, F2, F3, D1, D2, D3, L1, L2, L3, B1, B2, B3;

    val power: MovePower
        get() {
            return when (this) {
                D1, U1, L1, R1, F1, B1 -> ONE
                D2, U2, L2, R2, F2, B2 -> TWO
                D3, U3, L3, R3, F3, B3 -> THREE
            }
        }

    val axis: MoveAxis
        get() {
            return when (this) {
                U1, U2, U3 -> U
                R1, R2, R3 -> R
                F1, F2, F3 -> F
                D1, D2, D3 -> D
                L1, L2, L3 -> L
                B1, B2, B3 -> B
            }
        }

    val cubePattern: Float3
        get() {
            when (this) {
                D1, D2, D3 -> return Float3(ANY, -0.5f, ANY)
                U1, U2, U3 -> return Float3(ANY, 0.5f, ANY)
                L1, L2, L3 -> return Float3(-0.5f, ANY, ANY)
                R1, R2, R3 -> return Float3(0.5f, ANY, ANY)
                F1, F2, F3 -> return Float3(ANY, ANY, 0.5f)
                B1, B2, B3 -> return Float3(ANY, ANY, -0.5f)
            }
        }

    val rotationVector: Float3
        get() {
            when (this) {
                D3, U2, U1 -> return Float3(0f, -1f, 0f)
                D2, D1, U3 -> return Float3(0f, 1f, 0f)
                L3, R2, R1 -> return Float3(-1f, 0f, 0f)
                L2, L1, R3 -> return Float3(1f, 0f, 0f)
                F3, B2, B1 -> return Float3(0f, 0f, 1f)
                F2, F1, B3 -> return Float3(0f, 0f, -1f)
            }
        }

    val rotationAngle: Float
        get() {
            when (this) {
                D3, D1, U3, U1, L3, L1, R3, R1, F3, F1, B3, B1 -> return 90.0f
                D2, U2, L2, R2, F2, B2 -> return 180.0f
                else -> return 0.0f
            }
        }

    val counterMove: Move
        get() {
            return when (this) {
                U1 -> U3
                U2 -> U2
                U3 -> U1
                R1 -> R3
                R2 -> R2
                R3 -> R1
                F1 -> F3
                F2 -> F2
                F3 -> F1
                D1 -> D3
                D2 -> D2
                D3 -> D1
                L1 -> L3
                L2 -> L2
                L3 -> L1
                B1 -> B3
                B2 -> B2
                B3 -> B1
            }
        }

    fun checkPositionWithPattern(position: Float3): Boolean {
        val pattern = cubePattern
        return (pattern.x == ANY || equalFloat(pattern.x, position.x)) &&
                (pattern.y == ANY || equalFloat(pattern.y, position.y)) &&
                (pattern.z == ANY || equalFloat(pattern.z, position.z))
    }

    companion object {
        fun moveFromAxisAndPower(axis: MoveAxis, power: MovePower): Move {
            val move = values().find { it.axis == axis && it.power == power }
            return move!!
        }
    }
}
