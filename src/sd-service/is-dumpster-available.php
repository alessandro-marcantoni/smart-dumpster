<?php 

    /**
     * REST API that returns the state of the dumpster ("AV" -> available, "UN" -> unavailable).
     */

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "GET") {
        $data = json_decode(file_get_contents($templateParams["AVAILABILITY_FILE"]), TRUE);
        $string = $data["available"] == TRUE ? "AV" : "UN";
        header('Content-type: application/json');
        $state = json_encode(array("available" => $string));
        echo $state;
    }

?>