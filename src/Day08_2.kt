fun main() {
    val a = readInput("Day08")
    val rl = a[0]
    val m = a.drop(2).map { s ->
        val (p, qq) = s.split("=").map { it.trim() }
        val (ql, qr) = qq.removeSurrounding("(", ")").split(",").map { it.trim() }
        Pair(p, Pair(ql, qr))
    }.toMap()
    data class P(val j: Int, val s: String)
    data class Loop(val f: Int, val len: Int, val z: Int)
    val loops = m.keys.filter { it.endsWith("A") }.map { p0 ->
        var i = 0
        var s = p0
        val seen = HashMap<P, Int>()
        val zs = ArrayList<Int>()
        while (true) {
            val j = i % rl.length
            val p = P(j, s)
            if (seen.containsKey(p)) break
            seen[p] = i
            val z = rl[j]
            val q = m[s]!!
            s = when (z) {
                'L' -> q.first
                'R' -> q.second
                else -> error("!")
            }
            i++
            if (s.endsWith('Z')) zs.add(i)
        }
        val f = seen[P(i % rl.length, s)]!!
        // empirical checks: single & and loops at Z
        val z = zs.single()
        check(z >= f)
        check(i - f == z)
        Loop(f, i - f, z)
    }
    tailrec fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)
    fun lcm(x: Long, y: Long) = x * y / gcd(x, y)
    var ans = 1L
    for (l in loops) ans = lcm(ans, l.len.toLong())
    println(ans)
}