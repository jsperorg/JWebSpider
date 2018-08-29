package spider.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spider.analyze.HTMLAnalyze;
import spider.test.FileTypeTest;

public class Utility {


	/**
	 * web数据请求方法
	 * 根据url请求服务端并获得数据，返回List<Map<String, String>> 集合，
	 * Map代表一条数据，第一个String 为数据库表的字段名，第二个String为对应值。
	 * 服务端返回的数据格式为：
	 * [{id=168,articleTitle=文章标题1,deleted=false},{id=169,articleTitle=文章标题2,deleted=true}]
	 * 此种格式为java直接将对象print给请求端：out.print(list);。
	 * @param url
	 * @return List<Map<String, String>> 集合
	 */
	public static List<Map<String, String>> getServerResultByJavaObject(String url) throws Exception{
		List<Map<String, String>>  list = null;
		try{
			HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
			//创建输入流读取器对象
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream(),"utf-8"));
	        huc.connect();
	        String data="";
	        String line=null ;
	        //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	data+=("\r\n"+line);
			}
	        data = data.trim();
	        
	        //解析web返回的数据格式并封装成HashMap添加进List集合
	        if(data!=null && data.length()>10){
		        String[] rows =  data.split("\\}, \\{");//分割后如：class_id=2, class_name=猫扑社区 class_id=3, class_name=天涯杂谈
		        rows[0]=rows[0].substring(2);//去除首串的首两个字符“[{”
		        rows[rows.length-1]=rows[rows.length-1].substring(0,rows[rows.length-1].length()-2);//去除尾串的后两个字符“}]”
		         list = new ArrayList<Map<String, String>>();
		        for(String row:rows){
		        	String[] fields = row.split(", ");//分割后如：class_id=3 class_name=天涯杂谈
			        Map<String, String> map = new HashMap<String,String>();
		        	for(String field : fields){
		        		String[] kv = field.split("=");//分割后如：class_name 天涯杂谈
		        		if(kv.length>1){
		        			String kvs = "";
		        			for(int i=1;i<kv.length;i++){
		        				kvs+=kv[i]+"=";//防止【"attr"="val=val"】这种value中包含等号分割后数据不完整情况
		        			}
		        			kvs=kvs.substring(0, kvs.length()-1);
		        			map.put(kv[0], kvs);
		        		}else{
		        			map.put(kv[0], null);
		        		}
		        	}
			        list.add(map);
		        }
			}
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	
	
	
	

	/**
	 * web数据请求方法
	 * 根据url请求服务端并获得数据，返回List<Map<String, String>> 集合，
	 * Map代表一条数据，第一个String 为数据库表的字段名，第二个String为对应值。
	 * 服务端返回的数据格式为：
	 * [{"id":168,"articleTitle":"文章标题1","deleted":false},{"id":169,"articleTitle":"文章标题2","deleted":true}]
	 * 此种格式为服务器端将对象集合转换成JSON字符串输出的
	 * 本方法不支持key或value中包含双引号。
	 * @param url
	 * @return List<Map<String, String>> 集合
	 */
	public static List<Map<String, String>> getServerResultByJSON(String url) throws Exception{
		List<Map<String, String>>  list = null;
		try{
			HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
			//创建输入流读取器对象
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream(),"utf-8"));
	        huc.connect();
	        String data="";
	        String line=null ;
	        //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	data+=("\r\n"+line);
			}
	        data = data.trim();
	        
	        //System.out.println(data);
	        
	        
	        if(data!=null && data.length()>10){
		        String[] objects =  data.split("\\},\\{");//按},{分割，分割后如："class_id":2,"class_name":"猫扑社区" "class_id":3,"class_name":"天涯杂谈"
		        objects[0] = objects[0].replaceFirst("\\[\\{", "");//去掉第一串开始的：[{
		        objects[objects.length-1] = objects[objects.length-1].replaceFirst("\\}\\]", "");//去掉最后一串开始的：}]
		        //System.out.println("objects.length:"+objects.length);
		        list = new ArrayList<Map<String, String>>();
	        	for(String object:objects){//遍历对象列表
	        		object = object.replaceAll("http:", "http_");
	        		//System.out.println("object:"+object);
	        		
	        		String[] attributes = object.split(",");//按,（逗号）分割
	        		Map<String, String> map = new HashMap<String,String>();
	        		for(String attribute:attributes){
	        			String[] kv = attribute.split(":");
	        			kv[0]=kv[0].replaceAll("\"", "");
	        			//如果value被双引号包裹，要去除两边双引号。注意，此处不能将内容中的双引号去掉。
	        			if("\"".equals(kv[1].substring(0, 1))){
	        				kv[1] = kv[1].substring(1);
	        				kv[1] = kv[1].substring(0,kv[1].length()-1);
	        			}
	        			
	        			String key = unicodeConvert(kv[0]);
	        			String value = "";
	        			if(kv.length==1){
	        				value = "";
	        			}else{
	        				value = kv[1];
	        				value = value.replaceAll("\\\\\"", "\"");//将'\"'替换成'"'
	        				value = value.replaceAll("\\\\/", "/");//将'\/'替换成'/'
	        				value = value.replaceAll("\\\\n", "\r\n");//php json_encode()函数输出的json字符串中的回车换行为\n，而java中\r\n才代表回车换行
	        				value = value.replaceAll("\\\\t", "\t");//将'\t'替换成java中的制表符
	        				value = value.replaceAll("\\\\f", "\f");//将'\f'替换成java中的分页符
	        				
	        				value = unicodeConvert(value);
	        			}
	        			value = value.replaceAll("http_", "http:");
        				map.put(key, value);
	        		}
	        		list.add(map);
	        	}
	        }
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		return list;
	}
	
	
	
	
	
	
	/**
	 * 模拟浏览器POST提交数据方法
	 * 将装进HashMap的参数及值组合成URL并POST提交到web，
	 * web持久化后会将持久化结果返回
	 * @param action
	 * @param parMap
	 * @return
	 * @throws Exception
	 */
	public static String serverPostRequest(String url,Map<String,String> parMap) throws Exception{
		String resultSet = null;
        
		try{
			HttpURLConnection huc = (HttpURLConnection)  new URL(url).openConnection();
			//指定HTTP内容类型及URL格式为form表单格式
			//huc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			
			// 设置允许output
	        huc.setDoOutput(true);
	        // 设置提交方式为post方式
	        huc.setRequestMethod("POST");
	        String parameter="";
	        for(String key:parMap.keySet()){
	        	//组建参数URL并指定URL及参数编码格式
	        	parameter+=key+"="+  java.net.URLEncoder.encode(parMap.get(key),"utf-8")+"&";
	        }
	        parameter=parameter.substring(0, parameter.length()-1);
	        OutputStream os = huc.getOutputStream();
	        os.write(parameter.getBytes("utf-8"));//指定URL及参数编码格式
	        os.flush();
	        os.close();
	        //执行提交后获取执行结果
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream()));
	        huc.connect();
	        String line=null ;
	        resultSet = br.readLine();
	      //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	resultSet += line;//此处未加上\r\n
			}
	        br.close();
	        resultSet = resultSet.trim();
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		
        return resultSet;
	}
	
	
	
	
	
	
	
	
	
	

	/**
	 * 模拟浏览器GET提交数据方法
	 * 将装进HashMap的参数及值组合成URL并GET提交到web，
	 * web持久化后会将持久化结果返回
	 * @param action
	 * @param parMap
	 * @return
	 * @throws Exception
	 */
	public static String serverGetRequest(String url,Map<String,String> parMap) throws Exception{
		String resultSet = null;
        
		try{
			
			String parameter="";
	        for(String key:parMap.keySet()){
	        	//组建参数URL并指定URL及参数编码格式
	        	parameter+=key+"="+  java.net.URLEncoder.encode(parMap.get(key),"utf-8")+"&";
	        }
	        parameter=parameter.substring(0, parameter.length()-1);
	        url = url+"?"+parameter;
			
	        
			HttpURLConnection huc = (HttpURLConnection)  new URL(url).openConnection();
			//指定HTTP内容类型及URL格式为form表单格式
			//huc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			huc.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 9.0; Windows 7)");

//			huc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
//			huc.setRequestProperty("Accept", "Accept: image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
//			huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			// 设置允许output
	        huc.setDoOutput(true);
	        // 设置提交方式为get方式
	        huc.setRequestMethod("GET");
	        
	        
	        //执行提交后获取执行结果
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
	        huc.connect();
	        String line=null ;
	        resultSet = br.readLine();
	      //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	resultSet += line;//此处未加上\r\n
			}
	        resultSet = resultSet.trim();
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		
        return resultSet;
	}
	
	
	
	
	

	/***
	 * 文件上传方法
	 * 主要用于将抓取下载的图片上传到网站（反防盗链）
	 * @param f
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(File f,String url) throws Exception{
		String resultSet = null;
		

		try{
			HttpURLConnection huc = (HttpURLConnection)  new URL(url).openConnection();

	        
	        huc.setRequestMethod("POST");// 设置提交方式为post方式
			huc.setDoInput(true);
	        huc.setDoOutput(true);//设置允许output
	        huc.setUseCaches(false);//POST不能使用缓存
	        
	        //设置请求头信息
	        huc.setRequestProperty("Connection", "Keep-Alive");
			huc.setRequestProperty("Charset", "UTF-8");
	        
			// 设置边界
			String boundary = "----------" + System.currentTimeMillis();
			huc.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ boundary);
			
			
			// 头部：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // ////////必须多两道线
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ f.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");


			// 获得输出流
			OutputStream out = new DataOutputStream(huc.getOutputStream());
			out.write(sb.toString().getBytes("utf-8"));//写入header

			// 文件数据部分
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);//写入文件数据
			}
			in.close();

			// 结尾部分
			byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);//写入尾信息
			
			out.flush();
			out.close();

			
	        //执行提交后获取执行结果
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream()));
	        huc.connect();
	        String line=null ;
	        resultSet = br.readLine();
	        
	        //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	resultSet += line+"\r\n";//此处未加上\r\n
			}
	        br.close();
	        resultSet = resultSet.trim();
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		
		
		return resultSet;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	


	/***
	 * 文件上传方法
	 * 主要用于将抓取下载的图片上传到网站（反防盗链）
	 * @param f
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String fileUpload(byte[] file,String url) throws Exception{
		String resultSet = null;
		

		try{
			HttpURLConnection huc = (HttpURLConnection)  new URL(url).openConnection();

	        
	        huc.setRequestMethod("POST");// 设置提交方式为post方式
			huc.setDoInput(true);
	        huc.setDoOutput(true);//设置允许output
	        huc.setUseCaches(false);//POST不能使用缓存
	        
	        //设置请求头信息
	        huc.setRequestProperty("Connection", "Keep-Alive");
			huc.setRequestProperty("Charset", "UTF-8");
	        
			// 设置边界
			String boundary = "----------" + System.currentTimeMillis();
			huc.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ boundary);
			
			
			// 头部：
			StringBuilder sb = new StringBuilder();
			sb.append("--"); // ////////必须多两道线
			sb.append(boundary);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ "uploaded_file" + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");

			

			
			

			// 获得输出流
			OutputStream out = new DataOutputStream(huc.getOutputStream());
			out.write(sb.toString().getBytes("utf-8"));//写入header
			// 文件数据部分
			out.write(file, 0, file.length);//写入文件数据
			
			// 结尾部分
			byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);//写入尾信息
			
			out.flush();
			out.close();

			
	        //执行提交后获取执行结果
	        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream()));
	        huc.connect();
	        String line=null ;
	        resultSet = br.readLine();
	        
	        //循环按行读取文本流
	        while ((line = br.readLine()) != null) {
	        	resultSet += line+"\r\n";//此处未加上\r\n
			}
	        br.close();
	        resultSet = resultSet.trim();
	        huc.disconnect();
		}catch(Exception e){
			throw e;
		}
		
		
		return resultSet;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static byte[] fileDownload(String url) throws Exception{
		byte[] file = null;
		try {
			URL u = new URL(url);
			InputStream is = u.openStream();
			
			//取得文件真实类型（不通过后缀和content-type）
			//BufferedInputStream bis = new BufferedInputStream(is);
			//String fileType = HttpURLConnection.guessContentTypeFromStream(bis);
			//System.out.println(fileType);
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int length = -1;
			while((length = is.read(buffer))!=-1){
				
				os.write(buffer,0,length);
				
			}
			is.close();
			
			os.flush();
			
			
			
			file = os.toByteArray();
			os.close();
		} catch (Exception e) {
			throw e;
		}
		
		
		
		
		
		
		return file;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 正则表达式特殊字符转义函数，主要用于将字符串中的具有正则表达式特殊意义的字符转义成表达普通字符意义的字符。
	 * @param str
	 * @return
	 */
	public static String regexEscape(String str){
		String res = null;
		if(str!=null){
			res = str.replaceAll("\\(", "\\\\(");
			res = res.replaceAll("\\)", "\\\\)");
			res = res.replaceAll("\\.", "\\\\.");
			res = res.replaceAll("\\$", "\\\\$");
			res = res.replaceAll("\\^", "\\\\^");
			res = res.replaceAll("\\{", "\\\\{");
			res = res.replaceAll("\\}", "\\\\}");
			res = res.replaceAll("\\[", "\\\\]");
			res = res.replaceAll("\\]", "\\\\]");
			res = res.replaceAll("\\|", "\\\\|");
			res = res.replaceAll("\\*", "\\\\*");
			res = res.replaceAll("\\+", "\\\\+");
			res = res.replaceAll("\\?", "\\\\?");
			res = res.replaceAll("\\u005C", "\\\\");//替换万恶的反斜杠。
		}
		return res;
	}
	
	
	
	
	/***
	 * 将Unicode符号转换成字符，如\u3d22f转换成：陈
	 * @param str
	 * @return
	 */
	public static String unicodeConvert(String str) {  
		  
	    char[] in = str.toCharArray();  
	    int off = 0;  
	    int len = str.length();  
	    char[] out = new char[len];  
	    char aChar;  
	    int outLen = 0;  
	  
	    while (off < len) {  
	        aChar = in[off++];  
	        if (aChar == '\\') {  
	            aChar = in[off++];  
	            if (aChar == 'u') {  
	                int value = 0;  
	                for (int i = 0; i < 4; i++) {  
	                    aChar = in[off++];  
	                    switch (aChar) {  
	                    case '0':  
	                    case '1':  
	                    case '2':  
	                    case '3':  
	                    case '4':  
	                    case '5':  
	                    case '6':  
	                    case '7':  
	                    case '8':  
	                    case '9':  
	                        value = (value << 4) + aChar - '0';  
	                        break;  
	                    case 'a':  
	                    case 'b':  
	                    case 'c':  
	                    case 'd':  
	                    case 'e':  
	                    case 'f':  
	                        value = (value << 4) + 10 + aChar - 'a';  
	                        break;  
	                    case 'A':  
	                    case 'B':  
	                    case 'C':  
	                    case 'D':  
	                    case 'E':  
	                    case 'F':  
	                        value = (value << 4) + 10 + aChar - 'A';  
	                        break;  
	                    default:  
	                        throw new IllegalArgumentException("Malformed //uxxxx encoding.");  
	                    }  
	                }  
	                out[outLen++] = (char) value;  
	            }
	            /* 下面的处理已被getServerResultByJavaObject方法内的处理代替，本方法应只处理unicode代码。
	            else {  
	                if (aChar == 't')  
	                    aChar = '\t';  
	                else if (aChar == 'r')  
	                    aChar = '\r';  
	                else if (aChar == 'n')  
	                    aChar = '\n';  
	                else if (aChar == 'f')  
	                    aChar = '\f';  
	                else{
	                	out[outLen++] = '\\';
	                }
	                out[outLen++] = aChar;  
	            }  
	            */
	            
	        } else {  
	            out[outLen++] = (char) aChar;  
	        }  
	    }  
	    return new String(out, 0, outLen);  
	  
	}  
	
	
	
	
	/**
	 * 翻译
	 * 传入要翻译的文本内容和翻译提供商名称，返回翻译后的结果。
	 * @param args
	 */
	public static String translate(String text,String server) {
		
		//将回车换行替换成特征字符串，避免翻译中文档结构被破坏，翻译完后再反替换成回车换行符。
		text = text.replaceAll("\r\n", "f2khjf9zx1k0dk3z1l30fg");
		String result = "";
		
		try{
			
			//谷歌翻译
			if(server.equals("google")){
				
				String api = "http://translate.google.cn/translate_a/t";
				Map<String,String> parMap = new HashMap<String,String>();
				parMap.put("client", "t");
				parMap.put("sl", "auto");
				parMap.put("tl", "zh_CN");
				parMap.put("hl", "zh_CN");
				parMap.put("ie", "UTF-8");
				parMap.put("oe", "UTF-8");
				parMap.put("uptl", "zh_CN");
				parMap.put("alttl", "en");
				parMap.put("pc", "1");
				parMap.put("oc", "1");
				parMap.put("otf", "0");
				parMap.put("ssel", "0");
				parMap.put("tsel", "0");
				parMap.put("q", text);
			
				String resultSet = Utility.serverGetRequest(api, parMap);
				
				resultSet = resultSet.split("\"]],")[0];
				String[] strs = resultSet.split("\",\"");
				StringBuffer sb = new StringBuffer();
				for(int i=0;i<strs.length-1;i+=3){
					if(i==0)
						sb.append(strs[i].substring(4));
					else
						sb.append(strs[i].substring(5));
						
				}

				
				
				result = sb.toString();
			}
			
			//有道翻译
			else if(server.equals("youdao")){
	
				String api = "http://fanyi.youdao.com/translate?smartresult=dict&smartresult=rule&smartresult=ugc&sessionFrom=null";
				
				Map<String,String> parMap = new HashMap<String,String>();
				parMap.put("type", "AUTO");
				parMap.put("i", text);
				parMap.put("doctype", "json");
				parMap.put("xmlVersion", "1.6");
				parMap.put("keyfrom", "fanyi.web");
				parMap.put("ue", "UTF-8");
				parMap.put("typoResult", "true");
			
				String resultSet = Utility.serverPostRequest(api, parMap);
				
				//处理返回的JSON翻译结果
				StringBuffer sb = new StringBuffer();
				String[] strs = resultSet.split("\"tgt\":\"");
				strs[0]="";
				for(String s:strs){
					String d = s.split("\"}")[0];
					sb.append(d);
					
				}
				sb.append("\r\n");
				result = sb.toString();
			}
			
			//微软翻译
			else if(server.equals("bing")){
			
				
				String api = "http://api.microsofttranslator.com/v2/ajax.svc/TranslateArray2";


				Map<String,String> parMap = new HashMap<String,String>();
				parMap.put("appId", "\"TLNJM-_hh_o0CD7xoCtTUO_HgiqWP2DERjtQ1boaNNWIbR_gCj1DP9zthPa6CwxAL\"");
				parMap.put("from", "\"\"");
				parMap.put("to", "\"zh-chs\"");
				parMap.put("oncomplete", "_mstc2");
				parMap.put("onerror", "_mste2");
				parMap.put("loc", "zh-chs");
				parMap.put("ctr", "");
				parMap.put("ref", "WidgetV2");
				parMap.put("rgp", "3740068d");
				parMap.put("texts", "[\""+text+"\"]");
			
				String resultSet = Utility.serverGetRequest(api,parMap);

				//处理返回的JSON翻译结果
				result = resultSet.split("\"TranslatedText\":\"")[1].split("\",\"TranslatedTextSentenceLengths\"")[0];

				
				
			}
			
			
			
			//百度翻译
			else if(server.equals("baidu")){
				

				String api = "http://fanyi.baidu.com/v2transapi";

				Map<String,String> parMap = new HashMap<String,String>();
				parMap.put("from", "en");
				parMap.put("to", "zh");
				parMap.put("query", text);
				parMap.put("transtype", "trans");

				String resultSet = Utility.serverPostRequest(api,parMap);
				
				resultSet = unicodeConvert(resultSet);

				
				
				String[] strs = resultSet.split("\"dst\":\"");
				strs[0] = "";

				//处理返回的JSON翻译结果
				StringBuffer sb = new StringBuffer();
				for(String s:strs){
					sb.append(s.split("\",\"src\":\"")[0]);
				}

				result = sb.toString();
				
				
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		

		result = result.replaceAll("f2khjf9zx1k0dk3z1l30fg", "\r\n");
		
		return result;
		
		
	}
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		
		
		try {
			byte[] b = fileDownload("http://www.gaoxiaogif.com/d/file/20140102/8cdc86278f032a4a8ed05cb0808d8b03.gif");//http://www.baidu.com/img/bdlogo.gif
			String str = fileUpload(b,"http://127.0.0.1/shehui9/test/file_upload.php");
			System.out.println(str);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*
		String HTMLSourceCode = "fswe<img src='' alt=SD卡就发给我/>222b<img src=../../ljfdoe.gif style='width:43px;'>   333<img src='dsfsdf'  />444";
		
		String temp = HTMLSourceCode;
		Pattern pattern = Pattern.compile("(<img\\s+[^<>]*>)");
		Matcher matcher = pattern.matcher(HTMLSourceCode);
		
		while(matcher.find()){ 
			String str = matcher.group(1);
			temp = temp.replaceFirst(str, "\r\n");
		}
		
		System.out.println(temp);
		*/
		
		
		
		
		
		
		/*
		String url = HTMLAnalyze.buildURL("http://127.0.0.1/shehui9/test/file_upload.php", "../upload/2014/01/10/656251001389370539.gif");
		
		System.out.println(url);
		
		*/
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

	
}
