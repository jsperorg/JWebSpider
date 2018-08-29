
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<?php
/*
 * 登录验证过滤器
 * pages目录下的所有php文件中都引入本文件，用于后台管理的登录验证。
 */

session_start();
//如果未登录，则跳转至登录页面
if(!isset($_SESSION['admin_info'])){
	$uri = $_SERVER['REQUEST_URI'];
	
	$path_count = substr_count($uri,'/')-3;//此处-3定义到/zst/admin/下，登录页面在此目录下
	
	$new_path = 'index.html';
	//根据当前所访问的地址的路径深度构建登录页地址路径
	for($i=0;$i<$path_count;$i++){
		$new_path = '../'.$new_path;
	}
	//跳转至登录页面
	header('location:'.$new_path);
}

?>