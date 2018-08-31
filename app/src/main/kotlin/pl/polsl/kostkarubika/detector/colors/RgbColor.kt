package pl.polsl.kostkarubika.detector.colors

import org.opencv.core.Scalar

class RgbColor(val r: Double, val g: Double, val b: Double) {

    constructor(scalar: Scalar) : this(scalar.`val`[0], scalar.`val`[1], scalar.`val`[2]) { }

    fun toXYZ(): XyzColor {
        var red = r / 255
        var green = g / 255
        var blue = b / 255

        if (red > 0.04045) {
            red = Math.pow(((red + 0.055) / 1.055), 2.4)
        } else {
            red /= 12.92
        }
        if (green > 0.04045) {
            green = Math.pow(((green + 0.055) / 1.055), 2.4)
        } else {
            green /= 12.92
        }
        if (blue > 0.04045) {
            blue = Math.pow(((blue + 0.055) / 1.055), 2.4)
        } else {
            blue /= 12.92
        }

        red *= 100
        green *= 100
        blue *= 100

        val x = red * 0.4124 + green * 0.3576 + blue * 0.1805
        val y = red * 0.2126 + green * 0.7152 + blue * 0.0722
        val z = red * 0.0193 + green * 0.1192 + blue * 0.9505

        return XyzColor(x, y, z)
    }

    fun toLab(): LabColor {
        return toXYZ().toLab()
    }

    fun toScalar(): Scalar {
        return Scalar(r, g, b)
    }
}