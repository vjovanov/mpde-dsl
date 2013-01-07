import dsl.la.rep._
import collection.mutable.Stack
import org.scalatest._

/*
 * This tests shows the mechanics of the Rep[T] based approach.
 */
class RepSpec extends FlatSpec with ShouldMatchers {

  "A deep embedding of la with Rep types" should "compile" in {
    val x = new VectorDSL {
      def main = {
        //TODO problem when try to define vector via Map
//        val v0 = DenseVector(Map(1->liftTerm(1.0), 2->liftTerm(2.0), 3->liftTerm(3.0)))
        val v1 = DenseVector(liftTerm(1.0), liftTerm(2.0), liftTerm(3.0))
        val res = (v1 + (DenseVector(liftTerm(3.0), liftTerm(4.0), liftTerm(5.0)) * SparseVector[Double](liftTerm(6.0), liftTerm(7.0), liftTerm(8.0))))
        val mappedRes = res.map(_ + liftTerm(1.0))
        mappedRes
      }
    }
  }
}