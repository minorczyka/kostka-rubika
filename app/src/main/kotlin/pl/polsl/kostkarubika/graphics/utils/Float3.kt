package pl.polsl.kostkarubika.graphics.utils

import android.opengl.Matrix
import pl.polsl.kostkarubika.detector.colors.RgbColor

data class Float3(var x: Float = 0.0f, var y: Float = 0.0f, var z: Float = 0.0f) {

    constructor(rgbColor: RgbColor) : this((rgbColor.r / 255.0).toFloat(), (rgbColor.g / 255.0).toFloat(), (rgbColor.b / 255.0).toFloat())

    override fun toString(): String {
        return "x = $x, y = $y, z = $z"
    }

    var array: FloatArray
        get() {
            val array = floatArrayOf(x, y, z)
            return array
        }
        set(array) {
            x = array[0]
            y = array[1]
            z = array[2]
        }

    val length: Float
        get() = Matrix.length(x, y, z)

    val isNan: Boolean
        get() = java.lang.Float.isNaN(x) || java.lang.Float.isNaN(y) || java.lang.Float.isNaN(z)

    companion object {

        fun normalize(v: Float3): Float3 {
            val l = Matrix.length(v.x, v.y, v.z)
            return Float3(v.x / l, v.y / l, v.z / l)
        }

        fun cross(a: Float3, b: Float3): Float3 {
            val result = Float3()
            result.x = a.y * b.z - b.y * a.z
            result.y = a.z * b.x - b.z * a.x
            result.z = a.x * b.y - b.x * a.y
            return result
        }

        fun scalar(a: Float3, b: Float3): Float {
            return a.x * b.x + a.y * b.y + a.z * b.z
        }

        fun angle(a: Float3, b: Float3): Float {
            return Math.toDegrees(Math.acos((Float3.scalar(a, b) / (a.length * b.length)).toDouble())).toFloat()
        }
    }
}
