import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val input = readInput("Day22")
    data class P3(val x: Int, val y: Int, val z: Int)
    data class Brick(val a: P3, val b: P3)
    fun String.toP3() = split(",").map { it.toInt() }.let { (x, y, z) -> P3(x, y, z) }
    val bricks = input.map { s ->
        val (aa, bb) = s.split("~")
        var a = aa.toP3()
        var b = bb.toP3()
        if (a.z > b.z) { a = b.also { b = a } }
        Brick(a, b)
    }.sortedBy { it.a.z }
    val n = bricks.size
    val fz = Array(10) { IntArray(10 ) }
    val fi = Array(10) { IntArray(10 ) { -1 } }
    val sup = Array(n) { HashSet<Int>() }
    for ((ti, t) in bricks.withIndex()) {
        val dx = (t.b.x - t.a.x).sign
        val dy = (t.b.y - t.a.y).sign
        val lxy = abs(t.a.x - t.b.x) + abs(t.a.y - t.b.y)
        var sz = 0
        for (i in 0..lxy) {
            val x = t.a.x + i * dx
            val y = t.a.y + i * dy
            sz = maxOf(sz, fz[x][y])
        }
        for (i in 0..lxy) {
            val x = t.a.x + i * dx
            val y = t.a.y + i * dy
            if (sz == fz[x][y]) sup[ti] += fi[x][y]
        }
        val nz = sz + 1 + t.b.z - t.a.z
        for (i in 0..lxy) {
            val x = t.a.x + i * dx
            val y = t.a.y + i * dy
            fz[x][y] = nz
            fi[x][y] = ti
        }
    }
    var ans = 0
    for (ti in 0..<n) {
        val ds = HashSet<Int>()
        ds += ti
        do {
            var changes = false
            for (tj in 0..<n) if (tj !in ds && (sup[tj] - ds).isEmpty()) {
                ds += tj
                changes = true
            }
        } while (changes)
        ans += ds.size - 1
    }
    println(ans)
}