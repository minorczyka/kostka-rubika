package pl.polsl.kostkarubika.views.calibrator

import kotlinx.android.synthetic.main.activity_calibrator.*
import pl.polsl.kostkarubika.detector.ColorProvider
import pl.polsl.kostkarubika.detector.colors.RgbColor
import pl.polsl.kostkarubika.solver.enums.Color
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription

class CalibratorViewModel(val colorCalibrator: ColorCalibrator, val colorProvider: ColorProvider) {

    val stepSubject: BehaviorSubject<Color> = BehaviorSubject.create(Color.U)
    val detectedColorSubject: BehaviorSubject<RgbColor?> = BehaviorSubject.create()

    val subscriptions = CompositeSubscription()

    fun bind(calibratorActivity: CalibratorActivity) {
        stepSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            calibratorActivity.faceName.text = it.name;
        }.addTo(subscriptions)

        detectedColorSubject.observeOn(AndroidSchedulers.mainThread()).map {
            if (it != null) {
                android.graphics.Color.rgb(it.r.toInt(), it.g.toInt(), it.b.toInt())
            } else {
                null
            }
        }.subscribe {
            if (it != null) {
                calibratorActivity.confirmButton.isEnabled = true
                calibratorActivity.faceColor.setBackgroundColor(it)
            } else {
                calibratorActivity.confirmButton.isEnabled = false
                calibratorActivity.faceColor.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        }.addTo(subscriptions)

        calibratorActivity.confirmButton.setOnClickListener {
            val color = detectedColorSubject.value
            if (color != null) {
                if (colorCalibrator.colors.size == Color.values().size) {
                    colorProvider.setColors(colorCalibrator.colors)
                    colorCalibrator.reset()
                    calibratorActivity.finish()
                } else {
                    colorCalibrator.nextSide()
                    stepSubject.onNext(Color.values()[colorCalibrator.colors.size - 1])
                }
            }
        }

        colorCalibrator.listener = {
            detectedColorSubject.onNext(it)
        }
    }

    fun unbind() {
        subscriptions.unsubscribe()
    }
}