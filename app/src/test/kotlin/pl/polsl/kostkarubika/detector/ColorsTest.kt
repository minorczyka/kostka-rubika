package pl.polsl.kostkarubika.detector

import org.junit.Test
import pl.polsl.kostkarubika.detector.colors.RgbColor
import kotlin.test.assertTrue

class ColorsTest {

    fun doubleEquals(first: Double, second: Double): Boolean {
        val eps = 0.01
        return Math.abs(first - second) < eps
    }

    @Test
    fun rgbToXyzTest() {
        val rgb = RgbColor(143.0, 57.0, 235.0)
        val xyz = rgb.toXYZ()
        assertTrue(doubleEquals(xyz.x, 27.786))
        assertTrue(doubleEquals(xyz.y, 14.764))
        assertTrue(doubleEquals(xyz.z, 79.982))
    }

    @Test
    fun rgbToLabTest() {
        val rgb = RgbColor(143.0, 57.0, 235.0)
        val lab = rgb.toLab()
        assertTrue(doubleEquals(lab.l, 45.309))
        assertTrue(doubleEquals(lab.a, 67.579))
        assertTrue(doubleEquals(lab.b, -74.752))
    }

    @Test
    fun rgbToLabAndBackTest() {
        val rgb = RgbColor(143.0, 57.0, 235.0)
        val lab = rgb.toLab()
        val back = lab.toRgb()
        assertTrue(doubleEquals(rgb.r, back.r))
        assertTrue(doubleEquals(rgb.g, back.g))
        assertTrue(doubleEquals(rgb.b, back.b))
    }

    @Test
    fun distanceTest() {
        val first = RgbColor(11.0, 89.0, 240.0).toLab()
        val second = RgbColor(30.0, 17.0, 184.0).toLab()
        val distance = first.distance(second)
        assertTrue(doubleEquals(distance, 27.964))
    }
}