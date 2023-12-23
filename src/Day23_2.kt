fun main() {
    val a = readInput("Day23").toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    val g0 = HashMap<P2, ArrayList<P2>>()
    for (i in 0..<n) for (j in 0..<m) {
        if (a[i][j] == '#') continue
        for (d in 0..3) {
            val i1 = i + di[d]
            val j1 = j + dj[d]
            if (i1 !in 0..<n || j1 !in 0..<m || a[i1][j1] == '#') continue
            val p = P2(i, j)
            val p1 = P2(i1, j1)
            g0.getOrPut(p) { ArrayList() }.add(p1)
        }
    }
    data class E2(val v: P2, val w: Int)

    val g = HashMap<P2, ArrayList<E2>>()
    for ((p0, l0) in g0) if (l0.size != 2) {
        for (p1 in l0) {
            var pp = p0
            var pc = p1
            var w = 1
            while (true) {
                val lc = (g0[pc]?.toSet() ?: emptySet()) - setOf(pp)
                if (lc.size != 1) break
                pp = pc
                pc = lc.single()
                w++
            }
            g.getOrPut(p0) { ArrayList() }.add(E2(pc, w))
        }
    }

    val target = P2(n - 1, m - 2)
    val u = HashSet<P2>()
    fun find(p: P2): Int {
        if (p == target) return 0
        u += p
        var res = -1
        for (e in g[p]!!) if (e.v !in u) {
            val next = find(e.v)
            if (next < 0) continue
            res = maxOf(res, next + e.w)
        }
        u -= p
        return res
    }
    println(find(P2(0, 1)))
}