package org.sonar.samples.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

public class ForLoopConditionFunctionCallCheckTest {

    @Test
    public void test() {
        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/ForLoopConditionFunctionCallCheck.java")
                .withCheck(new ForLoopConditionFunctionCallChecks())
                .verifyNoIssues();

        JavaCheckVerifier.newVerifier()
                .onFile("src/test/files/ForLoopConditionFunctionCallCheckFail.java")
                .withCheck(new ForLoopConditionFunctionCallChecks())
                .verifyIssues();
    }
}

