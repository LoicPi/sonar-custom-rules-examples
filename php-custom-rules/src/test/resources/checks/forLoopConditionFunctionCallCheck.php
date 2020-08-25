<?php
function my_function(){
    $array=["a", "b", "c"];
    $count = count($array);
    for($j=0;$j<$count;++$j){
        echo $j;
    }
}