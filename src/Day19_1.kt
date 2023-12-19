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
    data class Part(val vs: Map<Char, Int>) {
        operator fun get(v: Char): Int = vs[v]!!
    }
    val ps = ArrayList<Part>()
    for (i in ii+1..<input.size) {
        val vs = HashMap<Char, Int>()
        for (s in input[i].removeSurrounding("{", "}").split(",")) {
            val v = s[0]
            check(s[1] == '=')
            val w = s.substring(2).toInt()
            vs[v] = w
        }
        ps += Part(vs)
    }
    var sum = 0L
    for (p in ps) {
        var cur = "in"
        while (cur != "A" && cur != "R") {
            val wf = wfs[cur]!!
            for (r in wf.rules) {
                val c = r.cond
                if (c != null) {
                    val pv = p[c.v]
                    val ok = when (c.op) {
                        '<' -> pv < c.w
                        '>' -> pv > c.w
                        else -> error("!")
                    }
                    if (!ok) continue
                }
                cur = r.dest
                break
            }
        }
        if (cur == "A") sum += p.vs.values.sum()
    }
    println(sum)
}