package pl.polsl.kostkarubika.views.calibrator

import org.opencv.core.Scalar
import pl.polsl.kostkarubika.detector.colors.RgbColor
import pl.polsl.kostkarubika.detector.medoid
import java.util.*

class ColorCalibrator {

    var listener: ((RgbColor?) -> Unit)? = null

    val colors = mutableListOf<LinkedList<RgbColor>>()

    fun found(color: Scalar) {
        if (colors.isEmpty()) {
            colors.add(LinkedList())
        }
        colors.last().add(RgbColor(color))
        if (colors.last().size > 20) {
            colors.last().poll()
            val color = colors.last().medoid()!!
            listener?.invoke(color)
        }
    }

    fun nextSide() {
        colors.add(LinkedList())
        listener?.invoke(null)
    }

    fun reset() {
        colors.clear()
    }
}