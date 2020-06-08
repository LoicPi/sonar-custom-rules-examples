<?php
function my_function(){
$i=0;
    for(; ; ){
        if ($i > 10) {
            break;
        }
        echo $i;
        //$i++; NOK
        ++$i;
    }

for($j=0;$j<10;++$j){ //$j++ NOK
    echo $j;
}

$k=0;
if(++$k<10){ //$k++ NOK
    echo "$k < 10";
}
else{
    ++$k;   //$k++ NOK
}
while(++$k<4){ //$k++ NOK
    echo "$k < 4";
}
do{
    ++$k; //$k++    NOK
    }
while($k<4);

}