fun main() {
    val a = readInput("Day16").toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    val m1 = intArrayOf(1, 0, 3, 2)
    val m2 = intArrayOf(3, 2, 1, 0)
    val v = Array(n) { Array(m) { BooleanArray(4) } }
    data class Q(val i: Int, val j: Int, val d: Int)
    val q = ArrayDeque<Q>()
    fun enq(i0: Int, j0: Int, d: Int) {
        val i = i0 + di[d]
        val j = j0 + dj[d]
        if (i !in 0..<n || j !in 0..<m) return
        if (v[i][j][d]) return
        v[i][j][d] = true
        q += Q(i, j, d)
    }
    enq(0, -1, 0)
    while (!q.isEmpty()) {
        val (i, j, d) = q.removeFirst()
        when (a[i][j]) {
            '\\' -> enq(i, j, m1[d])
            '/' -> enq(i, j, m2[d])
            '|' -> {
                if (d == 0 || d == 2) {
                    enq(i, j, 1)
                    enq(i, j, 3)
                } else {
                    enq(i, j, d)
                }
            }
            '-' -> {
                if (d == 1 || d == 3) {
                    enq(i, j, 0)
                    enq(i, j, 2)
                } else {
                    enq(i, j, d)
                }
            }
            else -> enq(i, j, d)
        }
    }
    var cnt = 0
    for (i in 0..<n) for (j in 0..<m) if (v[i][j].any { it }) cnt++
    println(cnt)
}