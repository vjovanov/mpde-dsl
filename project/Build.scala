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
      resolvers +=  "OSSH" at "https://oss.sonatype.org/content/groups/public",
      libraryDependencies += "org.scalatest" % "scalatest_2.10.0-RC5" % "2.0.M5-B1" % "test" 
    )).dependsOn(mpdeProj)

}
