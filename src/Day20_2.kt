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

    val modName2Inputs = modNames.associateWith { HashSet<String>() }
    for ((name, mod) in name2mod) {
        for (dest in mod.dest) {
            modName2Inputs[dest]!!.add(name)
        }
    }

    val used = HashSet<String>()
    val order = ArrayList<String>()
    fun dfs1(name: String) {
        if (name in used) return
        used += name
        for (dest in name2mod[name]!!.dest) dfs1(dest)
        order += name
    }
    for (name in modNames) dfs1(name)
    val idx2component = IntArray(modNames.size)
    var componentCnt = 0
    fun dfs2(name: String) {
        val idx = name2idx[name]!!
        if (idx2component[idx] > 0) return
        idx2component[idx] = componentCnt
        for (src in modName2Inputs[name]!!) dfs2(src)
    }
    val keyNames = ArrayList<String>()
    for (name in order.reversed()) {
        val idx = name2idx[name]!!
        if (idx2component[idx] > 0) continue
        keyNames += name
        componentCnt++
        dfs2(name)
    }

    fun comp(name: String) = idx2component[name2idx[name]!!]
    fun withType(name: String) = "${name2mod[name]!!.t}$name"
    fun withComp(names: Iterable<String>): List<String> =
        names.sortedBy { comp(it) }.map { name -> "${withType(name)} ${comp(name)}"}
    for (name in keyNames) {
        val mod = name2mod[name]!!
        println("${comp(name)} ${mod.t}$name -> ${withComp(mod.dest)} <- ${withComp(modName2Inputs[name]!!)}")
    }
    val aggregator = modName2Inputs["rx"]!!.single()
    val feeders = modName2Inputs[aggregator]!!.sortedBy { comp(it) }
    println("aggregator = $aggregator")
    println("feeders = ${withComp(feeders)}")
    check(modName2Inputs[aggregator] == feeders.toSet())
    check(name2mod[aggregator]!!.t == '&')

    fun dfsRecInputs(name: String) {
        if (name in used) return
        used += name
        for (src in modName2Inputs[name]!!) dfsRecInputs(src)
    }
    val feeder2RecInputs = feeders.associateWith { f ->
        used.clear()
        dfsRecInputs(f)
        used.toSet()
    }
    for ((name, inputs) in feeder2RecInputs) {
        println("feeder $name, all inputs ${inputs.size} ${withComp(inputs)}")
    }

    class FeederData(val stateMask: Long, val conjIdx: IntArray)
    var isFeeder = 0L
    val feeder2data= feeders.associateWith { f ->
        isFeeder = isFeeder or (1L shl name2idx[f]!!)
        var mask = 0L
        val conjs = ArrayList<String>()
        for (name in feeder2RecInputs[f]!!) {
            mask = mask or (1L shl name2idx[name]!!)
            if (name2mod[name]!!.t == '&') conjs += name
        }
        check(conjs.size == 2)
        FeederData(mask, conjs.map { name2idx[it]!! }.toIntArray())
    }

    var flipState = 0L
    val conjState = LongArray(mods.size)
    val allInputs = LongArray(mods.size)

    data class FeederState(val state: Long, val c1: Long, val c2: Long)
    fun feederState(f: String): FeederState {
        val data = feeder2data[f]!!
        return FeederState(flipState and data.stateMask, conjState[data.conjIdx[0]], conjState[data.conjIdx[1]])
    }
    val feederStates = feeders.associateWith { HashMap<FeederState, Int>() }
    val feederOnesAt = feeders.associateWith { ArrayList<Int>() }
    val feederCycle = HashMap<String, Int>()

    for ((src, mod) in mods.withIndex()) {
        for (name in mod.dest) {
            val dst = name2idx[name]!!
            allInputs[dst] = allInputs[dst] or (1L shl src)
        }
    }

    val qSrc = IntArray(1024)
    val qDst = IntArray(1024)
    val qPls = IntArray(1024)

    val rxIdx = name2idx["rx"]!!
    var cnt = 0
    var done = false
    var ans = 1L
    tailrec fun gcd(x: Long, y: Long): Long = if (y == 0L) x else gcd(y, x % y)
    fun lcm(x: Long, y: Long) = x * y / gcd(x, y)
    while(feederCycle.size < feeders.size && !done) {
        cnt++
        var qh = 0
        var qt = 0
        fun enq(src: Int, dst: Int, pls: Int) {
            if (dst == rxIdx && pls == 0) done = true
            qSrc[qt] = src
            qDst[qt] = dst
            qPls[qt] = pls
            qt++
        }
        fun enqAll(src: Int, pls: Int) {
            if (pls == 1 && ((1L shl src) and isFeeder) != 0L) {
                val f = modNames[src]
                feederOnesAt[f]!!.add(cnt)
            }
            val names = mods[src].dest
            for (name in names) {
                enq(src, name2idx[name]!!, pls)
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
        // all pulses sent

        for (f in feeders) {
            if (f !in feederCycle) {
                val s = feederState(f)
                val fs = feederStates[f]!!
                if (s in fs) {
                    val prev = fs[s]!!
                    val len = cnt - prev
                    println("Feeder $f cycle at $cnt to $prev, len = $len, ones = ${feederOnesAt[f]}")
                    check(feederOnesAt[f] == listOf(len))
                    feederCycle[f] = len
                    ans = lcm(ans, len.toLong())
                } else {
                    fs[s] = cnt
                }
            }
        }
    }
    println(ans)
}
