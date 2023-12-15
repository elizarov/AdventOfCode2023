fun main() {
    val input = readInput("Day15")
    val ss = input.joinToString("").split(",")
    val sum = ss.sumOf { s ->
        var h = 0
        for (c in s) {
            h = ((h + c.code) * 17) and 0xff
        }
        h
    }
    println(sum)
}