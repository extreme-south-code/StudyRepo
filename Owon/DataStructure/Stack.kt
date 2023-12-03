fun main() {
    val stack = Stack<Int>()

    stack.push(1)
    stack.push(2)
    stack.push(3)

    println("size:${stack.size()}")
    println("peek:${stack.peek()}")
    println("pop:${stack.pop()}")
    println("size:${stack.size()}")
    println("peek:${stack.peek()}")
}

class Stack<T> {
    private val items = mutableListOf<T>()

    fun push(item:T) {
        items.add(item)
    }
    fun pop():T? {
        if(isEmpty()) {
            return null
        }
        return items.removeAt(items.size -1)
    }
    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun size():Int {
        return items.size
    }

    fun peek(): T? {
        if(isEmpty()) {
            return null
        }
        return items[items.size-1]
    }
}