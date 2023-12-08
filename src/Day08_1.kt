fun main() {
    val a = readInput("Day08")
    val rl = a[0]
    val m = a.drop(2).map { s ->
        val (p, qq) = s.split("=").map { it.trim() }
        val (ql, qr) = qq.removeSurrounding("(", ")").split(",").map { it.trim() }
        Pair(p, Pair(ql, qr))
    }.toMap()
    var s = "AAA"
    var i = 0
    while (s != "ZZZ") {
        val q = m[s]!!
        val z = rl[i % rl.length]
        s = when (z) {
            'L' -> q.first
            'R' -> q.second
            else -> error("!")
        }
        i++
    }
    println(i)
}