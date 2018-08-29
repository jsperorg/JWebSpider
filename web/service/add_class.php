
<?php 
include_once '../common/common.php';



$className = $_REQUEST['className'];




$sql = "select * from class where class_name=?";

$param = array($className);
$result = query($sql,$param);
$count = count($result);
if($count>0){
	echo 0;//不可添加重复条目，否则返回成功0条记录。
}else{
	$sql = "insert into class values(null,?)";
	$rc = update($sql,$param);
	echo $rc;
}


?>