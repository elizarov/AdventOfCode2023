fun main() {
    val input = readInput("Day20")
    data class Mod(val t: Char, val dest: List<String>)
    val name2mod = HashMap<String, Mod>()
    val broadcaster = "broadcaster"
    for (ss in input) {
        val si = ss.indexOf(" -> ")
        var name = ss.substring(0, si)
        val dest = ss.substring(si + 4).split(",").map { it.trim() }
        val t = if (name == broadcaster) {
            '_'
        } else {
            name[0].also { name = name.substring(1) }
        }
        name2mod[name] = Mod(t, dest)
    }
    val modNames = listOf(broadcaster) + name2mod.values.flatMap { it.dest }.distinct()
    for (name in modNames) if (name !in name2mod) {
        name2mod[name] = Mod('0', emptyList())
    }
    val name2idx = modNames.withIndex().associateBy({ it.value }, { it.index })
    val mods = modNames.map { name2mod[it]!! }

    var flipState = 0L
    val conjState = LongArray(mods.size)
    val allInputs = LongArray(mods.size)

    for ((src, mod) in mods.withIndex()) {
        for (name in mod.dest) {
            val dst = name2idx[name]!!
            allInputs[dst] = allInputs[dst] or (1L shl src)
        }
    }

    var lps = 0L
    var hps = 0L
    val qSrc = IntArray(1024)
    val qDst = IntArray(1024)
    val qPls = IntArray(1024)
    repeat (1000) {
        lps++
        var qh = 0
        var qt = 0
        fun enq(src: Int, dst: Int, pls: Int) {
            qSrc[qt] = src
            qDst[qt] = dst
            qPls[qt] = pls
            qt++
        }
        fun enqAll(src: Int, pls: Int) {
            val names = mods[src].dest
            for (name in names) {
                enq(src, name2idx[name]!!, pls)
            }
            when (pls) {
                0 -> lps += names.size
                1 -> hps += names.size
                else -> error("!!!")
            }
        }
        enqAll(0, 0)
        while (qh < qt) {
            val src = qSrc[qh]
            val dst = qDst[qh]
            val pls = qPls[qh]
            qh++
            val mod = mods[dst]
            val outPls = when (mod.t) {
                '%' -> if (pls == 1) {
                    -1
                } else {
                    flipState = flipState xor (1L shl dst)
                    ((flipState shr dst) and 1).toInt()
                }
                '&' -> {
                    val state = (conjState[dst] and (1L shl src).inv()) or (pls.toLong() shl src)
                    val all = allInputs[dst]
                    conjState[dst] = state
                    if (state and all == all) 0 else 1
                }
                '0' -> -1
                else -> error("!!!")
            }
            if (outPls >= 0) {
                enqAll(dst, outPls)
            }
        }
    }
    println(lps * hps)
}