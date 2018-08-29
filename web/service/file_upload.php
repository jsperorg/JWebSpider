
<?php 

/**
 * 自动创建目录
 * @param string $destFolder 服务器路径
 * @static
 */
function makeDir($destFolder)
{
	if (! is_dir($destFolder) && $destFolder != './' && $destFolder != '../')
	{
		$dirname = '';
		$folders = explode( '/', $destFolder);
		foreach ($folders as $folder)
		{
			$dirname .= $folder . '/';
			if ($folder != '' && $folder != '.' && $folder != '..' && ! is_dir($dirname))
			{
				mkdir($dirname);
			}
		}

		// chmod($destFolder,0777);
	}
	return true;
}

/**
 * 判断文件类型
 * @param string $filename 文件全名
 * @static
 */
function file_type($filename)
{
	$file = fopen($filename, "rb");
	$bin = fread($file, 2); //只读2字节
	fclose($file);
	$strInfo = @unpack("C2chars", $bin);
	$typeCode = intval($strInfo['chars1'].$strInfo['chars2']);
	$fileType = '';
	switch ($typeCode)
	{
		case 7790:
			$fileType = 'exe';
			break;
		case 7784:
			$fileType = 'midi';
			break;
		case 8297:
			$fileType = 'rar';
			break;
		case 8075:
			$fileType = 'zip';
			break;
		case 255216:
			$fileType = 'jpg';
			break;
		case 7173:
			$fileType = 'gif';
			break;
		case 6677:
			$fileType = 'bmp';
			break;
		case 13780:
			$fileType = 'png';
			break;
		default:
			$fileType = 'unknown: '.$typeCode;
	}

	//Fix
	if ($strInfo['chars1']=='-1' AND $strInfo['chars2']=='-40' ) return 'jpg';
	if ($strInfo['chars1']=='-119' AND $strInfo['chars2']=='80' ) return 'png';

	return $fileType;
}


if ($_FILES["file"]["error"] > 0)
{
	echo "Error: " . $_FILES["file"]["error"] . "<br />";
}
else
{
	
	
	//echo "Upload: " . $_FILES["file"]["name"] . "<br />";
	//echo "Type: " . $_FILES["file"]["type"] . "<br />";
	//echo "Size: " . ($_FILES["file"]["size"] / 1024) . " Kb<br />";
	
	//取得文件类型，确定文件后缀
	$postfix = file_type($_FILES["file"]["tmp_name"]);
	
	
	//获取毫秒的时间戳定义文件名
	$time = substr(str_replace(' ','',microtime()),2);
	
	
	
	//由于网站上传文件管理目录为/upload，但本php在/test/下所以，上传路径应为../upload/
	$dir_name = "upload/".date('Y/m/d/');
	makeDir($dir_name);
	move_uploaded_file($_FILES["file"]["tmp_name"],$dir_name.$time.'.'.$postfix);
	echo $dir_name.$time.'.'.$postfix;
	
}

?>