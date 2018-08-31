package pl.polsl.kostkarubika.solver

import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Edge

import pl.polsl.kostkarubika.solver.enums.Corner.*
import pl.polsl.kostkarubika.solver.enums.Edge.*
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.enums.Move.*
import pl.polsl.kostkarubika.solver.utils.TransformTables


import pl.polsl.kostkarubika.solver.utils.TransformTables.cornerFacelet
import pl.polsl.kostkarubika.solver.utils.TransformTables.cornerColor
import pl.polsl.kostkarubika.solver.utils.TransformTables.edgeFacelet
import pl.polsl.kostkarubika.solver.utils.TransformTables.edgeColor
import java.util.*

open class CubeForm() {

    val cornerPermutation: Array<Corner>
    val cornerOrientation: ByteArray

    val edgePermutation: Array<Edge>
    val edgeOrientation: ByteArray

    init {
        cornerPermutation = arrayOf(URF, UFL, ULB, UBR, DFR, DLF, DBL, DRB)
        cornerOrientation = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        edgePermutation = arrayOf(UR, UF, UL, UB, DR, DF, DL, DB, FR, FL, BL, BR)
        edgeOrientation = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    constructor(cp: Array<Corner>, co: ByteArray, ep: Array<Edge>, eo: ByteArray) : this() {
        for (i in cp.indices) {
            cornerPermutation[i] = cp[i]
            cornerOrientation[i] = co[i]
        }
        for (i in ep.indices) {
            edgePermutation[i] = ep[i]
            edgeOrientation[i] = eo[i]
        }
    }

    constructor(other: CubeForm) : this(other.cornerPermutation, other.cornerOrientation, other.edgePermutation, other.edgeOrientation)

    override fun equals(other: Any?): Boolean {
        val otherCubeForm = other as? CubeForm
        if (otherCubeForm != null) {
            return Arrays.equals(cornerPermutation, otherCubeForm.cornerPermutation) &&
                    Arrays.equals(cornerOrientation, otherCubeForm.cornerOrientation) &&
                    Arrays.equals(edgePermutation, otherCubeForm.edgePermutation) &&
                    Arrays.equals(edgeOrientation, otherCubeForm.edgeOrientation)
        }
        return false
    }

    fun toFaceForm(): FaceForm {
        val faceForm = FaceForm()
        for (corner in Corner.values()) {
            val i = corner.ordinal
            val j = cornerPermutation[i].ordinal
            val orientation = cornerOrientation[i]
            for (k in 0..2) {
                faceForm.fields[cornerFacelet[i][(k + orientation) % 3].ordinal] = cornerColor[j][k]
            }
        }
        for (edge in Edge.values()) {
            val i = edge.ordinal
            val j = edgePermutation[i].ordinal
            val orientation = edgeOrientation[i]
            for (k in 0..1) {
                faceForm.fields[edgeFacelet[i][(k + orientation) % 2].ordinal] = edgeColor[j][k]
            }
        }
        return faceForm
    }

    val isValid: Boolean
        get() {
            val edgeCount = IntArray(12)
            for (edge in Edge.values()) {
                ++edgeCount[edgePermutation[edge.ordinal].ordinal]
            }
            for (v in edgeCount) {
                if (v != 1) return false
            }
            if (edgeOrientation.sum() % 2 != 0) return false

            val cornerCount = IntArray(8)
            for (corner in Corner.values()) {
                ++cornerCount[cornerPermutation[corner.ordinal].ordinal]
            }
            for (v in cornerCount) {
                if (v != 1) return false
            }
            if (cornerOrientation.sum() % 3 != 0) return false

            if (edgeParity.toInt() xor cornerParity.toInt() != 0) return false

            return true
        }

    val edgeParity: Short
        get() {
            var s = 0
            for (i in BR.ordinal downTo UR.ordinal + 1) {
                for (j in i - 1 downTo UR.ordinal) {
                    if (edgePermutation[j].ordinal > edgePermutation[i].ordinal) {
                        ++s
                    }
                }
            }
            return (s % 2).toShort()
        }

    val cornerParity: Short
        get() {
            var s = 0
            for (i in DRB.ordinal downTo URF.ordinal + 1) {
                for (j in i - 1 downTo URF.ordinal) {
                    if (cornerPermutation[j].ordinal > cornerPermutation[i].ordinal) {
                        ++s
                    }
                }
            }
            return (s % 2).toShort()
        }

    fun cornerMultiply(other: CubeForm) {
        val orientation = ByteArray(8)
        val permutation = arrayOfNulls<Corner>(8)
        for (corner in Corner.values()) {
            permutation[corner.ordinal] = cornerPermutation[other.cornerPermutation[corner.ordinal].ordinal]
            val oriA = cornerOrientation[other.cornerPermutation[corner.ordinal].ordinal]
            val oriB = other.cornerOrientation[corner.ordinal]
            var ori = (oriA + oriB).toByte()
            if (ori >= 3) {
                ori = (ori - 3).toByte()
            }
            orientation[corner.ordinal] = ori
        }
        for (corner in Corner.values()) {
            cornerPermutation[corner.ordinal] = permutation[corner.ordinal]!!
            cornerOrientation[corner.ordinal] = orientation[corner.ordinal]
        }
    }

    fun edgeMultiply(other: CubeForm) {
        val permutation = arrayOfNulls<Edge>(12)
        val orientation = ByteArray(12)
        for (edge in Edge.values()) {
            permutation[edge.ordinal] = edgePermutation[other.edgePermutation[edge.ordinal].ordinal]
            orientation[edge.ordinal] = ((other.edgeOrientation[edge.ordinal] + edgeOrientation[other.edgePermutation[edge.ordinal].ordinal]) % 2).toByte()
        }
        for (edge in Edge.values()) {
            edgePermutation[edge.ordinal] = permutation[edge.ordinal]!!
            edgeOrientation[edge.ordinal] = orientation[edge.ordinal]
        }
    }

    fun makeMove(move: Move) {
        for (i in 1..move.power.ordinal + 1) {
            val moveCube = when (move) {
                U1, U2, U3 -> TransformTables.moveCube[0]
                R1, R2, R3 -> TransformTables.moveCube[1]
                F1, F2, F3 -> TransformTables.moveCube[2]
                D1, D2, D3 -> TransformTables.moveCube[3]
                L1, L2, L3 -> TransformTables.moveCube[4]
                B1, B2, B3 -> TransformTables.moveCube[5]
            }
            cornerMultiply(moveCube)
            edgeMultiply(moveCube)
        }
    }
}
