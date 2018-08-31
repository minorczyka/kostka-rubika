package pl.polsl.kostkarubika.solver.algorithms.kociemba

data class CubeState(
        var flip: Int = 0,
        var twist: Int = 0,
        var slice: Int = 0,
        var urfToDlf: Int = 0,
        var frToBr: Int = 0,
        var parity: Int = 0,
        var urToUl: Int = 0,
        var ubToDf: Int = 0,
        var urToDf: Int = 0)
