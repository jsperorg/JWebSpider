<?php

include '../common/common.php';
//登录控制程序，接收传入的ID和密码查询数据库进行判断，登录成功将获得的用户数据存入session并跳转至管理主页。

session_start();

$admin_name = $_REQUEST['admin_name'];
$admin_password = $_REQUEST['admin_password'];
$user_validate_code = $_REQUEST['validate_code'];
$session_validate_code = $_SESSION['validate_code'];


if($user_validate_code != $session_validate_code){
	echo false;
	exit;
}

//查询数据库获得登陆者信息
$sql="select * from sys_admin_group where admin_account=? and admin_password=?";
$param= array($admin_name,$admin_password);
$admin_info = query($sql,$param);

//判断验证登陆者信息，如果验证失败则返回false，成功返回true
if($admin_info!=null){
	$_SESSION['admin_info']=$admin_info[0];
	echo true;
}else{
	echo false;
}


?>