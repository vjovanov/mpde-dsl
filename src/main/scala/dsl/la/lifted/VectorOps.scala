package dsl.la.rep

import scala.reflect.ClassTag

/*
 * This is a prototype implementation of the embedded DSL. In the first instance it can just print what is invoked. 
 * In this prototype we will use the approach similar to LMS.  
 * 
 * DSL. Once the library is complete all method implementations should
 * remain empty so users can build their own embedded compilers. There is need to enforce more than lifting on the library user.  
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
  
  implicit def toIntOps(v: Rep[Int]): IntOps = new IntOpsOf(v)
  class IntOpsOf(v: Rep[Int]) extends IntOps {
    def +(that: Rep[Int]): Rep[Int] = ???
  }

  implicit object LiftInt extends LiftEvidence[Int, Rep[Int]] {
    def lift(v: Int): Rep[Int] = ???  
  }
}



trait ArrayDSL extends Base {
  
  trait ArrayOps[T] {
    def apply(i: Rep[Int]): Rep[T] 
    // TODO complete the list of methods
  }
  
  implicit def toArrayOps[T](v: Rep[Array[T]]): ArrayOps[T] = new ArrayOpsOf(v)
  class ArrayOpsOf[T](v: Rep[Array[T]]) extends ArrayOps[T] {
    def apply(i: Rep[Int]): Rep[T] = ??? 
    // TODO complete
  }
  
  object Array {
    def apply[T](values: T*): Rep[Array[T]] = ???
    // TODO complete
  }
  
}



trait VectorDSL extends ArrayDSL with IntDSL with NumericOps with Base {
  
  trait VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]]
    def +(v: Rep[Vector[T]]): Rep[Vector[T]]
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]]
  }

  implicit def toVectorOps[T](v: Rep[Vector[T]]): VectorOps[T] = new VectorOpsOf(v)
  class VectorOpsOf[T](v: Rep[Vector[T]]) extends VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]] = ??? // here we pass the nodes
    def +(v: Rep[Vector[T]]): Rep[Vector[T]] = ???
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]] = ???
    // TODO complete
  }
  
  object DenseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???
  }

  /**
   * TODO how are we going to translate to objects and yet remain modular and reusable.
   */
  object SparseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???
  }

}