
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
	
	$sql = "update res set class_id=?,res_name=?,res_links_start=?,res_links_end=?,res_title_start=?,res_title_end=?,res_author_start=?,res_author_end=?,res_content_start=?,res_content_end=?,res_getComment=?,res_comment_content_area_start=?,res_comment_content_area_end=?,res_comment_authorid_start=?,res_comment_authorid_end=?,res_comment_time_start=?,res_comment_time_end=?,res_comment_content_start=?,res_comment_content_end=?,res_image_method=?,res_translate=? where res_url=?";
	$param = array($classId,$resName,$resLinksStart,$resLinksEnd,$resTitleStart,$resTitleEnd,$resAuthorStart,$resAuthorEnd,$resContentStart,$resContentEnd,$getComment,$commentContentAreaStarts,$commentContentAreaEnd,$commentAuthorIdStart,$commentAuthorIdEnd,$commentTimeStart,$commentTimeEnd,$commentContentStart,$commentContentEnd,$resImageMethod,$resTranslate,$resUrl);
	}else{
	$sql = "update res set class_id=?,res_name=?,res_links_start=?,res_links_end=?,res_title_start=?,res_title_end=?,res_author_start=?,res_author_end=?,res_content_start=?,res_content_end=?,res_getComment=?,res_image_method=?,res_translate=? where res_url=?";
	$param = array($classId,$resName,$resLinksStart,$resLinksEnd,$resTitleStart,$resTitleEnd,$resAuthorStart,$resAuthorEnd,$resContentStart,$resContentEnd,$getComment,$resImageMethod,$resTranslate,$resUrl);
}
$rc = update($sql,$param);
echo $rc;


?>