<?php
include_once '../common/common.php';


$result = query('select * from res join class on res.class_id=class.class_id',null);

echo json_encode($result);


?>