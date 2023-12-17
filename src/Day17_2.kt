fun main() {
    val a = readInput("Day17").toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    data class Pos(val i: Int, val j: Int, val d: Int, val k: Int)
    val heap = Heap<Pos, Int>()
    val done = HashSet<Pos>()
    fun enq(i: Int, j: Int, d: Int, k: Int, c: Int) {
        val p = Pos(i, j, d, k)
        if (p in done) return
        heap.putBetter(p, c)
    }
    enq(0, 0, 0, 0, 0)
    while (true) {
        val (p, c) = heap.removeMin()
        done += p
        if (p.i == n - 1 && p.j == m - 1) {
            println(c)
            break
        }
        for (dd in -1..1) {
            when (dd) {
                0 -> if (p.k == 10) continue
                else -> if (p.k < 4) continue
            }
            val d1 = (p.d + dd).mod(4)
            val k1 = if (dd == 0) p.k + 1 else 1
            val i1 = p.i + di[d1]
            val j1 = p.j + dj[d1]
            if (i1 in 0..<n && j1 in 0..<m) {
                enq(i1, j1, d1, k1, c + (a[i1][j1] - '0'))
            }
        }
    }
}