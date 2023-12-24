fun main() {
    val a = readInput("Day24").map { s -> s.split("@").map { vs -> vs.split(",").map { it.trim().toLong() } } }
    val tm = (0..2).firstNotNullOf { k ->
        a.indices.firstNotNullOfOrNull { i ->
            a.indices.map { j ->
                if (a[j][1][k] == a[i][1][k]) (if (a[j][0][k] == a[i][0][k]) 0L else -1L) else {
                    val tn = a[j][0][k] - a[i][0][k]
                    val td = a[i][1][k] - a[j][1][k]
                    if (tn % td == 0L) tn / td else -1L
                }
            }.takeIf { tm -> tm.all { it >= 0 } }
        }
    }
    val (i, j) = tm.withIndex().filter { it.value > 0 }.map { it.index }
    fun p(i: Int, k: Int, t: Long) = a[i][0][k] + a[i][1][k] * t
    println((0..2).sumOf { k -> p(i, k, tm[i]) - (p(i, k, tm[i]) - p(j, k, tm[j])) / (tm[i] - tm[j]) * tm[i] })
}