package dsl.la.norep

import scala.reflect.ClassTag
/*
 * This is a prototype implementation of the embedded DSL. In this prototype we will use the of 
 * polymorphic embedding of DSLs.  
 * 
 * DSL. Once the library is complete all method implementations should
 * remain empty so users can build their own embedded compilers. There is need to enforce more than lifting on the library user.  
 */
trait Base {

  trait LiftEvidence[T, U] {
    def lift(v: T): U
  }

  def liftTerm[T, Ret](v: T)(implicit liftEv: LiftEvidence[T, Ret]): Ret = liftEv.lift(v)
}

trait IntDSL extends DoubleDSL with Base {
  type Int = IntOps
  
  trait IntOps {
    def +(that: Int): Int
    // TODO complete
  }

  implicit object LiftInt extends LiftEvidence[scala.Int, Int] {
    def lift(v: scala.Int): Int = ???
  }

  implicit def IntOpsToDoubleOps(conv: IntOps): DoubleOps = ???
}

trait DoubleDSL extends Base {
  type Double = DoubleOps

  trait DoubleOps {
    def +(that: Double): Double
    def pow(power: Double)
    def sqrt
    // TODO complete
  }

  implicit object LiftDouble extends LiftEvidence[scala.Double, Double] {
    def lift(v: scala.Double): Double = ???
  }
}

trait NumericOps extends DoubleDSL with IntDSL with Base {
  type Numeric[T] = NumericOps[T]

  trait NumericOps[T] {
    def plus(x: T, y: T): T
    def minus(x: T, y: T): T
    def times(x: T, y: T): T
    def negate(x: T): T
    def fromInt(x: Int): T
    def toInt(x: T): Int
  }

  implicit object NumericDouble extends Numeric[DoubleOps] {
    def fromInt(x: IntOps): DoubleOps = ???
    def minus(x: DoubleOps, y: DoubleOps): DoubleOps = ???
    def negate(x: DoubleOps): DoubleOps = ???
    def plus(x: DoubleOps, y: DoubleOps): DoubleOps = ???
    def times(x: DoubleOps, y: DoubleOps): DoubleOps = ???
    // TODO these need to return the lifted types. This means that Numeric Type needs to be changed to something else.
    def toInt(x: DoubleOps): IntOps = ???
  }

  implicit object NumericInt extends Numeric[IntOps] {
    def fromInt(x: IntOps): IntOps = ???
    def minus(x: IntOps, y: IntOps): IntOps = ???
    def negate(x: IntOps): IntOps = ???
    def plus(x: IntOps, y: IntOps): IntOps = ???
    def times(x: IntOps, y: IntOps): IntOps = ???
    // TODO these need to return the lifted types. This means that Numeric Type needs to be changed to something else.
    def toInt(x: IntOps): IntOps = ???
    def compare(x: IntOps, y: IntOps): IntOps = ???
  }
}


trait ArrayDSL extends Base {
  type Array[T] = ArrayOps[T]

  trait ArrayOps[T] {
    def apply(i: Int): T
    // TODO complete the list of methods
  }

  object Array {
    def apply[T](values: T*): Array[T] = ???
    // TODO complete
  }

}

trait VectorDSL extends ArrayDSL with IntDSL with DoubleDSL with NumericOps with Base {
  type Vector[T] = VectorOps[T]

  trait VectorTransformer[T] {
    def transform(v: Vector[T]): Vector[T]
  }


  trait VectorOps[T] {
    implicit object VectorFunction extends VectorTransformer[T] {
       def transform(v: Vector[T]): Vector[T] = ???
    }

    //we didn't provide implementation - why does it compile?
    def negate: Vector[T]
    def *(v: Vector[T]): Vector[T]
    def +(v: Vector[T]): Vector[T]
    def map[U: Numeric: ClassTag](v: T => U): Vector[U]

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
    def transform(tr: VectorTransformer[T]): Vector[T] = ???
  }

  object DenseVector {
    def apply[T: Numeric: ClassTag](a: T*): Vector[T] = ???
    def apply[T: Numeric: ClassTag](a: Map[Int, T]): Vector[T] = ???
  }

  /**
   * TODO how are we going to translate to objects and yet remain modular and reusable.
   */
  object SparseVector {
    def apply[T: Numeric: ClassTag](a: T*): Vector[T] = ???
    def apply[T: Numeric: ClassTag](a: Map[Int, T]): Vector[T] = ???
  }

}
