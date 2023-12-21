import kotlin.math.abs

fun main() {
    val a = readInput("Day21").toCharArray2()
    val (n, m) = a.size2()
    val limitFull = 26501365
    val limitRep = limitFull / m
    val limitRem = limitFull.mod(m)
    println("$n x $m")
    println("$limitRep repeats")
    println("$limitRem remainder")
    check(limitRep % 2 == 0)
    check(limitRem * 2 + 1 == m)
    val (di, dj) = RDLU_DIRS
    for (i0 in 0..<n) for (j0 in 0..<m) if (a[i0][j0] == 'S') {
        var q0 = ArrayDeque<P2>()
        var q1 = ArrayDeque<P2>()
        val u = HashSet<P2>()
        fun enq(i: Int, j: Int) {
            if (a[i.mod(n)][j.mod(m)] == '#') return
            val p = P2(i, j)
            if (p in u) return
            u += p
            q1.add(P2(i, j))
        }
        enq(i0, j0)

        fun sumDiamond(ii: Int, jj: Int, sh: Int): Long {
            var sum = 0L
            val i1 = i0 + ii * m + sh * limitRem
            val j1 = j0 + jj * m + sh * limitRem
            when (sh) {
                0 -> for (i in i1 - limitRem..i1 + limitRem + limitRem) {
                    val w = limitRem - abs(i - i1)
                    for (j in j1 - w..j1 + w) if (P2(i, j) in u) sum++
                }
                1 -> for (i in i1 - limitRem + 1..i1 + limitRem) {
                    val w = if (i <= i1) limitRem - (i1 - i) else limitRem - (i - i1 - 1)
                    for (j in j1 - w + 1..j1 + w) if (P2(i, j) in u) sum++
                }
            }
            return sum
        }

        val rc = Array(2) { LongArray(2) }
        fun computed(dw: Int): Long {
            var sum = 0L
            for (i in -dw..dw) {
                val w = dw - abs(i)
                val t0 = (abs(i) + w + dw) % 2
                sum += rc[t0][0] * (w + 1) + rc[1 - t0][0] * w
            }
            for (i in -dw..dw - 1) {
                val w = if (i < 0) dw + i + 1 else dw - i
                sum += rc[0][1] * w + rc[1][1] * w
            }
            return sum
        }

        var cur = 0
        while (true) {
            if (q0.isEmpty()) {
                q0 = q1.also { q1 = q0 }
                if (cur % m == limitRem) {
                    val rep = cur / m
                    for (sh in 0..1) for (ii in -1..0) {
                        val mod = (ii + rep).mod(2)
                        if (rc[mod][sh] == 0L) rc[mod][sh] = sumDiamond(ii, 0, sh) else check(rc[mod][sh] == sumDiamond(ii, 0, sh))
                    }
                    val all = q0.size.toLong()
                    val computed = computed(rep)
                    println("repeat $rep: all = $all; computed = $computed")
                    check(all == computed)
                    var td = 0L
                    for (ii in -rep..rep) {
                        for (jj in -rep..rep) {
                            val sd = sumDiamond(ii, jj, 0)
                            td += sd
                            print(sd.toString().padStart(8))
                        }
                        println()
                        print("    ")
                        for (jj in -rep..rep) {
                            val sd = sumDiamond(ii, jj, 1)
                            td += sd
                            print(sd.toString().padStart(8))
                        }
                        println()
                    }
                    println("  td = $td")
                    check(all == td)
                    if (rep == 2) {
                        println("ANSWER = ${computed(limitRep)}")
                        return
                    }
                }
                u.clear()
                cur++
            }
            val (i, j) = q0.removeFirst()
            for (d in 0..3) enq(i + di[d], j + dj[d])
        }
    }
}
