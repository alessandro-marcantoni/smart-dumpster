<?php

//$json = json_decode(file_get_contents($file),TRUE);
//$json[$user] = array("first" => $first, "last" => $last);
//file_put_contents($file, json_encode($json));

$templateParams["DATA_FILE"] = "dumpster-data.json";
$templateParams["JSON_DATA"] = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);

?>


