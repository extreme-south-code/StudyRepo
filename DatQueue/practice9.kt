package com.example.kotlin_practice

fun main(args: Array<String>) {
    val p1 = Point(10, 20)
    val p2 = Point(30, 40)
    println(p1 + p2)

    val p4 = Point2(3,3)
    val p5 = "h".times(3)
    println("p4 $p4")
    println("p5 $p5")

    println(Point3(10,20).equalsWith(Point3(5,5)))
    println(Point3(10,20).equalsWith(Point3(10,20)))

    println("abc" < "bac")

    val rect = Rectangle(Point4(10, 20), Point4(50, 50))
    println(rect.containsWith(Point4(20, 30)))
    println(rect.containsWith(Point4(5, 5)))

    val n = 9
    println(0..(n + 1))
    (0..n).forEach{ println(it) }

    val p = Point5(10, 20)
    val (x, y) = p
    println("x $x")
    println("y $y")

    val (name, ext) = splitFilename("example.kt") // 구조 분해 선언 구문을 사용해 데이터 클래스를 푼다.
    println("name: $name")
    println("ext: $ext")

    val map = mapOf<String, String>("Oracle" to "Java", "JetBrains" to "Kotlin")
    println("map $map")
    printEntries(map)
}

/*
    -----------------------------------------------------------------------------------------------
    [연산자 오버로딩과 기타 컨벤션]
    -----------------------------------------------------------------------------------------------
 */



/*
    산술 연산자 오버로딩

    코틀린에서 컨벤션을 사용하는 가장 단순한 예는 산술 연산자이다. 자바에서는 원시 타입에 대해서만 산술 연산자를 사용할
    수 있고, 추가로 String에 대해 + 연산자를 사용할 수 있다.
 */


// method 1
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

// method 2
data class ExtensionPoint(val x: Int, val y: Int)

operator fun ExtensionPoint.plus(other: ExtensionPoint): ExtensionPoint {
    return ExtensionPoint(x + other.x, y + other.y)
}

data class Point2(val x: Int, val y: Int)

operator fun Point2.times(scale: Double): Point2 {
    return Point2((x * scale).toInt(), (y * scale).toInt())
}

operator fun String.times(count: Int): String {
    return toString().repeat(count)
}

/*
    비교 연산자 오버로딩

    동등성 연산자: equals

    코틀린이 == 연산자 호출을 equals 메서드 호출로 컴파일 한다는 사실을 알고 있다. != 연산자를 사용하는 식도
    equals 호출로 컴파일된다.
 */

// custom equals method
class Point3(val x: Int, val y: Int) {
    fun equalsWith(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Point3) return false
        return (other.x == x) && (other.y == y)
    }
}


/*
    컬렉션과 범위에 대해 쓸 수 있는 관례

    in 관례

    컬렉션이 지원하는 다른 연산자로는 in이 있다. In은 객체가 컬렉션에 들어있는지 검사한다. 그런 경우 in 연산자와
    대응하는 함수는 `contains`다.
 */

data class Point4(val x: Int, val y: Int)

data class Rectangle(val upperLeft: Point4, val lowerRight: Point4)

fun Rectangle.containsWith(p: Point4): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

/*
    구조 분해 선언과 component 함수

    구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화 할 수 있다.

    구조 분해 선언은 함수에서 여러 값을 반환할 때 유용하다. 여러 값을 한꺼번에 반환해야 하는 함수가 있다면 반환해야 하는
    모든 값이 들어갈 데이터 클래스를 정의하고 함수의 반환 타입을 그 데이터 클래스로 바꾼다.
    구조 분해 선언 구문을 사용하면 이런 함수가 반환하는 값을 쉽게 풀어서 여러 변수에 넣을 수 있다.
 */

data class Point5(val x: Int, val y: Int)

data class NameComponents(val name: String, val extension: String)

fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1]) // 함수에서 데이터 클래스의 인스턴스를 반환한다.
}

/*
    구조 분해 선언과 루프

    함수 본문 내의 선언문 뿐 아니라 변수 선언이 들어갈 수 있는 장소라면 어디든 구조 분해 선언을 사용할 수 있다.
 */

fun printEntries(map: Map<String, String>) {
    for ((key, value ) in map) {
        println("$key -> $value")
    }
}


/*
    프로퍼티 접근자 로직 재활용: 위임 프로퍼티

    위임 프로퍼티(delegated property)를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로
    작동하는 프로퍼티를 쉽게 구현할 수 있다. 또한 그 과정에서 접근자 로직을 매번 재구현할 필요도 없다.

    위임은 객체가 직접 작업을 수행하지 않고 다른 도우미 객체가 그 작업을 처리하게 맡기는 디자인 패턴을 말한다.
    이때 작업을 처리하는 도우미 객체를 `위임 객체`라고 부른다.
 */


/*
    위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연

    지연 초기화(lazy initialization)는 객체의 일부분을 초기화 하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우
    초기화할 때 흔히 쓰이는 패턴이다. 초기화 과정에 자원을 많이 사용하거나 객체를 사용할 때마다 꼭 초기화하지 않아도
    되는 프로퍼티에 대해 지연 초기화 패턴을 사용할 수 있다.
 */

class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}

fun loadEmails(email: Person) {}

