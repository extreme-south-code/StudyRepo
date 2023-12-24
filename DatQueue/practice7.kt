package com.example.kotlin_practice

fun main(args: Array<String>) {

    // sequence evaluation
    val newList = listOf<Int>(1, 2, 3, 4).asSequence() // 원본 컬렉션을 시퀀스로 변환한다.
        .map { println("map($it) "); it * it } // 시퀀스도 컬렉션과 똑같은 API를 제공한다.
        .filter { println("filter($it) "); it % 2 == 0 }
        .toList() // 결과 시퀀스를 다시 리스트로 변환한다.

    println(newList)  // [4, 16]

    val cal1 = (100..2_000)
        .map { it * 2 }
        .first { println("filter($it)"); it % 3 == 0 }

    println("cal1: $cal1")

    val ca12 = (100 .. 200_000_000).asSequence()
        .map { println("doubling $it"); it * 2 }
        .filter { println("filtering $it"); it % 3 == 0 }
        .first()  // sequence가 비었다면 NoSuchElementException 이 발생한다. (.fistOrNull() 를 사용할 수도 있다.)

    println("cal2: $ca12")

    println(oddNumbers.take(5).toList()) // [1, 3, 5, 7, 9]
    println(oddNumbers.count()) // 6 (include `null`)

    val iterator = listOf<Int>(1, 2, 3).iterator()
    println(iterator.next()) // 1
    println(iterator.next()) // 2
    println(iterator.next()) // 3
    // println(iterator.next()) // Exception

    val sequence = listOf<Int>(1, 2, 3).asSequence()
    println(sequence.elementAtOrNull(0)) // 1
    println(sequence.elementAtOrNull(1)) // 2
    println(sequence.elementAtOrNull(2)) // 3
    println(sequence.elementAtOrNull(3)) // null

    println("---------------------------------------------")

    val largeWordsList = generateLargeWordsList()

//    // Sequence 사용
//    val timeSequence = measureTimeMillis {
//        val wordsSequence = largeWordsList.asSequence()
//        val result = wordsSequence
//            .filter { it.length > 3 }
//            .map { it.length }
//            .take(4)
//            .toList()
//        println("Sequence result: $result")
//    }
//    println("Sequence time: $timeSequence ms")
//
//    println("---------------------------------------------")

//    // Iterator 사용
//    val timeIterator = measureTimeMillis {
//        val wordsIterator = largeWordsList.toList()
//        val result = wordsIterator
//            .filter { it.length > 3 }
//            .map { it.length }
//            .take(4)
//            .toList()
//        println("Iterator result: $result")
//    }
//    println("Iterator time: $timeIterator ms")
}

fun generateLargeWordsList(): List<String> {
    val words = "The quick brown fox jumps over the lazy dog".split(" ")
    val repeatedWords = List(1000000) { words }.flatten()
    return repeatedWords.shuffled()
}


/*
    -----------------------------------------------------------------------------------------------
    [람다로 프로그래밍]

    Calculation Collection of Lazy Evaluation (지연 계산 컬렉션 연산)

    map이나 filter 같은 몇 가지 컬렉션 함수를 살펴봤다. 그런 함수는 결과 컬렉션을 즉시 생성한다. 이는 컬렉션 함수를
    연쇄하면 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 뜻이다. 시퀀스(sequence)를 사용하면 중간
    임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.

    -- 예시 코드: <main 함수 참조>

    코틀린 지연 계산 시퀀스는 Sequence 인터페이스에서 시작한다. Sequence 안에는 iterator라는 단 하나의 메서드가
    있다. 그 메서드를 통해 시퀀스로부터 원소 값을 얻을 수 있다. (아래 코드 참조)


    public interface Sequence<out T> {
        public operator fun iterator(): Iterator<T>
    }


    그렇다면 왜 시퀀스를 다시 컬렉션으로 되돌려야 할까?

    컬렉션보다 시퀀스가 훨씬 더 낫다면 그냥 시퀀스를 쓰는 편이 나을수도 있다. 하지만 "항상 그렇지는 않다." 시퀀스의
    원소를 차례로 이터레이션해야 한다면 시퀀스를 직접 써도 된다. 하지만 시퀀스 원소를 인덱스를 사용해 접근하는 등의 다른
    API 메서드가 필요하다면 시퀀스를 리스트로 변환해야 한다.


    ‼ ‼ 시퀀스 연산 실행: 중간 연산과 최종 연산

    시퀀스에 대한 연산은 중간 연산과 최종 연산으로 나뉜다. 중간 연산은 다른 시퀀스를 반환한다. 그 시퀀스는 최초 시퀀스의
    원소를 변환하는 방법을 안다. 최종 연산은 결과를 반환한다.

    시퀀스의 경우 모든 연산은 각 원소에 대해 순차적으로 적용된다. 즉, 첫 번째 원소가 처리되고, 다시 두 번째 원소가 처리되며,
    이런 처리가 모든 원소에 대해 적용된다.
    -----------------------------------------------------------------------------------------------
 */

public fun <T : Any> customGenerateSequence(seed: T?, nextFunction: (T) -> T?): Sequence<T> =
    if (seed == null)
        CustomEmptySequence
    else
        CustomGeneratorSequence({ seed }, nextFunction)

internal interface DropTakeSequence<T> : Sequence<T> {
    fun drop(n: Int): Sequence<T>
    fun take(n: Int): Sequence<T>
}

internal object EmptyIterator : ListIterator<Nothing> {
    override fun hasNext(): Boolean = false
    override fun hasPrevious(): Boolean = false
    override fun nextIndex(): Int = 0
    override fun previousIndex(): Int = -1
    override fun next(): Nothing = throw NoSuchElementException()
    override fun previous(): Nothing = throw NoSuchElementException()
}


private object CustomEmptySequence : Sequence<Nothing>, DropTakeSequence<Nothing> {
    override fun iterator(): Iterator<Nothing> = EmptyIterator
    override fun drop(n: Int) = CustomEmptySequence
    override fun take(n: Int) = CustomEmptySequence
}

public class CustomGeneratorSequence<T: Any>(
    private val getInitialValue: () -> T?,
    private val getNextValue: (T) -> T?
) : Sequence<T> {
    override fun iterator(): Iterator<T> = object : Iterator<T> {
        var nextItem: T? = null
        var nextState: Int = -2 // -2 for initial unknown, -1 for next unknown, 0 for done, 1 for
        // continue

        private fun calcNext() {
            nextItem = if (nextState == -2) getInitialValue() else getNextValue(nextItem!!)
            nextState = if (nextItem == null) 0 else 1
        }

        override fun hasNext(): Boolean {
            if (nextState < 0)
                calcNext()
            return nextState == 1
        }

        override fun next(): T {
            if (nextState < 0)
                calcNext()

            if (nextState == 0)
                throw NoSuchElementException()

            val result = nextItem as T
            // Do not clean nextItem (to avoid keeping reference on yileded instance) -- need to keep state for getNextValue

            nextState = -1
            return result
        }

    }
}

val oddNumbers = customGenerateSequence(1) { if (it < 10 ) it + 2 else null } // `it` is the
// previous element






