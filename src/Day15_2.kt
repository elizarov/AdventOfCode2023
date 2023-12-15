fun main() {
    val input = readInput("Day15")
    val ss = input.joinToString("").split(",")
    fun hash(s: String): Int {
        var h = 0
        for (c in s) {
            h = ((h + c.code) * 17) and 0xff
        }
        return h
    }
    data class Lens(val lbl: String, val foc: Int)
    val hm = Array(256) { ArrayList<Lens>() }
    for (s0 in ss) {
        val op = if (s0.contains('-')) '-' else '='
        val (lbl, focs) = s0.split(op)
        val h = hash(lbl)
        when (op) {
            '-' -> {
                val i = hm[h].indexOfFirst { it.lbl == lbl }
                if (i >= 0) hm[h].removeAt(i)
            }
            '=' -> {
                val foc = focs.toInt()
                val i = hm[h].indexOfFirst { it.lbl == lbl }
                if (i < 0) {
                    hm[h].add(Lens(lbl, foc))
                } else {
                    hm[h].removeAt(i)
                    hm[h].add(i, Lens(lbl, foc))
                }
            }
        }
    }
    val sum = hm.withIndex().sumOf { (i, list) ->
        var c = 0L
        for ((j, l) in list.withIndex()) {
            c += (i + 1) * (j + 1).toLong() * l.foc
        }
        c
    }
    println(sum)
}

