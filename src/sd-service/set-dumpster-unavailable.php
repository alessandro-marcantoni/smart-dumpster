<?php

    /**
     * REST API that updates changes the availability of the dumpster.
     */

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        $data = json_decode(file_get_contents($templateParams["AVAILABILITY_FILE"]), TRUE);
        $data["available"] = false;
        file_put_contents($templateParams["AVAILABILITY_FILE"], json_encode($data));
    }

?>