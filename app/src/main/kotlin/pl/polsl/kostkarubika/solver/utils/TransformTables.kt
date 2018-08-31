package pl.polsl.kostkarubika.solver.utils

import pl.polsl.kostkarubika.solver.CubeForm

import pl.polsl.kostkarubika.solver.enums.Color.B
import pl.polsl.kostkarubika.solver.enums.Color.D
import pl.polsl.kostkarubika.solver.enums.Color.F
import pl.polsl.kostkarubika.solver.enums.Color.L
import pl.polsl.kostkarubika.solver.enums.Color.R
import pl.polsl.kostkarubika.solver.enums.Color.U
import pl.polsl.kostkarubika.solver.enums.Corner.DBL
import pl.polsl.kostkarubika.solver.enums.Corner.DFR
import pl.polsl.kostkarubika.solver.enums.Corner.DLF
import pl.polsl.kostkarubika.solver.enums.Corner.DRB
import pl.polsl.kostkarubika.solver.enums.Corner.UBR
import pl.polsl.kostkarubika.solver.enums.Corner.UFL
import pl.polsl.kostkarubika.solver.enums.Corner.ULB
import pl.polsl.kostkarubika.solver.enums.Corner.URF
import pl.polsl.kostkarubika.solver.enums.Edge.BL
import pl.polsl.kostkarubika.solver.enums.Edge.BR
import pl.polsl.kostkarubika.solver.enums.Edge.DB
import pl.polsl.kostkarubika.solver.enums.Edge.DF
import pl.polsl.kostkarubika.solver.enums.Edge.DL
import pl.polsl.kostkarubika.solver.enums.Edge.DR
import pl.polsl.kostkarubika.solver.enums.Edge.FL
import pl.polsl.kostkarubika.solver.enums.Edge.FR
import pl.polsl.kostkarubika.solver.enums.Edge.UB
import pl.polsl.kostkarubika.solver.enums.Edge.UF
import pl.polsl.kostkarubika.solver.enums.Edge.UL
import pl.polsl.kostkarubika.solver.enums.Edge.UR
import pl.polsl.kostkarubika.solver.enums.Facelet.B1
import pl.polsl.kostkarubika.solver.enums.Facelet.B2
import pl.polsl.kostkarubika.solver.enums.Facelet.B3
import pl.polsl.kostkarubika.solver.enums.Facelet.B4
import pl.polsl.kostkarubika.solver.enums.Facelet.B6
import pl.polsl.kostkarubika.solver.enums.Facelet.B7
import pl.polsl.kostkarubika.solver.enums.Facelet.B8
import pl.polsl.kostkarubika.solver.enums.Facelet.B9
import pl.polsl.kostkarubika.solver.enums.Facelet.D1
import pl.polsl.kostkarubika.solver.enums.Facelet.D2
import pl.polsl.kostkarubika.solver.enums.Facelet.D3
import pl.polsl.kostkarubika.solver.enums.Facelet.D4
import pl.polsl.kostkarubika.solver.enums.Facelet.D6
import pl.polsl.kostkarubika.solver.enums.Facelet.D7
import pl.polsl.kostkarubika.solver.enums.Facelet.D8
import pl.polsl.kostkarubika.solver.enums.Facelet.D9
import pl.polsl.kostkarubika.solver.enums.Facelet.F1
import pl.polsl.kostkarubika.solver.enums.Facelet.F2
import pl.polsl.kostkarubika.solver.enums.Facelet.F3
import pl.polsl.kostkarubika.solver.enums.Facelet.F4
import pl.polsl.kostkarubika.solver.enums.Facelet.F6
import pl.polsl.kostkarubika.solver.enums.Facelet.F7
import pl.polsl.kostkarubika.solver.enums.Facelet.F8
import pl.polsl.kostkarubika.solver.enums.Facelet.F9
import pl.polsl.kostkarubika.solver.enums.Facelet.L1
import pl.polsl.kostkarubika.solver.enums.Facelet.L2
import pl.polsl.kostkarubika.solver.enums.Facelet.L3
import pl.polsl.kostkarubika.solver.enums.Facelet.L4
import pl.polsl.kostkarubika.solver.enums.Facelet.L6
import pl.polsl.kostkarubika.solver.enums.Facelet.L7
import pl.polsl.kostkarubika.solver.enums.Facelet.L8
import pl.polsl.kostkarubika.solver.enums.Facelet.L9
import pl.polsl.kostkarubika.solver.enums.Facelet.R1
import pl.polsl.kostkarubika.solver.enums.Facelet.R2
import pl.polsl.kostkarubika.solver.enums.Facelet.R3
import pl.polsl.kostkarubika.solver.enums.Facelet.R4
import pl.polsl.kostkarubika.solver.enums.Facelet.R6
import pl.polsl.kostkarubika.solver.enums.Facelet.R7
import pl.polsl.kostkarubika.solver.enums.Facelet.R8
import pl.polsl.kostkarubika.solver.enums.Facelet.R9
import pl.polsl.kostkarubika.solver.enums.Facelet.U1
import pl.polsl.kostkarubika.solver.enums.Facelet.U2
import pl.polsl.kostkarubika.solver.enums.Facelet.U3
import pl.polsl.kostkarubika.solver.enums.Facelet.U4
import pl.polsl.kostkarubika.solver.enums.Facelet.U6
import pl.polsl.kostkarubika.solver.enums.Facelet.U7
import pl.polsl.kostkarubika.solver.enums.Facelet.U8
import pl.polsl.kostkarubika.solver.enums.Facelet.U9

object TransformTables {

    val cornerFacelet = arrayOf(arrayOf(U9, R1, F3), arrayOf(U7, F1, L3), arrayOf(U1, L1, B3), arrayOf(U3, B1, R3), arrayOf(D3, F9, R7), arrayOf(D1, L9, F7), arrayOf(D7, B9, L7), arrayOf(D9, R9, B7))

    val edgeFacelet = arrayOf(arrayOf(U6, R2), arrayOf(U8, F2), arrayOf(U4, L2), arrayOf(U2, B2), arrayOf(D6, R8), arrayOf(D2, F8), arrayOf(D4, L8), arrayOf(D8, B8), arrayOf(F6, R4), arrayOf(F4, L6), arrayOf(B6, L4), arrayOf(B4, R6))

    val cornerColor = arrayOf(arrayOf(U, R, F), arrayOf(U, F, L), arrayOf(U, L, B), arrayOf(U, B, R), arrayOf(D, F, R), arrayOf(D, L, F), arrayOf(D, B, L), arrayOf(D, R, B))

    val edgeColor = arrayOf(arrayOf(U, R), arrayOf(U, F), arrayOf(U, L), arrayOf(U, B), arrayOf(D, R), arrayOf(D, F), arrayOf(D, L), arrayOf(D, B), arrayOf(F, R), arrayOf(F, L), arrayOf(B, L), arrayOf(B, R))

    var moveCube: Array<CubeForm>

    init {
        val cpU = arrayOf(UBR, URF, UFL, ULB, DFR, DLF, DBL, DRB)
        val coU = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val epU = arrayOf(UB, UR, UF, UL, DR, DF, DL, DB, FR, FL, BL, BR)
        val eoU = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        val cpR = arrayOf(DFR, UFL, ULB, URF, DRB, DLF, DBL, UBR)
        val coR = byteArrayOf(2, 0, 0, 1, 1, 0, 0, 2)
        val epR = arrayOf(FR, UF, UL, UB, BR, DF, DL, DB, DR, FL, BL, UR)
        val eoR = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        val cpF = arrayOf(UFL, DLF, ULB, UBR, URF, DFR, DBL, DRB)
        val coF = byteArrayOf(1, 2, 0, 0, 2, 1, 0, 0)
        val epF = arrayOf(UR, FL, UL, UB, DR, FR, DL, DB, UF, DF, BL, BR)
        val eoF = byteArrayOf(0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0)

        val cpD = arrayOf(URF, UFL, ULB, UBR, DLF, DBL, DRB, DFR)
        val coD = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        val epD = arrayOf(UR, UF, UL, UB, DF, DL, DB, DR, FR, FL, BL, BR)
        val eoD = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        val cpL = arrayOf(URF, ULB, DBL, UBR, DFR, UFL, DLF, DRB)
        val coL = byteArrayOf(0, 1, 2, 0, 0, 2, 1, 0)
        val epL = arrayOf(UR, UF, BL, UB, DR, DF, FL, DB, FR, UL, DL, BR)
        val eoL = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

        val cpB = arrayOf(URF, UFL, UBR, DRB, DFR, DLF, ULB, DBL)
        val coB = byteArrayOf(0, 0, 1, 2, 0, 0, 2, 1)
        val epB = arrayOf(UR, UF, UL, BR, DR, DF, DL, BL, FR, FL, UB, DB)
        val eoB = byteArrayOf(0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1)

        moveCube = arrayOf(CubeForm(cpU, coU, epU, eoU),
                CubeForm(cpR, coR, epR, eoR),
                CubeForm(cpF, coF, epF, eoF),
                CubeForm(cpD, coD, epD, eoD),
                CubeForm(cpL, coL, epL, eoL),
                CubeForm(cpB, coB, epB, eoB))
    }
}
