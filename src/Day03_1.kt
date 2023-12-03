fun main() {
    val a = readInput("Day03").toCharArray2()
    var sum = 0L
    val (n, m) = a.size2()
    for (i in 0..<n) {
        var j = 0
        while (j < m) {
            var d = a[i][j]
            if (d !in '0'..'9') {
                j++
                continue
            }
            val j0 = j
            var num = 0L
            while (d in '0'..'9') {
                num = num * 10 + (d - '0')
                j++
                if (j >= m) break
                d = a[i][j]
            }
            var ok = false
            for (i1 in i - 1..i + 1) for (j1 in j0 - 1..j) {
                if (i1 in 0..<n && j1 in 0..<m) {
                    val c = a[i1][j1]
                    if (c != '.' && c !in '0'..'9') ok = true
                }
            }
            if (ok) sum += num
        }
    }
    println(sum)
}