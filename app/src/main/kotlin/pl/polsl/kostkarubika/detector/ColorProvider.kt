package pl.polsl.kostkarubika.detector

import android.content.SharedPreferences
import org.opencv.core.Scalar
import pl.polsl.kostkarubika.detector.colors.LabColor
import pl.polsl.kostkarubika.detector.colors.RgbColor
import pl.polsl.kostkarubika.solver.enums.Color

class ColorProvider(val sharedPreferences: SharedPreferences) {

    val defaultColors = mapOf(
            Pair(Color.D, RgbColor(170.0, 180.0, 160.0)),
            Pair(Color.F, RgbColor(170.0, 0.0, 0.0)),
            Pair(Color.R, RgbColor(0.0, 90.0, 50.0)),
            Pair(Color.L, RgbColor(0.0, 16.0, 100.0)),
            Pair(Color.U, RgbColor(190.0, 160.0, 0.0)),
            Pair(Color.B, RgbColor(180.0, 60.0, 0.0)))

    var labColors: List<Pair<Color, LabColor>>
    var medoids: Map<Color, RgbColor>

    init {
        val colors = Color.values().map { Pair(it, readColors(it) ?: listOf(defaultColors[it]!!)) }.toMap()
        medoids = colors.mapValues { it.value.medoid()!! }
        labColors = colors.flatMap { color ->
            color.value.map { Pair(color.key, it.toLab()) }
        }
    }

    private fun readColors(color: Color): List<RgbColor>? {
        val str = sharedPreferences.getString(color.name, "")
        if (str == "") {
            return null
        }
        return str.split(':').filter { it != "" }.map {
            val rgb = it.split(';')
            val r = rgb[0].toDouble()
            val g = rgb[1].toDouble()
            val b = rgb[2].toDouble()
            RgbColor(r, g, b)
        }
    }

    private fun writeColors(color: Color, colors: List<RgbColor>) {
        val builder = StringBuilder()
        colors.forEach {
            builder.append(it.r)
            builder.append(";")
            builder.append(it.g)
            builder.append(";")
            builder.append(it.b)
            builder.append(":")
        }
        val editor = sharedPreferences.edit()
        editor.putString(color.name, builder.toString())
        editor.apply()
    }

    operator fun get(color: Color): Scalar {
        return medoids[color]?.toScalar() ?: Scalar(255.0, 255.0, 255.0)
    }

    fun setColors(newColors: List<List<RgbColor>>) {
        val colors = newColors.mapIndexed { i, list -> Pair(Color.values()[i], list) }.toMap()
        colors.forEach {
            writeColors(it.key, it.value)
        }
        medoids = colors.mapValues { it.value.medoid()!! }
        labColors = colors.flatMap { color ->
            color.value.map { Pair(color.key, it.toLab()) }
        }
    }

    fun getFineColors(): Map<Color, RgbColor> {
        return medoids.mapValues { color ->
            val lab = color.value.toLab()
            LabColor(lab.l * 1.2, lab.a, lab.b).toRgb()
        }
    }
}