package com.example.kotlin_practice

import java.lang.Exception

/*
    Kotlin Basic & Function(declare, call)
    코틀린 기초 그리고 함수 정의와 호출
 */

fun main () {
    println("Hello World!")

    val person1 = Person("Dae Gyu")
    println(person1.name)

    val myName = MyClass()
    println(myName.name)

    println(recognize('c'))

//    println("set $set")
//    println("list $list")
//    println("map $map")

//    val list = listOf<Int>(1, 2, 3)
//    println(joinToString(list, "; ", "(",")"))
//    println(joinToString(collection = list, separator = ";", prefix = "(", postfix = ")"))
//    println(joinToString(list))
//    println(joinToString(list, "; "))

    val list1 = arrayListOf(1, 2, 3)
    println(list1.joinToString(""))

    println("lastChar $lastChar")

    println("Kotlin".lastChar)

    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println("lastchar $sb")

    val list = listOf<String>("one", "two", "eight")
    println(list)

    /* 정규식 */
    println("12.345-6.A".split("[.\\-]".toRegex()))
    println("12.345-6.A".split(".","-"))

    parsePath("/Users/yole/kotlin-book/chapter.adoc")
}




class Person(val name: String)

class MyClass {
    val name: String = "Dat Queue"
}

enum class Color(
        private val r: Int, private val g: Int, private val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 0, 255), BLUE(0, 0, 255),
    INDIGO(75, 0, 130), VIOLET(238, 130, 238);

    fun rgb() = (r * 256 + g) * 256 + b
}

fun getMnemonic(color: Color) =
        when (color) {
            Color.RED -> "Richard"
            Color.ORANGE -> "Of"
            Color.YELLOW -> "York"
            Color.GREEN -> "Gave"
            Color.BLUE -> "Battle"
            Color.INDIGO -> "In"
            Color.VIOLET -> "Vain"
        }

fun mix(c1: Color, c2: Color) =
        when (setOf(c1, c2)) {
            setOf(Color.RED, Color.YELLOW) -> Color.ORANGE
            setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
            setOf(Color.BLUE, Color.VIOLET) -> Color.INDIGO
            else -> throw Exception("Dirty color")
        }

fun mixOptimized(c1: Color, c2: Color) =
        when {
            (c1 == Color.RED && c2 == Color.YELLOW) ||
                    (c1 == Color.YELLOW && c2 == Color.RED) ->
                Color.ORANGE

            (c1 == Color.YELLOW && c2 == Color.BLUE) ||
                    (c1 == Color.BLUE && c2 == Color.YELLOW) ->
                Color.GREEN

            (c1 == Color.BLUE && c2 == Color.VIOLET) ||
                    (c1 == Color.VIOLET && c2 == Color.BLUE) ->
                Color.INDIGO

            else -> throw Exception("Dirty color")
        }

fun printLength(value: Any) {
    if (value is String) {
        // value가 String 타입일 때 자동으로 캐스팅되어 사용 가능
        println(value.length)
    }
}

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
    else -> "I don't know..."
}

//val set = hashSetOf<Int>(1, 7, 53)
//val list = arrayListOf<Int>(1, 7, 53)
//val map = hashMapOf<Int, String>(1 to "one", 7 to "seven", 53 to "fifty-three")

//fun <T> joinToString(
//    collection: Collection<T>,
//    separator: String = ";",
//    prefix: String = "(",
//    postfix: String = ")"
//): String {
//    val result = StringBuilder(prefix)
//
//    for ((index, element) in collection.withIndex()) {
//        if (index > 0) result.append(separator)
//        result.append(element)
//    }
//
//    result.append(postfix)
//    return result.toString()
//}

fun <T> Collection<T>.joinToString(
        separator: String = ", ",
        prefix: String = "",
        postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        println("$index $element")

        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

val String.lastChar: Char
    get() = get(length - 1)

val lastChar = "Hello".lastChar

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }

/* 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의 */
/* js나 java 처럼 ...을 붙이는 대신 파라미터 앞에 varag 변경자를 붙인다. */

public fun<T> listOf(vararg elements: T): List<T> = if (elements.isNotEmpty()) elements.asList() else emptyList()

/* 값의 쌍 다루기: 중위 호출과 구조 분해 선언 */
/* 맵을 만들려면 mapOf 함수를 사용한다. */

val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

/* 정규식없이 문자열 파싱 */
fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")

    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    println("Dir: $directory, name: $fileName, ext: $extension")
}

fun parsePathRegex(path: String) {
    val regex = """(.+)/(.+)\\.(.+)""".toRegex() // 3중 따옴표
    val mathResult = regex.matchEntire(path)
    if (mathResult != null) {
        val (directory, filename, extension) = mathResult.destructured
        println("Dir: $directory, name: $filename, ext: $extension")
    }
}

/*
    많은 개발자들이 좋은 코드의 중요한 특징 중 하나가 중복이 없는 것이라 믿는다. 그래서 그 원칙에는 반복하지 말라
    (DRY: Don't Repeat Yourself)라는 이름도 붙어있다. 하지만 자바 코드를 작성할 때는 DRY 원칙을 피하기는 쉽지 않다.
    많은 경우 메소드 추출 리팩토링을 적용해서 긴 메소드를 부분부분 나눠서 각 부분을 재활용할 수 있다. 하지만 그렇게 코드를
    리팩토링하면 클래스 안에 작은 메소드가 많아지고 각 메소드 사이의 관계를 파악하기 힘들어서 코드를 이해하기 더 어려워질 수도 있다.
    리팩토링을 진행해서 추출한 메소드를 별도의 내부 클래스안에 넣으면 코드를 깔끔하게 조작할 수는 있지만, 그에 따른 불필요한 준비 코드가 늘어난다.

    코틀린에는 더 깔끔한 해법이 있다. 코틀린에서는 함수에서 추출한 함수를 원 함수 내부에 중첩시킬 수 있다.
    그렇게 하면 문법적인 부가 비용을 들이지 않고도 깔끔하게 코드를 조직할 수 있다.
 */

fun saveUser(user: User) {
    if (user.name.isEmpty()) {
        throw IllegalArgumentException(
                "Can't save user ${user.id}: empty Name"
        )
    }

    if (user.address.isEmpty()) {
        throw IllegalArgumentException(
                "Can't save user ${user.id}: empty Address"
        )
    }

    // Save user to the database
}

class User(val id: Int, val name: String, val address: String)

/*
    클래스가 사용자의 필드를 검증할 때 필요한 여러 경우를 하나씩 처리하는 메소드가 중복된 것을 알 수 있다.
    이를 개선해보자.
 */

fun saveUserRefactor(user: User) {
    fun validate(user: User, value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                    "Can't save user ${user.id}: empty $fieldName"
            )
        }
    }

    validate(user, user.name, "Name")
    validate(user, user.address, "Address")

    // Save user to the database
}

fun saveUserRefactor2(user: User) {
    fun validate(value: String, fieldName: String) { // user 파라미터를 중복 사용하지 않는다.
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                    "Can't save user ${user.id}: " +
                            "empty $fieldName"
            )
        }
    }

    validate(user.name, "Name")
    validate(user.address, "Address")

    // Save user to the database
}

fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                    "Can't save user $id: empty $fieldName"
            )
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUserRefactor3(user: User) {
    user.validateBeforeSave()

    // Save user to the database
}

/*
    - 코틀린은 자체 컬렉션 클래스를 정의하지 않지만 자바 클래스를 확장해서 더 풍부한 API를 제공한다.
    - 함수 파라미터의 디폴트 값을 정의하면 오버로딩한 함수를 정의할 필요성이 줄어든다. 이름붙인 인자를 사용하면
      함수의 인자가 많을 때 함수 호출의 가독성을 더 향상시킬 수 있다.
    - 코틀린 파일에서 클래스 멤버가 아닌 최상위 함수와 프로퍼티를 직접 선언할 수 있다.
      이를 활용하면 코드 구조를 더 유연하게 만들 수 있다.
    - 확장 함수와 프로퍼티를 사용하면 외부 라이브러리에 정의된 크래스를 포함해 모든 클래스의 API를 그 클래스의
      소스코드를 바꿀 필요 없이 확장할 수 있다. 확장 함수를 사용해도 실행 시점에 부가 비용이 들지 않는다.
    - 중위 호출을 통해 인자가 하나 밖에 없는 메소드나 확장 함수를 더 깔끔한 구문으로 호출할 수 있다.
    - 코틀린은 정규식과 일반 문자열을 처리할 때 유용한 다양한 문자열 처리 함수를 제공한다.
    - 자바 문자열로 표현하려면 수많은 이스케이프가 필요한 문자열의 경우 3중 따움표 문자열을 사용하면
      더 깔끔하게 표현할 수 있다.
    - 로컬 함수를 써서 더 깔끔하게 유지하면서 중복을 제거할 수 있다.
 */






