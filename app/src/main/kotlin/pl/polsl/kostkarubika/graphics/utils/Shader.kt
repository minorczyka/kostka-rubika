package pl.polsl.kostkarubika.graphics.utils

import android.opengl.GLES20
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class Shader(val vertexShaderStream: InputStream, val fragmentShaderStream: InputStream) {

    var program: Int = 0

    fun init() {
        val vertexShaderCode = loadStringFromStream(vertexShaderStream)
        val fragmentShaderCode = loadStringFromStream(fragmentShaderStream)
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            val message = "Could not link program: " + GLES20.glGetProgramInfoLog(program)
            GLES20.glDeleteProgram(program)
            throw RuntimeException(message)
        }
    }

    private fun loadStringFromStream(inputStream: InputStream): String {
        val stringBuilder = StringBuilder()
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            reader.readLines().forEach { stringBuilder.append(it) }
        } catch (e: IOException) {
            Log.e("Shader", "Loading shader from assets error: " + e.message)
        } finally {
            try {
                reader?.close()
            } catch (e: IOException) {
                Log.e("Shader", "Closing stream error: " + e.message)
            }
        }
        return stringBuilder.toString()
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] != GLES20.GL_TRUE) {
            val message = "Could not compile program: " + GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            throw RuntimeException(message)
        }
        return shader
    }

    fun use() {
        GLES20.glUseProgram(program)
    }

    fun getAttribLocation(name: String): Int {
        return GLES20.glGetAttribLocation(program, name)
    }

    fun getUniformLocation(name: String): Int {
        return GLES20.glGetUniformLocation(program, name)
    }
}
