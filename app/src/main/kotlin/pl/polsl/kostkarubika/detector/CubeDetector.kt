package pl.polsl.kostkarubika.detector

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.utils.Either
import pl.polsl.kostkarubika.solver.utils.Left
import pl.polsl.kostkarubika.solver.utils.Right

class CubeDetector {

    val detectedFaces = Array(6) { mutableMapOf<Int, Int>() }
    val maxDetected = IntArray(6) { 0 }
    var requiredDetections = 5

    val stringFaces: List<String>
        get() {
            return detectedFaces.map { it.maxBy { it.value } }
                    .map { it?.key }
                    .map { if (it != null) decode(it) else null }
                    .map { colors ->
                        val builder = StringBuilder()
                        colors?.forEach { builder.append(it) }
                        builder.toString()
                    }
        }

    fun reset() {
        detectedFaces.forEach { it.clear() }
        maxDetected.fill(0)
    }

    fun found(list: List<Color>) {
        val face = list[4].ordinal
        val hash = encode(list)
        val alreadyDetected = detectedFaces[face][hash]
        val detected = (alreadyDetected ?: 0) + 1
        detectedFaces[face][hash] = detected
        if (detected > maxDetected[face]) {
            maxDetected[face] = detected
        }
    }

    fun enoughDetections(color: Color): Boolean {
        return maxDetected[color.ordinal] >= requiredDetections
    }

    fun enoughDetections(): Boolean {
        for (color in Color.values()) {
            if (!enoughDetections(color)) {
                return false
            }
        }
        return true
    }

    fun encode(colors: List<Color>): Int {
        var hash = 0
        for (color in colors) {
            hash *= Color.values().size
            hash += color.ordinal
        }
        return hash
    }

    fun decode(value: Int): List<Color> {
        var hash = value
        val result = mutableListOf<Color>()
        for (i in 0..8) {
            val v = hash % Color.values().size
            hash /= Color.values().size
            result += Color.values()[v]
        }
        return result.reversed()
    }

    fun toFaceForm(): Either<DetectionError, FaceForm> {
        if (!enoughDetections()) {
            return Left(DetectionError.NOT_ENOUGH)
        }
        return Companion.toFaceForm(stringFaces)
    }

    companion object {
        fun toFaceForm(faces: List<String>, setA: Int? = null, setB: Int? = null, setC: Int? = null, setD: Int? = null, setE: Int? = null, setF: Int? = null): Either<DetectionError, FaceForm> {
            val startA = setA ?: 0
            val stopA = setA ?: 3
            val startB = setB ?: 0
            val stopB = setB ?: 3
            val startC = setC ?: 0
            val stopC = setC ?: 3
            val startD = setD ?: 0
            val stopD = setD ?: 3
            val startE = setE ?: 0
            val stopE = setE ?: 3
            val startF = setF ?: 0
            val stopF = setF ?: 3

            val faceForms = mutableListOf<FaceForm>()
            for (a in startA..stopA) {
                for (b in startB..stopB) {
                    for (c in startC..stopC) {
                        for (d in startD..stopD) {
                            for (e in startE..stopE) {
                                for (f in startF..stopF) {
                                    val list = listOf(rotateFace(faces[0], a), rotateFace(faces[1], b), rotateFace(faces[2], c), rotateFace(faces[3], d), rotateFace(faces[4], e), rotateFace(faces[5], f))
                                    val builder = StringBuilder()
                                    list.forEach { builder.append(it) }
                                    val faceForm = FaceForm(builder.toString())
                                    val cubeForm = faceForm.toCubeForm()
                                    if (cubeForm.isValid) {
                                        faceForms.add(faceForm)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            val result = faceForms.distinctBy { it.toString() }
            when (result.size) {
                0 -> return Left(DetectionError.INVALID)
                1 -> return Right(result.first())
                else -> return Left(DetectionError.FOUND_MORE)
            }
        }

        fun rotateFace(face: CharSequence, turns: Int): CharSequence {
            val realTurns = turns % 4
            if (realTurns == 0) {
                return face
            }
            val transform = when (realTurns) {
                1 -> listOf(2, 5, 8, 1, 4, 7, 0, 3, 6)
                2 -> listOf(8, 7, 6, 5, 4, 3, 2, 1, 0)
                3 -> listOf(6, 3, 0, 7, 4, 1, 8, 5, 2)
                else -> listOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
            }
            val result = transform.map { face[it] }.toCharArray()
            return String(result)
        }
    }
}