fun main() {
    val input = readInput("Day06")
    val ts = input[0].substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    val ds = input[1].substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
    var ans = 1L
    for (i in ts.indices) {
        val t = ts[i]
        val d = ds[i]
        var nw = 0
        for (x in 1L..<t) {
            val y = (t - x) * x
            if (y > d) nw++
        }
        ans *= nw
    }
    println(ans)
}