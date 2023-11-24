package com.example.kotlin_practice

import java.io.Serializable
import java.lang.Exception

/*

    [2] Kotlin Class, Object, Interface

    코틀린의 클래스와 인터페이스는 자바 클래스, 인터페이스와는 약간 다르다. 예를 들어 인터페이스에 프로퍼티 선언이
    들어갈 수 있다. 자바와 달리 코틀린 선언은 기본적으로 final이며 public이다. 게다가 중첩 클래스는 기본적으로는
    내부 클래스가 아니다. 즉, 코틀린 중첩 클래스에는 외부 클래스에 대한 참조가 없다.

    코틀린 컴파일러는 번잡스러움을 피하기 위해 유용한 메소드를 자동으로 만들어준다. 클래스를 data로 선언하면 컴파일러가
    일부 표준 메소드를 생성해준다. 그리고 코틀린 언어가 제공하는 위임(delegation)을 사용하면 위임을 처리하기 위한 준비
    메소드를 직접 작성할 필요가 없다.

 */

fun main () {

}

/*
    [클래스 계층 정의]
 */


/*
    Kotlin Interface
 */

interface Clickable {
    fun click() // 일반 메소드 선언
    fun showOff() = println("I'm clickable!") // 디폴트 구현 메소드 선언
}

// 만약 다중 인터페이스 상속 관계에서 동일한 메서드가 구현되어 있다면?
interface Focusable {
    fun setFocus(b: Boolean) =
            println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}

/*
    class Button: Clickable, Focusable {
        override fun click() {
            println("I was clicked")
        }
    }

    다음의 에러가 발생한다.
    compile error:
    Class 'DoubleButton' must override public open fun showOff():
    Unit defined in com.gngsn.kotlindemo.ch4.Clickable
    because it inherits multiple interface methods of it
*/


// 그렇다면 어떻게 해결할 수 있을까?

// 방법 1: 하위 클래스에서 오버라이드하도록 강제한다.(컴파일 에러는 사라짐)
class Button1: Clickable, Focusable {
    override fun click() {
        println("Double implement Click")
    }

    override fun showOff() {
        println("I'm Button!")
    }
}

// 방법 2: super 키워드 사용 (상속)
class Button2: Clickable, Focusable {
    override fun click() {
        println("Double implement Click")
    }

    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}

// ------------------------------------------------------------------------------------------------

/*
    open, final, abstract 변경자: 기본적으로 final

    자바에서는 final로 명시적으로 상속을 금지하지 않는 모든 클래스를 다른 클래스가 상속할 수 있다.

    취약한 기반 클래스라는 문제는 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 깨져버린
    경우에 생긴다. 모든 하위 클래스를 분석하는 것은 불가능하므로 기반 클래스를 변경하는 경우 하위 클래스의 동작이 예기치
    않게 바뀔 수도 있다는 면에서 기반 클래스는 취약하다.

    이 문제를 해결하기 위해 이펙티브 자바에서는 "상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하라"라는
    조언을 한다. 이는 특별히 하위 클래스에서 오버라이드하게 의도된 클래스와 메소드가 아니라면 모두 final로 만들라는
    뜻이다.

    코틀린도 마찬가지 철학을 따른다. 코틀린의 클래스와 메소드는 기본적으로 final이다. 어떤 클래스의
    상속을 허용하려면 클래스 앞에 open 변경자를 붙여야 한다. 그와 더불어 오버라이드를 허용하고 싶은 메소드나 프로퍼티의
    앞에도 open 변경자를 붙여야 한다.
 */

open class RichButton: Clickable { // 이 클래스는 열려있다. 다른 클래스가 이 클래스를 상속할 수 있다.
    fun disable() {} // 이 함수는 파이널이다. 하위 클래스가 이 메소드를 오버라이드 할 수 없다.
    open fun animate() {} // 이 함수는 열려있다. 하위 클래스에서 이 메소드를 오버라이드 해도 된다.
    override fun click() {} // 이 함수는 (상위 클래스에서 선언된) 열려있는 메소드를 오버라이드 한다.
}

// 오버라이드하는 메소드의 구현을 하위 클래스에서 오버라이드하지 못하게 금지하려면 오버라이드하는 메소드 앞에 final을 명시해야 한다.
open class RichButton2: Clickable {
    // 'final'이 없는 'override' 메서드나 프로퍼티는 기본적으로 열려있다.
    final override fun click() {}
}

/*
    열린 클래스와 스마트 캐스트

    : 클래스의 기본적인 상속 가능 상태를 final로 함으로써 얻을 수 있는 큰 이익은 다양한 경우에 스마트 캐스트가
    가능하다는 점이다. 스마트 캐스트는 타입 검사 뒤에 변경될 수 없는 변수에만 적용 가능하다.
    클래스 프로퍼티의 경우 이는 val이면서 커스텀 접근자가 없는 경우에만 스마트 캐스트를 쓸 수 있다는 점이다.
    이 요구 사항은 또한 프로퍼티가 final이어야만 한다는 뜻이기도 하다.
    프로퍼티가 final이 아니라면 그 프로퍼티를 다른 클래스가 상속하면서 커스텀 접근자를 정의함으로써 스마트 캐스트의 요구
    사항을 깰 수 있다. 프로퍼티는 기본적으로 final이기 때문에 따로 고민할 필요 없이 대부분의 프로퍼티를 스마트 캐스트에
    활용할 수 있다.
 */

final class Animal(val name: String)

fun printAnimalName(animal: Animal) {
    if (animal is Animal) {
        // 여기서 animal이 Animal로 스마트 캐스트됨
        println("It's an animal named ${animal.name}")
    } else {
        println("Not an animal")
    }
}

open class AnimalOpen(open val name: String)

class Dog(override val name: String): AnimalOpen(name)

fun printAnimalName2(animal: AnimalOpen) {
    if (animal is Dog) {
        // 여기서 animal이 Dog로 스마트 캐스트됨
        println("It's a dog named ${animal.name}")
    } else {
        println("Not a dog")
    }
}

// 자바 처럼 코틀린에서도 클래스를 abstract로 선언할 수 있다. abstract로 선언한 추상 클래스는 인스턴스화할 수 없다.
// 따라서 추상 멤버 앞에 open 변경자를 명시할 필요가 있다.

abstract class Animated { // 이 클래스는 추상클래스이다. 해당 클래스의 인스턴스를 만들 수 없다.
    abstract fun animate() // 이 함수는 추상 함수다.
    open fun stopAnimating() {} // 추상 클래스에 속했더라도 비추상 함수는 기본적으로 파이널이지만 원한다면 open으로 오버라이드를 허용할 수 있다.
    fun animateTwice() {}
}

/*
    인터페이스 멤버의 경우 final, open, abstract를 사용하지 않는다. 인터페이스 멤버는 항상 열려있으며 final로 변경할 수 없다.
    인터페이스 멤버에게 본문이 없으면 자동으로 추상 멤버가 되지만, 그렇더라도 따로 멤버 선언 앞에 abstract 키워드를 덧붙일 필요가 없다.
 */

// -------------------------------------------------------------------------------------------------

/*
    가시성 변경자: 기본적으로 공개
 */

internal open class TalkativeButton: Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's a talk")
}

/*
    fun TalkativeButton.giveSpeech() {   // Talkative class는 internal하기 때문에 컴파일 에러 발생
        yell() // yell은 private 함수이므로 컴파일 에러 발생
        whisper // whisper역시 protected 함수이므로 컴파일 에러 발생
    }

    코틀린은 public 함수인 giveSpeech 안에서 그보다 가시성이 더 낮은(이 경우 internal) 타입인 TalkativeButton
    을 참조하지 못하게 한다.

    자바에서는 같은 패키지 안에서 protected 멤버에 접근할 수 있지만, 코틀린에서는 그렇지 않다는 점에서 자바와 코틀린의
    protected가 다르다.
 */

// -------------------------------------------------------------------------------------------------

/*
    내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

    자바처럼 코틀린에서도 클래스 안에 다른 클래스를 선언할 수 있다. 자바와의 차이는 코틀린의 중첩 클래스는 명시적으로
    요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다는 점이다.
 */

// View 요소를 정의하는 아래의 인터페이스가 존재한다.
interface State: Serializable

interface View {
    fun getCurentState(): State
    fun restoreState(state: State) {}
}

/*
    코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다. 이를 내부 클래스로 변경해서 바깥쪽
    클래스에 대한 참조를 포함하게 만들고 싶다면 inner 변경자를 붙여야 한다.
 */

class Button: View {
    override fun getCurentState(): State = ButtonState();
    override fun restoreState(state: State) {}
    class ButtonState: State {}
}

class Outer {
    var text = "Outer Class"

    class Nested{
        fun introduce(){
            println("Nested class")
        }
    }

    inner class Inner {
        private var text = "Inner Class"

        fun introduceInner() {
            println(text)
        }

        fun introduceOuter() {
            println(this@Outer.text)
        }
    }
}

// ---------------------------------------------------------------------------------------------

/*
    봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
 */


interface Expr
class Num(val value: Int): Expr
class Sum(val left: Expr, val right: Expr): Expr

fun eval(e: Expr): Int =
        when (e) {
            is Num -> e.value
            is Sum -> eval(e.right) + eval(e.left)
            else -> // Num과 Sum이 아닌 경우를 처리하는 default "else"분기를 반드시 넣어줘야 한다.
                throw IllegalArgumentException("Unknown expression")
        }

/*
    항상 디폴트 분기를 추가하는게 편하지는 않다. 그리고 디폴트 분기가 있으면 이런 클래스 계층에 새로운 하위 클래스를
    추가하더라도 컴파일러가 when이 모든 경우를 처리하는지 제대로 검사할 수 없다. 혹 실수로 새로운 클래스 처리를
    잊어버렸더라도 디폴트 분기가 선택되기 때문에 심각한 버그가 발생할 수 있다.

    코틀린은 이런 문제에 대한 해법을 제공한다. sealed 클래스가 그 답이다. 상위 클래스에 sealed 변경자를 붙이면
    그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다. sealed 클래스의 하위 클래스를 정의할 때는 반드시
    상위 클래스 안에 중첩시켜야 한다.
 */

sealed class Expr2 {
    class Num(val value: Int): Expr2()
    class Sum(val left: Expr, val right: Expr): Expr2()
}

fun eval2(e: Expr2): Int =
        when (e) {
            is Expr2.Num -> e.value
            is Expr2.Sum -> eval(e.right) + eval(e.left)
        }

// ------------------------------------------------------------------------------------------------
