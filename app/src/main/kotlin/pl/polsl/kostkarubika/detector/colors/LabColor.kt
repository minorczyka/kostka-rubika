package pl.polsl.kostkarubika.detector.colors

class LabColor(val l: Double, val a: Double, val b: Double) {

    fun distance(other: LabColor): Double {
        return Math.sqrt(Math.pow(l - other.l, 2.0) + Math.pow(a - other.a, 2.0) + Math.pow(b - other.b, 2.0))
    }

    fun toXyz(): XyzColor {
        var y = (l + 16) / 116
        var x = a / 500 + y
        var z = y - b / 200

        if (Math.pow(y, 3.0) > 0.008856) {
            y = Math.pow(y, 3.0)
        } else {
            y = (y - 16 / 116) / 7.787
        }
        if (Math.pow(x, 3.0) > 0.008856) {
            x = Math.pow(x, 3.0)
        } else {
            x = (x - 16 / 116) / 7.787
        }
        if (Math.pow(z, 3.0) > 0.008856) {
            z = Math.pow(z, 3.0)
        } else {
            z = (z - 16 / 116) / 7.787
        }

        x *= 95.047
        y *= 100.0
        z *= 108.883
        return XyzColor(x, y, z)
    }

    fun toRgb(): RgbColor {
        val rgb = toXyz().toRGB()
        val r = Math.min(Math.max(rgb.r, 0.0), 255.0)
        val g = Math.min(Math.max(rgb.g, 0.0), 255.0)
        val b = Math.min(Math.max(rgb.b, 0.0), 255.0)
        return RgbColor(r, g, b)
    }
}