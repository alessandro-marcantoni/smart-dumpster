<?php 

    require_once "index.php";

    if($_SERVER["REQUEST_METHOD"] == "GET") {
        $string = $templateParams["JSON_DATA"]["available"] == TRUE ? "AV" : "UN";
        header('Content-type: application/json');
        $state = json_encode(array("available" => $string));
        echo $state;
    }

?>