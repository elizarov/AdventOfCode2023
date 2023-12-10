fun main() {
    val a = readInput("Day10").toCharArray2()
    val (n, m) = a.size2()
    // RDLU
    val di = intArrayOf(0, 1, 0, -1)
    val dj = intArrayOf(1, 0, -1, 0)
    val db = intArrayOf(2, 3, 0, 1)
    val ps = "|-LJ7F".toCharArray()
    val cs = arrayOf(
        "0101", "1010", "1001", "0011", "0110", "1100"
    ).map { it.reversed().toInt(2) }
    main@for (i0 in 0..<n) for (j0 in 0..<m) if (a[i0][j0] == 'S') {
        var i = i0
        var j = j0
        var cc = 15
        var pi = -1
        var pj = -1
        val loop = ArrayList<P2>()
        loop@while (true) {
            loop += P2(i, j)
            var found = false
            for (d in 0..3) if (((1 shl d) and cc) != 0){
                val i1 = i + di[d]
                val j1 = j + dj[d]
                if (i1 !in 0..<n || j1 !in 0..<m) continue
                if (i1 == pi && j1 == pj) continue
                if (i1 == i0 && j1 == j0) break@loop
                val k = ps.indexOf(a[i1][j1])
                if (k < 0) continue
                val cn = cs[k]
                if (((1 shl db[d]) and cn) == 0) continue
                pi = i
                pj = j
                i = i1
                j = j1
                cc = cn
                found = true
                break
            }
            check(found) { "($i,$j) -> ${a[i][j]}" }
        }
        val b = Array(n) { IntArray(m) { -1 } }
        for (k in loop.indices) {
            val (i, j) = loop[k]
            b[i][j] = k
        }
        var cnt = 0
        for (i in 0..<n) {
            var inside = false
            var enter = ' '
            for (j in 0..<m) {
                val k = b[i][j]
                if (k >= 0) {
                    val (pi, pj) = loop[(k - 1).mod(loop.size)]
                    val (ni, nj) = loop[(k + 1).mod(loop.size)]
                    val up = ni < i || pi < i
                    val dn = ni > i || pi > i
                    val lt = nj < j || pj < j
                    val rt = nj > j || pj > j
                    when {
                        up && dn -> inside = !inside
                        lt && rt -> {}
                        dn && rt -> enter = 'D'
                        dn && lt -> if (enter == 'U') inside = !inside
                        up && rt -> enter = 'U'
                        up && lt -> if (enter == 'D') inside = !inside
                        else -> error("!!!")
                    }
                } else {
                    if (inside) cnt++
                }
            }

        }
        println(cnt)
        break@main
    }
}