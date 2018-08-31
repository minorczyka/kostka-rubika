package pl.polsl.kostkarubika.detector.colors

class XyzColor(val x: Double, val y: Double, val z: Double) {

    fun toLab(): LabColor {
        var varX = x / 95.047
        var varY = y / 100.000
        var varZ = z / 108.883

        if (varX > 0.008856) {
            varX = Math.pow(varX, 1.0 / 3)
        } else {
            varX = (7.787 * varX) + (16.0 / 116)
        }
        if (varY > 0.008856) {
            varY = Math.pow(varY, 1.0 / 3)
        } else {
            varY = (7.787 * varY) + (16.0 / 116)
        }
        if (varZ > 0.008856) {
            varZ = Math.pow(varZ, 1.0 / 3)
        } else {
            varZ = (7.787 * varZ) + (16.0 / 116)
        }

        val l = (116 * varY) - 16
        val a = 500 * (varX - varY)
        val b = 200 * (varY - varZ)
        return LabColor(l, a, b)
    }

    fun toRGB(): RgbColor {
        var varX = x / 100
        var varY = y / 100
        var varZ = z / 100
        var r = varX *  3.2406 + varY * -1.5372 + varZ * -0.4986
        var g = varX * -0.9689 + varY *  1.8758 + varZ *  0.0415
        var b = varX *  0.0557 + varY * -0.2040 + varZ *  1.0570
        if (r > 0.0031308) {
            r = 1.055 * (Math.pow(r, 1 / 2.4)) - 0.055
        } else {
            r *= 12.92
        }
        if (g > 0.0031308) {
            g = 1.055 * (Math.pow(g, 1 / 2.4)) - 0.055
        } else {
            g *= 12.92
        }
        if (b > 0.0031308) {
            b = 1.055 * (Math.pow(b, 1 / 2.4)) - 0.055
        } else {
            b *= 12.92
        }
        r *= 255.0
        g *= 255.0
        b *= 255.0
        return RgbColor(r, g, b)
    }
}