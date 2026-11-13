package hangman.observer

trait Observer[T] {
  def update(value: T): Unit
}
