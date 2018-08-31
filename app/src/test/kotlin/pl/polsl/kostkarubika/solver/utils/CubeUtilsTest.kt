package pl.polsl.kostkarubika.solver.utils

import org.junit.Test

import org.junit.Assert.*
import pl.polsl.kostkarubika.solver.enums.Move.*
import pl.polsl.kostkarubika.solver.enums.MoveAxis.*
import java.util.*

class CubeUtilsTest {

    @Test
    fun transformMovesTest() {
        val front = transformMoves(listOf(U1, D2, F1, L2, B3, R1), F)
        assertTrue(Arrays.equals(front.toTypedArray(), arrayOf(U1, D2, F1, L2, B3, R1)))
        val left = transformMoves(listOf(U1, D2, F1, L2, B3, R1), L)
        assertTrue(Arrays.equals(left.toTypedArray(), arrayOf(U1, D2, L1, B2, R3, F1)))
        val back = transformMoves(listOf(U1, D2, F1, L2, B3, R1), B)
        assertTrue(Arrays.equals(back.toTypedArray(), arrayOf(U1, D2, B1, R2, F3, L1)))
        val right = transformMoves(listOf(U1, D2, F1, L2, B3, R1), R)
        assertTrue(Arrays.equals(right.toTypedArray(), arrayOf(U1, D2, R1, F2, L3, B1)))
    }
}