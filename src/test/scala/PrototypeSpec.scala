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
    val x = la {
    //TODO implement this methods

      val a: Double = 5;
      val t0 = DenseVector(Array(1, 2.0)) baseVectors
      val t1 = DenseVector(a, 2, 3.0) baseVectors
      val test1: Vector[Double] = t1(0)
      val t2 = DenseVector(1.0, 2, 3.0) partition (_ == 4)
      val test2: (Vector[Double], Vector[Double]) = t2;
      val t3 = DenseVector(1.0, 2.0, 3) dotProduct t2._1
      val t4 = DenseVector(1, 2.0, 3.0) splice (t2._1, t2._2)
      val t5: Vector[Double] = t4 spliceT ((t2._1, t2._2))

      //TODO fix (implement) correct implicit for transform
      //        val t6: Vector[Double] = t5 transform
    }



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
