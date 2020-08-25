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
        key = ForLoopConditionFunctionCallCheck.KEY,
        priority = Priority.MAJOR,
        name = "Function call in For loop condition",
        tags = {"brain-overload"},
// Description can either be given in this annotation or through HTML name <ruleKey>.html located in package src/resources/org/sonar/l10n/php/rules/<repositoryKey>
        description = "<p>Function call inside a for loop condition should not be used:</p><ul><li>Example: </li><li>Replace </li><li>for($i = 0; $i < count($array); $i++)</li><li>with $count = count($array); </li><li>for($i = 0; $i < $count; $i++) </li></ul>"
)
public class ForLoopConditionFunctionCallCheck extends PHPVisitorCheck  {


    public static final String KEY = "S1";
    private static final String MESSAGE = "<p>Function call inside a for loop condition should not be used</p>";


    private Deque<Set<String>> counterStack = new ArrayDeque<>();

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        counterStack.clear();
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitForStatement(ForStatementTree forStatement) {
        visitAll(forStatement.condition());

        Set<String> newCounters = new HashSet<>();
        if (!counterStack.isEmpty()) {
            newCounters.addAll(counterStack.peek());
        }
        newCounters.addAll(getCounterNames(forStatement));

        counterStack.push(newCounters);
        visitAll(forStatement.statements());
        checkFunctionCall(forStatement);
        counterStack.pop();
    }

    private void visitAll(Iterable<? extends Tree> trees) {
        for (Tree tree : trees) {
            tree.accept(this);
        }
    }

    private void checkFunctionCall(ForStatementTree forStatement) {
        condition = forStatement.condition();
        for(int i=0;i<condition.size();++i){
            if(i.is(Kind.FUNCTIONCALL)){
                context().newIssue(this, forStatement, String.format(MESSAGE, forStatement.toString()
                ));
            }
        }
    }

    private static Set<String> getCounterNames(ForStatementTree forStatement) {
        Set<String> counterNames = new HashSet<>();
        for (ExpressionTree condition : forStatement.condition()) {
            if (!condition.isEmpty()){
                counterNames.add(((FunctionCallTree) initExpression).variable().toString());
            }
        }
        return counterNames;
    }


}

