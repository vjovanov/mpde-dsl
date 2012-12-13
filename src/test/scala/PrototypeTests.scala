import collection.mutable.Stack
import org.scalatest._

import dsl.la._ 

class PrototypeSpec extends FlatSpec with ShouldMatchers {

  "A Shallow embedding of la" should "produce values" in {
     val x = la {
       Vector(1,2,3) + Vector(3,4,5) * Vector(6,7,8)
     }
     
     x should equal (Vector(19, 30, 43))
  }

  it should "compile" in {
    /*val x = laLifted {
       Vector(1,2,3) + Vector(3,4,5) * Vector(6,7,8)
    }*/
  }
}
