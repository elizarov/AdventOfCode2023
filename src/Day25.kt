fun main() {
    val input = readInput("Day25")
    val gs = HashMap<String, HashSet<String>>()
    for (ss in input) {
        val b = ss.split(":", " ").map { it.trim() }.filter { it.isNotEmpty() }
        val u = b[0]
        for (v in b.drop(1)) {
            gs.getOrPut(u) { HashSet() } += v
            gs.getOrPut(v) { HashSet() } += u
        }
    }

    val i2s = gs.keys.toList()
    val s2i = i2s.withIndex().associateBy({ it.value }, { it.index })
    val g = Array(i2s.size) { i -> gs[i2s[i]]!!.map { s2i[it]!! }.toIntArray() }

    val wires = g.withIndex().flatMap { (v, l) ->
        l.map { listOf(v, it).sorted() }
    }.distinct().map { it.toIntArray() }

    val n = g.size
    val m = wires.size
    println("size = $n")
    println("wires = $m")

    val bridges = ArrayList<List<Int>>()
    val used = BooleanArray(n)
    val tin = IntArray(n)
    val fup = IntArray(n)

    val total = m * (m + 1) / 2
    var checked = 0
    var prevPercent = 0
    for (ai in wires.indices) {
        val (a0, a1) = wires[ai]
        for (bi in ai + 1..<m) {
            val percent = (checked++) * 100 / total
            if (percent != prevPercent) {
                println("Checked $percent%")
                prevPercent = percent
            }
            val (b0, b1) = wires[bi]
            used.fill(false)
            tin.fill(0)
            fup.fill(0)
            var timer = 0

            fun dfs(v: Int, p: Int) {
                used[v] = true
                val t = timer++
                tin[v] = t
                fup[v] = t
                for (u in g[v]) {
                    if (u == p) continue
                    if (u == a0 && v == a1 || u == a1 && v == a0) continue
                    if (u == b0 && v == b1 || u == b1 && v == b0) continue
                    if (used[u]) {
                        fup[v] = minOf(fup[v], tin[u])
                    } else {
                        dfs(u, v)
                        fup[v] = minOf(fup[v], fup[u])
                        if (fup[u] > tin[v]) {
                            bridges += listOf(v, u)
                        }
                    }
                }
            }

            dfs(0, -1)
            check(used.all { it })

            check(bridges.size <= 1) { "bridges = $bridges" }
            if (bridges.size == 1) {
                val (c0, c1) = bridges[0]
                println("${i2s[a0]}/${i2s[a1]} ${i2s[b0]}/${i2s[b1]} ${i2s[c0]}/${i2s[c1]}")

                used.fill(false)
                fun dfs2(v: Int) {
                    used[v] = true
                    for (u in g[v]) {
                        if (used[u]) continue
                        if (u == a0 && v == a1 || u == a1 && v == a0) continue
                        if (u == b0 && v == b1 || u == b1 && v == b0) continue
                        if (u == c0 && v == c1 || u == c1 && v == c0) continue
                        dfs2(u)
                    }
                }
                dfs2(0)
                val n1 = used.count { it }
                val n2 = n - n1
                println("n1 = $n1")
                println("n2 = $n2")
                println(n1 * n2)
                return
            }
        }
    }
}