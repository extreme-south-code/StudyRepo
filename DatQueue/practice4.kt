package com.example.kotlin_practice

fun main() {
    val b = BaseImpl(10)
    Derived(b).print()  // print --> static method

    val myDamas = CarModel("Damas 2010", VanImpl("100마력"))
    val my350z = CarModel("350Z 2008", SportImpl("350마력"))
    myDamas.carInfo() // 2. carinfo에 대한 다형성을 나타냄
    my350z.carInfo() // 동일

    // CountingSet 생성
    val countingSet = CountingSet<Int>()

    // 요소 추가
    countingSet.add(1)
    countingSet.add(2)
    countingSet.add(3)

    // 여러 요소 한 번에 추가
    val elementsToAdd = listOf<Int>(4, 5, 6)
    countingSet.addAll(elementsToAdd)

    // 결과 출력
    println("CountingSet: ${countingSet.innerSet}")
    println("Objects added: ${countingSet.objectsAdded}")
}

/*
    ----------------------------------------------------------------------
    Class, Object, Interface
    [Ch.3]: 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임
    ----------------------------------------------------------------------
 */



/*
    데이터 클래스(Data Class): 모든 클래스가 정의해야 하는 메소드 자동 생성

    어떤 클래스가 데이터를 저장하는 역할만을 수행한다면 `toString`, `equals`, `hashCode` 를 반드시 오버라이드 해야한다.
    다행이 IDE는 자동으로 그런 메소드를 정의해주고, 작성된 메소드의 정확성및 일관성을 검사해준다.

    코틀린은 더 편리하다. 바로 [data class]를 이용하는 것이다.
 */

data class Client(val name: String, val postalCode: Int)

/*
    위와 같이 class를 data class로 정의함으로써, Client 클래스는 자바에서 요구하는 모든 메소드를 포함한다.

    - 인스턴스 간 비교를 위한 equals
    - HashMap과 같은 해시 기반 컨테이너에서 키로 사용할 수 있는 hashCode
    - 클래스의 각 필드를 각 순서대로 표시하는 문자열 표현을 만들어주는 toString


    !! 데이터 클래스와 불변성: copy() 메소드

    데이터 클래스의 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 "불변 클래스"로 만들라고 권장한다.
    HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성은 필수적이다.

    데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 코틀린 컴파일러는 한 가지 편의 메서드를 제공한다.
    그 메서드는 객체를 복사하면서 일부 프로퍼티를 바꿀 수 있게 해주는 `copy` 메서드이다.

    복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 프로그램에서
    원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.
 */

//------------------------------------------------------------------------------------------------

/*
    Class Delegation(클래스 위임): by 키워드 사용

    종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다. 이럴 때 사용하는 일반적인 방법이
    `데코레이터 패턴`이다. 이 패턴의 핵심은 상속을 허용하지 않는 클래스 대신 사용할 수 있는 새로운 클래스를 만들되
    기존 클래스와 같은 인터페이스를 데코레이터가 제공하게 만들고, 기존 클래스를 데코레이터 내부에 필드로 유지하는 것이다.
    이런 접근 방법의 단점은 준비 코드가 상당히 많이 필요하다는 점이다.
 */

interface Animal {
    fun eat() {}

    // ... abstract method
}

class Cat : Animal {}
val cat = Cat()
class Robot: Animal by cat // Animal에 정의된 Cat의 모든 멤버를 Robot에 위임

/*
    위의 예시를 통해 간단히 `by를 이용한 클래스 위임`을 알아보자.
    Robot은 Cat이 가지는 모든 Animal의 메서드를 가지는데 이것을 클래스 위임(Class Delegation)이라고 한다.
    사실 `Cat은 Animal 자료형의 private 멤버로 Robot 클래스 안에 저장`되며 Cat에서 구현된 모든 Animal의 메서드는
    `정적 메서드`로 생성된다. 따라서 우리가 Robot 클래스를 사용할 때 Animal을 명시적으로 참조하지 않고도 eat()을 바로
    호출하는 것이 가능하다. (정적 메서드로 생성되므로 이미 메모리 상단에 올라간 상태이다 --> 바로 호출 가능)


    그렇다면 왜(Why) 위임을 할까?

    기본적으로 코틀린이 가지고 있는 표준 라이브러리는 open으로 정의되지 않은 클래스를 사용하고 있는데(Any 타입 제외),
    다시 말하면 모두 `final` 형태의 클래스이므로 상속이나 직접 클래스의 기능 확장이 어렵게 된다. 오히려 이렇게 어렵게
    만들어둠으로써 표준 라이브러리의 무분별한 상속에 따른 복잡한 문제를 방지할 수 있다. 따라서 필요한 경우에만 위임을 통해
    `상속과 비슷하게` 해당 클래스의 모든 기능을 사용하면서 동시에 기능을 추가 확장 구현할 수 있는 것이다.
 */

// ex) Kotlin_docs

interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() {
        println(x)
    }
}

class Derived(b: Base) : Base by b

/*
    ex 2) 자동차 예제
 */

interface Car {
    fun go(): String
}

// Interface Car의 추상 메서드 go 구현
class VanImpl(val power: String) : Car {
    override fun go() = "은 짐을 적재하며 $power 을 가집니다."
}

// Interface Car의 추상 메서드 go 구현
class SportImpl(val power: String) : Car {
    override fun go() = "은 경주용에 사용되며 $power 을 가집니다."
}

// impl으 CarModel에 위임되어 각 구현 클래스인 VanImpl과 SportImpl의 go() 메서드를
// 생성된 위임자에 맞춰 호출할 수 있다.

class CarModel(val model: String, impl: Car): Car by impl {
    fun carInfo() {
        // 1. 참조 없이 각 인터페이스 구현 클래스의 go()에 접근
        println("$model ${go()}")
    }
}

class CountingSet<T>(
        val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet {

    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}

/*
    add와 addAll을 오버라이드해서 카운터를 증가시키고, MutableCollection 인터페이스의 나머지 메서드는
    내부 컨테이너(innerSet)에게 위임한다.
 */


// 만약 위임을 사용하지 않았더라면?

class CountingSetNotDelegation<T> (
        val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T>  {
    var objectsAdded = 0

    override val size: Int
        get() = innerSet.size

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }

    override fun clear() {
        innerSet.clear()
    }

    override fun contains(element: T): Boolean {
        return innerSet.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        return innerSet.containsAll(elements)
    }

    override fun isEmpty(): Boolean {
        return innerSet.isEmpty()
    }

    override fun iterator(): MutableIterator<T> {
        return innerSet.iterator()
    }

    override fun remove(element: T): Boolean {
        objectsAdded--
        return innerSet.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        objectsAdded -= elements.size
        return innerSet.removeAll(elements)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        // TODO: retainAll 구현
        return innerSet.retainAll(elements)
    }
}





