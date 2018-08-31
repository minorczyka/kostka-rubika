package pl.polsl.kostkarubika.views.solver

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent

import javax.inject.Inject

class CubeSurfaceView : GLSurfaceView {

    @Inject lateinit var cubeRenderer: CubeRenderer

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    fun init(context: Context) {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true

        val solverActivity = context as SolverActivity
        solverActivity.cubeComponent.inject(this)

        setRenderer(cubeRenderer)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        cubeRenderer.onTouchEvent(e)
        return true
    }

    override fun isInEditMode(): Boolean {
        return false
    }
}
