package com.example.kotlin_practice
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

fun main(args: Array<String>) {
    showProgress(50)

    println(Person("Sam", 35).isOlderThan(Person("Amy", 42)))
    println(Person("Sam", 35).isOlderThan(Person("Jane")))

    val i = 1
    val l2: Long = i.toLong() // 꼭 toLong()을 해주어야 한다. 원래는 i가 Int이고, 이를 자바와는 다르게 자동으로 변환해주지 않는다.

//    val company = Company(null)
//    val address = company.address ?: fail("No address")
//    println(address.city)

    val letters = Array<String>(26) { i -> ('a' + i).toString() }
    println(letters.joinToString(""))

    val strings = listOf<String>("a", "b", "c")
    println("%s/%s/%s".format(*strings.toTypedArray()))

    val squares = IntArray(5) { i -> (i + 1) * (i + 1)}
    println(squares.joinToString())

    args.forEachIndexed { index, element ->
        println("Argument $index is: $element")
    }
}

/*
    ------------------------------------------------------------------------------------------------
    [코틀린 타입 시스템]
    ------------------------------------------------------------------------------------------------
 */



/*
    Nullable

    나중에 초기화할 프로퍼티 (late initialize property)

    코틀린에서 클래스 안의 널이 될 수 없는 프로퍼티를 생성자 안에서 초기화하지 않고 특별한 메서드 내부에서 초기화 할 순 없다.
    코틀린에서는 일반적으로 생성자에서 모든 프로퍼티를 초기화 해야 한다. 게다가 프로퍼티 타입이 널이 될 수 없는 타입이라면
    반드시 널이 아닌 값으로 그 프로퍼티를 초기화 해야 한다. 그런 초기화 값을 제공할 수 없으면 널이 될 수 있는 타입을 사용할
    수 밖에 없다. 하지만 널이 될 수 있는 타입을 사용하면 모든 프로퍼티 접근에 널 검사를 넣거나 !! 연산자를 써야 한다.
 */

class MyService {
    fun performAction(): String = "foo"
}

class MyTest {
    private lateinit var myService: MyService // lateinit을 통해 초기화를 미룸

    @BeforeEach
    fun setup() {
        myService = MyService() // setup 메서드 안에서 진짜 초깃값을 지정한다.
    }

    @Test
    fun testAction() {
        Assertions.assertEquals("foo", myService.performAction())
    }
}

fun showProgress(progress: Int) {
    val percent = progress.coerceIn(0, 100)
    println("We're ${percent}% done!")
}

data class Person(
    val name: String,
    val age: Int? = null
) {
    fun isOlderThan(other: Person): Boolean? {
        if (age == null || other.age == null)
            return null
        return age > other.age
    }
}

/*
    Unit 타입: 코틀린의 void

    코틀린의 Unit 타입은 자바 void와 같은 기능을 한다. 관심을 가질 만한 내용을 전혀 반환하지 않는 함수의 반환 타입으로
    Unit을 쓸 수 있다. 이는 반환 타입 선언 없이 정의한 블록이 본문인 함수와 같다.

    조금 더 부연 설명을 하자면 코틀린은 기본적으로 원시 타입(primitive type)을 사용하지 않고, 기존의 문자/숫자/불리언
    값을 표현하던 타입들도 전부 클래스로 만들어 실제 런타임 시에 원시 타입으로 변환되도록 하고 있다.

    그래서 모든 타입은 Any(자바의 Object)를 상속받도록 하고 있는데, void를 Unit으로 래핑해서 사용하는 것도 같은 이유라
    볼 수 있다.

    Unit은 특별한 상태나 행동을 가지고 있지 않기 때문에 싱글턴으로 생성되어 실제로는 하나의 인스턴스를 사용한다.
 */

interface Processor<T> {
    fun process(): T
}

class NoResultProcessor : Processor<Unit> {
    override fun process() {
        // 업무 처리 코드 -> 여기서 return을 명시할 필요가 없다.
    }
}

/*
    Nothing 타입: 이 함수는 결코 정상적으로 끝나지 않는다.

    코틀린도 위의 경우에 관례적으로 Void를 쓸 수도 있었겠지만, `Nothing`이란 색다른 기능이 존재한다.
    코틀린에는 결코 성공적으로 값을 돌려주는 일이 없으므로 '반환 값'이라는 개념 자체가 의미 없는 함수가 일부 존재한다.
 */

fun fail(message: String) : Nothing {
    throw IllegalStateException(message)
}

data class Address(val city: String)

data class Company(val address: Address?)







// ---------------------------------------------------------------------------------------------


/*
    컬렉션과 배열

    널 가능성과 컬렉션

    컬렉션 안에 널 값을 넣을 수 있는지 여부는 어떤 변수의 값이 널이 될 수 있는지 여부와 마찬가지로 중요하다.
 */

fun addValidNumbers(numbers: List<Int?>) {
    var sumOfValidNumbers = 0
    var invalidNumbers = 0

    numbers.forEach { number ->
        if (number != null) {
            sumOfValidNumbers += number
        } else {
            invalidNumbers++
        }
    }
    println("Sum of valid numbers: $sumOfValidNumbers")
    println("Invalid numbers: $invalidNumbers")
}

fun addValidNumbersWithFilter(numbers: List<Int?>) {
    val validNumbers = numbers.filterNotNull()
    println("Sum of valid numbers: ${validNumbers.sum()}")
    println("Invalid numbers: ${numbers.size - validNumbers.size}")
}

/*
    읽기 전용과 변경 가능한 컬렉션

    코틀린 컬렉션과 자바 컬렉션을 나누는 가장 중요한 특성 하나는 `코틀린에서는` 컬렉션안의 데이터에 접근하는 인터페이스와
    컬렉션 안의 데이터를 변경하는 인터페이스를 분리했다는 점이다.

    일반적인 읽기 전용 라이브러리를 사용하려면 kotlin.collection.Collection 라이브러리를 사용하면 된다. 그러나
    컬렉션의 데이터를 수정하려면 kotlin.collection.MutableCollection 인터페이스를 사용하면 원소를 추가하거나,
    삭제하거나, 컬렉션 안의 원소를 모두 지우는 등의 메소드를 더 제공한다.
 */

/*
    객체의 배열과 원시 타입의 배열

    `코틀린 배열`은 타입 파라미터를 받는 클래스이다. 배열의 원소 타입은 바로 그 타입 파라미터에 의해 정해진다.
    코틀린에서 배열을 만드는 방법은 다양하다.

    - `arrayOf` 함수에 원소를 넘기면 배열을 만들 수 있다.
    - `arrayOfNulls` 함수에 정수 값을 인자로 넘기면 모든 원소가 null이고 인자로 넘긴 값과 크기가 같은 배열을 만들 수
       있다. 물론 원소 타입이 널이 될 수 있는 타입인 경우에만 이 함수를 쓸 수 있다.
    - `Array` 생성자는 배열 크기와 람다를 인자로 받아서 람다를 호출해서 각 배열 원소를 초기화해준다.
      `arrayOf`를 쓰지 않고 각 원소가 널이 아닌 배열을 만들어야 하는 경우 이 생성자를 사용한다.

      예시 코드는 main() 함수에서 ...
 */

/*
    - 코틀린은 널이 될 수 있는 타입을 지원해 NullPointerException 오류를 컴파일 시점에 감지할 수 있다.
    - 코틀린의 안전한 호출(?.), 엘비스 연산자(?:), 널 아님 단언(!!), let 함수 등을 사용하면 널이 될 수 있는 타입을
      간결한 코드로 다룰 수 있다.
    - as? 연산자를 사용하면 값을 다른 타입으로 취급한다. 개발자는 플랫폼 타입을 널이 될 수 있는 타입으로도, 널이 될 수
      없는 타입으로도 사용할 수 있다.
    - 코틀린에서는 수를 표현하는 타입(Int 등)이 일반 클래스와 똑같이 생겼고 일반 클래스와 똑같이 동작한다. 하지만
      대부분 컴파일러는 숫자 타입을 자바 원시 타입(int 등)으로 컴파일한다.
    - 널이 될 수 있는 원시 타입(Int? 등)은 자바의 박싱한 원시 타입에 대응한다.
    - Any 타입은 다른 모든 타입의 조상 타입이며, 자바의 Object에 해당한다. Unit은 자바의 void와 비슷하다.
    - 정상적으로 끝나지 않는 함수의 반환 타입을 지정할 때 Nothing 타입을 사용한다.
    - 코틀린 컬렉션은 표준 자바 컬렉션 클래스를 사용한다. 하지만 코틀린은 자바보다 컬렉션을 더 개선해서 읽기 전용 컬렉션과
      변경 가능한 컬렉션을 구별해 제공한다.
    - 자바 클래스를 코틀린에서 확장하거나 자바 인터페이스를 코틀린에서 구현하는 경우 메서드 파라미터의 널 가능성과 변경
      가능성에 대해 깊이 생각해야 한다.
    - 코틀린의 Array 클래스는 일반 제네릭 클래스처럼 보인다. 하지만 Array는 자바 배열로 컴파일 된다.
    - 원시 타입의 배열은 `IntArray`와 같이 각 타입에 대한 특별한 배열로 표현된다.
 */




