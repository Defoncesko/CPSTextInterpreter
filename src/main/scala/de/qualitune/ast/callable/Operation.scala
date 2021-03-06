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

package de.qualitune.ast.callable

case class Operation(name: String, typ: String, body: String) extends Callable {
  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    var b = ""
    if (body.matches("\\s*"))
      b = " }"
    else
      b = body + "\n\t\t" + ident + "}"

    var t = ""
    if (!typ.toLowerCase.equals("void")) {
      t = ": " + typ + " = "
    }

    name + "()" + t + " {\n\t\t\t" + ident + b
  }

  override def toString = prettyPrint(0)
}