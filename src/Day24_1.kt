import java.math.BigDecimal

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

    data class Line(val a: BigDecimal, val b: BigDecimal, val c: BigDecimal)

    fun Stone.toLine(): Line {
        val a = v.y.toBigDecimal()
        val b = (-v.x).toBigDecimal()
        val c = a * p.x.toBigDecimal() + b * p.y.toBigDecimal()
        return Line(a, b, c)
    }

    fun det(a: BigDecimal, b: BigDecimal, c: BigDecimal, d: BigDecimal): BigDecimal = a * d - b * c

//    val rMin = 7.toBigDecimal()
//    val rMax = 27.toBigDecimal()
    val rMin = 200000000000000.toBigDecimal()
    val rMax = 400000000000000.toBigDecimal()

    // n / d in rMin..rMax
    fun inRange(n: BigDecimal, d: BigDecimal): Boolean = when {
        d > BigDecimal.ZERO -> n in (rMin * d)..(rMax * d)
        d < BigDecimal.ZERO -> n in (rMax * d)..(rMin * d)
        else -> error("!!!")
    }

    fun isFuture(p: Long, v: Long, n: BigDecimal, d: BigDecimal): Boolean = when {
        v > 0 -> if (d > BigDecimal.ZERO) n > d * p.toBigDecimal() else n < d * p.toBigDecimal()
        v < 0 -> if (d > BigDecimal.ZERO) n < d * p.toBigDecimal() else n > d * p.toBigDecimal()
        else -> error("!!!")
    }

    var cnt = 0
    for (i in stones.indices) for (j in i + 1..<stones.size) {
        val si = stones[i]
        val li = si.toLine()
        val sj = stones[j]
        val lj = sj.toLine()
        val d = det(li.a, li.b, lj.a, lj.b)
        if (d == BigDecimal.ZERO) {
            if (li.a * sj.p.x.toBigDecimal() + li.b * sj.p.y.toBigDecimal() == li.c) {
                error(" !!! same")
            }
            continue
        }
        val nx = det(li.c, li.b, lj.c, lj.b) // x = nx / d
        val ny = det(li.a, li.c, lj.a, lj.c) // y = ny / d
        if (!inRange(nx, d) || !inRange(ny, d)) continue
        if (!isFuture(si.p.x, si.v.x, nx, d) || !isFuture(si.p.y, si.v.y, ny, d)) continue
        if (!isFuture(sj.p.x, sj.v.x, nx, d) || !isFuture(sj.p.y, sj.v.y, ny, d)) continue
//        println("intersection: ${nx.toDouble() / d.toDouble()}, ${ny.toDouble() / d.toDouble()}")
//        println(" A: $si")
//        println(" B: $sj")
        cnt++
    }
    println(cnt)
}