fun main() {
    val input = readInput("Day06")
    val t = input[0].substringAfter(":").filter { it != ' ' }.toCharArray().concatToString().toLong()
    val d = input[1].substringAfter(":").filter { it != ' ' }.toCharArray().concatToString().toLong()
    var nw = 0
    for (x in 1L..<t) {
        val y = (t - x) * x
        if (y > d) nw++
    }
    println(nw)
}