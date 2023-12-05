fun main() {
	val linkedList = Linkedlist<Int>()
    linkedList.add(1)
    linkedList.add(2)
    linkedList.printList()
    
    linkedList.del(2)
	linkedList.printList()
}

class Node<T>(var data: T, var next: Node<T>?= null)

class Linkedlist<T> {
    var head: Node<T>?= null
    
    fun add(data: T) {
        val newNode = Node(data)
        if (head == null) {
            head = newNode
        } else {
            var current = head
            while (current?.next != null) {
                current = current.next
            }
             current?.next = newNode
        }
    }
   	fun del(data: T) {
        if(head == null) return
        
        if(head!!.data == data) {
            head = head!!.next
            return
        }
        var current = head
        while (current?.next != null && current.next!!.data != data) {
            current = current.next
        }
        if (current?.next != null) {
            current.next = current.next!!.next
        }
    }
    fun printList() {
        var current = head
        while(current != null) {
            print("${current.data} ->")
            current = current.next
        }
        println("null")
    }
}