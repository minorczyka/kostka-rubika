package pl.polsl.kostkarubika.solver

import org.junit.Assert.*
import org.junit.Test
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.utils.randomCube

class CubeFormTest {

    @Test
    fun plainEqualityTest() {
        val first = CubeForm()
        val second = CubeForm()
        assertTrue(first == second)
    }

    @Test
    fun parameterEqualityTest() {
        val faceForm = randomCube().toFaceForm()
        val first = faceForm.toCubeForm()
        val second = faceForm.toCubeForm()
        assertTrue(first == second)
    }

    @Test
    fun negativeEqualityTest() {
        val first = randomCube()
        val second = randomCube()
        assertFalse(first == second)
    }

    @Test
    fun faceFormToCubeFormAndBack() {
        val str = "FFFUUUBBBRRRRRRRRRUDUFFFDUDBBBDDDFFFLLLLLLLLLUDUBBBDUD"
        val faceForm = FaceForm(str)
        val cubeForm = faceForm.toCubeForm()
        assertTrue(cubeForm.isValid)
        val back = cubeForm.toFaceForm()
        assertEquals(str, back.toString())
    }
}