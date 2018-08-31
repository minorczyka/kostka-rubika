package pl.polsl.kostkarubika.solver

import org.junit.Test
import pl.polsl.kostkarubika.solver.utils.randomCube
import kotlin.test.assertEquals

class FaceFormTest {

    @Test
    fun creatingFaceFormSecondTest() {
        check("FUBFUBFUBRRRRRRRRRDFUDFUDFUFDBFDBFDBLLLLLLLLLUBDUBDUBD")
    }

    fun check(input: String) {
        val faces = (0..5).map { input.subSequence(it * 9, (it + 1) * 9) }
        faces.forEach { assertEquals(it.length, 9) }
        var count = 0
        val correct = mutableListOf<String>()
        for (a in 0..3) {
            for (b in 0..3) {
                for (c in 0..3) {
                    for (d in 0..3) {
                        for (e in 0..3) {
                            for (f in 0..3) {
                                val list = listOf(rotateFace(faces[0], a), rotateFace(faces[1], b), rotateFace(faces[2], c), rotateFace(faces[3], d), rotateFace(faces[4], e), rotateFace(faces[5], f))
                                val builder = StringBuilder()
                                list.forEach { builder.append(it) }
                                val faceForm = FaceForm(builder.toString())
                                val cubeForm = faceForm.toCubeForm()
                                if (cubeForm.isValid) {
                                    ++count
                                    correct += builder.toString()
                                }
                            }
                        }
                    }
                }
            }
        }
        println("count = $count, distinct = ${correct.distinct().size}")
    }

    fun rotateFace(face: CharSequence): CharSequence {
        val transform = listOf(2, 5, 8, 1, 4, 7, 0, 3, 6)
        val result = transform.map { face[it] }.toCharArray()
        return String(result)
    }

    fun rotateFace(face: CharSequence, turns: Int): CharSequence {
        var result = face
        for (i in 1..turns) {
            result = rotateFace(result)
        }
        return result
    }
}