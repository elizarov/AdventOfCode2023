fun main() {
    val a = readInput("Day03").toCharArray2()
    val (n, m) = a.size2()
    val gc = Array(n) { IntArray(m) }
    val gr = Array(n) { LongArray(m) { 1 } }
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
            for (i1 in i - 1..i + 1) for (j1 in j0 - 1..j) {
                if (i1 in 0..<n && j1 in 0..<m) {
                    val c = a[i1][j1]
                    if (c == '*') {
                        gc[i1][j1]++
                        gr[i1][j1] *= num
                    }
                }
            }
        }
    }
    var sum = 0L
    gc.forEachIndexed2 { i, j, x -> if (x == 2) sum += gr[i][j] }
    println(sum)
}