fun main() {
    val input = readInput("Day19")
    val ii = input.indexOf("")
    data class Cond(val v: Char, val op: Char, val w: Int)
    data class Rule(val cond: Cond?, val dest: String)
    data class WF(val rules: List<Rule>)
    val wfs = HashMap<String, WF>()
    for (i in 0..<ii) {
        val s = input[i]
        val oi = s.indexOf("{")
        val name = s.substring(0, oi)
        val rules = s.substring(oi).removeSurrounding("{", "}").split(",").map { rs ->
            val ci = rs.indexOf(":")
            val cond = if (ci >= 0) {
                val v = rs[0]
                val op = rs[1]
                val w = rs.substring(2, ci).toInt()
                Cond(v, op, w)
            } else null
            val dest = rs.substring(ci + 1)
            Rule(cond, dest)
        }
        wfs[name] = WF(rules)
    }
    fun compute(cur: String, rm0: Map<Char, IntRange>): Long {
        if (cur == "R") return 0
        var res = 0L
        if (cur == "A") {
            res = 1
            for (r in rm0.values) res *= r.last - r.first + 1
            return res
        }
        val wf = wfs[cur]!!
        val rm = rm0.toMutableMap()
        for (r in wf.rules) {
            val c = r.cond
            if (c == null) {
                res += compute(r.dest, rm)
                break
            } else {
                val cr = rm[c.v]!!
                val (rt, rf) = when (c.op) {
                    '<' -> cr.first..minOf(cr.last, c.w - 1) to maxOf(cr.first, c.w)..cr.last
                    '>' -> maxOf(cr.first, c.w + 1)..cr.last to cr.first..minOf(cr.last, c.w)
                    else -> error("!")
                }
                if (!rt.isEmpty()) {
                    val rm1 = rm.toMutableMap()
                    rm1[c.v] = rt
                    res += compute(r.dest, rm1)
                }
                if (rf.isEmpty()) break
                rm[c.v] = rf
            }
        }
        return res
    }
    val rm = HashMap<Char, IntRange>()
    for (v in "xmas") rm[v] = 1..4000
    println(compute("in", rm))
}