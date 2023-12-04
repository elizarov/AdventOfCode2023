fun main() {
    val a = readInput("Day04")
    val c = IntArray(a.size)
    c.fill(1)
    val sum = a.withIndex().sumOf { (i, s)  ->
        val (w, m) = s.substringAfter(":").trim().split("|").map {
            it.trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        }
        val cnt = m.count { w.contains(it) }
        for (j in i + 1..i + cnt) c[j] += c[i]
        c[i]
    }
    println(sum)
}