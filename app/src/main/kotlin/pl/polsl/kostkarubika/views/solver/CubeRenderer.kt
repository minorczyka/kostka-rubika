package pl.polsl.kostkarubika.views.solver

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.view.MotionEvent

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.graphics.rubik.RubikCube
import pl.polsl.kostkarubika.graphics.utils.Arcball
import pl.polsl.kostkarubika.graphics.utils.Camera
import pl.polsl.kostkarubika.graphics.utils.Float3

class CubeRenderer(
        val camera: Camera,
        val rubikCube: RubikCube,
        val arcball: Arcball,
        val faceForm: FaceForm) : GLSurfaceView.Renderer {

    private var lastTime: Long = 0

    override fun onSurfaceCreated(gl10: GL10, eglConfig: EGLConfig) {
        rubikCube.init()

        GLES20.glEnable(GLES20.GL_CULL_FACE)
        GLES20.glCullFace(GLES20.GL_BACK)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        GLES20.glClearColor(0.2f, 0.2f, 0.2f, 1.0f)

        lastTime = System.currentTimeMillis()
    }

    override fun onSurfaceChanged(gl10: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        camera.onSurfaceChanged(width, height)
        arcball.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl10: GL10) {
        val time = System.currentTimeMillis()
        val deltaTime = time - lastTime
        lastTime = time
        rubikCube.animate(deltaTime)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        rubikCube.draw(camera)
    }

    fun onTouchEvent(e: MotionEvent) {
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> arcball.setPrevPosition(Float3(x, y, 0f))
            MotionEvent.ACTION_MOVE -> {
                val rotation = arcball.getRotation(Float3(x, y, 0f))
                if (rotation != null) {
                    rubikCube.rotateM(rotation)
                }
            }
        }
    }
}
