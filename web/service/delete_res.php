
<?php 
include_once '../common/common.php';



$resUrl = $_REQUEST['resUrl'];
$resName = $_REQUEST['resName'];
$sql = "delete from res where res_url=? and res_name=?";
$param = array($resUrl,$resName);
$rc = update($sql,$param);
echo $rc;


?>