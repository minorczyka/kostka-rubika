package pl.polsl.kostkarubika.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.JavaCameraView
import pl.polsl.kostkarubika.detector.ColorDetector
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.detector.ContourDetector
import pl.polsl.kostkarubika.detector.CubeDetector
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.views.calibrator.CalibratorFrameProcessor
import pl.polsl.kostkarubika.views.calibrator.CalibratorViewModel
import pl.polsl.kostkarubika.views.calibrator.ColorCalibrator
import pl.polsl.kostkarubika.views.detector.DetectorFrameProcessor
import pl.polsl.kostkarubika.views.detector.DetectorBaseLoaderCallback

@Module
class OpenCvModule(val context: Context, val cameraView: JavaCameraView) {

    @Provides
    @ActivityScope
    fun provideBaseLoaderCallback(): BaseLoaderCallback {
        return DetectorBaseLoaderCallback(context, cameraView)
    }

    @Provides
    @ActivityScope
    fun provideContourDetector(): ContourDetector {
        val contourDetector = ContourDetector()
        contourDetector.maxContoursCount = 1
        return contourDetector
    }

    @Provides
    @ActivityScope
    fun provideColorDetector(colorProvider: ColorProvider): ColorDetector {
        return ColorDetector(colorProvider)
    }

    @Provides
    @ActivityScope
    fun provideCubeDetector(): CubeDetector {
        val cubeDetector = CubeDetector()
        return cubeDetector
    }

    @Provides
    @ActivityScope
    fun provideDetectorFrameProcessor(contourDetector: ContourDetector,
                                      colorDetector: ColorDetector,
                                      cubeDetector: CubeDetector,
                                      colorProvider: ColorProvider): DetectorFrameProcessor {
        return DetectorFrameProcessor(contourDetector, colorDetector, cubeDetector, colorProvider)
    }

    @Provides
    @ActivityScope
    fun provideCalibratorFrameProcessor(contourDetector: ContourDetector,
                                        colorDetector: ColorDetector,
                                        colorCalibrator: ColorCalibrator,
                                        colorProvider: ColorProvider): CalibratorFrameProcessor {
        return CalibratorFrameProcessor(contourDetector, colorDetector, colorCalibrator, colorProvider)
    }

    @Provides
    @ActivityScope
    fun provideColorCalibrator(): ColorCalibrator {
        return ColorCalibrator()
    }

    @Provides
    @ActivityScope
    fun provideCalibratorViewModel(colorCalibrator: ColorCalibrator, colorProvider: ColorProvider): CalibratorViewModel {
        return CalibratorViewModel(colorCalibrator, colorProvider)
    }
}