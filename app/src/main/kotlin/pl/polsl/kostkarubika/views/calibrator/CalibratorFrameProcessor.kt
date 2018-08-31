package pl.polsl.kostkarubika.views.calibrator

import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import pl.polsl.kostkarubika.detector.*

class CalibratorFrameProcessor(
        val contourDetector: ContourDetector,
        val colorDetector: ColorDetector,
        val colorCalibrator: ColorCalibrator,
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
            val centerColor = colorDetector.getCenterRealColor(result, contour)
            if (centerColor != null) {
                Imgproc.circle(result, contour.intersections[4], (radius * 0.5).toInt(), centerColor, radius.toInt())
                colorCalibrator.found(centerColor)
            }
        }
        return result
    }
}