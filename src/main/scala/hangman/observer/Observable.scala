package hangman.observer

import scala.collection.mutable.ListBuffer

trait Observable[T] {
  private val observers = ListBuffer.empty[Observer[T]]

  def addObserver(observer: Observer[T]): Unit =
    if !observers.contains(observer) then observers += observer

  def removeObserver(observer: Observer[T]): Unit =
    observers -= observer

  protected def notifyObservers(value: T): Unit =
    observers.toList.foreach(_.update(value))
}
