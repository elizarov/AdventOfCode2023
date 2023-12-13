fun main() {
    val input = readInput("Day13")
    fun find(a: CharArray2): Set<Int> {
        val s = HashSet<Int>()
        val (m, n) = a.size2()
        for (k in 1..<n) {
            var ok = true
            for (i in 0..<m) for (j in 0..<minOf(n - k, k)) {
                if (a[i][k - j - 1] != a[i][j + k]) ok = false
            }
            if (ok) s += k
        }
        for (k in 1..<m) {
            var ok = true
            for (i in 0..<minOf(m - k, k)) for (j in 0..<n) {
                if (a[k - i - 1][j] != a[i + k][j]) ok = false
            }
            if (ok) s += -k
        }
        return s
    }
    var sum = 0L
    input.parts { aa ->
        val a = aa.toCharArray2()
        val (m, n) = a.size2()
        val s0 = find(a)
        search@for (i in 0..<m) for (j in 0..<n) {
            val c = a[i][j]
            a[i][j] = when(c) {
                '.' -> '#'
                else -> '.'
            }
            val s = find(a) - s0
            if (s.isNotEmpty()) {
                for (k in s) {
                    if (k > 0) sum += k else sum += 100 * -k
                }
                break@search
            }
            a[i][j] = c
        }
    }
    println(sum)
}