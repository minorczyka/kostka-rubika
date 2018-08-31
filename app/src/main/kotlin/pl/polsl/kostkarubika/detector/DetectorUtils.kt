package pl.polsl.kostkarubika.detector

import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import pl.polsl.kostkarubika.detector.colors.RgbColor

fun MatOfPoint.toMatOfPoint2f(): MatOfPoint2f {
    return MatOfPoint2f(*this.toArray())
}

fun MatOfPoint2f.toMatOfPoint(): MatOfPoint {
    return MatOfPoint(*this.toArray())
}

fun Point.distance(other: Point): Double {
    val p = this - other
    return Math.sqrt(p.x * p.x + p.y * p.y)
}

operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}

operator fun Point.minus(other: Point): Point {
    return Point(x - other.x, y - other.y)
}

operator fun Point.times(value: Double): Point {
    return Point(x * value, y * value)
}

operator fun Point.div(value: Double): Point {
    return Point(x / value, y / value)
}

fun Pair<Point, Point>.intersect(other: Pair<Point, Point>): Point? {
    val x = other.first - first
    val d1 = second - first
    val d2 = other.second - other.first
    val cross = d1.x * d2.y - d1.y * d2.x
    if (Math.abs(cross) < 1e-8) {
        return null
    }
    val t1 = (x.x * d2.y - x.y * d2.x) / cross
    return first + (d1 * t1)
}

fun List<RgbColor>.medoid(): RgbColor? {
    val colors = this.map { Pair(it, it.toLab()) }
    return colors.map { center ->
        val distance = colors.map { it.second.distance(center.second) }.sum()
        Pair(center.first, distance)
    }.minBy { it.second }?.first
}