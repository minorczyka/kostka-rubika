package pl.polsl.kostkarubika.solver

import org.junit.Test
import pl.polsl.kostkarubika.solver.algorithms.kociemba.CoordTransformer
import pl.polsl.kostkarubika.solver.algorithms.kociemba.KociembaAlgorithmSolver
import pl.polsl.kostkarubika.solver.algorithms.lbl.LayerByLayerSolver
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SolutionTest {

    val transformer = CoordTransformer()
    val faceForm = FaceForm("BBFUURULDFURDRBLRDBDRDFFFRFDDUBDFDULUFRFLBBLLURLUBLBLR")

    init {
        transformer.prepare()
    }

    @Test
    fun kociembaSolverTest() {
        val solver = KociembaAlgorithmSolver(transformer)

        val solution = solver.getSolution(faceForm)
        assertNotNull(solution)

        val cubeForm = faceForm.toCubeForm()
        solution?.moveList?.forEach { cubeForm.makeMove(it) }
        println(solution.toString())
        assertEquals(cubeForm, CubeForm())
    }

    @Test
    fun layerByLayerSolverTest() {
        val solver = LayerByLayerSolver()

        val solution = solver.getSolution(faceForm)
        assertNotNull(solution)

        val cubeForm = faceForm.toCubeForm()
        solution?.moveList?.forEach { cubeForm.makeMove(it) }
        println(solution.toString())
        assertEquals(cubeForm, CubeForm())
    }
}
