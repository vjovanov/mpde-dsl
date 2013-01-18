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

//TODO (possible) remove AnyValDSL
trait AnyValDSL extends Base {
  type AnyVal = AnyValOps

  trait AnyValOps {
  }
}

trait AnyRefDSL extends Base {
  type AnyRef = AnyRefOps

  trait AnyRefOps {
  }
}

trait IntDSL extends Base with AnyValDSL with DoubleDSL {
  type Int = IntOps

  //TODO (TOASK) where do we provide the implementation for this methods (in result DSL object)
  trait IntOps extends AnyVal {
    def +(that: Int): Int
    def +(that: Double) : Double
    def *(that: Int): Int
    def *(that: Double) : Double
    def unary_- : Int
    def toInt : Int
    def toDouble : Double
  }

  implicit object LiftInt extends LiftEvidence[scala.Int, Int] {
    def lift(v: scala.Int): Int = ???
  }

  implicit def intOpsToDoubleOps(conv: Int): Double = ???
}

//TODO (TOASK) problem how to provide access (import) to correct Int class
//if I add with IntDSL - it's compilation problem because it's impossible to
//resolve correct Double type
trait DoubleDSL extends Base with AnyValDSL {
  type Double = DoubleOps

  trait DoubleOps extends AnyVal{
    def +(that: Double): Double
//    def +(that: Int): Double

    def *(that: Int): Double
    def *(x: Double): Double
//    def toInt: Int
    def toDouble: Double
    def unary_- : Double


    //TODO this defs not from original Double and just to provide
    //implementation for Vector operations
    def pow(power: Double)
    def sqrt
  }

  //TODO (TOASK) maybe extends LiftEvidence[scala.Double, DoubleDSL#Double]
  implicit object LiftDouble extends LiftEvidence[scala.Double, Double] {
    def lift(v: scala.Double): Double = ???
  }
}

//TODO (TOASK) check the correctness of this implementation (correctness for DSL usage)
//maybe implement Ordering to get it complexier
//maybe rename to NumericLifted
trait NumericDSL extends Base with AnyRefDSL with IntDSL with DoubleDSL {
  type Numeric[T] = NumericOps[T]

  trait NumericOps[T] extends AnyRef {
    def plus(x: T, y: T): T
    def minus(x: T, y: T): T
    def times(x: T, y: T): T
    def negate(x: T): T
    def fromInt(x: Int): T
    def toInt(x: T): Int
    def toDouble(x: T): Double

    def zero = fromInt(liftTerm(0))
    def one = fromInt(liftTerm(1))

    def abs(x: T): T = ???
    def signum(x: T): Int = ???

    class Ops(lhs: T) {
      def +(rhs: T) = plus(lhs, rhs)
      def -(rhs: T) = minus(lhs, rhs)
      def *(rhs: T) = times(lhs, rhs)
      def unary_-() = negate(lhs)

      def abs(): T = NumericOps.this.abs(lhs)
      def toInt(): Int = NumericOps.this.toInt(lhs)
      def toDouble(): Double = NumericOps.this.toDouble(lhs)
    }

    implicit def mkNumericOps(lhs: T): Ops = new Ops(lhs)
  }

  //companion for train Numeric Ops
  //here we can provide all implicit objects
  object NumericOps {

    implicit object NumericInt extends Numeric[Int] {
      def plus(x: Int, y: Int): Int = ???
      def minus(x: Int, y: Int): Int = ???
      def times(x: Int, y: Int): Int = ???
      def negate(x: Int): Int = ???

      def fromInt(x: Int): Int = ???
      def toInt(x: Int): Int = ???
      def toDouble(x: Int): Double = ???
    }

    implicit object NumericDouble extends Numeric[Double] {
      def plus(x: Double, y: Double): Double = ???
      def minus(x: Double, y: Double): Double = ???
      def times(x: Double, y: Double): Double = ???
      def negate(x: Double): Double = ???
      def fromInt(x: Int): Double = ???
      // TODO these need to return the lifted types. This means that Numeric Type needs to be changed to something else.
      def toInt(x: Double): Int = ???
      def toDouble(x: Double): Double = ???
    }
  }

  //TODO find another place
  //TODO (TOASK) problem how to write Double type or where to place this implicit?
  //TODO if it works correctly in IntDSL and check DEPENDENCIES (correctness mixing of DoubleDSL in IntDSL
//  implicit def intOpsToDoubleOps(conv: Int): Double = ???
}

//TODO (TOASK) why do we need ArrayDSL - do we need to use in Vector traits
//or just to test separately
trait ArrayDSL extends Base with AnyRefDSL with IntDSL {
  type Array[T] = ArrayOps[T]

  trait ArrayOps[T] extends AnyRef {
    def apply(i: Int): T
    // TODO complete the list of methods
  }

  object Array {
    def apply[T](values: T*): Array[T] = ???
    def fill[T: ClassTag](n : Int)(elem : => T) : Array[T] = ???
    // TODO complete
  }

}

trait VectorDSL extends ArrayDSL with AnyRefDSL with IntDSL with DoubleDSL with NumericDSL with Base {
  type Vector[T] = VectorOps[T]

  //TODO test and correct its usage
  trait VectorTransformer[T] {
    def transform(v: Vector[T]): Vector[T]
  }


  trait VectorOps[T] extends AnyRef{
    //TODO test and correct its usage
    implicit object VectorFunction extends VectorTransformer[T] {
       def transform(v: Vector[T]): Vector[T] = ???
    }

    def negate: Vector[T]
    def *(v: Vector[T]): Vector[T]
    def +(v: Vector[T]): Vector[T]
    def map[U: Numeric: ClassTag](v: T => U): Vector[U]

    //TODO it's a bad idea to provide here implementation
    def baseVectors: List[Vector[T]] = ??? //find base vectors

    def partition(fun: T => Boolean): (Vector[T], Vector[T])

    def dotProduct(v: Vector[T]): T

    def splice(vs: Vector[T]*): Vector[T]

    def spliceT(v: (Vector[T], Vector[T])): Vector[T]

    def transform(tr: VectorTransformer[T]): Vector[T]
  }

  object DenseVector {
//    def apply(a: Double*): Vector[Double] = ???
    def apply[T <: Double,  Double: Numeric: ClassTag](a: T*): Vector[T] = ???
//    def apply[T <: AnyVal: Numeric: ClassTag](a: Map[Int, T]): Vector[T] = ???
  }

  /**
   * TODO how are we going to translate to objects and yet remain modular and reusable.
   */
  object SparseVector {
    def apply[T: Numeric: ClassTag](a: T*): Vector[T] = ???

    //TODO required to model Map to use
    def apply[T: Numeric: ClassTag](a: Map[Int, T]): Vector[T] = ???
  }

}
