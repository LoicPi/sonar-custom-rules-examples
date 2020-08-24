/*
 * SonarQube Java Custom Rules Example
 * Copyright (C) 2016-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.samples.java.checks;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;
import java.util.*;

@Rule(
        key = PrefixAssignmentChecks.KEY,
        priority = Priority.MAJOR,
        name = "Avoid Post-incrementation or Post-decrementation",
        tags = {"brain-overload"},
// Description can either be given in this annotation or through HTML name <ruleKey>.html located in package src/resources/org/sonar/l10n/php/rules/<repositoryKey>
        description = "<p>Post-incrementation or Post-decrementation should not be used:</p> <ul><li>Replace $i++ with ++$i</li></ul>"
)
/**
 * To use subsctiption visitor, just extend the IssuableSubscriptionVisitor.
 */
public class PrefixAssignmentChecks  extends IssuableSubscriptionVisitor {

  public static final String KEY = "S1";
  private static final String MESSAGE = "Refactor the code to avoid creating provisional variable: use ";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Arrays.asList(
            Tree.Kind.POSTFIX_INCREMENT,
            Tree.Kind.POSTFIX_DECREMENT);
  }
  @Override
  public void visitNode(Tree tree) {
   if(tree.is(Tree.Kind.POSTFIX_INCREMENT) || tree.is(Tree.Kind.POSTFIX_DECREMENT)){
     UnaryExpressionTree unaryExpressionTree = (UnaryExpressionTree) tree;
     String letter = unaryExpressionTree.expression().toString();
     reportIssue(unaryExpressionTree, MESSAGE+"++"+letter+" instead of "+letter+"++");
   }
   super.visitNode(tree);
  }


}
