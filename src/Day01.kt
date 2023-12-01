fun main() {
    val input = readInput("Day01")
    val digits = listOf("", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val sum = input.sumOf { s ->
        val d = buildString {
            for (i in s.indices) when (s[i]) {
                in '0'..'9' -> append(s[i])
                else -> {
                    val ss = s.substring(i)
                    for (j in 1..9) if (ss.startsWith(digits[j])) {
                        append('0' + j)
                    }
                }
            }
        }
        "${d.first()}${d.last()}".toLong()
    }
    println(sum)
}