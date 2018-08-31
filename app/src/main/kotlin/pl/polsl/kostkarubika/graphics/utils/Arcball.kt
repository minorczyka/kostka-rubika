package pl.polsl.kostkarubika.graphics.utils

import android.opengl.Matrix

class Arcball {

    private var width: Int = 0
    private var height: Int = 0
    private var prevPosition: Float3 = Float3()

    fun onSurfaceChanged(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setPrevPosition(prevPosition: Float3) {
        this.prevPosition = arcballVector(prevPosition)
    }

    fun getRotation(position: Float3): FloatArray? {
        var varPosition = position
        varPosition = arcballVector(varPosition)
        val angle = Float3.angle(prevPosition, varPosition)
        val axis = Float3.normalize(Float3.cross(prevPosition, varPosition))
        if (java.lang.Float.isNaN(angle) || axis.isNan) {
            return null
        }
        val matrix = FloatArray(16)
        Matrix.setRotateM(matrix, 0, angle, axis.x, axis.y, axis.z)
        prevPosition = varPosition
        return matrix
    }

    private fun arcballVector(v: Float3): Float3 {
        var result = Float3(v.x / width * 2 - 1, -(v.y / height * 2 - 1), 0f)
        val square = result.x * result.x + result.y * result.y
        if (square <= 1) {
            result.z = Math.sqrt((1 - square).toDouble()).toFloat()
        } else {
            result = Float3.normalize(result)
        }
        return result
    }
}
