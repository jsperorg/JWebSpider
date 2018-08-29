<?php
include_once '../common/common.php';


$result = query('select * from class',null);

echo json_encode($result);


	
?>