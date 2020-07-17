<?php 

    /**
     * REST API that returns the past throws.
     */

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "GET") {
        $data = json_decode(file_get_contents($templateParams["DATA_FILE"]), TRUE);
        $n_throws = $data["data"];
        header('Content-type: application/json');
        $state = json_encode(array("data" => $data));
        echo $state;
    }

?>