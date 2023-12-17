fun main() {
    val a = readInput("Day17").toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    data class Pos(val i: Int, val j: Int, val d: Int, val k: Int)
    val v = HashMap<Pos, Int>()
    val q = HashSet<Pos>()
    fun enq(i: Int, j: Int, d: Int, k: Int, c: Int) {
        val p = Pos(i, j, d, k)
        val cp = v[p]
        if (cp != null && c >= cp) return
        v[p] = c
        q.add(p)
    }
    enq(0, 0, 0, 0, 0)
    while (true) {
        var p: Pos = q.first()
        var c = v[p]!!
        for (pp in q) {
            val cc = v[pp]!!
            if (cc < c) {
                c = cc
                p = pp
            }
        }
        q.remove(p)
        if (p.i == n - 1 && p.j == m - 1) {
            println(c)
            break
        }
        for (dd in -1..1) {
            if (dd == 0 && p.k == 3) continue
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