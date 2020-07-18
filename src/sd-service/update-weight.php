<?php

    /**
     * REST API that updates the weight in the dumpster.
     */

    require_once "index.php";

    $w_max = 90;

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        $data = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);
        $input = json_decode(file_get_contents("php://input"));
        $throw = array("date"=>date("Y-m-d"), "weight"=>$input->value);
        array_push($data["data"], $throw);
        if ($input->value >= $w_max) {
            $av = json_decode(file_get_contents($templateParams["AVAILABILITY_FILE"]), TRUE);
            $av["available"] = false;
            file_put_contents($templateParams["AVAILABILITY_FILE"], json_encode($av));
        }
        file_put_contents($templateParams["DATA_FILE"], json_encode($data));
    }

?>