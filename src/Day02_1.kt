fun main() {
    val input = readInput("Day02")
    val colors = listOf("red", "green", "blue")
    val bag = listOf(12, 13, 14)
    var sum = 0
    var id = 0
    for (s in input) {
        id++
        val ss = s.substringAfter("Game $id: ").split(";")
        var ok = true
        for (t in ss) {
            val tt = t.trim().split(",")
            for (cc in tt) {
                val (ns, cs) = cc.trim().split(" ")
                val n = ns.toInt()
                val c = colors.indexOf(cs)
                if (n > bag[c]) ok = false
            }
        }
        if (ok) sum += id
    }
    println(sum)
}