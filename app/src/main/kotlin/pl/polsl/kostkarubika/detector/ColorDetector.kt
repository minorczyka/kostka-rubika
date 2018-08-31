package pl.polsl.kostkarubika.detector

import org.opencv.core.Mat
import org.opencv.core.Scalar
import pl.polsl.kostkarubika.detector.colors.RgbColor
import pl.polsl.kostkarubika.solver.enums.Color

class ColorDetector(val colorProvider: ColorProvider) {

    fun detectColors(image: Mat, contour: Contour): List<Color>? {
        val intersections = contour.intersections.filterNotNull()
        if (intersections.size != 9) {
            return null
        }
        return intersections.map { point ->
            val r = image[point.y.toInt(), point.x.toInt()][0]
            val g = image[point.y.toInt(), point.x.toInt()][1]
            val b = image[point.y.toInt(), point.x.toInt()][2]
            val lab = RgbColor(r, g, b).toLab()
            val distances = colorProvider.labColors.map { Pair(it.first, it.second.distance(lab)) }
            val neighbours = distances.sortedBy { it.second }.map { it.first }
            var result = Color.U
            for (i in 7 downTo 1) {
                val colors = mutableListOf(0, 0, 0, 0, 0, 0)
                neighbours.take(i).forEach { colors[it.ordinal]++ }
                val max = colors.max()!!
                if (colors.filter { it == max }.size == 1) {
                    result = Color.values()[colors.indexOf(max)]
                    break
                }
            }
            result
        }
    }

    fun getCenterRealColor(image: Mat, contour: Contour): Scalar? {
        val intersections = contour.intersections.filterNotNull()
        if (intersections.size != 9) {
            return null
        }
        val r = image[intersections[4].y.toInt(), intersections[4].x.toInt()][0]
        val g = image[intersections[4].y.toInt(), intersections[4].x.toInt()][1]
        val b = image[intersections[4].y.toInt(), intersections[4].x.toInt()][2]
        return Scalar(r, g, b)
    }
}