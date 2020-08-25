<?php
function my_function(){
    $array=["a", "b", "c"];
    for($i=0;$i<count($array);++$i){ //NOK
        echo $i;
    }
}