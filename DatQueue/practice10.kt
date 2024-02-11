package com.example.kotlin_practice

/*
    -----------------------------------------------------------------------------------------
    고차 함수

    고차 함수 정의
    -----------------------------------------------------------------------------------------
 */

fun main() {
    twoAndThree { a, b -> a + b }
    twoAndThree { a, b -> a * b }

    val letters = listOf<String>("Alpha", "Beta")
    println(letters.joinToString()) // 디폴트 변환 함수를 사용한다. -> Alpha, Beta
    println(letters.joinToString { it.lowercase() }) // 람다를 인자로 전달한다. -> alpha, beta
    println(letters.joinToString(separator = "! ", postfix = "! ",
        transform = { it.uppercase() })) // 이름 붙은 인자 구문을 사용해 람다를 포함하는 여러 인자를 전달한다. -> ALPHA! BETA!

    val contacts = listOf<Person>(
        Person("Dmitry", "Jemerov", "123-4567"),
        Person("Svetlana", "Isakova", null)
    )

    val contactListFilters = ContactListFilters()
    with (contactListFilters) {
        prefix = "Dm"
        onlyWithPhoneNumber = true
    }

    println(contacts.filter(contactListFilters.getPredicate()))

    println(log.averageDurationFor(OS.WINDOWS))
    println(log.averageDurationFor(OS.MAC))

    println(log.averageDurationForV2 {
        it.os in setOf<OS>(OS.ANDROID, OS.IOS)
    })
    println(log.averageDurationForV2 {
        it.os == OS.IOS && it.path == "/signup"
    })
}

fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("The result is $result")
}

/*
    함수를 함수에서 반환

    다른 함수를 반환하는 함수를 정의하려면 함수의 반환 타입으로 함수 타입을 지정해야 한다. 함수를 반환하려면 return 식에
    람다나 멤버 참조나 함수 타입의 값을 계산하는 식 등을 넣으면 된다.
 */

data class Person(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
)

class ContactListFilters {
    var prefix: String = ""
    var onlyWithPhoneNumber: Boolean = false

    fun getPredicate(): (Person) -> Boolean { // 함수를 반환하는 함수를 정의
        val startsWithPrefix = { p: Person ->
            p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
        }
        if (!onlyWithPhoneNumber) {
            return startsWithPrefix // 함수 타입의 변수를 반환한다
        }
        return { startsWithPrefix(it) && it.phoneNumber != null } // 람다를 반환한다.
    }
}

/*
    람다를 활용한 중복 제거

    함수 타입과 람다 식은 재활용하기 좋은 코드를 만들 때 쓸 수 있는 훌륭한 도구다. 웹 사이트 방문 기록을 분석하는 예를
    살펴보자.
 */

data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf<SiteVisit>(
    SiteVisit("/", 34.0, OS.WINDOWS),
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID)
)

val averageWindowsDuration = log
    .filter { it.os === OS.WINDOWS }
    .map(SiteVisit::duration)
    .average()

fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map(SiteVisit::duration).average()

/*
    하지만 위의 확장함수는 충분히 강력하지 않다. 만약 모바일 디바이스(IOS, ANDROID)의 평균 방문 시간을 구하고
    싶다거나 IOS 사용자의 /signup 페이지 평균 방문 시간을 구하고 싶을 경우는 어떻게 해야 할까?

    이는 고차 함수를 이용하여 함수를 확장할 수 있다.
 */

fun List<SiteVisit>.averageDurationForV2(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()
