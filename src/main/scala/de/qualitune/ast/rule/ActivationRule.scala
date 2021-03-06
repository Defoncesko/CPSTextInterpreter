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

package de.qualitune.ast.rule

import de.qualitune.ast.ASTElement

case class Settings(interval: Int, after: Int, continuously: Boolean)

case class ActivationRule(activateFor: List[ActivationRuleVariable], when: String, bindings: List[ActivationRuleBinding], settings: Settings) extends ASTElement {
  def prettyPrint(identLevel: Int): String = {
    var ident = ""
    (1 to identLevel).foreach(e => {
      ident += "\t"
    })

    "activate for {\n" +
      activateFor.map(_.toString).mkString("\t\t" + ident, "\n\t\t" + ident, "\n\t" + ident) +
      "} when { " + when + " } with bindings {\n" +
      bindings.map(_.toString).mkString("\t\t" + ident, "\n\t\t" + ident, "\n\t" + ident) +
      "} with settings {" + "\n\t\t" + ident +
      "interval " + settings.interval + ";\n\t\t" + ident +
      "after " + settings.after + ";\n\t\t" +
      "continuously " + settings.continuously + ";\n\t" + ident + "}"
  }

  override def toString = {
    prettyPrint(0)
  }

  def getBindingForVariable(variable: ActivationRuleVariable) = {
    bindings.filter(_.variableName == variable.variableName)(0) // there cannot be more than one
  }

  def name = {
    hashCode().abs
  }
}