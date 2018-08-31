package pl.polsl.kostkarubika.solver

import pl.polsl.kostkarubika.solver.enums.Color
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Edge

import pl.polsl.kostkarubika.solver.utils.TransformTables.cornerFacelet
import pl.polsl.kostkarubika.solver.utils.TransformTables.cornerColor
import pl.polsl.kostkarubika.solver.utils.TransformTables.edgeFacelet
import pl.polsl.kostkarubika.solver.utils.TransformTables.edgeColor

class FaceForm constructor(str: String = "UUUUUUUUURRRRRRRRRFFFFFFFFFDDDDDDDDDLLLLLLLLLBBBBBBBBB") {

    val fields = Array<Color>(54, { i -> Color.valueOf(str.substring(i, i + 1)) })

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        for (field in fields) {
            stringBuilder.append(field.toString())
        }
        return stringBuilder.toString()
    }

    fun toCubeForm(): CubeForm {
        val cubeForm = CubeForm()
        for (i in cubeForm.cornerPermutation.indices) {
            cubeForm.cornerPermutation[i] = Corner.URF
        }
        for (i in cubeForm.edgePermutation.indices) {
            cubeForm.edgePermutation[i] = Edge.UR
        }

        for (i in Corner.values()) {
            var orientation: Int
            orientation = 0
            while (orientation < 3) {
                val field = fields[cornerFacelet[i.ordinal][orientation].ordinal]
                if (field == Color.U || field == Color.D) {
                    break
                }
                ++orientation
            }
            val color0 = fields[cornerFacelet[i.ordinal][orientation % 3].ordinal]
            val color1 = fields[cornerFacelet[i.ordinal][(orientation + 1) % 3].ordinal]
            val color2 = fields[cornerFacelet[i.ordinal][(orientation + 2) % 3].ordinal]
            for (j in Corner.values()) {
                if (color0 == cornerColor[j.ordinal][0] && color1 == cornerColor[j.ordinal][1] && color2 == cornerColor[j.ordinal][2]) {
                    cubeForm.cornerPermutation[i.ordinal] = j
                    cubeForm.cornerOrientation[i.ordinal] = (orientation % 3).toByte()
                    break
                }
            }
        }
        for (i in Edge.values()) {
            for (j in Edge.values()) {
                if (fields[edgeFacelet[i.ordinal][0].ordinal] == edgeColor[j.ordinal][0] && fields[edgeFacelet[i.ordinal][1].ordinal] == edgeColor[j.ordinal][1]) {
                    cubeForm.edgePermutation[i.ordinal] = j
                    cubeForm.edgeOrientation[i.ordinal] = 0
                    break
                }
                if (fields[edgeFacelet[i.ordinal][0].ordinal] == edgeColor[j.ordinal][1] && fields[edgeFacelet[i.ordinal][1].ordinal] == edgeColor[j.ordinal][0]) {
                    cubeForm.edgePermutation[i.ordinal] = j
                    cubeForm.edgeOrientation[i.ordinal] = 1
                    break
                }
            }
        }
        return cubeForm
    }

    fun getCornerColors(corner: Corner): Array<Color> {
        val color1 = fields[cornerFacelet[corner.ordinal][0].ordinal]
        val color2 = fields[cornerFacelet[corner.ordinal][1].ordinal]
        val color3 = fields[cornerFacelet[corner.ordinal][2].ordinal]
        return arrayOf<Color>(color1, color2, color3)
    }

    fun getEdgeColors(edge: Edge): Array<Color> {
        val color1 = fields[edgeFacelet[edge.ordinal][0].ordinal]
        val color2 = fields[edgeFacelet[edge.ordinal][1].ordinal]
        return arrayOf<Color>(color1, color2)
    }
}
