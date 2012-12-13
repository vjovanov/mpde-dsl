import sbt._
import Keys._
import java.io.File

object LMSBuild extends Build {
  
  lazy val mpdeProj = RootProject(uri("./mpde/"))
 
  lazy val dsl = Project(
    id = "simple-dsl",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      scalaVersion := "2.10.0-RC5",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"
    )).dependsOn(mpdeProj)

}
