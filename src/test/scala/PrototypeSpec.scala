import collection.mutable.Stack
import org.scalatest._

import dsl.la._ 

class PrototypeSpec extends FlatSpec with ShouldMatchers {

  "A shallow embedding of la" should "produce values" in {
     val x = la {
       val v1 = DenseVector(Map(1->1,2->2,3->3))
       //val v1 = DenseVector(1,2,3)
       //val v1 = DenseVector((1,2,3))
       (v1 + (DenseVector(3,4,5) * SparseVector[Int](6,7,8)): Vector[Int]).map(_ + 1) negate
     }
     
     x should equal (DenseVector(-20, -31, -44))
  }

  it should "compile" in {
    /*val x = laLifted {
       val v1 = DenseVector(1,2,3)
       (v1 + (DenseVector(3,4,5) * SparseVector[Int](6,7,8)): Vector[Int]).map(_ + 1)
    }*/
  }
  
  // let's do baby steps first
  it should "work with a simple object" in {
    //val x = laLifted { DenseVector(1, 2) }
    //x shouldBe la.DenseVector(1,2)
  }
 
}
