package pl.polsl.kostkarubika.di.components

import dagger.Component
import pl.polsl.kostkarubika.di.modules.OpenCvModule
import pl.polsl.kostkarubika.di.scopes.ActivityScope
import pl.polsl.kostkarubika.views.calibrator.CalibratorActivity
import pl.polsl.kostkarubika.views.detector.DetectorActivity

@ActivityScope
@Component(modules = arrayOf(OpenCvModule::class), dependencies = arrayOf(AppComponent::class))
interface OpenCvComponent {

    fun inject(activity: DetectorActivity)
    fun inject(activity: CalibratorActivity)
}