typealias IntArray2 = Array<IntArray>

fun IntArray2.size2(): P2 {
    val n = size
    val m = get(0).size
    for (i in 1..<n) require(get(i).size == m) { "Row $i has size ${get(i)}, but expected $m" }
    return P2(n, m)
}

inline fun IntArray2.forEachIndexed2(action: (i: Int, j: Int, x: Int) -> Unit) {
    for (i in indices) {
        val b = get(i)
        for (j in b.indices) {
            action(i, j, b[j])
        }
    }
}
