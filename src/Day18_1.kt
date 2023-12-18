fun main() {
    val a = readInput("Day18")
    val pts = ArrayList<P2>()
    val (di, dj) = RDLU_DIRS
    val dn = "RDLU"
    var ci = 0
    var cj = 0
    fun dig() { pts += P2(ci, cj) }
    dig()
    for (ss in a) {
        val (ds, ns) = ss.split(" ")
        val k = ns.toInt()
        val d = dn.indexOf(ds[0])
        repeat(k) {
            ci += di[d]
            cj += dj[d]
            dig()
        }
    }
    val minI = pts.minOf { it.i }
    val minJ = pts.minOf { it.j }
    val maxI = pts.maxOf { it.i }
    val maxJ = pts.maxOf { it.j }
    val n = maxI - minI + 3
    val m = maxJ - minJ + 3
    val f = Array(n) { IntArray(m) }
    for ((i, j) in pts) {
        f[i - minI + 1][j - minJ + 1] = -1
    }
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
    println(n * m - cnt)
}