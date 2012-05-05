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

package de.qualitune.interpreter

import de.qualitune.ast.rule.ActivationRule
import de.qualitune.ast.ASTElement

/**
 * @author Max Leuthaeuser
 * @since 22.11.2011
 */
class ActivationRuleInterpreter extends ASTElementInterpreter {
  // TODO make it possible to bind a variable to more than one role
  override def apply[E <: ASTElement, T <: AnyRef](s: EvaluableString, elem: E, data: T) = {
    elem match {
      case ar: ActivationRule => {
        // create variables for all bindings
        ar.bindings.foreach(b => s + ("@volatile var " + b.variableName + ": " + b.roleName + " = null\n"))

        val actorName = "Context_Activator_" + ar.name
        s + ("val " + actorName.toLowerCase + " = new " + actorName + "()\n")
        s + ("class " + actorName + " extends Actor {\ndef act() {\n")

        // timeout
        if (ar.settings.timeout > 0)
          s + ("Thread.sleep(" + ar.settings.timeout + ")\n")

        // while around everything
        s + ("while(true) {\n")

        // get all core objects and filter
        s + ("val cores = Registry.cores.filter(e => " + ar.activateFor.map("e.hasRole(\"" + _.roleName + "\")").mkString(" || ") + ")\n")
        // calculate permutations
        s + ("val perm = ListUtils.combinations(" + ar.activateFor.size + ", cores)\n")
        // iterate and check context condition
        s + ("for (c_list <- perm) {\n")
        // check if core object plays the required roles
        s + (" if (" + ar.activateFor.view.zipWithIndex.map {
          case (v, i) => "c_list(" + i + ").hasRole(\"" + v.roleName + "\")"
        }.mkString(" && ") + ") {\n")
        // and now the inner loop to get all roles
        s + ("  for(" + ar.activateFor.view.zipWithIndex.map {
          case (v, i) => v.variableName + " <- c_list(" + i + ").getRole(\"" + v.roleName + "\")"
        }.mkString("; ") + ") {\n")
        // transform the context activation condition (apply casts)
        var cond = ar.when.replaceAll(" ", "")
        // TODO test regex!
        ar.activateFor.foreach(e => cond = cond.replaceAll(e.variableName + ".", e.variableName + ".asInstanceOf[" + e.roleName + "]."))
        // check the condition
        s + ("   if(" + cond + ") {\n")
        s + (ar.bindings.map(e => {
          // get the winner
          val i = ar.activateFor.indexWhere(_.variableName == e.variableName)
          // create new instance of role
          "val new_" + e.variableName + "=new " + e.roleName + "(c_list(" + i + "))\n" +
            // update role mapping (name of role + instance)
            "c_list(" + i + ").addRole(new_" + e.variableName + ")\n" +
            // make them globally in the current scope available
            e.variableName + " = " + "new_" + e.variableName + "\n" +
            // activate role
            "new_" + e.variableName + " ! token_" + e.roleName
        }).mkString("\n"))

        // end if
        s + ("\n   exit() }\n")
        // end for
        s + ("  }\n")
        // end if
        s + (" }\n")
        // end for
        s + ("}\n")
        // end while
        s + ("Thread.sleep(" + ar.settings.interval + ")\n}\n")
        // end act method
        s + ("}\n")
        // end actor
        s + ("}\n")
        s
        // fuck. I realy need templates or stuff.
      }
      case _ => throw new IllegalArgumentException("Unknown ActivationRule type!")
    }
  }
}