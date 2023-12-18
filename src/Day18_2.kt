import kotlin.math.abs

fun main() {
    val input = readInput("Day18")
    val (di, dj) = RDLU_DIRS
    var ci = 0
    var cj = 0
    var sa = 0L
    var sk = 0L
    for (ss in input) {
        val (_, _, cc) = ss.split(" ")
        val hex = cc.removeSurrounding("(#", ")")
        val k = hex.dropLast(1).toInt(16)
        val d = hex.last() - '0'
        val ni = ci + di[d] * k
        val nj = cj + dj[d] * k
        sa += (nj - cj).toLong() * (ni + ci)
        sk += k
        ci = ni
        cj = nj
    }
    println(abs(sa) / 2 + sk / 2 + 1)
}