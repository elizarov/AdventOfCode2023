fun main() {
    val a = readInput("Day18")
    data class Move(val k: Int, val d: Int)
    val mvs = ArrayList<Move>()
    val pts = ArrayList<P2>()
    val (di, dj) = RDLU_DIRS
    var ci = 0
    var cj = 0
    fun dig() { pts += P2(ci, cj) }
    dig()
    for (ss in a) {
        val (_, _, cc) = ss.split(" ")
        val hex = cc.removeSurrounding("(#", ")")
        val k = hex.dropLast(1).toInt(16)
        val d = hex.last() - '0'
        mvs += Move(k, d)
        ci += di[d] * k
        cj += dj[d] * k
        dig()
    }
    check(ci == 0 && cj == 0)
    val dis = pts.map { it.i }.distinct().sorted()
    val djs = pts.map { it.j }.distinct().sorted()
    data class SL(val x: Int, val k: Int)
    val stub = listOf(SL(Int.MIN_VALUE, 0), SL(Int.MAX_VALUE, 0))
    val ii = (stub + dis.map { SL(it, 1) } + dis.zipWithNext { a, b -> SL(a + 1, b - a - 1) }.filter { it.k > 0 }).sortedBy { it.x }
    val jj = (stub + djs.map { SL(it, 1) } + djs.zipWithNext { a, b -> SL(a + 1, b - a - 1) }.filter { it.k > 0 }).sortedBy { it.x }
    val n = ii.size
    val m = jj.size
    val f = Array(n) { IntArray(m) }
    ci = 0
    cj = 0
    var pi = ii.indexOfFirst { it.x == 0 }
    var pj = jj.indexOfFirst { it.x == 0 }
    for ((k, d) in mvs) {
        val ni = ci + di[d] * k
        val nj = cj + dj[d] * k
        while (ci != ni || cj != nj) {
            f[pi][pj] = -1
            pi += di[d]
            pj += dj[d]
            ci = ii[pi].x
            cj = jj[pj].x
        }
    }
    check(ci == 0 && cj == 0)
    val q = ArrayDeque<P2>()
    var cnt = 0
    fun enq(i: Int, j: Int) {
        if (i !in 0..<n || j !in 0..<m) return
        if (f[i][j] != 0) return
        f[i][j] = 1
        q += P2(i, j)
        cnt++
    }
    for (i in 0..<n) {
        enq(i, 0)
        enq(i, m - 1)
    }
    for (j in 0..<m) {
        enq(0, j)
        enq(n - 1, j)
    }
    while (!q.isEmpty()) {
        val (i, j) = q.removeFirst()
        for (d in 0..3) {
            enq(i + di[d], j + dj[d])
        }
    }
    var sum = 0L
    for (i in 0..<n) for (j in 0..<m) {
        if (f[i][j] <= 0) {
            sum += ii[i].k.toLong() * jj[j].k
        }
    }
    println(sum)
}