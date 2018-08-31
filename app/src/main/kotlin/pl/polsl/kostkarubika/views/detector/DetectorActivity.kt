package pl.polsl.kostkarubika.views.detector

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.WindowManager
import android.widget.PopupMenu
import org.opencv.android.OpenCVLoader
import pl.polsl.kostkarubika.R
import kotlinx.android.synthetic.main.activity_detector.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import pl.polsl.kostkarubika.detector.CubeDetector
import pl.polsl.kostkarubika.di.components.DaggerOpenCvComponent
import pl.polsl.kostkarubika.di.components.OpenCvComponent
import pl.polsl.kostkarubika.di.modules.OpenCvModule
import pl.polsl.kostkarubika.views.solver.SolverActivity
import javax.inject.Inject
import pl.polsl.kostkarubika.KostkaRubikaApp
import pl.polsl.kostkarubika.detector.DetectionError
import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.utils.Left
import pl.polsl.kostkarubika.solver.utils.Right
import pl.polsl.kostkarubika.solver.utils.randomCube
import pl.polsl.kostkarubika.views.calibrator.CalibratorActivity
import pl.polsl.kostkarubika.views.draw.DrawActivity
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class DetectorActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var openCvComponent : OpenCvComponent

    @Inject lateinit var baseLoaderCallback: BaseLoaderCallback
    @Inject lateinit var detectorFrameProcessor: DetectorFrameProcessor
    @Inject lateinit var cubeDetector: CubeDetector

    private var progressDialog: ProgressDialog? = null
    private var blockDetection = false
    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detector)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        cameraView.setCvCameraViewListener(this)

        openCvComponent = DaggerOpenCvComponent.builder()
                .openCvModule(OpenCvModule(this, cameraView))
                .appComponent((application as KostkaRubikaApp).appComponent)
                .build()
        openCvComponent.inject(this)

        menuButton.setOnClickListener {
            val popup = PopupMenu(this, it)
            popup.menuInflater.inflate(R.menu.detector_menu, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.reset -> {
                        cubeDetector.reset()
                        true
                    }
                    R.id.randomCube -> {
                        openFaceForm(randomCube().toFaceForm())
                        true
                    }
                    R.id.calibration -> {
                        val intent = Intent(this, CalibratorActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
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

    override fun onCameraViewStarted(width: Int, height: Int) {
        detectorFrameProcessor.imageSize = width * height
    }

    override fun onCameraViewStopped() { }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        if (blockDetection) {
            return inputFrame?.rgba() ?: Mat()
        }
        val result = detectorFrameProcessor.onCameraFrame(inputFrame)
        if (cubeDetector.enoughDetections()) {
            Log.i("DETECTING", "Checking cube...")
            blockDetection = true
            runOnUiThread {
                progressDialog = ProgressDialog.show(this, getString(R.string.detector_dialog_title), getString(R.string.detector_dialog_processing), true, false)
                subscription = Observable.defer {
                    val detection = cubeDetector.toFaceForm()
                    Observable.just(detection)
                }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe { detection ->
                    when (detection) {
                        is Left -> {
                            if (detection.value == DetectionError.FOUND_MORE) {
                                val intent = Intent(this, DrawActivity::class.java)
                                intent.putExtra(DrawActivity.FACES, cubeDetector.stringFaces.toTypedArray())
                                startActivityForResult(intent, DrawActivity.REQUEST_CODE)
                            } else {
                                AlertDialog.Builder(this)
                                        .setTitle(R.string.detector_dialog_title)
                                        .setMessage(R.string.detector_dialog_error)
                                        .setPositiveButton(R.string.ok, { dialog, i -> dialog.dismiss() })
                                        .create()
                                        .show()
                            }
                            cubeDetector.reset()
                            Log.e("DETECTING", detection.value.name)
                        }
                        is Right -> {
                            openFaceForm(detection.value)
                        }
                    }
                    progressDialog?.dismiss()
                    progressDialog = null
                    blockDetection = false
                }
            }
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DrawActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val result = data.getStringExtra(DrawActivity.RESULT)
            openFaceForm(FaceForm(result))
        }
    }

    private fun openFaceForm(faceForm: FaceForm) {
        Log.i("DETECTING", "Found cube.")
        val intent = Intent(this, SolverActivity::class.java)
        intent.putExtra(SolverActivity.FACE_FORM, faceForm.toString())
        startActivity(intent)
        cubeDetector.reset()
    }
}
