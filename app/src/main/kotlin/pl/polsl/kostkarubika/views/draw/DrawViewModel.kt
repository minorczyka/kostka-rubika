package pl.polsl.kostkarubika.views.draw

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_draw.*
import pl.polsl.kostkarubika.R
import pl.polsl.kostkarubika.detector.CubeDetector
import pl.polsl.kostkarubika.detector.DetectionError
import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.utils.Left
import pl.polsl.kostkarubika.solver.utils.Right
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription

class DrawViewModel(val faces: List<String>) {

    val rotationsSubject: BehaviorSubject<List<Int>> = BehaviorSubject.create(listOf(0))

    val subscriptions = CompositeSubscription()

    fun bind(drawActivity: DrawActivity) {
        rotationsSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            drawActivity.drawCubeView.sides = when(it.size) {
                1 -> listOf(Color.B, Color.R, Color.F, Color.L)
                2 -> listOf(Color.U, Color.B, Color.D, Color.F)
                3 -> listOf(Color.U, Color.R, Color.D, Color.L)
                4 -> listOf(Color.F, Color.R, Color.B, Color.L)
                5 -> listOf(Color.U, Color.F, Color.D, Color.B)
                else -> listOf(Color.U, Color.L, Color.D, Color.R)
            }
            val face = CubeDetector.rotateFace(faces[it.size - 1], it.last())
            drawActivity.drawCubeView.face = face.map {
                Color.valueOf(it.toString())
            }
            drawActivity.drawCubeView.invalidate()
        }.addTo(subscriptions)

        drawActivity.prevButton.setOnClickListener {
            val list = rotationsSubject.value.toMutableList()
            list[list.lastIndex] = (list[list.lastIndex] + 1) % 4
            rotationsSubject.onNext(list)
        }
        drawActivity.nextButton.setOnClickListener {
            val list = rotationsSubject.value.toMutableList()
            list[list.lastIndex] = if (list.last() >= 0) list[list.lastIndex] - 1 else 3
            rotationsSubject.onNext(list)
        }
        drawActivity.cancelButton.setOnClickListener {
            drawActivity.finish()
        }
        drawActivity.okButton.setOnClickListener {
            val list = rotationsSubject.value.toMutableList()
            Observable.defer {
                val detection = when (list.size) {
                    1 -> CubeDetector.toFaceForm(faces, list[0])
                    2 -> CubeDetector.toFaceForm(faces, list[0], list[1])
                    3 -> CubeDetector.toFaceForm(faces, list[0], list[1], list[2])
                    4 -> CubeDetector.toFaceForm(faces, list[0], list[1], list[2], list[3])
                    5 -> CubeDetector.toFaceForm(faces, list[0], list[1], list[2], list[3], list[4])
                    6 -> CubeDetector.toFaceForm(faces, list[0], list[1], list[2], list[3], list[4], list[5])
                    else -> CubeDetector.toFaceForm(faces)
                }
                Observable.just(detection)
            }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe { detection ->
                when (detection) {
                    is Left -> {
                        if (detection.value == DetectionError.FOUND_MORE) {
                            list.add(0)
                            rotationsSubject.onNext(list)
                        } else {
                            AlertDialog.Builder(drawActivity)
                                    .setTitle(R.string.detector_dialog_title)
                                    .setMessage(R.string.detector_dialog_error)
                                    .setPositiveButton(R.string.ok, { dialog, i -> dialog.dismiss() })
                                    .setOnDismissListener { drawActivity.finish() }
                                    .create()
                                    .show()
                        }
                    }
                    is Right -> {
                        val intent = Intent()
                        intent.putExtra(DrawActivity.RESULT, detection.value.toString())
                        drawActivity.setResult(Activity.RESULT_OK, intent)
                        drawActivity.finish()
                    }
                }
            }.addTo(subscriptions)
        }

        drawActivity.drawCubeView.face = listOf(Color.U, Color.R, Color.D, Color.L, Color.U, Color.R, Color.D, Color.L, Color.U)
        drawActivity.drawCubeView.sides = listOf(Color.U, Color.R, Color.D, Color.L)
        drawActivity.drawCubeView.invalidate()
    }

    fun unbind() {
        subscriptions.unsubscribe()
    }
}