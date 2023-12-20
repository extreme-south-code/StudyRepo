package com.example.kotlin_practice

fun main(args: Array<String>) {
    val people = listOf<Person>(Person("John", 20), Person("Tom", 25))
    findTheOldest(people)

    val personWithMaxAge4 = people.customMaxBy { it.age }
    val personWithMaxAge5 = people.customMaxBy(Person::age)
    println(personWithMaxAge4)
    println(personWithMaxAge5)

    val messages = listOf<String>("DaeGyu", "SeonU", "YounGwan")
    val prefix = "Good-Guys: "
    printMessagesWithPrefix(messages, prefix)

    val responses = listOf<String>("200 OK", "404 Not Found", "500 Internal Server Error", "403 " +
            "Forbidden", "201 Created")
    printProblemCounts(responses)

    // 이벤트 핸들러 등록
    val button = Button()

    // 클로저를 받아옴
    val resultCounter  = tryToCountButtonClicks(button)

    // 클로저를 호출하여 버튼 클릭 횟수 증가
    button.simulateClick()
    button.simulateClick()
    button.simulateClick()
    button.simulateClick()

    // 최신 클릭 횟수 출력
    val result = resultCounter.clicks
    println("Button clicks: $result")

    // filter && map
    val peopleA = listOf<Person>(Person("Ye-Yong", 27), Person("Dae-Gyu", 25))
    println(peopleA.filter { it.age > 26 })  // [Person(name=Ye-Yong, age=27)]
    println(peopleA.map { it.name })  // [Ye-Yong, Dae-Gyu]

    val numbers = mapOf<Int, String>(0 to "zero", 1 to "one")
    println(numbers.mapValues { it.value.uppercase()}) // {0=ZERO, 1=ONE}

    // all
    println(peopleA.all(canBeInClub27)) // false

    // flatMap
    val strings = listOf<String>("abc", "def")
    println(strings.flatMap { it.toList() })

    val books = listOf<Book>(Book("Thursday Next", listOf<String>("Jasper Fforde")),
        Book("Mort", listOf("Terry Pratchett")),
        Book("Good Omens", listOf("Terry Pratchett", "Neil Gaiman")))

    println(books.flatMap { it.novelist })   // [Jasper Fforde, Terry Pratchett, Terry Pratchett, Neil Gaiman]
    println(books.flatMap { it.novelist }.toSet())   // [Jasper Fforde, Terry Pratchett, Neil Gaiman]
}

/*
    -----------------------------------------------------------------------------------------------
    [람다로 프로그래밍]

    Lamda expressions and member references (람다 식과 멤버 참조)
    -----------------------------------------------------------------------------------------------
 */


/*
    람다 소개: 코드 블록을 함수 인자로 넘기기

    클래스를 선언하고 그 클래스의 인스턴스를 함수에 넘기는 대신 함수형 언어에서는 함수를 직접 다른 함수에 전달할 수
    없다. 람다 식을 사용하면 코드가 더욱 더 간결해진다. 람다 식을 사용하면 함수를 선언할 필요가 없고 코드 블록을
    직접 함수의 인자로 전달할 수 있다.
 */

data class Person(val name: String, val age: Int)

/* Java */
fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}

public fun <T, R : Comparable<R>> Iterable<T>.customMaxBy(selector: (T) -> R): T? {
    val iterator = iterator()
    if (!iterator.hasNext()) return null
    var maxElem = iterator.next()
    if (!iterator.hasNext()) return maxElem
    var maxValue = selector(maxElem)
    do {
        val e = iterator.next()
        val v = selector(e)
        if (maxValue < v) {
            maxElem = e
            maxValue = v
        }
    } while (iterator.hasNext())
    return maxElem
}

/*
    현재 영역에 있는 변수에 접근
 */

fun printMessagesWithPrefix(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}

// 코틀린에선 자바와 달리 람다에서 람다 밖 함수에 있는 `파이널이 아닌` 변수에 접근할 수 있고, 그 변수를 변경할 수도 있다.

fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    responses.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        } else if (it.startsWith("5")) {
            serverErrors++
        }
    }
    println("$clientErrors client errors, $serverErrors server errors")
}

/*
    어떻게 그런 동작이 가능할까?

    파이널 변수를 포획한 경우에는 람다 코드를 변수 값과 함께 저장한다. 파이널이 아닌 변수를 포획할 경우에는 변수를 특별한
    래퍼로 감싸서 나중에 변경하거나 읽을 수 있게 한 다음, 래퍼에 대한 참조를 람다 코드와 함께 저장한다.

    한 가지 꼭 알아둬야 할 함정이 있다. 람다를 이벤트 핸들러나 다른 비동기적으로 실행되는 코드로 활용하는 경우 함수 호출이
    끝난 다음에 로컬 변수가 변경될 수도 있다.
 */


class Button {
    private var clickListener: (() -> Unit)? = null

    fun onClick(listener: () -> Unit) {
        this.clickListener = listener
    }

    fun simulateClick() {
        clickListener?.invoke()
        println("Button clicked")
    }
}

data class ClickCounter(var clicks: Int)

fun tryToCountButtonClicks(button: Button): ClickCounter {
    val clickCounter = ClickCounter(clicks = 0)
    button.onClick { clickCounter.clicks++ }
    return clickCounter
}







/*
    -----------------------------------------------------------------------------------------------
    [람다로 프로그래밍]

    Collection Functional API (컬렉션 함수형 API)
    -----------------------------------------------------------------------------------------------
 */


/*
    필수적인 함수: `filter`와 `map`

    filter 함수는 컬렉션에서 원치 않는 원소를 제거한다. 하지만 filter는 원소를 변환할 수는 없다. 원소를 변환하려면
    map 함수를 사용해야 한다. map 함수는 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만든다.

    <main() 함수 참조>
 */

/*
    `all`, `any`, `count`, `find`:  컬렉션에 술어 적용

    컬렉션에 대해 자주 수행하는 연산으로 컬렉션의 모든 원소가 어떤 조건을 만족하는지 판단하는 연산이 있다. 코틀린에선
    all과 any가 이런 연산이다.

    <main() 함수 참조>
 */

val canBeInClub27 = { p: Person -> p.age <= 26 }

/*
    flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리

    flatMap 함수는 먼저 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를
    한 리스트로 한데 모은다.
 */

data class Book(val name: String, val novelist: List<String>)
