trait Iterator[A]:
  def hasNext : Boolean
  def next() : A

class IntIterator(to : Int) extends Iterator[Int] :
  private var current = 0
  override def hasNext = current < to
  override def next() =
    if hasNext then
      val t = current
      current +=1
      t
    else
      0
end IntIterator

val iterator = IntIterator(5)
iterator.next()

iterator.hasNext
iterator.next()
iterator.next()
iterator.next()
iterator.next()
iterator.next()

trait Pet:
  val name : String
  def age : Int


class dog(val name : String, val age : Int) extends Pet
class cat(val name : String, val age : Int) extends Pet

val Thomas = dog("Tubby", 12)
val Kitty = cat("Nyauu", 21)

s"The dog is calle ${Thomas.name} and he is ${Thomas.age} yrs"
s"The cat is ${Kitty.name}"
"just a string"
67