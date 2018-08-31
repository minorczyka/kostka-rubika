package pl.polsl.kostkarubika.solver.utils

val ANY = 1.0f

fun CnK(n: Int, k: Int): Int {
    var k = k
    var i: Int
    var j: Int
    var s: Int
    if (n < k) {
        return 0
    }
    if (k > n / 2) {
        k = n - k
    }
    s = 1
    i = n
    j = 1
    while (i != n - k) {
        s *= i
        s /= j
        --i
        ++j
    }
    return s
}

private val EPS = 0.001f

fun equalFloat(a: Float, b: Float): Boolean {
    return a - b >= -EPS && a - b <= EPS
}
