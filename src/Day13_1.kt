fun main() {
    val input = readInput("Day13")
    var sum = 0L
    input.parts { aa ->
        val a = aa.toCharArray2()
        val (m, n) = a.size2()
        for (k in 1..<n) {
            var ok = true
            for (i in 0..<m) for (j in 0..<minOf(n - k, k)) {
                if (a[i][k - j - 1] != a[i][j + k]) ok = false
            }
            if (ok) sum += k
        }
        for (k in 1..<m) {
            var ok = true
            for (i in 0..<minOf(m - k, k)) for (j in 0..<n) {
                if (a[k - i - 1][j] != a[i + k][j]) ok = false
            }
            if (ok) sum += 100 * k
        }
    }
    println(sum)
}