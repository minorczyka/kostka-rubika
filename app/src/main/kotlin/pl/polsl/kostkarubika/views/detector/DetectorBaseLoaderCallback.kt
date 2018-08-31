package pl.polsl.kostkarubika.views.detector

import android.content.Context
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.JavaCameraView

class DetectorBaseLoaderCallback(context: Context, val cameraView: JavaCameraView) : BaseLoaderCallback(context) {

    override fun onManagerConnected(status: Int) {
        when (status) {
            BaseLoaderCallback.SUCCESS -> cameraView.enableView()
            else -> super.onManagerConnected(status)
        }
    }
}