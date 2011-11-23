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

import ast.{CPSProgram, Context}
import ast.variable.{InitVariableDecl, EmptyVariableDecl}
import ast.role.Role
import collection.mutable.Map

/**
 *  Object containing some methods for checking a CPSProgram statically.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSChecks {
  /**
   * Check if names are used more than once and hiding is possible to
   * generate warnings.
   *
   * @param cst: CPSProgram to check
   * @throws DuplicateNameException if the same name is used multiple times at one specific level.
   */
  def checkNames(cst: CPSProgram) {
    var contextNames = List[String]()
    var roleNames = List[String]()
    val varNames = Map[Int, String]()

    def checkContextNames(c: List[Context]) {
      c.foreach(e => {
        if (contextNames.contains(e.name))
          throw new DuplicateNameException("Context '" + e.name + "' is already defined!")
        else
          contextNames = e.name :: contextNames
      })
      c.foreach(e => {
        checkContextNames(e.inner)
      })
    }

    def checkRoleNames(c: List[Context]) {
      c.foreach(e => {
        e.roles.foreach(r => {
          if (roleNames.contains(r.name))
            throw new DuplicateNameException("Role '" + e.name + "' is already defined!")
          else
            roleNames = r.name :: roleNames
        })
      })
      c.foreach(e => {
        checkRoleNames(e.inner)
      })
    }

    def checkVariableNames(c: List[Context], level: Int = 0) {
      def matches(o: ScalaObject) {
        o match {
          case c: Context => {
            var vl = List[String]()
            c.variables.foreach(_ match {
              case va: EmptyVariableDecl => {
                val name = va.asInstanceOf[EmptyVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
              case va: InitVariableDecl => {
                val name = va.asInstanceOf[InitVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
            })
          }
          case r: Role => {
            var vl = List[String]()
            r.variables.foreach(_ match {
              case va: EmptyVariableDecl => {
                val name = va.asInstanceOf[EmptyVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
              case va: InitVariableDecl => {
                val name = va.asInstanceOf[InitVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
            })
          }
        }
      }

      // in contexts first:
      c.foreach(e => {
        matches(e)
      })
      c.foreach(e => {
        checkVariableNames(e.inner, level + 1)
      })

      // roles now:
      c.foreach(e => {
        e.roles.foreach(r => {
          matches(r)
        })
      })
    }

    /**
     * Algorithm:
     *  - run through all contexts and
     *  - run through all roles
     * and check if their names and embedded variables are unique.
     *
     * Same name for role or contexts generates error, same name for variables on top level
     * generate error, same name for variables in embedded contexts/roles will generate hiding warning.
     */
    checkContextNames(cst.contexts)
    checkRoleNames(cst.contexts)
    checkVariableNames(cst.contexts)
  }

  /**
   * Check if all given bindings from CPS object to roles are well formed,
   * hence the same assignment is not allowed to be defined more than once
   * and no binding should be left without definition and visa versa.
   *
   * @param cst: CPSProgram to check
   */
  def checkBindings(cst: CPSProgram) {

  }

  /**
   * Check if all role definitions are well formed, hence no cyclic playedBy definitions.
   *
   * @param cst: CPSProgram to check
   */
  def checkRoles(cst: CPSProgram) {

  }

  /**
   * Check if CPSObjects are well formed, hence no same IP:PORT or same name for different devices.
   *
   * @param cst: CPSProgram to check
   */
  def checkCPSObjects(cst: CPSProgram) {

  }
}