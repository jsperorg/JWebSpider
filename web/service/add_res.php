
<?php 
include_once '../common/common.php';



$resUrl = $_REQUEST['resUrl'];
$classId = $_REQUEST['classId'];
$resName = $_REQUEST['resName'];
$resLinksStart = $_REQUEST['resLinksStart'];
$resLinksEnd = $_REQUEST['resLinksEnd'];
$resTitleStart = $_REQUEST['resTitleStart'];
$resTitleEnd = $_REQUEST['resTitleEnd'];
$resAuthorStart = $_REQUEST['resAuthorStart'];
$resAuthorEnd = $_REQUEST['resAuthorEnd'];
$resContentStart = $_REQUEST['resContentStart'];
$resContentEnd = $_REQUEST['resContentEnd'];

$resImageMethod = $_REQUEST['resImageMethod'];
$resTranslate = $_REQUEST['resTranslate'];



$getComment = $_REQUEST['getComment'];

$sql = '';

$param = null;
if(isset($getComment) && $getComment=='true'){
	

	$commentContentAreaStarts = $_REQUEST['commentContentAreaStarts'];
	$commentContentAreaEnd = $_REQUEST['commentContentAreaEnd'];
	$commentAuthorIdStart = $_REQUEST['commentAuthorIdStart'];
	$commentAuthorIdEnd = $_REQUEST['commentAuthorIdEnd'];
	$commentTimeStart = $_REQUEST['commentTimeStart'];
	$commentTimeEnd = $_REQUEST['commentTimeEnd'];
	$commentContentStart = $_REQUEST['commentContentStart'];
	$commentContentEnd = $_REQUEST['commentContentEnd'];
	$sql = "insert into res values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	$param = array($resUrl,$classId,$resName,$resLinksStart,$resLinksEnd,$resTitleStart,$resTitleEnd,$resAuthorStart,$resAuthorEnd,$resContentStart,$resContentEnd,$getComment,$commentContentAreaStarts,$commentContentAreaEnd,$commentAuthorIdStart,$commentAuthorIdEnd,$commentTimeStart,$commentTimeEnd,$commentContentStart,$commentContentEnd,$resImageMethod,$resTranslate);
}else{
	$sql = "insert into res values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	$param = array($resUrl,$classId,$resName,$resLinksStart,$resLinksEnd,$resTitleStart,$resTitleEnd,$resAuthorStart,$resAuthorEnd,$resContentStart,$resContentEnd,$getComment,'','','','','','','','',$resImageMethod,$resTranslate);
}

$rc = update($sql,$param);
echo $rc;


?>