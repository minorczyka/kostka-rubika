package pl.polsl.kostkarubika.detector

import org.opencv.core.MatOfPoint
import org.opencv.core.Point

class Contour {

    val points: List<Point>

    constructor(mat: MatOfPoint) {
        points = (0..3).map { Point(mat[it, 0]) }
    }

    constructor(points: List<Point>) {
        this.points = points
    }

    private var _sidePoints: List<Point>? = null
    val sidePoints: List<Point>
        get() {
            if (_sidePoints == null) {
                _sidePoints = (0..3).flatMap {
                    val first = points[it]
                    val second = points[(it + 1) % 4]
                    val direction = second - first
                    val delta = direction / 6.0

                    val a = first + delta
                    val b = first + (delta * 3.0)
                    val c = first + (delta * 5.0)
                    listOf(a, b, c)
                }
            }
            return _sidePoints!!
        }

    private var _lines: List<Pair<Point, Point>>? = null
    val lines: List<Pair<Point, Point>>
        get() {
            if (_lines == null) {
                val side = sidePoints
                _lines = listOf(Pair(side[0], side[8]), Pair(side[1], side[7]), Pair(side[2], side[6]),
                        Pair(side[3], side[11]), Pair(side[4], side[10]), Pair(side[5], side[9]))
            }
            return _lines!!
        }

    private var _intersections: List<Point?>? = null
    val intersections: List<Point?>
        get() {
            if (_intersections == null) {
                val l = lines
                _intersections = listOf(l[0].intersect(l[5]), l[1].intersect(l[5]), l[2].intersect(l[5]),
                        l[0].intersect(l[4]), l[1].intersect(l[4]), l[2].intersect(l[4]),
                        l[0].intersect(l[3]), l[1].intersect(l[3]), l[2].intersect(l[3]))
            }
            return _intersections!!
        }

    private var _minLength: Double? = null
    val minLength: Double
        get() {
            if (_minLength == null) {
                _minLength = (0..3).map {
                    val first = points[it]
                    val second = points[(it + 1) % 4]
                    first.distance(second)
                }.min()
            }
            return _minLength!!
        }
}