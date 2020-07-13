<?php

    /**
     * REST API that updates the weight in the dumpster.
     */

    require_once "index.php";

    $w_max = 90;

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        $data = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);
        $input = json_decode(file_get_contents("php://input"));
        array_push($data["data"], $input->value);
        if ($input->value >= $w_max) {
            $data["available"] = false;
        }
        file_put_contents($templateParams["DATA_FILE"], json_encode($data));
    }

?>