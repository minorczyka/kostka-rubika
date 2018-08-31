package pl.polsl.kostkarubika.views.detector

import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import pl.polsl.kostkarubika.detector.*
import pl.polsl.kostkarubika.solver.enums.Color

class DetectorFrameProcessor(
        val contourDetector: ContourDetector,
        val colorDetector: ColorDetector,
        val cubeDetector: CubeDetector,
        val colorProvider: ColorProvider) {

    var imageSize = 0

    fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        if (inputFrame == null) {
            return Mat()
        }
        val contours = contourDetector.findContours(inputFrame.gray(), imageSize * 0.1)
        val first = contours.firstOrNull()

        val result = inputFrame.rgba()
        if (first != null) {
            Imgproc.drawContours(result, listOf(first), -1, Scalar(0.0, 255.0, 0.0), 2)
            val contour = Contour(first)
            val radius = contour.minLength * 0.15
            val colors = colorDetector.detectColors(result, contour)
            if (colors != null) {
                for (i in 0..8) {
                    Imgproc.circle(result, contour.intersections[i], (radius * 0.5).toInt(), colorProvider[colors[i]], radius.toInt())
                }
                cubeDetector.found(colors)
            }
        }

        Color.values()
                .filter { cubeDetector.enoughDetections(it) }
                .forEach { Imgproc.circle(result, Point(20.0 + it.ordinal * 30.0, 20.0), 6, colorProvider[it], 12) }
        return result
    }
}