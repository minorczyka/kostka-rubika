package pl.polsl.kostkarubika.views.draw

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_draw.*
import pl.polsl.kostkarubika.KostkaRubikaApp
import pl.polsl.kostkarubika.R
import pl.polsl.kostkarubika.detector.ColorProvider
import javax.inject.Inject

class DrawActivity : AppCompatActivity() {

    @Inject lateinit var colorProvider: ColorProvider

    private lateinit var viewModel: DrawViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw)
        (application as KostkaRubikaApp).appComponent.inject(this)

        drawCubeView.setupPaints(colorProvider)
        val faces = intent.getStringArrayExtra(FACES).toList()
        viewModel = DrawViewModel(faces)
        viewModel.bind(this)
    }

    override fun onDestroy() {
        viewModel.unbind()
        super.onDestroy()
    }

    companion object {
        val FACES = "faces"
        val RESULT = "result"

        val REQUEST_CODE = 0
    }
}
