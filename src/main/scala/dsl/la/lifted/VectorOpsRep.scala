package dsl.la.rep

import scala.reflect.ClassTag

/*
 * This is a prototype implementation of the embedded DSL with Rep[T] types. The Rep[T] marks that the value
 * will be available in the next stage of computation. In this prototype we will use the approach similar to LMS.
 * 
 * We need to provide the interface for basic Scala library features. 
 */
trait Base {
  type Rep[+T]
  
  trait LiftEvidence[T, U] {
    def lift(v: T): U
  }
  
  def liftTerm[T, Ret](v: T)(implicit liftEv: LiftEvidence[T, Ret]): Ret = liftEv.lift(v) 
}

trait NumericOps extends Base {
  // TODO complete 
}


trait IntDSL extends Base {
  trait IntOps {
    def +(that: Rep[Int]): Rep[Int] 
    // TODO complete
  }
  
  implicit class IntOpsOf(v: Rep[Int]) extends IntOps {
    def +(that: Rep[Int]): Rep[Int] = ???
  }

  implicit object LiftInt extends LiftEvidence[Int, Rep[Int]] {
    def lift(v: Int): Rep[Int] = ???
  }
}

trait DoubleDSL extends Base {
  trait DoubleOps {
    def +(that: Rep[Double]): Rep[Double]
    // TODO complete
  }

  implicit class DoubleOpsOf(v: Rep[Double]) extends DoubleOps {
    def +(that: Rep[Double]): Rep[Double] = ???
  }

  implicit object LiftDouble extends LiftEvidence[Double, Rep[Double]] {
    def lift(v: Double): Rep[Double] = ???
  }
}

trait ArrayDSL extends Base {
  
  trait ArrayOps[T] {
    def apply(i: Rep[Int]): Rep[T] 
    // TODO complete the list of methods
  }
  
  implicit class ArrayOpsOf[T](v: Rep[Array[T]]) extends ArrayOps[T] {
    def apply(i: Rep[Int]): Rep[T] = ??? 
    // TODO complete
  }
  
  object Array {
    def apply[T](values: T*): Rep[Array[T]] = ???
    // TODO complete
  }
  
}

trait VectorDSL extends ArrayDSL with DoubleDSL with IntDSL with NumericOps with Base {

  //TO-ASK - why here is not defined in Vector.scala Vector

  trait VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]]
    def +(v: Rep[Vector[T]]): Rep[Vector[T]]
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]]
    def negate: Vector[T] = ???
    def length: Double = ???

  }

  implicit class VectorOpsOf[T](v: Rep[Vector[T]]) extends VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]] = ???
    def +(v: Rep[Vector[T]]): Rep[Vector[T]] = ???
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]] = ???
    //TO-ASK - problem with compilation it needs override parameter
    //when we don't provide parameters to def
    //def negate: Rep[Vector[T]] = ???
    //def length: Rep[Double] = ???
    // TODO complete
  }
  
  object DenseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???
    def apply[T: Numeric: ClassTag](a: Rep[Map[Int, T]]): Rep[Vector[T]] = ???
  }

  /**
   * TODO how are we going to translate to objects and yet remain modular and reusable.
   */
  object SparseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???
    def apply[T: Numeric: ClassTag](a: Rep[Map[Int, T]]): Rep[Vector[T]] = ???
  }

}