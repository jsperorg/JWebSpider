

<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>社汇</title>
		<meta content="keywords" content="外文译读"/>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		
		<style type="text/css">
			span {margin-left:20px;}
		</style>
	</head>
	<body>
		

<?php


include_once 'common/common.php';

$id = $_REQUEST['id'];

$result = query('select * from article where article_id=?',array($id));


echo '<h3>'.$result[0]['article_title'].'</h3>';
echo '	<span>时间：'.$result[0]['article_publish_time'].'</span>';
echo '	<span>原文：'.$result[0]['article_from_url'].'</span>';
echo '<hr>';
echo '<div>'.$result[0]['article_content'].'</div>';







?>




	</body>
</html>