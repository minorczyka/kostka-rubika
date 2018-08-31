package pl.polsl.kostkarubika.solver

import org.junit.Test
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.algorithms.lbl.LayerByLayerSolver
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.solver.utils.randomCube
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LayerByLayerTest {

    val faceForm = FaceForm("UBULURUFURURFRBRDRFUFLFRFDFDFDLDRDBDLULBLFLDLBUBRBLBDB")

    fun solvedCubeForm(): CubeForm {
        val solver = LayerByLayerSolver()
        val solution = solver.getSolution(faceForm)
        assertNotNull(solution)

        val cubeForm = faceForm.toCubeForm()
        solution?.moveList?.forEach { cubeForm.makeMove(it) }
        return cubeForm
    }

    @Test
    fun crossTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(Edge.DR, cubeForm.edgePermutation[4])
        assertEquals(Edge.DF, cubeForm.edgePermutation[5])
        assertEquals(Edge.DL, cubeForm.edgePermutation[6])
        assertEquals(Edge.DB, cubeForm.edgePermutation[7])
        assertEquals(0, cubeForm.edgeOrientation[4])
        assertEquals(0, cubeForm.edgeOrientation[5])
        assertEquals(0, cubeForm.edgeOrientation[6])
        assertEquals(0, cubeForm.edgeOrientation[7])
    }

    @Test
    fun firstLayerTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(Corner.DFR, cubeForm.cornerPermutation[4])
        assertEquals(Corner.DLF, cubeForm.cornerPermutation[5])
        assertEquals(Corner.DBL, cubeForm.cornerPermutation[6])
        assertEquals(Corner.DRB, cubeForm.cornerPermutation[7])
        assertEquals(0, cubeForm.cornerOrientation[4])
        assertEquals(0, cubeForm.cornerOrientation[5])
        assertEquals(0, cubeForm.cornerOrientation[6])
        assertEquals(0, cubeForm.cornerOrientation[7])
    }

    @Test
    fun secondLayerTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(Edge.FR, cubeForm.edgePermutation[8])
        assertEquals(Edge.FL, cubeForm.edgePermutation[9])
        assertEquals(Edge.BL, cubeForm.edgePermutation[10])
        assertEquals(Edge.BR, cubeForm.edgePermutation[11])
        assertEquals(0, cubeForm.edgeOrientation[8])
        assertEquals(0, cubeForm.edgeOrientation[9])
        assertEquals(0, cubeForm.edgeOrientation[10])
        assertEquals(0, cubeForm.edgeOrientation[11])
    }

    @Test
    fun topCrossTest() {
        val cubeForm = solvedCubeForm()
        val faceForm = cubeForm.toFaceForm()
        assertEquals(Color.U, faceForm.fields[1])
        assertEquals(Color.U, faceForm.fields[3])
        assertEquals(Color.U, faceForm.fields[4])
        assertEquals(Color.U, faceForm.fields[5])
        assertEquals(Color.U, faceForm.fields[7])
    }

    @Test
    fun topCrossPermutationTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(Edge.UR, cubeForm.edgePermutation[0])
        assertEquals(Edge.UF, cubeForm.edgePermutation[1])
        assertEquals(Edge.UL, cubeForm.edgePermutation[2])
        assertEquals(Edge.UB, cubeForm.edgePermutation[3])
        assertEquals(0, cubeForm.edgeOrientation[0])
        assertEquals(0, cubeForm.edgeOrientation[1])
        assertEquals(0, cubeForm.edgeOrientation[2])
        assertEquals(0, cubeForm.edgeOrientation[3])
    }

    @Test
    fun cornersPositionTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(Corner.URF, cubeForm.cornerPermutation[0])
        assertEquals(Corner.UFL, cubeForm.cornerPermutation[1])
        assertEquals(Corner.ULB, cubeForm.cornerPermutation[2])
        assertEquals(Corner.UBR, cubeForm.cornerPermutation[3])
    }

    @Test
    fun cornersOrientationTest() {
        val cubeForm = solvedCubeForm()
        assertEquals(0, cubeForm.cornerOrientation[0])
        assertEquals(0, cubeForm.cornerOrientation[1])
        assertEquals(0, cubeForm.cornerOrientation[2])
        assertEquals(0, cubeForm.cornerOrientation[3])
    }

    @Test
    fun bigTest() {
        val solvedCube = CubeForm()
        val solver = LayerByLayerSolver()
        val count = 1000
        for (i in 1..count) {
            val cubeForm = randomCube()
            val solution = solver.getSolution(cubeForm.toFaceForm())
            assertNotNull(solution)
            solution?.moveList?.forEach { cubeForm.makeMove(it) }
            assertEquals(cubeForm, solvedCube, cubeForm.toFaceForm().toString())
        }
    }

    @Test
    fun cornerOrientationTest() {
        val cubeForm = CubeForm()
        val corner = Corner.DFR
        val moves = listOf(Move.F3)
        moves.forEach { cubeForm.makeMove(it) }
        val index = cubeForm.cornerPermutation.indexOf(corner)
        val orientation = cubeForm.cornerOrientation[index]
        println("orientation = $orientation")
    }

    @Test
    fun edgeOrientationTest() {
        val cubeForm = CubeForm()
        val edge = Edge.FL
        val moves = listOf(Move.F1)
        moves.forEach { cubeForm.makeMove(it) }
        val index = cubeForm.edgePermutation.indexOf(edge)
        val orientation = cubeForm.edgeOrientation[index]
        println("orientation = $orientation")
    }
}