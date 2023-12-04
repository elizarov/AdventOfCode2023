fun main() {
    val a = readInput("Day04")
    val sum = a.sumOf { s  ->
        val (w, m) = s.substringAfter(":").trim().split("|").map {
            it.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        }
        val cnt = m.count { w.contains(it) }
        if (cnt >= 1) 1 shl (cnt - 1) else 0
    }
    println(sum)
}
