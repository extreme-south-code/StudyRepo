fun main() {
    val myHashMap = HashMap<String,String>()
    myHashMap.put("key1","value1")
    myHashMap.put("key2","value2")
    myHashMap.put("key3","value3")
	
    myHashMap.print()
    
    println("key2의 값: ${myHashMap["key2"]}")
}

class HashMap<K,V> {
    val buckets: Array<MutableList<Pair<K,V>>> = Array(16) { mutableListOf()}
    val size: Int
    	get() = buckets.sumOf {it.size}
    fun put(key:K, value: V) {
        val index = key.hashCode() % buckets.size
        val bucket = buckets[index]
        
       	for ((existingKey, _) in bucket) {
            if (existingKey == key) {
                bucket.removeIf {it.first == key}
            	break
            }
        }
        
        bucket.add(Pair(key,value))
    }
    
    operator fun get(key: K):V?{
        val index = key.hashCode() % buckets.size
        val bucket = buckets[index]
        
        for ((existingKey, value) in bucket) {
            if (existingKey == key) {
                return value
            }
        }
        
        return null
    }
    
    fun remove(key: K) {
        val index = key.hashCode() % buckets.size
        val bucket = buckets[index]
        
        bucket.removeIf {it.first == key}
    }
    
    fun print() {
        for ((index, bucket) in buckets.withIndex()) {
            println("Bucket $index:")
            for ((key,value) in bucket) {
                println("  $key: $value")
            }
        }
    }
}