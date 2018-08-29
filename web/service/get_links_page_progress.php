<?php
include_once '../common/common.php';


$resUrl = $_REQUEST['resUrl'];
$result = query('select * from progress where res_url=?',array($resUrl));

$str = '';
if($result!=null && count($result)>0){
	$str .= $result[0]['page_number'].','.$result[0]['link_index'];
}else{
	update('insert into progress (progress_id,res_url,page_number,link_index) values(null,?,-1,-1);',array($resUrl));
	
	$str = '-1,-1';
}

echo $str;


?>