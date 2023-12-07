fun main() {
    val input = readInput("Day01")
    val sum = input.sumOf { s ->
        val d = s.filter { it in '0'..'9' }
        "${d.first()}${d.last()}".toLong()
    }
    println(sum)
}