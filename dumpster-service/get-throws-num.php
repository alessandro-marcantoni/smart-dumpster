<?php 

    /**
     * REST API that returns the number of throws made on the dumpster.
     */

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "GET") {
        $data = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);
        $n_throws = $data["throws"];
        header('Content-type: application/json');
        $state = json_encode(array("throws" => $n_throws));
        echo $state;
    }

?>