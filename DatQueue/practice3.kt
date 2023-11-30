package com.example.kotlin_practice

import android.provider.ContactsContract.CommonDataKinds.Nickname

fun main() {
    val person1 = Person("Daegyu", 25)
    println(person1)
    val person2 = Person("Gildong")
    println(person2)

    val user4 = User4("DatQueue")
    user4.address = "Pohang-hang"
}



/*
    ----------------------------------------------------------
    Class, Object, Interface
    [Ch.2]: 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언
    ----------------------------------------------------------
 */


/*
    Class Initializing(클래스 초기화): 주 생성자와 초기화 블록

    주 생성자는 생성자 파라미터를 지정하고 그 생성자 파라미터에 의해 초기화되는 프로퍼티를 정의하는 두가지 목적에 쓰인다.
 */

class User constructor(_nickname: String) { // 파라미터가 하나만 있는 주 생성자
    val nickname: String

    init { // 초기화 블록
        nickname = _nickname
    }
}

/*
    "부 생성자": 상위 클래스를 다른 방식으로 초기화

    일반적으로 코틀린에서는 생성자가 여럿 있는 경우가 자바보다 훨씬 적다. 그래도 생성자가 여럿 필요한 경우가 가끔 있다.
    부 생성자가 필요한 이유는 자바 상호운용성이다.

    하지만 부 생성자가 필요한 다른 경우도 있다. 클래스 인스턴스를 생성할 때 파라미터 목록이 다른 생성 방법이 여럿 존재하는
    경우에는 부 생성자를 여럿 둘 수 밖에 없다.
 */

class Person(firstName: String, out: Unit = println("[Primary Constructor] Parameter")) {  //"주 생성자"
    val fName = println("[Property Person fName]: $firstName")

    init {
        println("[init] Person init block")
    }

    constructor(   // "부 생성자"
            firstName: String,
            age: Int,
            out: Unit = println("[Secondary Constructor] Parameter")
    ): this(firstName) {
        println("[Secondary Constructor] Body: $firstName $age")
    }
}

// -----------------------------------------------------------------------------------------------

/*
    Implements property which declared in interface(인터페이스에 선언된 프로퍼티 구현)

    사실 인터페이스는 아무 상태도 포함할 수 없으므로 상태를 저장할 필요가 있다면 인터페이스를 구현한 하위 클래스에서
    상태 저장을 위한 프로퍼티 등을 만들어야 한다.
 */

interface User2 {
    val nickName: String
}

class SubscribingUser(val email: String): User2 {
    override val nickName: String
        get() = email.substringBefore('@')
}

interface User3 {
    val email: String
    val nickname: String
        get() = email.substringBefore('@') // 프로퍼티에 뒷받침하는 필드가 없다. 대신 매번 결과를 계산해 돌려준다.
}
// 하위 클래스는 추상 프로퍼티인 email을 반드시 override 해야한다. 반면 nickname은 오버라이드하지 않고 상속할 수 있다.


// -------------------------------------------------------------------------------------------------

/*
    게터와 세터에서 뒷받침하는 필드에 접근

    값을 저장하는 동시에 로직을 실행할 수 있게 하기 위해서는 접근자 안에서 프로퍼티를 뒷받침하는 필드에 접근할 수 있어야 한다.

    프로퍼티에 저장된 값의 변경 이력을 로그에 남기려는 경우를 생각해보자. 그런 경우 변경 가능한 프로퍼티를 정의하되
    세터에서 프로퍼티 값을 바꿀 때마다 약간의 코드를 추가로 실행해야 한다.
 */

class User4(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println(
                    """
                    Address was changed for $name:
                    "$field" -> "$value".""".trimIndent()
            )
            field = value
        }
}


// ------------------------------------------------------------------------------------------------

/*
    접근자의 가시성 변경

    접근자의 가시성은 기본적으로 프로퍼티의 가시성과 같다. 하지만 원한다면 get이나 set앞에 가시성 변경자를 추가해서
    접근자의 가시성을 변경할 수 있다.
 */

class LengthCounter {
    var counter: Int = 0
        private set // 이 클래스 밖에서 이 프로퍼티의 값을 바꿀 수 없다.

    fun addWord(word: String) {
        counter += word.length
    }
}