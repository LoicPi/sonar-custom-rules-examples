class PrefixAssignmentCheckClass{
        public void function(){
            int i = 0;
            for(;;){
                if(i>10){
                    break;
                }
                // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++i instead of i++}}
               i++;
        }
        // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++j instead of j++}}
        for(int j=0;j<10;j++){
        }

        int k=0;
        // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++k instead of k++}}
        if(k++<10){
        }
        else{
        // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++k instead of k++}}
        k++;
        }
        // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++k instead of k++}}
        while(k++<4){
        }
        do{
        // Noncompliant@+1 {{Refactor the code to avoid creating provisional variable: use ++k instead of k++}}
        k++;
        }
        while(k<4);

        }
        }