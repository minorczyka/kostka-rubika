package pl.polsl.kostkarubika.solver

import pl.polsl.kostkarubika.solver.enums.Move

class Solution(val moveList: Array<Move>, val phases: Array<Int>) {

    override fun toString(): String {
        return toString(false)
    }

    fun toString(useSeparator: Boolean): String {
        val res = StringBuilder()
        var phase = if (useSeparator) 0 else phases.size
        for (i in moveList.indices) {
            var s = ""
            when (moveList[i]) {
                Move.U1, Move.U2, Move.U3 -> s += "U"
                Move.R1, Move.R2, Move.R3 -> s += "R"
                Move.F1, Move.F2, Move.F3 -> s += "F"
                Move.D1, Move.D2, Move.D3 -> s += "D"
                Move.L1, Move.L2, Move.L3 -> s += "L"
                Move.B1, Move.B2, Move.B3 -> s += "B"
            }
            when (moveList[i]) {
                Move.U1, Move.R1, Move.F1, Move.D1, Move.L1, Move.B1 -> s += " "
                Move.U2, Move.R2, Move.F2, Move.D2, Move.L2, Move.B2 -> s += "2 "
                Move.U3, Move.R3, Move.F3, Move.D3, Move.L3, Move.B3 -> s += "' "
            }
            res.append(s)
            if (phase < phases.size && i == phases[phase] - 1) {
                res.append(". ")
                ++phase
            }
        }
        return res.toString()
    }
}
