fun main() {
    val input = readInput("Day12")
    fun cnt(s: String, g: IntArray, i: Int, j: Int, u: Int): Int {
        if (i >= s.length) return if (j >= g.size || j == g.size - 1 && u == g[j]) 1 else 0
        val c = s[i]
        var res = 0
        if (c == '.' || c == '?') {
            if (u > 0 && g[j] == u) {
                res += cnt(s, g, i + 1, j + 1, 0)
            } else if (u == 0) {
                res += cnt(s, g, i + 1, j, 0)
            }
        }
        if (c == '#' || c == '?') {
            if (j < g.size && u < g[j]) {
                res += cnt(s, g, i + 1, j, u + 1)
            }
        }
        return res
    }
    val sum = input.sumOf { ss ->
        val (s, gs) = ss.split(" ")
        val g = gs.split(",").map { it.toInt() }.toIntArray()
        cnt(s, g, 0, 0, 0)
    }
    println(sum)
}