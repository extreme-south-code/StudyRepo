fun main() {
    val queue = Queue<Int>()
    
    queue.push(1)
    queue.push(2)
    queue.push(3)
    
    println("size:${queue.size()}")
    println("pop: ${queue.pop()}")
    println("first: ${queue.first()}")
    println("queue:${queue.items}")
}

class Queue<T> {
    val items = mutableListOf<T>()
    
    fun push(item:T) {
        items.add(item)
    }
    fun pop():T? {
        if(isEmpty()){
            return null
        }
        return items.removeAt(0)
    }
    fun isEmpty(): Boolean {
        return items.isEmpty()
    }
    fun size(): Int {
        return items.size
    }
    fun first():T? {
        if(isEmpty()) {
            return null
        }
        return items[0]
    }
    
}