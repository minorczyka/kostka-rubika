package pl.polsl.kostkarubika.graphics.utils

import android.opengl.Matrix

class Camera {

    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    private var front = Float3(0.0f, 0.0f, -1.0f)
    private var up = Float3()
    private var right = Float3()
    private val worldUp = Float3(0.0f, 1.0f, 0.0f)
    private val yaw = -90.0f
    private val pitch = 0.0f
    var position = Float3(0.0f, 0.0f, 5.0f)
        set(position) {
            field = position
            updateCameraVectors()
        }

    init {
        updateCameraVectors()
    }

    fun onSurfaceChanged(width: Int, height: Int) {
        val ratio = width.toFloat() / height
        Matrix.perspectiveM(projectionMatrix, 0, 45f, ratio, 0.1f, 100.0f)
    }

    val viewProjectionMatrix: FloatArray
        get() {
            val viewProjectionMatrix = FloatArray(16)
            Matrix.setLookAtM(viewMatrix, 0,
                    this.position.x, this.position.y, this.position.z,
                    this.position.x + front.x, this.position.y + front.y, this.position.z + front.z,
                    up.x, up.y, up.z)
            Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
            return viewProjectionMatrix
        }

    private fun updateCameraVectors() {
        front.x = Math.cos(Math.toRadians(yaw.toDouble())).toFloat() * Math.cos(Math.toRadians(pitch.toDouble())).toFloat()
        front.y = Math.sin(Math.toRadians(pitch.toDouble())).toFloat()
        front.z = Math.sin(Math.toRadians(yaw.toDouble())).toFloat() * Math.cos(Math.toRadians(pitch.toDouble())).toFloat()
        front = Float3.normalize(front)
        right = Float3.normalize(Float3.cross(front, worldUp))
        up = Float3.normalize(Float3.cross(right, front))
    }
}
