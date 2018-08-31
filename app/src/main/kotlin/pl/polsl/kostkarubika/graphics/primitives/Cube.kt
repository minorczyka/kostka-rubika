package pl.polsl.kostkarubika.graphics.primitives

import android.opengl.GLES20
import android.opengl.Matrix

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

import pl.polsl.kostkarubika.graphics.utils.Camera
import pl.polsl.kostkarubika.graphics.utils.Float3
import pl.polsl.kostkarubika.graphics.utils.Shader

class Cube(private val shader: Shader) {

    private val colors = FloatArray(108)
    private val vertexBuffer: FloatBuffer
    private val normalBuffer: FloatBuffer
    private val coordBuffer: FloatBuffer
    private lateinit var colorBuffer: FloatBuffer

    private var position = Float3()
    private val rotationMatrix = FloatArray(16)
    private var scale = 1.0f

    init {
        Matrix.setIdentityM(rotationMatrix, 0)

        var bb = ByteBuffer.allocateDirect(vertices.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(vertices)
        vertexBuffer.position(0)

        bb = ByteBuffer.allocateDirect(normals.size * 4)
        bb.order(ByteOrder.nativeOrder())
        normalBuffer = bb.asFloatBuffer()
        normalBuffer.put(normals)
        normalBuffer.position(0)

        bb = ByteBuffer.allocateDirect(coords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        coordBuffer = bb.asFloatBuffer()
        coordBuffer.put(coords)
        coordBuffer.position(0)

        val faces = arrayOf(Float3(), Float3(), Float3(), Float3(), Float3(), Float3())
        setColors(faces)
    }

    fun draw(camera: Camera, parentModelMatrix: FloatArray) {
        shader.use()

        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.scaleM(modelMatrix, 0, scale, scale, scale)
        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, position.x, position.y, position.z)
        Matrix.multiplyMM(modelMatrix, 0, parentModelMatrix, 0, modelMatrix, 0)

        val positionHandle = shader.getAttribLocation("position")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, vertexBuffer)
        val normalHandle = shader.getAttribLocation("normal")
        GLES20.glEnableVertexAttribArray(normalHandle)
        GLES20.glVertexAttribPointer(normalHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, normalBuffer)
        val coordsHandle = shader.getAttribLocation("coords")
        GLES20.glEnableVertexAttribArray(coordsHandle)
        GLES20.glVertexAttribPointer(coordsHandle, COORD_SIZE, GLES20.GL_FLOAT, false, COORD_STRIDE, coordBuffer)
        val colorsHandle = shader.getAttribLocation("colors")
        GLES20.glEnableVertexAttribArray(colorsHandle)
        GLES20.glVertexAttribPointer(colorsHandle, VERTEX_SIZE, GLES20.GL_FLOAT, false, VERTEX_STRIDE, colorBuffer)
        val modelMatrixHandle = shader.getUniformLocation("model")
        GLES20.glUniformMatrix4fv(modelMatrixHandle, 1, false, modelMatrix, 0)
        val viewProjectionMatrixHandle = shader.getUniformLocation("viewProjection")
        GLES20.glUniformMatrix4fv(viewProjectionMatrixHandle, 1, false, camera.viewProjectionMatrix, 0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.size / VERTEX_SIZE)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    fun setPosition(position: Float3) {
        this.position = position
    }

    val worldPosition: Float3
        get() {
            val pos = floatArrayOf(position.x, position.y, position.z, 0f)
            val res = floatArrayOf(0f, 0f, 0f, 0f)
            val modelMatrix = FloatArray(16)
            Matrix.setIdentityM(modelMatrix, 0)
            Matrix.scaleM(modelMatrix, 0, scale, scale, scale)
            Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, modelMatrix, 0)
            Matrix.multiplyMV(res, 0, modelMatrix, 0, pos, 0)
            return Float3(res[0], res[1], res[2])
        }

    fun rotateM(rotation: FloatArray) {
        Matrix.multiplyMM(rotationMatrix, 0, rotation, 0, rotationMatrix, 0)
    }

    fun setColors(a: Float3, b: Float3, c: Float3, d: Float3, e: Float3, f: Float3) {
        val colors = arrayOf(a, b, c, d, e, f)
        setColors(colors)
    }

    fun setColors(faces: Array<Float3>) {
        for (i in 0..colors.size / 3 - 1) {
            colors[3 * i] = faces[i / 6].x
            colors[3 * i + 1] = faces[i / 6].y
            colors[3 * i + 2] = faces[i / 6].z
        }

        val bb = ByteBuffer.allocateDirect(colors.size * 4)
        bb.order(ByteOrder.nativeOrder())
        colorBuffer = bb.asFloatBuffer()
        colorBuffer.put(colors)
        colorBuffer.position(0)
    }

    fun setScale(scale: Float) {
        this.scale = scale
    }

    companion object {

        private val VERTEX_SIZE = 3
        private val VERTEX_STRIDE = VERTEX_SIZE * 4
        private val COORD_SIZE = 2
        private val COORD_STRIDE = COORD_SIZE * 4
        private val vertices = floatArrayOf(
                // Back face
                -0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                // Front face
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                // Left face
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                // Right face
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                // Bottom face
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                // Top face
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f)
        private val normals = floatArrayOf(
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,
                0.0f, 0.0f, -1.0f,

                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f,

                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,

                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,

                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,

                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 1.0f, 0.0f)
        private val coords = floatArrayOf(
                0.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,

                0.0f, 0.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,

                1.0f, 0.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f,

                1.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,

                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 0.0f,
                0.0f, 0.0f,
                0.0f, 1.0f,

                0.0f, 1.0f,
                1.0f, 0.0f,
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                0.0f, 0.0f)
    }
}
