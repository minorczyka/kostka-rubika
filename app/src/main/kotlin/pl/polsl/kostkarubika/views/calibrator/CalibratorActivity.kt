package pl.polsl.kostkarubika.views.calibrator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_detector.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat
import pl.polsl.kostkarubika.KostkaRubikaApp
import pl.polsl.kostkarubika.R
import pl.polsl.kostkarubika.di.components.DaggerOpenCvComponent
import pl.polsl.kostkarubika.di.components.OpenCvComponent
import pl.polsl.kostkarubika.di.modules.OpenCvModule
import javax.inject.Inject

class CalibratorActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var openCvComponent : OpenCvComponent

    @Inject lateinit var baseLoaderCallback: BaseLoaderCallback
    @Inject lateinit var calibratorFrameProcessor: CalibratorFrameProcessor
    @Inject lateinit var viewModel: CalibratorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibrator)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        cameraView.setCvCameraViewListener(this)

        openCvComponent = DaggerOpenCvComponent.builder()
                .openCvModule(OpenCvModule(this, cameraView))
                .appComponent((application as KostkaRubikaApp).appComponent)
                .build()
        openCvComponent.inject(this)
        viewModel.bind(this)
    }

    override fun onResume() {
        super.onResume()
        if (OpenCVLoader.initDebug()) {
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS)
        } else {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, baseLoaderCallback)
        }
    }

    override fun onPause() {
        cameraView.disableView()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.unbind()
        super.onDestroy()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        calibratorFrameProcessor.imageSize = width * height
    }

    override fun onCameraViewStopped() { }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        val result = calibratorFrameProcessor.onCameraFrame(inputFrame)
        return result
    }
}
