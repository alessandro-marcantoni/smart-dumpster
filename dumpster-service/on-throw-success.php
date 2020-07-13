<?php

    /**
     * REST API that updates the number of throws of the dumpster.
     */

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "POST") {
        $data = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);
        $data["throws"]++;
        file_put_contents($templateParams["DATA_FILE"], json_encode($data));
    }

?>