package dsl

import ch.epfl.lamp.mpde.MPDE._
import scala.language.experimental.macros

package object la {
  def la[T](body: => T) = body
  
  def laLifted[T](block: => T) = macro lift[T]
}