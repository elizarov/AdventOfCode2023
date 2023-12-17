class Heap<K, V : Comparable<V>> {
    private data class Data<V>(var v: V, var i: Int)
    private var size = 0
    private var h = arrayOfNulls<Any>(1024)
    private val d = HashMap<K, Data<V>>()

    fun isEmpty() = size == 0

    fun removeMin(): Pair<K, V> {
        val resK = h[0] as K
        val resV = d[resK]!!.v
        size--
        var i = 0
        val ik = h[size] as K
        val id = d[ik]!!
        h[0] = ik
        id.i = 0
        h[size] = null
        while (true) {
            var t = 2 * i + 1
            var j = i
            var jd = id
            if (t >= size) break
            var td = d[h[t]]!!
            if (td.v < jd.v) {
                j = t
                jd = td
            }
            t++
            if (t < size) {
                td = d[h[t]]!!
                if (td.v < jd.v) {
                    j = t
                    jd = td
                }
            }
            if (j == i) break
            val jk = h[j] as K
            h[i] = jk
            jd.i = i
            h[j] = ik
            id.i = j
            i = j
        }
        return resK to resV
    }

    fun putBetter(k: K, v: V): Boolean {
        val kd0 = d[k]
        var i: Int
        val id: Data<V>
        if (kd0 == null) {
            i = size++
            id = Data(v, i)
            if (i >= h.size) h = h.copyOf(h.size * 2)
            h[i] = k
            d[k] = id
        } else {
            if (kd0.v <= v) return false
            i = kd0.i
            id = kd0
            id.v = v
        }
        while (i > 0) {
            val j = (i - 1) / 2
            val jd = d[h[j]]!!
            if (jd.v <= id.v) break
            val jk = h[j] as K
            h[i] = jk
            jd.i = i
            h[j] = k
            id.i = j
            i = j
        }
        return true
    }
}