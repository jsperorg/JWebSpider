<?php


include_once 'common/common.php';


$result = query('select article_id,article_title,article_publish_time from article order by article_id desc',null);

?>

<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>社汇</title>
		<meta content="keywords" content="外文译读"/>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<style type="text/css">
			.list {height:20px;line-height:20px;font-size:14px;margin:2px;}
			.link {width:600px;float:left;}
			.puttime {width:170px;float:left;}
		</style>
	</head>
	<body>
		
<?php

foreach($result as $row){
	echo '<div  class="list">';
	echo '	<div class="link"><a href="article.php?id='.$row['article_id'].'">'.$row['article_title'].'</a></div>';
	echo '	<div class="puttime">'.$row['article_publish_time'].'</div>';
	echo '</div>';
}

?>


	</body>
</html>