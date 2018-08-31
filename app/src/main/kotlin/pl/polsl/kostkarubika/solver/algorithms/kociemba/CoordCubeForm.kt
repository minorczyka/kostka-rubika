package pl.polsl.kostkarubika.solver.algorithms.kociemba

import pl.polsl.kostkarubika.solver.CubeForm
import pl.polsl.kostkarubika.solver.enums.Corner
import pl.polsl.kostkarubika.solver.enums.Edge

import pl.polsl.kostkarubika.solver.enums.Corner.*
import pl.polsl.kostkarubika.solver.enums.Edge.*
import pl.polsl.kostkarubika.solver.utils.CnK
import pl.polsl.kostkarubika.solver.utils.rotateLeft
import pl.polsl.kostkarubika.solver.utils.rotateRight

class CoordCubeForm : CubeForm {

    constructor() : super() {
    }

    constructor(cubeForm: CubeForm) : super(cubeForm) {
    }

    var twist: Short
        get() {
            var ret: Short = 0
            for (i in URF.ordinal..DRB.ordinal - 1) {
                ret = (3 * ret + cornerOrientation[i]).toShort()
            }
            return ret
        }
        set(twist) {
            var t = twist
            var twistParity = 0
            for (i in DRB.ordinal - 1 downTo URF.ordinal) {
                cornerOrientation[i] = (t % 3).toByte()
                twistParity += cornerOrientation[i].toInt()
                t = (t / 3).toShort()
            }
            cornerOrientation[DRB.ordinal] = ((3 - twistParity % 3) % 3).toByte()
        }

    var flip: Short
        get() {
            var ret: Short = 0
            for (i in UR.ordinal..BR.ordinal - 1) {
                ret = (2 * ret + edgeOrientation[i]).toShort()
            }
            return ret
        }
        set(flip) {
            var f = flip
            var flipParity = 0
            for (i in BR.ordinal - 1 downTo UR.ordinal) {
                edgeOrientation[i] = (f % 2).toByte()
                flipParity += edgeOrientation[i].toInt()
                f = (f / 2).toShort()
            }
            edgeOrientation[BR.ordinal] = ((2 - flipParity % 2) % 2).toByte()
        }

    var fRtoBR: Short
        get() {
            var a = 0
            var x = 0
            val edge4 = Array(4, { UR })
            for (j in BR.ordinal downTo UR.ordinal) {
                if (FR.ordinal <= edgePermutation[j].ordinal && edgePermutation[j].ordinal <= BR.ordinal) {
                    a += CnK(11 - j, x + 1)
                    edge4[3 - x++] = edgePermutation[j]
                }
            }
            var b = 0
            for (j in 3 downTo 1) {
                var k = 0
                while (edge4[j].ordinal != j + 8) {
                    rotateLeft(edge4, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return (24 * a + b).toShort()
        }
        set(idx) {
            var x: Int
            val sliceEdge = arrayOf(FR, FL, BL, BR)
            val otherEdge = arrayOf(UR, UF, UL, UB, DR, DF, DL, DB)
            var b = idx % 24
            var a = idx / 24
            for (e in Edge.values()) {
                edgePermutation[e.ordinal] = DB
            }

            run {
                var j = 1
                var k: Int
                while (j < 4) {
                    k = b % (j + 1)
                    b /= j + 1
                    while (k-- > 0) {
                        rotateRight(sliceEdge, 0, j)
                    }
                    j++
                }
            }

            x = 3
            for (j in UR.ordinal..BR.ordinal) {
                if (a - CnK(11 - j, x + 1) >= 0) {
                    edgePermutation[j] = sliceEdge[3 - x]
                    a -= CnK(11 - j, x-- + 1)
                }
            }
            x = 0
            for (j in UR.ordinal..BR.ordinal) {
                if (edgePermutation[j] === DB) {
                    edgePermutation[j] = otherEdge[x++]
                }
            }
        }

    var urFtoDLF: Short
        get() {
            var a = 0
            var x = 0
            val corner6 = Array(6, { URF })
            for (j in URF.ordinal..DRB.ordinal) {
                if (cornerPermutation[j].ordinal <= DLF.ordinal) {
                    a += CnK(j, x + 1)
                    corner6[x++] = cornerPermutation[j]
                }
            }

            var b = 0
            for (j in 5 downTo 1) {
                var k = 0
                while (corner6[j].ordinal != j) {
                    rotateLeft(corner6, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return (720 * a + b).toShort()
        }
        set(idx) {
            var x: Int
            val corner6 = arrayOf(URF, UFL, ULB, UBR, DFR, DLF)
            val otherCorner = arrayOf(DBL, DRB)
            var b = idx % 720
            var a = idx / 720
            for (c in Corner.values()) {
                cornerPermutation[c.ordinal] = DRB
            }

            run {
                var j = 1
                var k: Int
                while (j < 6) {
                    k = b % (j + 1)
                    b /= j + 1
                    while (k-- > 0) {
                        rotateRight(corner6, 0, j)
                    }
                    j++
                }
            }
            x = 5
            for (j in DRB.ordinal downTo 0) {
                if (a - CnK(j, x + 1) >= 0) {
                    cornerPermutation[j] = corner6[x]
                    a -= CnK(j, x-- + 1)
                }
            }
            x = 0
            for (j in URF.ordinal..DRB.ordinal) {
                if (cornerPermutation[j] == DRB) {
                    cornerPermutation[j] = otherCorner[x++]
                }
            }
        }

    var uRtoDF: Int
        get() {
            var a = 0
            var x = 0
            val edge6 = Array(6, { UR })
            for (j in UR.ordinal..BR.ordinal) {
                if (edgePermutation[j].ordinal <= DF.ordinal) {
                    a += CnK(j, x + 1)
                    edge6[x++] = edgePermutation[j]
                }
            }

            var b = 0
            for (j in 5 downTo 1) {
                var k = 0
                while (edge6[j].ordinal != j) {
                    rotateLeft(edge6, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return 720 * a + b
        }
        set(idx) {
            var x: Int
            val edge6 = arrayOf(UR, UF, UL, UB, DR, DF)
            val otherEdge = arrayOf(DL, DB, FR, FL, BL, BR)
            var b = idx % 720
            var a = idx / 720
            for (e in Edge.values()) {
                edgePermutation[e.ordinal] = BR
            }
            run {
                var j = 1
                var k: Int
                while (j < 6) {
                    k = b % (j + 1)
                    b /= j + 1
                    while (k-- > 0) {
                        rotateRight(edge6, 0, j)
                    }
                    j++
                }
            }
            x = 5
            for (j in BR.ordinal downTo 0) {
                if (a - CnK(j, x + 1) >= 0) {
                    edgePermutation[j] = edge6[x]
                    a -= CnK(j, x-- + 1)
                }
            }
            x = 0
            for (j in UR.ordinal..BR.ordinal) {
                if (edgePermutation[j] === BR) {
                    edgePermutation[j] = otherEdge[x++]
                }
            }
        }

    var uRtoUL: Short
        get() {
            var a = 0
            var x = 0
            val edge3 = Array(3, { UR })
            for (j in UR.ordinal..BR.ordinal) {
                if (edgePermutation[j].ordinal <= UL.ordinal) {
                    a += CnK(j, x + 1)
                    edge3[x++] = edgePermutation[j]
                }
            }

            var b = 0
            for (j in 2 downTo 1) {
                var k = 0
                while (edge3[j].ordinal != j) {
                    rotateLeft(edge3, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return (6 * a + b).toShort()
        }
        set(idx) {
            var x: Int
            val edge3 = arrayOf(UR, UF, UL)
            var b = idx % 6
            var a = idx / 6
            for (e in Edge.values()) {
                edgePermutation[e.ordinal] = BR
            }

            run {
                var j = 1
                var k: Int
                while (j < 3) {
                    k = b % (j + 1)
                    b /= j + 1
                    while (k-- > 0) {
                        rotateRight(edge3, 0, j)
                    }
                    j++
                }
            }
            x = 2
            for (j in BR.ordinal downTo 0) {
                if (a - CnK(j, x + 1) >= 0) {
                    edgePermutation[j] = edge3[x]
                    a -= CnK(j, x-- + 1)
                }
            }
        }

    var uBtoDF: Short
        get() {
            var a = 0
            var x = 0
            val edge3 = Array(3, { UR })
            for (j in UR.ordinal..BR.ordinal) {
                if (UB.ordinal <= edgePermutation[j].ordinal && edgePermutation[j].ordinal <= DF.ordinal) {
                    a += CnK(j, x + 1)
                    edge3[x++] = edgePermutation[j]
                }
            }

            var b = 0
            for (j in 2 downTo 1) {
                var k = 0
                while (edge3[j].ordinal != UB.ordinal + j) {
                    rotateLeft(edge3, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return (6 * a + b).toShort()
        }
        set(idx) {
            var x: Int
            val edge3 = arrayOf(UB, DR, DF)
            var b = idx % 6
            var a = idx / 6
            for (e in Edge.values()) {
                edgePermutation[e.ordinal] = BR
            }

            run {
                var j = 1
                var k: Int
                while (j < 3) {
                    k = b % (j + 1)
                    b /= j + 1
                    while (k-- > 0) {
                        rotateRight(edge3, 0, j)
                    }
                    j++
                }
            }
            x = 2
            for (j in BR.ordinal downTo 0) {
                if (a - CnK(j, x + 1) >= 0) {
                    edgePermutation[j] = edge3[x]
                    a -= CnK(j, x-- + 1)
                }
            }
        }

    var urFtoDLB: Int
        get() {
            val perm = Array(8, { URF })
            var b = 0
            for (i in 0..7) {
                perm[i] = cornerPermutation[i]
            }
            for (j in 7 downTo 1) {
                var k = 0
                while (perm[j].ordinal != j) {
                    rotateLeft(perm, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return b
        }
        set(idx) {
            var i = idx
            val perm = arrayOf(URF, UFL, ULB, UBR, DFR, DLF, DBL, DRB)
            var k: Int
            for (j in 1..7) {
                k = i % (j + 1)
                i /= j + 1
                while (k-- > 0) {
                    rotateRight(perm, 0, j)
                }
            }
            var x = 7
            for (j in 7 downTo 0) {
                cornerPermutation[j] = perm[x--]
            }
        }

    var uRtoBR: Int
        get() {
            val perm = Array(12, { UR })
            var b = 0
            for (i in 0..11) {
                perm[i] = edgePermutation[i]
            }
            for (j in 11 downTo 1) {
                var k = 0
                while (perm[j].ordinal != j) {
                    rotateLeft(perm, 0, j)
                    k++
                }
                b = (j + 1) * b + k
            }
            return b
        }
        set(idx) {
            var i = idx
            val perm = arrayOf(UR, UF, UL, UB, DR, DF, DL, DB, FR, FL, BL, BR)
            var k: Int
            for (j in 1..11) {
                k = i % (j + 1)
                i /= j + 1
                while (k-- > 0) {
                    rotateRight(perm, 0, j)
                }
            }
            var x = 11
            for (j in 11 downTo 0) {
                edgePermutation[j] = perm[x--]
            }
        }
}
