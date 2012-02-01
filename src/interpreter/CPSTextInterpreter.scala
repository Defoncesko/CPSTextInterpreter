/**
 * CPSTextInterpreter - parses and interprets the CPSText DSL.
 * Copyright (C) 2011 Max Leuthaeuser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package interpreter

import ast.CPSProgram
import parser.CPSTextParser
import java.io.{InputStreamReader, BufferedReader, File, FileWriter}

/**
 * Interpreter for CPSText containing static methods for interpreting CPSText code and programs.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSTextInterpreter {

  private object Time {
    def apply[T](name: String)(block: => T) {
      val start = System.currentTimeMillis
      try {
        block
      } finally {
        val diff = System.currentTimeMillis - start
        println("# " + name + " completed, time taken: " + diff + " ms (" + diff / 1000.0 + " s)")
      }
    }
  }

  /**
  * Used for reading/writing to database, files, etc.
  * Code From the book "Beginning Scala"
  * http://www.amazon.com/Beginning-Scala-David-Pollak/dp/1430219890
  */
  private def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  private def writeToFile(fileName: String, data: String) =
    using(new FileWriter(fileName)) {
      fileWriter => fileWriter.write(data)
    }

  /**
   * Interprets a CPSProgram representing a piece of CPSText code.
   *
   * @param cst: the CPSProgram representing the concrete syntax tree
   * @param db: optional boolean flag, set to true if you want additional debug information printed to stdout. (predefined: false)
   */
  def interpretCST(cst: CPSProgram, db: Boolean = false) {
    // Some static checks before starting the actual interpretation.
    if (db) {
      println("Running static checks...")
      println("\t1) Checking names")
    }
    CPSChecks.checkNames(cst)
    if (db) println("\t2) Checking imports")
    CPSChecks.checkImports(cst)
    if (db) println("\t2) Checking role bindings")
    CPSChecks.checkBindings(cst)
    if (db) println("\t3) Checking roles")
    CPSChecks.checkRoles(cst)
    if (db) println("\t4) Checking CPS objects")
    CPSChecks.checkCPSObjects(cst)
    if (db) println("\t5) Checking role constrains")
    CPSChecks.checkConstrains(cst)

    println("# Starting")
    Time("Interpretation") {
      val s = new EvaluableString()
      s + ("object cpsprogram_Main {\n")
      new CPSProgramInterpreter().apply(s, cst)
      s + ("def main(args: Array[String]) { " + s.getInPlace + "} \n}")

      writeToFile("cpsprogram_Main.scala", s.toString)
      Runtime.getRuntime().exec("cmd.exe /C scalac -d temp -Xexperimental -classpath out/production/CPSTextInterpreter;lib/json.jar cpsprogram_Main.scala", null, new File(".")) // !
    }
    // TODO check classpath things here
    Time("Execution") {
      val proc = Runtime.getRuntime().exec("cmd.exe /C scala -classpath temp;out/production/CPSTextInterpreter;lib/json.jar;. cpsprogram_Main", null, new File("."))
      println("# Output: \n")
      val reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
      Stream.continually(reader.readLine()).takeWhile(_ != null).foreach(println(_))
      val exitCode = proc.waitFor()
      println("# Terminated. Exit code: " + exitCode)
    }
  }

  /**
   * Parses and interprets a String containing CPSText code.
   *
   * @param code: the piece of CPSText code you want to interpret.
   * @param db: optional boolean flag, set to true if you want additional debug information printed to stdout. (predefined: false)
   */
  def interpretCode(code: String, db: Boolean = false) {
    interpretCST(CPSTextParser.parse(code), db)
  }
}