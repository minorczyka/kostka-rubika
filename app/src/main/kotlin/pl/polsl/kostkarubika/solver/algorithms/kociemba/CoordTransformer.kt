package pl.polsl.kostkarubika.solver.algorithms.kociemba

import java.io.IOException

import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.utils.TransformTables

class CoordTransformer(val coordDataLoader: CoordDataLoader? = null) {

    val parityMove = arrayOf(shortArrayOf(1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1, 1, 0, 1), shortArrayOf(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0))

    val twistMove = Array(N_TWIST) { ShortArray(N_MOVE) }
    val flipMove = Array(N_FLIP) { ShortArray(N_MOVE) }
    val FRtoBR_Move = Array(N_FRtoBR) { ShortArray(N_MOVE) }
    val URFtoDLF_Move = Array(N_URFtoDLF) { ShortArray(N_MOVE) }
    val URtoDF_Move = Array(N_URtoDF) { ShortArray(N_MOVE) }
    val URtoUL_Move = Array(N_URtoUL) { ShortArray(N_MOVE) }
    val UBtoDF_Move = Array(N_UBtoDF) { ShortArray(N_MOVE) }
    val MergeURtoULandUBtoDF = Array(336) { ShortArray(336) }
    val Slice_URFtoDLF_Parity_Prun = ByteArray(N_SLICE2 * N_URFtoDLF * N_PARITY / 2)
    val Slice_URtoDF_Parity_Prun = ByteArray(N_SLICE2 * N_URtoDF * N_PARITY / 2)
    val Slice_Twist_Prun = ByteArray(N_SLICE1 * N_TWIST / 2 + 1)
    val Slice_Flip_Prun = ByteArray(N_SLICE1 * N_FLIP / 2)

    private var ready = false

    fun prepare() {
        if (ready) {
            return
        }
        if (coordDataLoader != null) {
            try {
                coordDataLoader.read(this)
                ready = true
                return
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        preProcess()
        ready = true
        if (coordDataLoader != null) {
            try {
                coordDataLoader.write(this)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun preProcess() {
        computeTwistMove()
        computeFlipMove()
        computeFRtoBR_Move()
        computeURFtoDLF_Move()
        computeURtoDF_Move()
        computeURtoUL_Move()
        computeUBtoDF_Move()
        computeMergeURtoULandUBtoDF()
        computeSlice_URFtoDLF_Parity_Prun()
        computeSlice_URtoDF_Parity_Prun()
        computeSlice_Twist_Prun()
        computeSlice_Flip_Prun()
    }

    private fun computeTwistMove() {
        val a = CoordCubeForm()
        for (i in 0..N_TWIST - 1) {
            a.twist = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.cornerMultiply(TransformTables.moveCube[j])
                    twistMove[i][3 * j + k] = a.twist
                }
                a.cornerMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeFlipMove() {
        val a = CoordCubeForm()
        for (i in 0..N_FLIP - 1) {
            a.flip = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.edgeMultiply(TransformTables.moveCube[j])
                    flipMove[i][3 * j + k] = a.flip
                }
                a.edgeMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeFRtoBR_Move() {
        val a = CoordCubeForm()
        for (i in 0..N_FRtoBR - 1) {
            a.fRtoBR = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.edgeMultiply(TransformTables.moveCube[j])
                    FRtoBR_Move[i][3 * j + k] = a.fRtoBR
                }
                a.edgeMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeURFtoDLF_Move() {
        val a = CoordCubeForm()
        for (i in 0..N_URFtoDLF - 1) {
            a.urFtoDLF = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.cornerMultiply(TransformTables.moveCube[j])
                    URFtoDLF_Move[i][3 * j + k] = a.urFtoDLF
                }
                a.cornerMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeURtoDF_Move() {
        val a = CoordCubeForm()
        for (i in 0..N_URtoDF - 1) {
            a.uRtoDF = i.toInt()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.edgeMultiply(TransformTables.moveCube[j])
                    URtoDF_Move[i][3 * j + k] = a.uRtoDF.toShort()
                }
                a.edgeMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeURtoUL_Move() {
        val a = CoordCubeForm()
        for (i in 0..N_URtoUL - 1) {
            a.uRtoUL = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.edgeMultiply(TransformTables.moveCube[j])
                    URtoUL_Move[i][3 * j + k] = a.uRtoUL
                }
                a.edgeMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeUBtoDF_Move() {
        val a = CoordCubeForm()
        for (i in 0..N_UBtoDF - 1) {
            a.uBtoDF = i.toShort()
            for (j in 0..5) {
                for (k in 0..2) {
                    a.edgeMultiply(TransformTables.moveCube[j])
                    UBtoDF_Move[i][3 * j + k] = a.uBtoDF
                }
                a.edgeMultiply(TransformTables.moveCube[j])
            }
        }
    }

    private fun computeMergeURtoULandUBtoDF() {
        for (uRtoUL in 0..335) {
            for (uBtoDF in 0..335) {
                MergeURtoULandUBtoDF[uRtoUL][uBtoDF] = getURtoDF(uRtoUL.toShort(), uBtoDF.toShort()).toShort()
            }
        }
    }

    private fun computeSlice_URFtoDLF_Parity_Prun() {
        for (i in 0..N_SLICE2 * N_URFtoDLF * N_PARITY / 2 - 1)
            Slice_URFtoDLF_Parity_Prun[i] = -1
        var depth = 0
        setPruning(Slice_URFtoDLF_Parity_Prun, 0, 0.toByte())
        var done = 1
        while (done != N_SLICE2 * N_URFtoDLF * N_PARITY) {
            for (i in 0..N_SLICE2 * N_URFtoDLF * N_PARITY - 1) {
                val parity = i % 2
                val URFtoDLF = i / 2 / N_SLICE2
                val slice = i / 2 % N_SLICE2
                if (getPruning(Slice_URFtoDLF_Parity_Prun, i).toInt() == depth) {
                    loop@ for (j in 0..17) {
                        when (j) {
                            3, 5, 6, 8, 12, 14, 15, 17 -> continue@loop
                            else -> {
                                val newSlice = FRtoBR_Move[slice][j].toInt()
                                val newURFtoDLF = URFtoDLF_Move[URFtoDLF][j].toInt()
                                val newParity = parityMove[parity][j].toInt()
                                if (getPruning(Slice_URFtoDLF_Parity_Prun, (N_SLICE2 * newURFtoDLF + newSlice) * 2 + newParity).toInt() == 0x0f) {
                                    setPruning(Slice_URFtoDLF_Parity_Prun, (N_SLICE2 * newURFtoDLF + newSlice) * 2 + newParity,
                                            (depth + 1).toByte())
                                    done++
                                }
                            }
                        }
                    }
                }
            }
            depth++
        }
    }

    private fun computeSlice_URtoDF_Parity_Prun() {
        for (i in 0..N_SLICE2 * N_URtoDF * N_PARITY / 2 - 1)
            Slice_URtoDF_Parity_Prun[i] = -1
        var depth = 0
        setPruning(Slice_URtoDF_Parity_Prun, 0, 0.toByte())
        var done = 1
        while (done != N_SLICE2 * N_URtoDF * N_PARITY) {
            for (i in 0..N_SLICE2 * N_URtoDF * N_PARITY - 1) {
                val parity = i % 2
                val URtoDF = i / 2 / N_SLICE2
                val slice = i / 2 % N_SLICE2
                if (getPruning(Slice_URtoDF_Parity_Prun, i).toInt() == depth) {
                    loop@ for (j in 0..17) {
                        when (j) {
                            3, 5, 6, 8, 12, 14, 15, 17 -> continue@loop
                            else -> {
                                val newSlice = FRtoBR_Move[slice][j].toInt()
                                val newURtoDF = URtoDF_Move[URtoDF][j].toInt()
                                val newParity = parityMove[parity][j].toInt()
                                if (getPruning(Slice_URtoDF_Parity_Prun, (N_SLICE2 * newURtoDF + newSlice) * 2 + newParity).toInt() == 0x0f) {
                                    setPruning(Slice_URtoDF_Parity_Prun, (N_SLICE2 * newURtoDF + newSlice) * 2 + newParity,
                                            (depth + 1).toByte())
                                    done++
                                }
                            }
                        }
                    }
                }
            }
            depth++
        }
    }

    private fun computeSlice_Twist_Prun() {
        for (i in 0..N_SLICE1 * N_TWIST / 2 + 1 - 1)
            Slice_Twist_Prun[i] = -1
        var depth = 0
        setPruning(Slice_Twist_Prun, 0, 0.toByte())
        var done = 1
        while (done != N_SLICE1 * N_TWIST) {
            for (i in 0..N_SLICE1 * N_TWIST - 1) {
                val twist = i / N_SLICE1
                val slice = i % N_SLICE1
                if (getPruning(Slice_Twist_Prun, i).toInt() == depth) {
                    for (j in 0..17) {
                        val newSlice = FRtoBR_Move[slice * 24][j] / 24
                        val newTwist = twistMove[twist][j].toInt()
                        if (getPruning(Slice_Twist_Prun, N_SLICE1 * newTwist + newSlice).toInt() == 0x0f) {
                            setPruning(Slice_Twist_Prun, N_SLICE1 * newTwist + newSlice, (depth + 1).toByte())
                            done++
                        }
                    }
                }
            }
            depth++
        }
    }

    private fun computeSlice_Flip_Prun() {
        for (i in 0..N_SLICE1 * N_FLIP / 2 - 1)
            Slice_Flip_Prun[i] = -1
        var depth = 0
        setPruning(Slice_Flip_Prun, 0, 0.toByte())
        var done = 1
        while (done != N_SLICE1 * N_FLIP) {
            for (i in 0..N_SLICE1 * N_FLIP - 1) {
                val flip = i / N_SLICE1
                val slice = i % N_SLICE1
                if (getPruning(Slice_Flip_Prun, i).toInt() == depth) {
                    for (j in 0..17) {
                        val newSlice = FRtoBR_Move[slice * 24][j] / 24
                        val newFlip = flipMove[flip][j].toInt()
                        if (getPruning(Slice_Flip_Prun, N_SLICE1 * newFlip + newSlice).toInt() == 0x0f) {
                            setPruning(Slice_Flip_Prun, N_SLICE1 * newFlip + newSlice, (depth + 1).toByte())
                            done++
                        }
                    }
                }
            }
            depth++
        }
    }

    private fun getURtoDF(idx1: Short, idx2: Short): Int {
        val a = CoordCubeForm()
        val b = CoordCubeForm()
        a.uRtoUL = idx1
        b.uBtoDF = idx2
        for (i in 0..7) {
            if (a.edgePermutation[i] !== Edge.BR)
                if (b.edgePermutation[i] !== Edge.BR)
                // collision
                    return -1
                else
                    b.edgePermutation[i] = a.edgePermutation[i]
        }
        return b.uRtoDF
    }

    internal fun setPruning(table: ByteArray, index: Int, value: Byte) {
        if (index and 1 == 0)
            table[index / 2] = (table[index / 2].toInt() and (0xf0 or value.toInt())).toByte()
        else
            table[index / 2] = (table[index / 2].toInt() and (0x0f or (value.toInt() shl 4))).toByte()
    }

    internal fun getPruning(table: ByteArray, index: Int): Byte {
        if (index and 1 == 0)
            return (table[index / 2].toInt() and 0x0f).toByte()
        else
            return (table[index / 2].toInt() and 0xf0).ushr(4).toByte()
    }

    companion object {

        val N_TWIST = 2187
        val N_FLIP = 2048
        val N_SLICE1 = 495
        val N_SLICE2 = 24
        private val N_PARITY = 2
        private val N_URFtoDLF = 20160
        private val N_FRtoBR = 11880
        private val N_URtoUL = 1320
        private val N_UBtoDF = 1320
        private val N_URtoDF = 20160

        private val N_MOVE = 18

        val N_URFtoDLB = 40320
        val N_URtoBR = 479001600
    }
}
