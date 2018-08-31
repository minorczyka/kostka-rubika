package pl.polsl.kostkarubika.views.solver

import android.util.Log
import kotlinx.android.synthetic.main.activity_solver.*
import pl.polsl.kostkarubika.R
import pl.polsl.kostkarubika.graphics.rubik.RubikCube
import pl.polsl.kostkarubika.solver.FaceForm
import pl.polsl.kostkarubika.solver.Solution
import pl.polsl.kostkarubika.solver.algorithms.Solver
import pl.polsl.kostkarubika.solver.algorithms.kociemba.KociembaAlgorithmSolver
import pl.polsl.kostkarubika.solver.algorithms.lbl.LayerByLayerSolver
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.addTo
import rx.schedulers.Schedulers
import rx.subjects.BehaviorSubject
import rx.subscriptions.CompositeSubscription
import kotlin.concurrent.withLock

class SolverViewModel(
        val faceForm: FaceForm,
        val kociembaSolver: KociembaAlgorithmSolver,
        val lblSolver: LayerByLayerSolver,
        val rubikCube: RubikCube) {

    val solutionSubject: BehaviorSubject<Solution?> = BehaviorSubject.create()
    val stepSubject: BehaviorSubject<Int> = BehaviorSubject.create(0)
    val playSubject: BehaviorSubject<Boolean> = BehaviorSubject.create(false)

    val subscriptions = CompositeSubscription()
    var solutionSubscription: Subscription? = null

    init {
        rubikCube.moveProvider = f@ {
            if (!playSubject.value) {
                return@f null
            }
            val solution = solutionSubject.value ?: return@f null
            val step = stepSubject.value
            if (step < solution.moveList.size) {
                stepSubject.onNext(step + 1)
                solution.moveList[step]
            } else {
                playSubject.onNext(false)
                null
            }
        }
    }

    fun bind(solverActivity: SolverActivity) {
        playSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            val resId = if (it) R.drawable.ic_pause_white else R.drawable.ic_play_arrow_white
            solverActivity.playPause.setImageResource(resId)
        }.addTo(subscriptions)

        Observable.combineLatest(stepSubject, solutionSubject, { step, solution ->
            if (solution != null) {
                val phase = solution.phases.indexOfLast{ it <= step }
                "$step/${solution.moveList.size} (${phase + 1}/${solution.phases.size})"
            } else {
                solverActivity.getString(R.string.solving)
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe {
            solverActivity.stepDescription.text = it
        }.addTo(subscriptions)

        solverActivity.skipToBeginning.setOnClickListener { skipToBeginning() }
        solverActivity.prevPhase.setOnClickListener { prevPhase() }
        solverActivity.prevMove.setOnClickListener { prevMove() }
        solverActivity.playPause.setOnClickListener { playPause() }
        solverActivity.nextMove.setOnClickListener { nextMove() }
        solverActivity.nextPhase.setOnClickListener { nextPhase() }
        solverActivity.skipToEnd.setOnClickListener { skipToEnd() }
    }

    fun unbind() {
        solutionSubscription?.unsubscribe()
        subscriptions.unsubscribe()
    }

    fun solveWithKociemba() {
        solveCube(kociembaSolver)
    }

    fun solveWithLbl() {
        solveCube(lblSolver)
    }

    private fun solveCube(solver: Solver) {
        solutionSubscription?.unsubscribe()
        skipToBeginning()
        solutionSubject.onNext(null)
        solutionSubscription = Observable.defer {
            Log.e("RUBIK", faceForm.toString())
            Log.e("RUBIK", "Pre processing...")
            solver.prepare()
            Log.e("RUBIK", "Getting solutionSubject...")
            val startTime = System.currentTimeMillis()
            val solution = solver.getSolution(faceForm)
            Log.e("RUBIK", "Solution complete in ${System.currentTimeMillis() - startTime}ms!")
            Observable.just(solution)
        }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe { sol ->
            solutionSubject.onNext(sol)
            stepSubject.onNext(0)
            playSubject.onNext(false)
        }
    }

    fun playPause() {
        if (playSubject.value) {
            playSubject.onNext(false)
        } else {
            val solution = solutionSubject.value
            if (solution != null && stepSubject.value < solution.moveList.size) {
                playSubject.onNext(true)
            }
        }
    }

    fun nextMove() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            val step = stepSubject.value
            if (step < solution.moveList.size) {
                stepSubject.onNext(step + 1)
                rubikCube.animateMove(solution.moveList[step])
            }
        }
    }

    fun prevMove() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            val step = stepSubject.value
            if (step > 0) {
                stepSubject.onNext(step - 1)
                rubikCube.animateMove(solution.moveList[step - 1].counterMove)
            }
        }
    }

    fun nextPhase() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            var step = stepSubject.value
            val targetStep = solution.phases.firstOrNull { it > step } ?: solution.moveList.size
            while (step < solution.moveList.size && step < targetStep) {
                rubikCube.makeMove(solution.moveList[step++])
            }
            stepSubject.onNext(targetStep)
        }
    }

    fun prevPhase() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            var step = stepSubject.value
            val targetStep = solution.phases.lastOrNull { it < step } ?: return
            while (step > 0 && step > targetStep) {
                rubikCube.makeMove(solution.moveList[--step].counterMove)
            }
            stepSubject.onNext(targetStep)
        }
    }

    fun skipToEnd() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            val step = stepSubject.value
            val moves = solution.moveList.copyOfRange(step, solution.moveList.size)
            moves.forEach { rubikCube.makeMove(it) }
            stepSubject.onNext(solution.moveList.size)
        }
    }

    fun skipToBeginning() {
        rubikCube.lock.withLock {
            playSubject.onNext(false)
            val solution = solutionSubject.value ?: return
            val step = stepSubject.value
            val moves = solution.moveList.copyOfRange(0, step).reversed()
            moves.forEach { rubikCube.makeMove(it.counterMove) }
            stepSubject.onNext(0)
        }
    }
}