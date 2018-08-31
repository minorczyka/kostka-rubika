package pl.polsl.kostkarubika.graphics.rubik

import android.opengl.Matrix
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.detector.colors.RgbColor

import java.util.ArrayList

import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Edge
import pl.polsl.kostkarubika.solver.enums.Move
import pl.polsl.kostkarubika.graphics.primitives.Cube
import pl.polsl.kostkarubika.graphics.utils.Camera
import pl.polsl.kostkarubika.graphics.utils.Float3
import pl.polsl.kostkarubika.graphics.utils.Shader
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class RubikCube(val shader: Shader, val faceForm: FaceForm, val colorProvider: ColorProvider) {

    private val black = Float3(0.0f, 0.0f, 0.0f)
    private lateinit var down: Float3
    private lateinit var up: Float3
    private lateinit var front: Float3
    private lateinit var back: Float3
    private lateinit var left: Float3
    private lateinit var right: Float3
    private lateinit var mainColors: Array<Float3>

    private lateinit var cubes: Array<Cube>
    private val modelMatrix = FloatArray(16)
    private var currentAnimation: RubikCubeAnimation? = null
    val lock = ReentrantLock()

    var moveProvider: (() -> Move?)? = null

    fun init() {
        val fineColors = colorProvider.getFineColors()
        down = Float3(fineColors[Color.D] ?: RgbColor(0.0, 0.0, 0.0))
        up = Float3(fineColors[Color.U] ?: RgbColor(0.0, 0.0, 0.0))
        front = Float3(fineColors[Color.F] ?: RgbColor(0.0, 0.0, 0.0))
        back = Float3(fineColors[Color.B] ?: RgbColor(0.0, 0.0, 0.0))
        left = Float3(fineColors[Color.L] ?: RgbColor(0.0, 0.0, 0.0))
        right = Float3(fineColors[Color.R] ?: RgbColor(0.0, 0.0, 0.0))
        mainColors = arrayOf(up, right, front, down, left, back)

        shader.init()
        cubes = Array<Cube>(26, { Cube(shader) })

        Matrix.setIdentityM(modelMatrix, 0)
        var colors: Array<Float3>

        cubes[0].setPosition(Float3(-1.0f, 1.0f, -1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.ULB))
        cubes[0].setColors(colors[2], black, colors[1], black, black, colors[0])
        cubes[0].setScale(0.5f)
        cubes[1].setPosition(Float3(0.0f, 1.0f, -1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.UB))
        cubes[1].setColors(colors[1], black, black, black, black, colors[0])
        cubes[1].setScale(0.5f)
        cubes[2].setPosition(Float3(1.0f, 1.0f, -1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.UBR))
        cubes[2].setColors(colors[1], black, black, colors[2], black, colors[0])
        cubes[2].setScale(0.5f)
        cubes[3].setPosition(Float3(-1.0f, 1.0f, 0.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.UL))
        cubes[3].setColors(black, black, colors[1], black, black, colors[0])
        cubes[3].setScale(0.5f)
        cubes[4].setPosition(Float3(0.0f, 1.0f, 0.0f))
        cubes[4].setColors(black, black, black, black, black, up)
        cubes[4].setScale(0.5f)
        cubes[5].setPosition(Float3(1.0f, 1.0f, 0.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.UR))
        cubes[5].setColors(black, black, black, colors[1], black, colors[0])
        cubes[5].setScale(0.5f)
        cubes[6].setPosition(Float3(-1.0f, 1.0f, 1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.UFL))
        cubes[6].setColors(black, colors[1], colors[2], black, black, colors[0])
        cubes[6].setScale(0.5f)
        cubes[7].setPosition(Float3(0.0f, 1.0f, 1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.UF))
        cubes[7].setColors(black, colors[1], black, black, black, colors[0])
        cubes[7].setScale(0.5f)
        cubes[8].setPosition(Float3(1.0f, 1.0f, 1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.URF))
        cubes[8].setColors(black, colors[2], black, colors[1], black, colors[0])
        cubes[8].setScale(0.5f)

        cubes[9].setPosition(Float3(-1.0f, 0.0f, -1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.BL))
        cubes[9].setColors(colors[0], black, colors[1], black, black, black)
        cubes[9].setScale(0.5f)
        cubes[10].setPosition(Float3(0.0f, 0.0f, -1.0f))
        cubes[10].setColors(back, black, black, black, black, black)
        cubes[10].setScale(0.5f)
        cubes[11].setPosition(Float3(1.0f, 0.0f, -1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.BR))
        cubes[11].setColors(colors[0], black, black, colors[1], black, black)
        cubes[11].setScale(0.5f)
        cubes[12].setPosition(Float3(-1.0f, 0.0f, 0.0f))
        cubes[12].setColors(black, black, left, black, black, black)
        cubes[12].setScale(0.5f)
        cubes[13].setPosition(Float3(1.0f, 0.0f, 0.0f))
        cubes[13].setColors(black, black, black, right, black, black)
        cubes[13].setScale(0.5f)
        cubes[14].setPosition(Float3(-1.0f, 0.0f, 1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.FL))
        cubes[14].setColors(black, colors[0], colors[1], black, black, black)
        cubes[14].setScale(0.5f)
        cubes[15].setPosition(Float3(0.0f, 0.0f, 1.0f))
        cubes[15].setColors(black, front, black, black, black, black)
        cubes[15].setScale(0.5f)
        cubes[16].setPosition(Float3(1.0f, 0.0f, 1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.FR))
        cubes[16].setColors(black, colors[0], black, colors[1], black, black)
        cubes[16].setScale(0.5f)

        cubes[17].setPosition(Float3(-1.0f, -1.0f, -1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.DBL))
        cubes[17].setColors(colors[1], black, colors[2], black, colors[0], black)
        cubes[17].setScale(0.5f)
        cubes[18].setPosition(Float3(0.0f, -1.0f, -1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.DB))
        cubes[18].setColors(colors[1], black, black, black, colors[0], black)
        cubes[18].setScale(0.5f)
        cubes[19].setPosition(Float3(1.0f, -1.0f, -1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.DRB))
        cubes[19].setColors(colors[2], black, black, colors[1], colors[0], black)
        cubes[19].setScale(0.5f)
        cubes[20].setPosition(Float3(-1.0f, -1.0f, 0.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.DL))
        cubes[20].setColors(black, black, colors[1], black, colors[0], black)
        cubes[20].setScale(0.5f)
        cubes[21].setPosition(Float3(0.0f, -1.0f, 0.0f))
        cubes[21].setColors(black, black, black, black, down, black)
        cubes[21].setScale(0.5f)
        cubes[22].setPosition(Float3(1.0f, -1.0f, 0.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.DR))
        cubes[22].setColors(black, black, black, colors[1], colors[0], black)
        cubes[22].setScale(0.5f)
        cubes[23].setPosition(Float3(-1.0f, -1.0f, 1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.DLF))
        cubes[23].setColors(black, colors[2], colors[1], black, colors[0], black)
        cubes[23].setScale(0.5f)
        cubes[24].setPosition(Float3(0.0f, -1.0f, 1.0f))
        colors = getColors(faceForm.getEdgeColors(Edge.DF))
        cubes[24].setColors(black, colors[1], black, black, colors[0], black)
        cubes[24].setScale(0.5f)
        cubes[25].setPosition(Float3(1.0f, -1.0f, 1.0f))
        colors = getColors(faceForm.getCornerColors(Corner.DFR))
        cubes[25].setColors(black, colors[1], black, colors[2], colors[0], black)
        cubes[25].setScale(0.5f)
    }

    fun draw(camera: Camera) {
        for (cube in cubes) {
            cube.draw(camera, modelMatrix)
        }
    }

    fun animate(deltaTime: Long) {
        lock.withLock {
            if (currentAnimation == null) {
                val move = moveProvider?.invoke()
                if (move != null) {
                    currentAnimation = RubikCubeAnimation(move)
                }
            }
            val animation = currentAnimation
            if (animation != null) {
                val rotation = FloatArray(16)
                val list = getCubesForMove(animation.move)
                val rotationVector = animation.rotationVector
                val rotationAngle = animation.getAngleLeft(deltaTime)
                Matrix.setRotateM(rotation, 0, rotationAngle, rotationVector.x, rotationVector.y, rotationVector.z)
                for (cube in list) {
                    cube.rotateM(rotation)
                }
                if (currentAnimation?.isFinished == true) {
                    currentAnimation = null
                }
            }
        }
    }

    fun rotateM(rotation: FloatArray) {
        Matrix.multiplyMM(modelMatrix, 0, rotation, 0, modelMatrix, 0)
    }

    private fun getColors(colors: Array<Color>): Array<Float3> {
        if (colors.size == 3) {
            return arrayOf(mainColors[colors[0].ordinal], mainColors[colors[1].ordinal], mainColors[colors[2].ordinal])
        } else {
            return arrayOf(mainColors[colors[0].ordinal], mainColors[colors[1].ordinal])
        }
    }

    private fun getCubesForMove(move: Move): List<Cube> {
        val result = ArrayList<Cube>(9)
        for (cube in cubes) {
            if (move.checkPositionWithPattern(cube.worldPosition)) {
                result.add(cube)
            }
        }
        return result
    }

    fun makeMove(move: Move) {
        val animation = currentAnimation
        if (animation != null) {
            completeAnimation(animation)
            currentAnimation = null
        }
        val moveAnimation = RubikCubeAnimation(move)
        completeAnimation(moveAnimation)
    }

    fun animateMove(move: Move) {
        val animation = currentAnimation
        if (animation != null) {
            completeAnimation(animation)
        }
        currentAnimation = RubikCubeAnimation(move)
    }

    private fun completeAnimation(animation: RubikCubeAnimation) {
        val rotation = FloatArray(16)
        val list = getCubesForMove(animation.move)
        val rotationVector = animation.rotationVector
        val rotationAngle = animation.angleLeft
        Matrix.setRotateM(rotation, 0, rotationAngle, rotationVector.x, rotationVector.y, rotationVector.z)
        for (cube in list) {
            cube.rotateM(rotation)
        }
    }
}
