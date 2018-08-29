<?php

/****
 * 该文件定义了常用函数和变量
 * 所有php文件都必须引入该文件
 */

header('content-type: text/html; charset=utf-8');

//时区设置
date_default_timezone_set('Etc/GMT-8');

include_once 'config.php';

/*
//如果系统全局变量里不存在创建好的连接和选择好的数据库，则创建和选择以便于数据库操作函数调用
if(!isset($GLOBALS['db_connect'])){
	$GLOBALS['db_connect']=mysql_connect($cfg_db['address'].':'.$cfg_db['port'],$cfg_db['username'],$cfg_db['password']) or die ('数据库连接失败...'.mysql_error());
}
if(!isset($GLOBALS['selected_db'])){
	$GLOBALS['selected_db']=mysql_select_db($cfg_db['dbname'], $con) or die ('目标数据库不存在或没有权限访问...'.mysql_error());
}
*/




/***
 * 数据库查询函数，select
* 返回 2d array
* $param为传入的SQL的参数组成的数组
* 例子：
* 
	$countryName = $_POST['countryName'];
	$regionName = $_POST['regionName'];
	
	$sql = 'select commercial_district_id,commercial_district_name from commercial_district where country_name=? and region_name=?';
	$param = array($countryName,$regionName);
	$result = query($sql,$param);

	echo json_encode($result);

*/
function query($sql,$param){

	global $cfg_db;

	$uri = $cfg_db['uri'];
	$port = $cfg_db['port'];
	$dbname = $cfg_db['dbname'];
	$username = $cfg_db['username'];
	$password = $cfg_db['password'];

	$dbh = new PDO("mysql:host=$uri;port=$port;dbname=$dbname", $username, $password);
	$dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES, false); //禁用prepared statements的仿真效果
	$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$dbh->exec('set names utf8');

	//$sql = "select * from country where country_id = :id";
	//$param = array(':id'=>'4');

	$stmt = $dbh->prepare($sql);

	if(isset($param) && $param!=null){
		$stmt->execute($param);
	}else{
		$stmt->execute();
	}
	$result = $stmt->fetchAll(PDO::FETCH_ASSOC);
	$dbh=null;//断开连接


	return $result;


}






/***
 * 数据库更新函数，insert、update、delete、...
* 返回所影响到的行数
* $param为传入的SQL的参数组成的数组
* 例子：
	$name = 'jack';
	$age = 27;
	$sql = "insert into student(id,name,age) values(null,?,?)";
	$param = array($name,$age);
	$rc = update($sql,$param);
	if($rc>0){
		//跳转至页面
		header('location:main.php');
	}
*/
function update($sql,$param){

	global $cfg_db;

	$uri = $cfg_db['uri'];
	$port = $cfg_db['port'];
	$dbname = $cfg_db['dbname'];
	$username = $cfg_db['username'];
	$password = $cfg_db['password'];

	$dbh = new PDO("mysql:host=$uri;port=$port;dbname=$dbname", $username, $password);
	$dbh->setAttribute(PDO::ATTR_EMULATE_PREPARES, false); //禁用prepared statements的仿真效果
	$dbh->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	$dbh->exec('set names utf8');


	$stmt = $dbh->prepare($sql);
	if($param!=null){
		$stmt->execute($param);
	}else{
		$stmt->execute();
	}
	$result = $stmt->rowCount();//影响的行数
	$dbh=null;//断开连接
	return $result;



}







//在字符串中找到最后字符串最后出现的位置
function last_index_of($all,$part){
	if(trim($all)=="" || trim($part)=="") return 0;
	$offset=0;
	$lastindexof=0;
	while(strpos($all,$part)!==false)
	{
		$indexof = strpos($all,$part);
		$lastindexof = $lastindexof + $indexof + $offset;
		$all = substr($all,$indexof+ strlen($part));
		$offset = strlen($part);
	}
	return $lastindexof;
}


?>