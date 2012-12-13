package dsl.la

import scala.reflect.ClassTag

trait Vector[T] {
  protected[la] def underlying: Array[T]
  def *(v: Vector[T]): Vector[T]
  def +(v: Vector[T]): Vector[T]
}

object Vector {
  def apply[T: Numeric: ClassTag](a: T*): Vector[T] = apply(a.toArray)
  def apply[T: Numeric: ClassTag](v: Array[T]): Vector[T] = new DenseVector(v)
}

final private class DenseVector[T: Numeric: ClassTag](val x: Array[T]) extends Vector[T] {
  def underlying = x
  
  def num = implicitly[Numeric[T]]
  
  def +(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.plus(x._1, x._2))).toArray
  )
  
  def *(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.times(x._1, x._2))).toArray
  )
  
  override def equals(that: Any) = that match {
    case t: Vector[T] => t.underlying.toSeq == underlying.toSeq
    case _ => false
  }
  
  override def toString = underlying.mkString("Vector(", ",", ")") 
} 


