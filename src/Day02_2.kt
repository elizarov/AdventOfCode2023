fun main() {
    val input = readInput("Day02")
    val colors = listOf("red", "green", "blue")
    var sum = 0L
    var id = 0
    for (s in input) {
        id++
        val ss = s.substringAfter("Game $id: ").split(";")
        val m = IntArray(3)
        for (t in ss) {
            val tt = t.trim().split(",")
            for (cc in tt) {
                val (ns, cs) = cc.trim().split(" ")
                val n = ns.toInt()
                val c = colors.indexOf(cs)
                m[c] = maxOf(m[c], n)
            }
        }
        sum += m[0].toLong() * m[1] * m[2]
    }
    println(sum)
}