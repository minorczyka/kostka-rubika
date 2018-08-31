package pl.polsl.kostkarubika.solver.enums

enum class MovePower {
    ONE, TWO, THREE;

    val twist: Int
        get() {
            return when (this) {
                ONE -> 1
                TWO -> 2
                THREE -> 3
            }
        }
}