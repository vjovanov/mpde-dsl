import dsl.la.norep._
import dsl.la._
import collection.mutable.Stack

import org.scalatest._

/*
 * This tests shows the mechanics of the Rep[T] based approach.
 */
class NoRepSpec extends FlatSpec with ShouldMatchers {

  "A deep embedding of la without Rep types" should "compile" in {
    val x = new VectorDSL {
      def main = {
        val t1 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) baseVectors
        val test1: Vector[Double] = t1(0)
        val t2 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) partition (_.eq(liftTerm(4)))
        val test2: (Vector[Double], Vector[Double]) = t2;
        val t3 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) dotProduct t2._1
        val t4 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) splice (t2._1, t2._2)
        val t5: Vector[Double] = t4 spliceT ((t2._1, t2._2))
//        val t6: Vector[Double] = t5 transform

        val v0 = DenseVector(Map(liftTerm(1)->liftTerm(1.0), liftTerm(2)->liftTerm(2.0), liftTerm(3)->liftTerm(3.0))) negate
        val v1 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) negate
        val res = (v1 + (DenseVector(liftTerm(3.0), liftTerm(4.0), liftTerm(5.0)) * SparseVector[Double](liftTerm(6), liftTerm(7.0), liftTerm(8.0)))) negate
        val mappedRes = res.map(_ + liftTerm(1.0)).negate
        mappedRes
      }
    }
  }
  
  "Baby steps first: No code at all. Just return a constant!!" should "compile" in {
    val out = 1
    var varOut = 2
    laLifted ("test", {val x = Vector(out,varOut,3); var y = x; ();})
  }
}