fun main() {
    val a = readInput("Day05")
    val seeds = a[0].substringAfter(":").trim().split(" ").map { it.toLong() }
    data class Range(val dst: Long, val src: Long, val len: Long)
    data class Dst(val name: String, val ranges: List<Range>)
    val map = HashMap<String, Dst>()
    a.drop(2).parts { p ->
        val (sn, dn) = Regex("(\\w+)-to-(\\w+) map:").matchEntire(p[0])!!.destructured
        val ranges = p.drop(1).map { s ->
            val (dst, src, len) = s.split(" ").map { it.toLong() }
            Range(dst, src, len)
        }
        map[sn] = Dst(dn, ranges)
    }
    fun convert(cn: String, x: Long): Pair<String, Long>? {
        val (dn, ranges) = map[cn] ?: return null
        for (r in ranges) {
            if (x in r.src..<r.src + r.len)
                return dn to x - r.src + r.dst
        }
        return dn to x
    }
    val result = seeds.minOf { seed ->
        var cn = "seed"
        var x = seed
        while (true) {
            val r = convert(cn, x) ?: break
            cn = r.first
            x = r.second
        }
        check(cn == "location")
        x
    }
    println(result)
}