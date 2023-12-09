fun main() {
    val a = readInput("Day09")
    val sum = a.sumOf { s ->
        var b = s.split(" ").map { it.toLong() }
        val z = ArrayList<Long>()
        while (!b.all { it == 0L }) {
            z.add(b.last())
            val c = b.windowed(2) { it[1] - it[0] }
            b = c
        }
        z.sum()
    }
    println(sum)
}