class PrefixAssignmentCheckClass{
        public void function(){
            int i = 0;
            for(;;){
                if(i>10){
                    break;
                }
               // i++; //NOK
               ++i;
        }

        for(int j=0;j<10;++j){ //j++ NOK
        }

        int k=0;
        if(++k<10){ //k++ NOK
        }
        else{
        ++k;   //k++ NOK
        }
        while(++k<4){ //k++ NOK
        }
        do{
        ++k; //k++    NOK
        }
        while(k<4);

        }
        }