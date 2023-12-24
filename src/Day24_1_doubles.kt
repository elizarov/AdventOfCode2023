fun main() {
    val input = readInput("Day24")

    data class V3(val x: Long, val y: Long, val z: Long) {
        override fun toString(): String = "$x, $y, $z"
    }
    data class Stone(val p: V3, val v: V3) {
        override fun toString(): String = "$p @ $v"
    }

    fun String.toV3(): V3 = split(",")
        .map { it.trim().toLong() }
        .let { (x, y, z) -> V3(x, y, z) }

    val stones = input.map { s ->
        val (p, v) = s.split("@")
        Stone(p.toV3(), v.toV3())
    }
//    println(stones.size)

    data class Line(val a: Long, val b: Long, val c: Long)

    fun Stone.toLine(): Line {
        val a = v.y
        val b = -v.x
        val c = Math.multiplyExact(a, p.x) + Math.multiplyExact(b, p.y)
        return Line(a, b, c)
    }

    fun det(a: Long, b: Long, c: Long, d: Long): Double = a.toDouble() * d - b.toDouble() * c.toDouble()

//    val rMin = 7
//    val rMax = 27
    val rMin = 200000000000000
    val rMax = 400000000000000

    // n / d in rMin..rMax
    fun inRange(t: Double): Boolean = t >= rMin && t <= rMax

    fun isFuture(p: Long, v: Long, t: Double): Boolean = when {
        v > 0 -> t >= p
        v < 0 -> t <= p
        else -> error("!!!")
    }

    var cnt = 0
    for (i in stones.indices) for (j in i + 1..<stones.size) {
        val si = stones[i]
        val li = si.toLine()
        val sj = stones[j]
        val lj = sj.toLine()
        val d = det(li.a, li.b, lj.a, lj.b)
        if (d == 0.0) continue
        val x = det(li.c, li.b, lj.c, lj.b) / d
        val y = det(li.a, li.c, lj.a, lj.c) / d
        if (!inRange(x) || !inRange(y)) continue
        if (!isFuture(si.p.x, si.v.x, x) || !isFuture(si.p.y, si.v.y, y)) continue
        if (!isFuture(sj.p.x, sj.v.x, x) || !isFuture(sj.p.y, sj.v.y, y)) continue
        cnt++
    }
    println(cnt)
}