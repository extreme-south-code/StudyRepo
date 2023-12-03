package com.example.kotlin_practice
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class UserInfo (
    val name: String,
)

fun main(args: Array<String>) {
    println(CaseInsensitiveFileComparator.compare(
        File("/User"),
        File("/user")
    ))
    val files = listOf<File>(File("/Z"), File("/a"))
    println(files.sortedWith(CaseInsensitiveFileComparator))

    val persons = listOf<Person>(Person("Bob"), Person("Alice"))
    println(persons.sortedWith(Person.NameComparator))

    // companion object
    A.bar()

    val subscribingUser = User.newSubscribingUser("daegyu@gmail.com")
    val newFacebookUser = User.newFacebookUser(1)
    println(subscribingUser.nickname)      // daegyu
    println(newFacebookUser.nickname)      // 1

    val person2 = Person2.getPersonName()
    println(person2.name)    // NamDaeGyu

    val jsonText = """{"name": "John"}""".trimIndent()
    val person = Person3.fromJSON(jsonText)
    println(person.userInfo)
    println(person.userInfo.name) // 출력 결과: John
}

/*
    -----------------------------------------------------------------------------------------------
    Class, Object, Interface
    [Ch.4]: object 키워드: 클래스 선언과 인스턴스 생성

    - 객체 선언은 싱글턴을 정의하는 방법 중 하나다.
    - 동반 객체는 인스턴스 메서드는 아니지만 어떤 클래스와 관련 있는 메서드와 팩토리 메서드를 담을 때 쓰인다.
      동반 객체 메서드는 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수 있다.
    - 객체 식은 자바의 무명 내부 클래스 대신 쓰인다.
    -----------------------------------------------------------------------------------------------
 */


/*
    객체 선언: 싱글턴을 쉽게 만들기

    자바에서는 보통 클래스의 생성자를 private으로 제한하고 정적인 필드에 그 클래스의 유일한 객체를 저장하는 싱글턴 패턴
    을 통해 이를 구현한다. 반면에 코틀린은 `객체 선언 기능`을 통해 싱글턴을 언어에서 기본 지원한다.
    객체 선언은 클래스 선언과 그 클래스에 속한 단일 인스턴스의 선언을 합친 선언이다.
 */

public object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(
            file2.path,
            ignoreCase = true
        )
    }
}

// class 안에서도 객체를 선언할 수 있다. 그런 객체도 인스턴스는 물론 하나뿐이다.
data class Person(val name: String) {
    object NameComparator : Comparator<Person> {
        override fun compare(p1: Person, p2: Person): Int =
            p1.name.compareTo(p2.name)

    }
}

/*
    동반 객체: 팩토리 메서드와 정적 멤버가 들어갈 장소

    코틀린 클래스 안에는 정적인 멤버가 없다. 코틀린 언어는 자바 `static` 키워드를 지원하지 않는다.
    그 대신 코틀린에서는 패키지 수준의 최상위 함수와 객체 선언을 활용할 수 있다.

    클래스 안에 정의된 객체 중 하나에 `companion`이라는 특별한 표시를 붙이면 그 클래스의 동반 객체로 만들 수 있다.
    동반 객체의 프로퍼티나 메서드에 접근하려면 그 동반 객체가 정의된 클래스 이름을 사용한다.
 */

class A {
    companion object {
        fun bar() {
            println("Companion object called")
        }
    }
}

/*
    동반 객체(companion object)가 private 생성자를 호출하기 좋은 위치이다.
    동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다.
 */

class User private constructor(val nickname: String) {
    companion object {
        // create User by extracting "email"
        fun newSubscribingUser(email: String) =
            User(email.substringBefore('@'))
        // create User by extracting "id"
        fun newFacebookUser(id:Int) =
            User("${id}")
    }
}

/*
    동반 객체를 일반 객체처럼 사용

    동반 객체는 클래스 안에 정의된 일반 객체다. 따라서 동반 객체에 이름을 붙이거나, 동반 객체가 인터페이스를 상속하거나,
    동반 객체 안에 확장 함수와 프로퍼티를 정의할 수 있다.
 */

// 동반 객체에 이름 지정
class Person2(val name: String) {
    companion object NameLoader {
        fun getPersonName(): Person2 {
            return Person2("NamDaeGyu")
        }
    }
}

// 동반 객체에 인터페이스 구현
interface JSONFactory {
    fun fromJSON(jsonText: String) : Person3
}

class Person3(val userInfo: UserInfo) {

    // companion object implements interface
    companion object : JSONFactory {
        override fun fromJSON(jsonText: String) : Person3 {
            val parsedObject = Json.decodeFromString<UserInfo>(jsonText)
            return Person3(parsedObject)
        }
    }
}











