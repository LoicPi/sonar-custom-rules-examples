package org.sonar.samples.java.checks;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.*;

@Rule(
        key = ForLoopConditionFunctionCallChecks.KEY,
        priority = Priority.MAJOR,
        name = "Function call in For loop condition",
        tags = {"ecoscan"},
// Description can either be given in this annotation or through HTML name <ruleKey>.html located in package src/resources/org/sonar/l10n/php/rules/<repositoryKey>
        description = "<p>Function call inside a for loop condition should not be used:</p><ul><li>Example: </li><li>Replace </li><li>for($i = 0; $i < count($array); $i++)</li><li>with $count = count($array); </li><li>for($i = 0; $i < $count; $i++) </li></ul>"
)

public class ForLoopConditionFunctionCallChecks extends IssuableSubscriptionVisitor {

    public static final String KEY = "ForLoopConditionFunctionCallCheck";
    private static final String MESSAGE = "Function call inside a for loop condition should not be used";

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return ImmutableList.of(
                Tree.Kind.METHOD_INVOCATION
        );
    }

    @Override
    public void visitNode(Tree tree) {

        MethodInvocationTree methodInvocationTree = (MethodInvocationTree) tree;
        if(methodInvocationTree.parent().parent().is(Tree.Kind.FOR_STATEMENT)) {
            reportIssue(methodInvocationTree, MESSAGE + ".");
        }

        super.visitNode(tree);
    }

}
