name := """benchpress-app"""
organization := "com.techmonad"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(guice, ws, jdbc)

libraryDependencies += "com.h2database" % "h2" % "1.4.194"

inConfig(Test)(PlayEbean.scopedSettings)

playEbeanModels in Test := Seq("models.*")
