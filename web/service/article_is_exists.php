
<?php 
include_once '../common/common.php';



$articleTitle = $_REQUEST['articleTitle'];
$articleFromUrl = $_REQUEST['articleFromUrl'];


$sql = "select * from article where article_title='$articleTitle' and article_from_url=?";
$param = array($articleFromUrl);
$result = query($sql,$param);
if(count($result)>0){
	echo 'true';
}else{
	echo 'false';
}


?>