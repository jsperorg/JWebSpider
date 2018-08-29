<?php
include_once '../common/common.php';


$resUrl = $_REQUEST['resUrl'];
$pageNumber = $_REQUEST['pageNumber'];
$linkIndex = $_REQUEST['linkIndex'];



$rc = update('update progress set page_number=?,link_index=? where res_url=?',array($pageNumber,$linkIndex,$resUrl));
if($rc>0){
	echo 'true';
}else{
	echo 'false';
}


?>