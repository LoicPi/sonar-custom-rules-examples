<?php
function my_function(){
$i=0;
    for(; ; ){
        if ($i > 10) {
            break;
        }
        echo $i;
        $i++; 
    }

for($j=0;$j<10;$j++){ 
    echo $j;
}

$k=0;
if($k++<10){ 
    echo "$k < 10";
}
else{
    $k++;   
}
while($k++<4){ 
    echo "$k < 4";
}
do{
    $k++;     }
while($k<4);

}