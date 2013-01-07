package dsl.la

import scala.reflect.ClassTag
import scala.collection.mutable.WrappedArray

trait Vector[T] {
  type VectorTransformer[T] = Vector[T]=>Vector[T]

  //TODO refactoring of DENSE and SPARSE vectors
  protected[la] def underlying: IndexedSeq[T]
  def *(v: Vector[T]): Vector[T]
  def +(v: Vector[T]): Vector[T]

  //method without parameters which should return List of Vectors (our type)
  def negate: Vector[T]

  //method without parameters which should return Scala standart type
  def length: Double

  //returns list of Vectors - to test with Rep Types
  def baseVectors: List[Vector[T]] = ??? //find base vectors

  //divide vector on 2 vectors according to condition
  //to see behaviour of tuple when tuple is a result
  def partition(fun: T => Boolean): (Vector[T], Vector[T]) = ???

  def dotProduct(v: Vector[T]): T = ???

  def splice(vs: Vector[T]*): Vector[T] = ???
  //to see behaviour of tuples when they behave as parameter type
  def spliceT(v: (Vector[T], Vector[T])): Vector[T] = ???

  //to see behaviour of functional type parameter with generics
  def transform[U: Numeric: ClassTag: VectorTransformer]: Vector[T] = ???

  def map[U: Numeric: ClassTag](v: T => U): Vector[U]
}


//Maybe we can provide companion object for Vector for implementation of common apply methods

object DenseVector {
  def apply[T: Numeric: ClassTag](a: T*): Vector[T] = apply(a.toArray)
  def apply[T: Numeric: ClassTag](v: Array[T]): Vector[T] = new DenseVector(v)

  //TODO change implementation for Dense Vector
  def apply[T: Numeric: ClassTag](v: Map[Int, T]): Vector[T] = new DenseVector(v.values.seq.toArray)

  //ERROR when using with apply(a: T*)
  //def apply[T: Numeric: ClassTag](v: (T, T, T)): Vector[T] = new SparseVector[T](List(v._1, v._2, v._3))
}

final private class DenseVector[T: Numeric: ClassTag](val x: Array[T]) extends Vector[T] {
  def underlying = x
  
  def num: Numeric[T] = implicitly[Numeric[T]]
  
  def +(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.plus(x._1, x._2))).toArray
  )
  
  def *(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.times(x._1, x._2))).toArray
  )

  def negate = new DenseVector[T](underlying.map(num.negate(_)).toArray)

  //def length = scala.math.sqrt(num.toDouble(underlying.map((x) => num.times(x, x)).sum))
  def length = scala.math.sqrt(num.toDouble(underlying.map((x) => num.times(x, x)).sum))

  def map[U: Numeric: ClassTag](f: T => U): Vector[U] = new DenseVector(underlying.map(f).toArray)
  
  override def equals(that: Any) = that match {
    case t: Vector[T] => t.underlying.toSeq == underlying.toSeq
    case _ => false
  }
  
  override def toString = underlying.mkString("DenseVector(", ",", ")")
}


object SparseVector {
  // TODO we need index, value tuples. For that we need tuples
  def apply[T: Numeric: ClassTag](a: T*): Vector[T] = apply(a.toList)
  def apply[T: Numeric: ClassTag](v: List[T]): Vector[T] = new SparseVector(v)

  //apply to Tuples (of course I didn't provide apply for all TupleN classes)
  //ERROR when using with apply(a: T*)
  //def apply[T: Numeric: ClassTag](v: (T, T, T)): Vector[T] = new SparseVector[T](List(v._1, v._2, v._3))
  def apply[T: Numeric: ClassTag](v: Map[Int, T]): Vector[T] = new SparseVector(v.values.seq.toList)

}

final private class SparseVector[T: Numeric: ClassTag](val x: List[T]) extends Vector[T] {
  def underlying: WrappedArray[T] = WrappedArray.make(x.toArray) // imagine here we have abstraction over the sparse representation
  def num = implicitly[Numeric[T]]
  
  // these can be slow, we do not care
  def +(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.plus(x._1, x._2))).toArray
  )
  
  // these can be slow, we do not care
  def *(v: Vector[T]) = new DenseVector[T](
      ((underlying zip v.underlying) map ((x: (T, T)) =>  num.times(x._1, x._2))).toArray
  )

  def negate = new SparseVector[T](underlying.map(num.negate(_)).toList)

  def length = scala.math.sqrt(num.toDouble(underlying.map((x) => num.times(x, x)).sum))
  
  def map[U: Numeric: ClassTag](f: T => U): Vector[U] = new SparseVector(underlying.map(f).toList)
    
  override def equals(that: Any) = that match {
    case t: Vector[T] => t.underlying.toSeq == underlying.toSeq
    case _ => false
  }
  
  override def toString = underlying.mkString("SparseVector(", ",", ")")
}