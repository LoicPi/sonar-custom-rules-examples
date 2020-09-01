<?php
function my_function(){
    $i=0;
    for(; ; ){
        if ($i > 10) {
            break;
        }
        echo $i;
        $i++; // Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
    }

    for($j=0;$j<10;$j++){ // Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
        echo $j;
    }

    $k=0;
    if($k++<10){ // Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
        echo "$k < 10";
    }
    else{
        $k++;   // Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
    }
    while($k++<4){ //Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
        echo "$k < 4";
    }
    do{
        $k++;   // Noncompliant {{Refactor the code to avoid creating provisional variable: ++$i / $i++}}
    }
    while($k<4);
    $i=0;
    for(; ; ){
        if ($i > 10) {
            break;
        }
        echo $i;
        ++$i;
    }

    for($j=0;$j<10;++$j){
        echo $j;
    }

    $k=0;
    if(++$k<10){
        echo "$k < 10";
    }
    else{
        ++$k;
    }
    while(++$k<4){
        echo "$k < 4";
    }
    do{
        ++$k;
    }
    while($k<4);

}