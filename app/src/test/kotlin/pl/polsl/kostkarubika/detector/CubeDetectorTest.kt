package pl.polsl.kostkarubika.detector

import org.junit.Test

import org.junit.Assert.*
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Color.*

class CubeDetectorTest {

    @Test
    fun encode() {
        val list = listOf(R, B, L, B, U, D, B, U, L)
        val cubeDetector = CubeDetector()
        val hash = cubeDetector.encode(list)
        assertEquals(3305632, hash)
    }

    @Test
    fun decode() {
        val hash = 3305632
        val cubeDetector = CubeDetector()
        val decoded = cubeDetector.decode(hash)
        val list = listOf(R, B, L, B, U, D, B, U, L)
        for (i in 0..list.size - 1) {
            assertEquals("i = $i, decoded = $decoded", list[i], decoded[i])
        }
    }
}