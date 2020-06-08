/*
 * SonarQube PHP Custom Rules Example
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
package org.sonar.samples.php.checks;

import java.util.*;
import java.util.regex.Pattern;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.expression.*;
import org.sonar.plugins.php.api.tree.statement.ForStatementTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;
import org.apache.commons.lang.StringUtils;

/**
 * Example of implementation of a check by extending {@link PHPVisitorCheck}.
 * PHPVisitorCheck provides methods {@link PHPVisitorCheck#visitForStatement(ForStatementTree)} to visit loops
 * and other statements
 *<p>
 * Those methods should be overridden to process information
 * related to node and issue can be created via the context that can be
 * accessed through {@link PHPVisitorCheck#context()}.
 */
@Rule(
  key = PrefixAssignmentCheck.KEY,
  priority = Priority.MAJOR,
  name = "Avoid Post-incrementation or Post-decrementation",
  tags = {"brain-overload"},
// Description can either be given in this annotation or through HTML name <ruleKey>.html located in package src/resources/org/sonar/l10n/php/rules/<repositoryKey>
  description = "<p>Post-incrementation or Post-decrementation should not be used:</p> <ul><li>Replace $i++ with ++$i</li></ul>"
  )
public class PrefixAssignmentCheck extends PHPVisitorCheck  {

  public static final String PREFIX_PATTERN = "^.*\\+{2}|\\-{2}$";
  public static final String KEY = "S3";
  private static final String MESSAGE = "Refactor the code to avoid creating provisional variable: ++$i / $i++";
  private Pattern pattern;

  private static final Kind[] INCREMENT_DECREMENT = {
          Kind.PREFIX_INCREMENT,
          Kind.PREFIX_DECREMENT,
          Kind.POSTFIX_INCREMENT,
          Kind.POSTFIX_DECREMENT};

  @RuleProperty(
          key = "format",
          defaultValue = PREFIX_PATTERN)
          String format = PREFIX_PATTERN;

  @Override
  public void init() {
    pattern = Pattern.compile(format);
  }
  private Deque<Set<String>> counterStack = new ArrayDeque<>();

  @Override
  public void visitCompilationUnit(CompilationUnitTree tree) {
    counterStack.clear();
    super.visitCompilationUnit(tree);
  }

  @Override
  public void visitForStatement(ForStatementTree forStatement) {
    visitAll(forStatement.init());
    visitAll(forStatement.condition());
    visitAll(forStatement.update());

    Set<String> newCounters = new HashSet<>();
    if (!counterStack.isEmpty()) {
      newCounters.addAll(counterStack.peek());
    }
    newCounters.addAll(getCounterNames(forStatement));

    counterStack.push(newCounters);
    visitAll(forStatement.statements());
    counterStack.pop();
  }

  private void visitAll(Iterable<? extends Tree> trees) {
    for (Tree tree : trees) {
      tree.accept(this);
    }
  }

  @Override
  public void visitPrefixExpression(UnaryExpressionTree tree) {
    checkUnaryExpressionTree(tree);
    super.visitPrefixExpression(tree);
  }

  @Override
  public void visitPostfixExpression(UnaryExpressionTree tree) {
    checkUnaryExpressionTree(tree);
    super.visitPostfixExpression(tree);
  }

  private void checkUnaryExpressionTree(UnaryExpressionTree tree) {
    if (tree.is(INCREMENT_DECREMENT)) {
      if(isNotCompliant(tree.toString())){
        context().newIssue(this, tree, String.format(MESSAGE, tree.toString()
        ));
      }
    }
  }
  private boolean isNotCompliant(String varName) {
    return pattern.matcher(StringUtils.remove(varName, "$")).matches();
  }

  private static Set<String> getCounterNames(ForStatementTree forStatement) {
    Set<String> counterNames = new HashSet<>();
    for (ExpressionTree initExpression : forStatement.init()) {

      if (initExpression.is(Kind.ASSIGNMENT)) {
        counterNames.add(((AssignmentExpressionTree) initExpression).variable().toString());
      } else if (initExpression.is(INCREMENT_DECREMENT)) {
        counterNames.add(((UnaryExpressionTree) initExpression).expression().toString());
      }

    }
    return counterNames;
  }


}

