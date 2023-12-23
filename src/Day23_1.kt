fun main() {
    val a = readInput("Day23").toCharArray2()
    val (n, m) = a.size2()
    val (di, dj) = RDLU_DIRS
    val dc = ">v<^".toCharArray()
    val g0 = HashMap<P2, ArrayList<P2>>()
    for (i in 0..<n) for (j in 0..<m) {
        if (a[i][j] == '#') continue
        for (d in 0..3) {
            if (a[i][j] != '.' && a[i][j] != dc[d]) continue
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
    val gr = HashMap<P2, ArrayList<E2>>()
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
            gr.getOrPut(pc) { ArrayList() }.add(E2(p0, w))
        }
    }
    fun complete(g: HashMap<P2, ArrayList<E2>>) {
        val all = g.values.flatten().map { it.v }.distinct()
        for (p in all) g.getOrPut(p) { ArrayList() }
    }
    complete(g)
    complete(gr)
    println("gsize = ${g.size}")

    val used1 = HashSet<P2>()
    val order = ArrayList<P2>()
    fun dfs1(p: P2) {
        if (p in used1) return
        used1 += p
        for (e in g[p]!!) dfs1(e.v)
        order += p
    }
    for (p in g.keys) dfs1(p)
    val p2c = HashMap<P2, Int>()
    var componentCnt = 0
    fun dfs2(p: P2) {
        if (p in p2c) return
        p2c[p] = componentCnt
        for (e in gr[p]!!) dfs2(e.v)
    }
    for (p in order.reversed()) {
        if (p in p2c) continue
        componentCnt++
        dfs2(p)
    }
    println("componentCnt = $componentCnt")
    val dp = HashMap<P2, Int>()
    val target = P2(n - 1, m - 2)
    fun answer(p0: P2): Int {
        dp[p0]?.let { return it }
        val c0 = p2c[p0]!!
        val u = HashSet<P2>()
        fun find(p: P2): Int {
            if (p == target) return 0
            u += p
            var res = -1
            for (e in g[p]!!) if (e.v !in u) {
                val next = if (p2c[e.v] == c0) find(e.v) else answer(e.v)
                if (next < 0) continue
                res = maxOf(res, next + e.w)
            }
            u -= p
            return res
        }
        return find(p0).also { dp[p0] = it }
    }
    println(answer(P2(0, 1)))
}