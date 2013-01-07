import dsl.la.rep._
import collection.mutable.Stack
import org.scalatest._

/*
 * This tests shows the mechanics of the Rep[T] based approach.
 */
class RepSpec extends FlatSpec with ShouldMatchers {



  "A deep embedding of la with Rep types" should "compile" in {
    val x = new VectorDSL {

      def produceTuple[T]: Rep[(Vector[T], Vector[T])] = ???

      def main = {
        //TODO need way to lift Map to corresponding Rep type
//        val v0 = DenseVector(Map(1->liftTerm(1.0), 2->liftTerm(2.0), 3->liftTerm(3.0)))

        val t1: Rep[List[Vector[Double]]] = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) baseVectors
        val t2 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0))

        //TODO need way to lift functions to their Rep types
//        val test2 = t2.partition(_.eq(liftTerm(4)))

        val t3 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) dotProduct t2
        val t4 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) splice (t2, t2)

        //TODO need way to lift Tuple to Rep[Tuple]
        val tuple: Rep[(Vector[Double], Vector[Double])] = produceTuple
        val t5 = t4 spliceT (tuple)

        //TODO fix transform method
        //        val t6: Vector[Double] = t5 transform

        val v1 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0)) negate
        val res = (v1 + (DenseVector(liftTerm(3.0), liftTerm(4.0), liftTerm(5.0)) * SparseVector[Double](liftTerm(6.0), liftTerm(7.0), liftTerm(8.0))))
        val mappedRes = res.map(_ + liftTerm(1.0))
        mappedRes
      }
    }
  }
}