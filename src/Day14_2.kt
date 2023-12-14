fun main() {
    val a = readInput("Day14").toCharArray2()
    val (m, n) = a.size2()
    class Data(val a: CharArray2) {
        override fun equals(other: Any?): Boolean = other is Data && a.contentDeepEquals(other.a)
        override fun hashCode(): Int = a.contentDeepHashCode()
        override fun toString(): String = a.joinToString("\n") { it.concatToString() }
    }
    val d2i = HashMap<Data, Int>()
    val i2d = HashMap<Int, Data>()
    var cnt = 0
    val target = 1000000000
    while (true) {
        val d = Data(Array(m) { i -> a[i].copyOf() })
        val start = d2i.put(d, cnt)
        if (start != null) {
            val sz = cnt - start
            val q = i2d[start + (target - start) % sz]!!
            for (i in 0..<m) for (j in 0..<n) a[i][j] = q.a[i][j]
            break
        }
        i2d[cnt] = d
        cnt++
        // north
        for (j in 0..<n) {
            for (i in 0..<m) if (a[i][j] == 'O') {
                var k = i
                while (k > 0 && a[k - 1][j] == '.') k--
                a[i][j] = '.'
                a[k][j] = 'O'
            }
        }
        // west
        for (i in 0..<m) {
            for (j in 0..<n) {
                if (a[i][j] == 'O') {
                    var k = j
                    while (k > 0 && a[i][k - 1] == '.') k--
                    a[i][j] = '.'
                    a[i][k] = 'O'
                }
            }
        }
        // south
        for (j in 0..<n) {
            for (i in m - 1 downTo 0) if (a[i][j] == 'O') {
                var k = i
                while (k < m - 1 && a[k + 1][j] == '.') k++
                a[i][j] = '.'
                a[k][j] = 'O'
            }
        }
        // eest
        for (i in 0..<m) {
            for (j in n - 1 downTo 0) {
                if (a[i][j] == 'O') {
                    var k = j
                    while (k < n - 1 && a[i][k + 1] == '.') k++
                    a[i][j] = '.'
                    a[i][k] = 'O'
                }
            }
        }
    }
    var sum = 0
    for (i in 0..<m) for (j in 0..<n) if (a[i][j] == 'O') {
        sum += m - i
    }
    println(sum)

}