package pl.polsl.kostkarubika.detector

import android.util.Log
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.imgproc.Imgproc

class ContourDetector {

    var maxContoursCount = Int.MAX_VALUE

    private fun cosineOfAngle(p1: DoubleArray, p2: DoubleArray, p0: DoubleArray): Double {
        val dx1 = p1[0] - p0[0]
        val dy1 = p1[1] - p0[1]
        val dx2 = p2[0] - p0[0]
        val dy2 = p2[1] - p0[1]
        return (dx1*dx2 + dy1*dy2) / Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10)
    }

    private fun maxCosine(contour: MatOfPoint2f): Double {
        var result = 0.0
        for (i in 2..5) {
            val cosine = cosineOfAngle(contour[i % 4, 0], contour[i - 2, 0], contour[(i - 1) % 4, 0])
            result = Math.max(result, cosine)
        }
        return result
    }

    fun findContours(inputFrame: Mat, minSize: Double = Double.MAX_VALUE): List<MatOfPoint> {
        val frame = Mat()
        inputFrame.copyTo(frame)
        val contours = mutableListOf<MatOfPoint>()
        val result = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()

        Imgproc.medianBlur(frame, frame, 5)
        Imgproc.adaptiveThreshold(frame, frame, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 5.0)
        Imgproc.dilate(frame, frame, Mat.ones(5, 5, CvType.CV_8UC1))
        Imgproc.findContours(frame, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)
        contours.sortBy { -Imgproc.contourArea(it) }

        var found = 0
        for (it in contours) {
            Log.i("FOUND", "${Imgproc.contourArea(it)} / $minSize")
            if (found >= maxContoursCount || Imgproc.contourArea(it) < minSize) {
                break
            }
            val contour = it.toMatOfPoint2f()
            val approx = MatOfPoint2f()
            val peri = Imgproc.arcLength(contour, true)
            Imgproc.approxPolyDP(contour, approx, 0.05 * peri, true)
            if (approx.rows() == 4 && maxCosine(approx) < 0.1) {
                Log.i("SIZE", "area = ${Imgproc.contourArea(approx)}")
                result += approx.toMatOfPoint()
                found += 1
            }
        }
        Log.i("SIZE", "----------------------------------------")

        return result
    }
}