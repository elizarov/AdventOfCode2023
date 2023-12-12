fun main() {
    val input = readInput("Day12")
    val rep = 5
    val sum = input.sumOf { ss ->
        val (s0, gs) = ss.split(" ")
        val g0 = gs.split(",").map { it.toInt() }

        val s = Array(rep) { s0 }.joinToString("?")
        val g = buildList { repeat(rep) { addAll(g0) } }.toIntArray()

        val dp = Array(s.length) {
            Array(g.size + 1) { j -> LongArray(g.getOrElse(j) { 0 } + 1) { -1 } }
        }

        fun cnt(s: String, g: IntArray, i: Int, j: Int, u: Int): Long {
            if (i >= s.length) return if (j >= g.size || j == g.size - 1 && u == g[j]) 1 else 0
            if (dp[i][j][u] >= 0) return dp[i][j][u]
            val c = s[i]
            var res = 0L
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
            dp[i][j][u] = res
            return res
        }
        cnt(s, g, 0, 0, 0)
    }
    println(sum)
}