package pl.polsl.kostkarubika.views.draw

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.solver.enums.Color

class DrawCubeView : View {

    var face: List<Color>? = null
    var sides: List<Color>? = null
    val colorPaints = Color.values().map { Pair(it, Paint()) }.toMap()
    val strokePaint = Paint()
    val strokeWidth = convertDpToPixel(2.0f)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        val face = face
        val sides = sides
        if (canvas == null || face == null || sides == null) {
            return
        }
        val rectSide = (width - strokeWidth) / 5.0f
        drawRect(canvas, rectSide, 2, 0, sides[0])
        drawRect(canvas, rectSide, 4, 2, sides[1])
        drawRect(canvas, rectSide, 2, 4, sides[2])
        drawRect(canvas, rectSide, 0, 2, sides[3])
        drawRect(canvas, rectSide, 1, 1, face[0])
        drawRect(canvas, rectSide, 2, 1, face[1])
        drawRect(canvas, rectSide, 3, 1, face[2])
        drawRect(canvas, rectSide, 1, 2, face[3])
        drawRect(canvas, rectSide, 2, 2, face[4])
        drawRect(canvas, rectSide, 3, 2, face[5])
        drawRect(canvas, rectSide, 1, 3, face[6])
        drawRect(canvas, rectSide, 2, 3, face[7])
        drawRect(canvas, rectSide, 3, 3, face[8])
    }

    private fun drawRect(canvas: Canvas, rectSide: Float, left: Int, top: Int, color: Color) {
        canvas.drawRect(left * rectSide + strokeWidth / 2, top * rectSide + strokeWidth / 2, (left + 1) * rectSide + strokeWidth / 2, (top + 1) * rectSide + strokeWidth / 2, colorPaints[color])
        canvas.drawRect(left * rectSide + strokeWidth / 2, top * rectSide + strokeWidth / 2, (left + 1) * rectSide + strokeWidth / 2, (top + 1) * rectSide + strokeWidth / 2, strokePaint)
    }

    fun setupPaints(colorProvider: ColorProvider) {
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth
        strokePaint.color = android.graphics.Color.BLACK

        val fineColors = colorProvider.getFineColors()
        Color.values().forEach {
            val rgb = fineColors[it]
            if (rgb != null) {
                colorPaints[it]?.color = android.graphics.Color.rgb(rgb.r.toInt(), rgb.g.toInt(), rgb.b.toInt())
            }
            colorPaints[it]?.style = Paint.Style.FILL
        }
    }

    private fun convertDpToPixel(dp: Float): Float {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px).toFloat()
    }
}