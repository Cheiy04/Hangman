package hangman.observer

trait Observable[T] {
  private var observers: Set[Observer[T]] = Set.empty

  def addObserver(observer: Observer[T]): Unit =
    observers += observer

  def removeObserver(observer: Observer[T]): Unit =
    observers -= observer

  protected def notifyObservers(value: T): Unit =
    observers.foreach(_.update(value))
}
