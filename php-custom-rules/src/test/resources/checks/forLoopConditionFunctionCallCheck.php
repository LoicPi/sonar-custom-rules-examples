<?php
function my_function()
{
    $array = ["a", "b", "c"];
    $count = count($array);
    for ($j = 0; $j < $count; ++$j) {
        echo $j;
    }

    for($i=0;$i<count($array);++$i){ //Noncompliant {{Function call inside a for loop condition should not be used}}
        echo $i;
    }
}

