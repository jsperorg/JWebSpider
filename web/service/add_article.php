
<?php 
include_once '../common/common.php';



$resId = $_REQUEST['resId'];
$articleTitle = $_REQUEST['articleTitle'];
$articleAuthor = $_REQUEST['articleAuthor'];
$articlePublishTime = $_REQUEST['articlePublishTime'];
//$articlePublishTime2 = $_REQUEST['articlePublishTime2'];
$articleFromUrl = $_REQUEST['articleFromUrl'];
$articleContent = $_REQUEST['articleContent'];

/* 
if(isset($articlePublishTime2)){
	$articlePublishTime = $articlePublishTime." ".$articlePublishTime2;
}
 */

$sql = "insert into article values(null,?,?,?,?,?,?)";

$param = array($resId,$articleTitle,$articleAuthor,$articlePublishTime,$articleFromUrl,$articleContent);
$rc = update($sql,$param);
echo $rc;



?>