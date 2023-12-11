fun main() {
    val a = readInput("Day11").toCharArray2()
    val (m, n) = a.size2()
    val factor = 1000000
    val er = IntArray(m) { i -> if ((0..<m).all { j -> a[i][j] == '.'}) factor else 1 }
    val ec = IntArray(n) { j -> if ((0..<n).all { i -> a[i][j] == '.'}) factor else 1 }
    val g = ArrayList<P2>()
    for (i in 0..<m) for (j in 0..<n) if (a[i][j] == '#') g.add(P2(i, j))
    val cr = IntArray(m)
    val cc = IntArray(n)
    fun f(c: IntArray, a: Int, b: Int) {
        for (i in minOf(a, b)..<maxOf(a, b)) c[i]++
    }
    for (p in g.indices) for (q in p + 1..<g.size) {
        f(cr, g[p].i, g[q].i)
        f(cc, g[p].j, g[q].j)
    }
    var sum = 0L
    for (i in 0..<m) sum += cr[i].toLong() * er[i]
    for (j in 0..<n) sum += cc[j].toLong() * ec[j]
    println(sum)
}