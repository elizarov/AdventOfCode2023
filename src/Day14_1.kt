fun main() {
    val a = readInput("Day14").toCharArray2()
    val (m, n) = a.size2()
    for (j in 0..<n) {
        for (i in 0..<m) if (a[i][j] == 'O') {
            var k = i
            while (k > 0 && a[k - 1][j] == '.') k--
            a[i][j] = '.'
            a[k][j] = 'O'
        }
    }
    var sum = 0
    for (i in 0..<m) for (j in 0..<n) if (a[i][j] == 'O') {
        sum += m - i
    }
    println(sum)

}