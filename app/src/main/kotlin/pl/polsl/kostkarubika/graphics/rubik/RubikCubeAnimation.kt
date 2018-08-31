package pl.polsl.kostkarubika.graphics.rubik

import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.graphics.utils.Float3

class RubikCubeAnimation(val move: Move) {

    var angleLeft: Float = 0.0f
    val rotationVector: Float3

    init {
        angleLeft = move.rotationAngle
        rotationVector = move.rotationVector
    }

    fun getAngleLeft(deltaTime: Long): Float {
        val delta = Math.min(ANIMATION_SPEED * deltaTime, angleLeft)
        angleLeft -= delta
        return delta
    }

    val isFinished: Boolean
        get() = angleLeft <= 0.0f

    companion object {

        val ANIMATION_SPEED = 0.2f
    }
}
