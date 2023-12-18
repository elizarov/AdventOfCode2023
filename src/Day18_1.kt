import kotlin.math.abs

fun main() {
    val input = readInput("Day18")
    val (di, dj) = RDLU_DIRS
    val dn = "RDLU"
    var ci = 0
    var cj = 0
    var sa = 0
    var sk = 0
    for (ss in input) {
        val (ds, ns) = ss.split(" ")
        val k = ns.toInt()
        val d = dn.indexOf(ds[0])
        val ni = ci + di[d] * k
        val nj = cj + dj[d] * k
        sa += (nj - cj) * (ni + ci)
        sk += k
        ci = ni
        cj = nj
    }
    println(abs(sa) / 2 + sk / 2 + 1)
}