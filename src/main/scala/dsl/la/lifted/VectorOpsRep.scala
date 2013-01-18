package dsl.la.rep

import scala.reflect.ClassTag

/*
 * This is a prototype implementation of the embedded DSL with Rep[T] types. The Rep[T] marks that the value
 * will be available in the next stage of computation. In this prototype we will use the approach similar to LMS.
 * 
 * We need to provide the interface for basic Scala library features. 
 */

//TODO create tuples class
trait Base {
  type Rep[+T]
  
  trait LiftEvidence[T, U] {
    def lift(v: T): U
  }
  
  def liftTerm[T, Ret](v: T)(implicit liftEv: LiftEvidence[T, Ret]): Ret = liftEv.lift(v) 
}

//TODO (TOASK) - Am I correct with such implementation of Numeric type (partly from scala.math.Numeric, partly based
//on other provided types in Rep
trait NumericDSL extends Base {
  trait NumericOps[T] {
    def plus(x: Rep[T], y: Rep[T]): Rep[T]
    def minus(x: Rep[T], y: Rep[T]): Rep[T]
    def times(x: Rep[T], y: Rep[T]): Rep[T]
    def negate(x: Rep[T]): Rep[T]
    def fromInt(x: Rep[Int]): Rep[T]
    def toInt(x: Rep[T]): Rep[Int]
    def toDouble(x: Rep[T]): Rep[Double]

    def zero: Rep[T]
    def one: Rep[T]

    def abs(x: Rep[T]): Rep[T]
    def signum(x: Rep[T]): Rep[Int]

    class Ops(lhs: Rep[T]) {
      def +(rhs: Rep[T]) = plus(lhs, rhs)
      def -(rhs: Rep[T]) = minus(lhs, rhs)
      def *(rhs: Rep[T]) = times(lhs, rhs)
      def unary_-() = negate(lhs)

      //TODO see compilation problems in implementation
      //this methods are implemented in terms of NumericOps
      def abs(): Rep[T] = ??? //NumericOps.this.abs(lhs)
      def toInt(): Rep[Int] = ??? //NumericOps.this.toInt(lhs)
      def toDouble(): Rep[Double] = ??? //NumericOps.this.toDouble(lhs)
    }

    implicit def mkNumericOps(lhs: Rep[T]): Ops
  }

  trait NumericOpsOf[T] extends NumericOps[T] {
    def plus(x: Rep[T], y: Rep[T]): Rep[T]
    def minus(x: Rep[T], y: Rep[T]): Rep[T]
    def times(x: Rep[T], y: Rep[T]): Rep[T]
    def negate(x: Rep[T]): Rep[T]
    def fromInt(x: Rep[Int]): Rep[T]
    def toInt(x: Rep[T]): Rep[Int]
    def toDouble(x: Rep[T]): Rep[Double]

    def zero: Rep[T] = ???
    def one: Rep[T] = ???

    def abs(x: Rep[T]): Rep[T] = ???
    def signum(x: Rep[T]): Rep[Int] = ???

    override implicit def mkNumericOps(lhs: Rep[T]) = ???
  }

  //conctete implicit objects for Numeric[Double] and Numeric[Int]
  object NumericOpsOf {
    implicit object NumericInt extends NumericOpsOf[Int] {
      def plus(x: Rep[Int], y: Rep[Int]): Rep[Int] = ???
      def minus(x: Rep[Int], y: Rep[Int]): Rep[Int] = ???
      def times(x: Rep[Int], y: Rep[Int]): Rep[Int] = ???
      def negate(x: Rep[Int]): Rep[Int] = ???

      def fromInt(x: Rep[Int]): Rep[Int] = ???
      def toInt(x: Rep[Int]): Rep[Int] = ???
      def toDouble(x: Rep[Int]): Rep[Double] = ???
    }

    implicit object NumericDouble extends NumericOpsOf[Double] {
      def plus(x: Rep[Double], y: Rep[Double]): Rep[Double] = ???
      def minus(x: Rep[Double], y: Rep[Double]): Rep[Double] = ???
      def times(x: Rep[Double], y: Rep[Double]): Rep[Double] = ???
      def negate(x: Rep[Double]): Rep[Double] = ???

      def fromInt(x: Rep[Int]): Rep[Double] = ???
      // TODO these need to return the lifted types. This means that Numeric Type needs to be changed to something else.
      def toInt(x: Rep[Double]): Rep[Int] = ???
      def toDouble(x: Rep[Double]): Rep[Double] = ???
    }
  }
}

trait IntDSL extends Base {
  //Rep versions of Int operations
  trait IntOps {
    //    def +(that: Rep[Int]): Rep[Int]
    def +(that: Rep[Double]) : Rep[Double]
    //    def *(that: Rep[Int]): [Int]
    def *(that: Rep[Double]) : Rep[Double]
    def unary_- : Rep[Int]
    def toInt : Rep[Int]
    def toDouble : Rep[Double]
  }

  //implementation of this operations (using implicit conversion to IntOpsOf class
  //before operation
  implicit class IntOpsOf(v: Rep[Int]) extends IntOps {
    //    def +(that: Rep[Int]): Rep[Int] = ???
    def +(that: Rep[Double]): Rep[Double] = ???
    //    def *(that: Rep[Int]): [Int] = ???
    def *(that: Rep[Double]) : Rep[Double] = ???
    def unary_- : Rep[Int] = ???
    def toInt : Rep[Int] = ???
    def toDouble : Rep[Double] = ???
  }

  implicit object LiftInt extends LiftEvidence[Int, Rep[Int]] {
    def lift(v: Int): Rep[Int] = ???
  }
}

//TODO (TOASK) problem to define lift methods
//where there were in default Double several methods with one parameter of different types
trait DoubleDSL extends Base {
  trait DoubleOps {
//    def +(that: Rep[Int]): Rep[Int]
    def +(that: Rep[Double]) : Rep[Double]
//    def *(that: Rep[Int]): [Int]
    def *(that: Rep[Double]) : Rep[Double]
    def unary_- : Rep[Int]
    def toInt : Rep[Int]
    def toDouble : Rep[Double]
  }

  implicit class DoubleOpsOf(v: Rep[Double]) extends DoubleOps {
    //    def +(that: Rep[Int]): Rep[Int] = ???
    def +(that: Rep[Double]): Rep[Double] = ???
    //    def *(that: Rep[Int]): [Int] = ???
    def *(that: Rep[Double]) : Rep[Double] = ???
    def unary_- : Rep[Int] = ???
    def toInt : Rep[Int] = ???
    def toDouble : Rep[Double] = ???
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

    //TODO (TOASK) - what should we do with parameters like elem of type => T
    def fill[T: ClassTag](n : Rep[Int])(elem : => Rep[T]) : ArrayOps[T] = ???
    // TODO complete
  }
  
}

trait TupleDSL extends Base {

  trait Tuple2Ops[T1, T2] extends AnyRef {
    def _1: Rep[T1]
    def _2: Rep[T2]
    def swap: Tuple2[Rep[T2], Rep[T1]]
  }

  //Wrapper to work with Rep tuples
  implicit class Tuple2OpsOf[T1, T2](v: Rep[Tuple2[T1, T2]]) extends Tuple2Ops[T1, T2] {
    def _1: Rep[T1] = ???
    def _2: Rep[T2] = ???
    def swap: Tuple2[Rep[T2], Rep[T1]] = ???
  }

  object Tuple2 {
    def apply[T1, T2](x1: T1, x2: T2): Rep[Tuple2[T1, T2]] = ???
  }

}

trait VectorDSL extends ArrayDSL with DoubleDSL with IntDSL with NumericDSL with Base with TupleDSL {

  type Vector[T] = dsl.la.Vector[T]

  //TODO (TOASK) problem how to write Double type or where to place this implicit?
  implicit def intOpsToDoubleOps(conv: Rep[Int]): Rep[Double] = ???

  trait VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]]
    def +(v: Rep[Vector[T]]): Rep[Vector[T]]
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]]
    def negate: Rep[Vector[T]]
    def length: Rep[Double]

    //returns list of Vectors - to test with Rep Types
    //TODO (TOASK) how to correctly provide Rep type?
    //Rep[List[Vector[T]] or List[Rep[Vector[T]]
    //or Rep[List[Rep[Vector[T]]]] ???
    def baseVectors: Rep[ArrayOps[Vector[T]]] //find base vectors

    def partition(fun: Rep[T] => Rep[Boolean]): Tuple2Ops[Rep[Vector[T]], Rep[Vector[T]]]

    def dotProduct(v: Rep[Vector[T]]): Rep[T]

    def splice(vs: Rep[Vector[T]]*): Rep[Vector[T]]

    //TODO (TOASK)
    //the same question with tuple Rep[(El, El)] or (Rep[El], Rep[El]) or Rep[(Rep[El], Rep[El])]
    def spliceT(v: Tuple2Ops[Rep[Vector[T]], Rep[Vector[T]]]): Rep[Vector[T]]

  }

  implicit class VectorOpsOf[T](v: Rep[Vector[T]]) extends VectorOps[T] {
    def *(v: Rep[Vector[T]]): Rep[Vector[T]] = ???
    def +(v: Rep[Vector[T]]): Rep[Vector[T]] = ???
    def map[U: Numeric: ClassTag](v: Rep[T] => Rep[U]): Rep[Vector[U]] = ???

    def negate: Rep[Vector[T]] = ???
    def length: Rep[Double] = ???

    //TODO think about correctness of usafe (Rep[List[...]] -> List[Rep[...]])
    def baseVectors: Rep[ArrayOps[Vector[T]]] = ??? //find base vectors

    //TODO model tuples - it should be Tuple(Rep[Vector[T]], Rep[Vector[T]])
    def partition(fun: Rep[T] => Rep[Boolean]): Tuple2Ops[Rep[Vector[T]], Rep[Vector[T]]] = ???

    def dotProduct(v: Rep[Vector[T]]): Rep[T] = ???

    def splice(vs: Rep[Vector[T]]*): Rep[Vector[T]] = ???

    //TODO model tuples
    def spliceT(v: Tuple2Ops[Rep[Vector[T]], Rep[Vector[T]]]): Rep[Vector[T]] = ???

    // TODO complete
  }
  
  object DenseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???

    //TODO required possibility to lift Map to Rep[Map] - map Map
    def apply[T: Numeric: ClassTag](a: Rep[Map[Int, T]]): Rep[Vector[T]] = ???
  }

  /**
   * TODO how are we going to translate to objects and yet remain modular and reusable.
   */
  object SparseVector {
    def apply[T: Numeric: ClassTag](a: Rep[T]*): Rep[Vector[T]] = ???

    //TODO incorrect - should model Map instead
    //TODO (TOASK) - what classes we should model (like Tuples) and what we can use (like Double)
    def apply[T: Numeric: ClassTag](a: Rep[Map[Int, T]]): Rep[Vector[T]] = ???
  }

}