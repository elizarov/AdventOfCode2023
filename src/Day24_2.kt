import java.math.BigInteger
import kotlin.math.*

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

    fun Long.divSurely(that: Long): Long {
        check(this % that == 0L)
        return this / that
    }

    data class Range(val pi: LongRange, val v: Long)

    fun checkTimes(coord: String, ps: List<Long>, vs: List<Long>): Long {
        val n = ps.size
        check(vs.size == n)

        val minV = -1000L
        val maxV = 1000L
        val minP = 0L
        val maxP = 1_000_000_000_000_000L
        check(minV < vs.min() && maxV > vs.max())
        check(minP < ps.min() && maxP > ps.max())
        val vss = vs.zip(ps).groupBy { it.first }.mapValues { e -> e.value.map { it.second }.toSet() }
        val rs = ArrayList<Range>()

        tailrec fun gcd(x: BigInteger, y: BigInteger): BigInteger = if (y == BigInteger.ZERO) x else gcd(y, x % y)
        fun lcm(x: BigInteger, y: BigInteger) = x * y / gcd(x, y)
        fun BigInteger.floorDiv(d: BigInteger): BigInteger =
            if (this >= BigInteger.ZERO) divide(d) else -(-this + d - BigInteger.ONE).divide(d)

        fun Long.floorDiv(d: BigInteger): BigInteger = toBigInteger().floorDiv(d)
        fun Long.mod(d: BigInteger): BigInteger = toBigInteger().mod(d)
        fun modRoundUp(x: Long, m: BigInteger, r: BigInteger): BigInteger =
            (x.floorDiv(m) + if (x.mod(m) <= r) BigInteger.ZERO else BigInteger.ONE) * m + r

        fun modRoundDn(x: Long, m: BigInteger, r: BigInteger): BigInteger =
            (x.floorDiv(m) - if (x.mod(m) >= r) BigInteger.ZERO else BigInteger.ONE) * m + r

        vloop@ for (v in minV..maxV) {
            val p1 = vs.withIndex().filter { v < it.value }.maxOfOrNull { ps[it.index] } ?: minP
            val p2 = vs.withIndex().filter { v > it.value }.minOfOrNull { ps[it.index] } ?: maxP
            if (p1 > p2) continue
            var pmod = BigInteger.ONE
            var prem = BigInteger.ZERO
            var p1r = p1
            var p2r = p2
            for (i in 0..<n) {
                val pi = ps[i]
                val vi = vs[i]
                if (v == vi) {
                    val p0 = vss[v]?.singleOrNull() ?: continue@vloop
                    if (p0 !in p1r..p2r) continue@vloop
                    p1r = p0
                    p2r = p0
                    continue
                }
                // t_meet = (p - pi) / (vi - v)
                val d = abs(vi - v).toBigInteger()
                val r = pi.mod(d)
                val pmod2 = lcm(pmod, d)
                var prem2 = prem
                while (prem2 < pmod2) {
                    if (prem2.remainder(d) == r) break
                    prem2 += pmod
                    if (prem2 >= pmod2) continue@vloop
                    if (prem2 > p2r.toBigInteger()) continue@vloop
                }
                pmod = pmod2
                prem = prem2
                val p1n = modRoundUp(p1r, pmod, prem)
                val p2n = modRoundDn(p2r, pmod, prem)
                if (p1n > p2n) continue@vloop
                check(p1n >= p1r.toBigInteger())
                check(p2n <= p2r.toBigInteger())
                p1r = p1n.toLong()
                p2r = p2n.toLong()
            }
            println("$coord -> $p1r .. $p2r, v = $v")
            rs += Range(p1r..p2r, v)
        }
        println("$coord: ${rs.size} ranges")
        return rs.single().pi.first
    }

    val x = checkTimes("x", stones.map { it.p.x }, stones.map { it.v.x })
    val y = checkTimes("y", stones.map { it.p.y }, stones.map { it.v.y })
    val z = checkTimes("z", stones.map { it.p.z }, stones.map { it.v.z })

    println(x + y + z)
}