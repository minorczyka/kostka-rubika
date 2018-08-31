package pl.polsl.kostkarubika.views.solver

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import pl.polsl.kostkarubika.KostkaRubikaApp

import pl.polsl.kostkarubika.R
import pl.polsl.kostkarubika.di.components.CubeComponent
import pl.polsl.kostkarubika.di.components.DaggerCubeComponent
import pl.polsl.kostkarubika.di.modules.CubeModule
import pl.polsl.kostkarubika.di.modules.OpenGLModule
import pl.polsl.kostkarubika.di.modules.SolverModule
import pl.polsl.kostkarubika.solver.FaceForm
import javax.inject.Inject

class SolverActivity : AppCompatActivity() {

    lateinit var cubeComponent: CubeComponent

    @Inject lateinit var viewModel: SolverViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cubeComponent = DaggerCubeComponent.builder()
                .appComponent((application as KostkaRubikaApp).appComponent)
                .cubeModule(CubeModule(FaceForm(intent.getStringExtra(FACE_FORM))))
                .solverModule(SolverModule())
                .openGLModule(OpenGLModule())
                .build()
        cubeComponent.inject(this)

        setContentView(R.layout.activity_solver)
        viewModel.bind(this)
    }

    override fun onDestroy() {
        viewModel.unbind()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.solver_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.lbl -> {
                viewModel.solveWithLbl()
                return true
            }
            R.id.kociemba -> {
                viewModel.solveWithKociemba()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        val FACE_FORM = "face_form"
    }
}
