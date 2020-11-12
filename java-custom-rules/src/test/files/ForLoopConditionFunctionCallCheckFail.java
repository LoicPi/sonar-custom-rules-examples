class ForLoopConditionFunctionCallCheckFailClass {
    public void function() {
        ArrayList<String> array = {"a", "b", "c"};

        // Noncompliant@+1 {{Function call inside a for loop condition should not be used.}}
        for (int i = 0; i < array.size(); ++i) {

        }

    }
}