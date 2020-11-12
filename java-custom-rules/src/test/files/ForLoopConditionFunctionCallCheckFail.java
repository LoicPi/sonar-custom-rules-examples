class ForLoopConditionFunctionCallCheckFailClass {
    public void function() {
        String[] array = {"a", "b", "c"};

        for (int i = 0; i < array.length; ++i) {
            // Noncompliant@+1 {{Function call inside a for loop condition should not be used}}
        }

    }
}