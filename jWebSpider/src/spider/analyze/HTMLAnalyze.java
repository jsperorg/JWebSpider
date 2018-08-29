package spider.analyze;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

	/**
	 * HTML源代码解析类
	 * 该类用于分析HTML源码中的成分
	 * @author 陈建宇
	 * 2009-03-16 16:15:??
	 */
public class HTMLAnalyze {

	public static String connection(String url) throws Exception{
		String HTMLSourceCode = null;
		try {
			CodepageDetectorProxy cdp = CodepageDetectorProxy.getInstance();
			cdp.add(JChardetFacade.getInstance());
			String encoding = getEncoding(url);
			if(encoding!=null && (encoding.substring(0,2).equals("GB") || encoding.equals("UTF-8"))){
	            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(); 
	            con.setReadTimeout(10000);
	            HttpURLConnection.setFollowRedirects(true); 
	            con.setInstanceFollowRedirects(true); 
	            con.setRequestMethod("GET"); 
	            con.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 9.0; Windows 7)");
	            
	            con.connect();
	            //System.out.println("字符集编码:"+encoding);
	            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),encoding)); 
	            String s = ""; 
	            StringBuffer sb = new StringBuffer(); 
	            while ((s = br.readLine()) != null) { 
	                sb.append(s + "\r\n"); 
	            } 
	            con.disconnect();
	            br.close();
	            HTMLSourceCode = sb.toString();
	            
	            
	            //System.out.println("\r\n\r\n\r\n"+HTMLSourceCode+"\r\n\r\n\r\n");
	            
			}
        } catch (Exception e) {
        	throw e;
        	//将异常信息写入日志。
        }
		
        return HTMLSourceCode;
	}
	
	/**
	 * 根据URL得到HTML内容的字符编码
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String  getEncoding(String url) throws Exception{
		String encoding=null;

		try {
		CodepageDetectorProxy cdp = CodepageDetectorProxy.getInstance();
		cdp.add(JChardetFacade.getInstance());
			encoding = cdp.detectCodepage(new URL(url)).name();
		} catch (Exception e) {
			throw e;
		}
		return encoding;
	}
	
	
	/**
	 * 获得HTML源内所有href值
	 * @param s HTML源
	 * @return
	 */
	public static ArrayList<String> getHref(String HTMLSourceCode){
		ArrayList<String> list = null;
		HashMap<String,String> hm = null;
		Pattern pattern = Pattern.compile("[h|H]+[r|R]+[e|E]+[f|F]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)");
		Matcher matcher = pattern.matcher(HTMLSourceCode);
		boolean result = matcher.find();
		if(result){
			hm = new HashMap<String,String>();
		}
		while(result){ 
			String str = matcher.group(1);
			//去除双引号和单引号。
			if(str.charAt(0)== '"' || str.charAt(0)== '\''){
				str = str.substring(1,str.length());
			}
			if(str.charAt(str.length()-1) =='"' || str.charAt(str.length()-1) =='\''){
				str = str.substring(0,str.length()-1);
			}
			hm.put(str,str);
		 	result=matcher.find(); 
		}
		
		if(hm!=null && hm.size()>0){
			list = new ArrayList<String>();
			for(String href:hm.keySet()){
				list.add(href);
			}
		}
		return list;
	}
	
	/**
	 * 获得HTML源内所有src值
	 * @param s HTML源
	 * @return
	 */
	public static ArrayList<String> getSrc(String HTMLSourceCode){
		ArrayList<String> list = null;
		HashMap<String,String> hm = null;
		Pattern pattern = Pattern.compile("[s|S]+[r|R]+[c|C]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)");
		Matcher matcher = pattern.matcher(HTMLSourceCode);
		boolean result = matcher.find();
		if(result){
			hm = new HashMap<String,String>();
		}
		while(result){ 
			String str = matcher.group(1);
			//去除双引号和单引号。
			if(str.charAt(0)== '"' || str.charAt(0)== '\''){
				str = str.substring(1,str.length());
			}
			if(str.charAt(str.length()-1) =='"' || str.charAt(str.length()-1) =='\''){
				str = str.substring(0,str.length()-1);
			}
			hm.put(str,str);
		 	result=matcher.find(); 
		}
		
		if(hm!=null && hm.size()>0){
			list = new ArrayList<String>();
			for(String src:hm.keySet()){
				list.add(src);
			}
		}
		return list;
	}
	
	
	
	
	
	
	
	/**
	 * 获得所有有效url及文本，结果map中的结构例如：{"http://www.google.com/":"谷歌"}
	 * @return
	 */
	public static HashMap<String,String> getAHrefAndText(String basiceUrl,String HTMLSourceCode){
		HashMap<String,String> a = new HashMap<String,String>();
		
		ArrayList<String> links = getTagByA(HTMLSourceCode);//调用获得所有超链接的方法
		if(links.size()>0){
			for(String link:links){
				Pattern pattern = Pattern.compile("[h|H]+[r|R]+[e|E]+[f|F]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)");
				Matcher matcher = pattern.matcher(link);
				String str = "";
				if(matcher.find()){
					str = matcher.group(1).trim();
				}
				String httpAddrees = buildURL(basiceUrl,str);
				if(!httpAddrees.equals("#")){
					a.put(httpAddrees, link.replaceAll("<.+?>", "").replaceAll(" ", "").replaceAll("\t", " ").trim());
				}
			}
		}
		return a;
	}
	
	/***
	 * 拆解组合完整url
	 * （方法可能会产生错误的结果）
	 * @param path
	 * @return
	 */
	public static String buildURL(String url,String path){
		String u = url;
		String p = path;
		String uh = "";
		String pu = "#";
		
		//如果url最后一个字符是“/”，即去掉
		if(u.charAt(u.length()-1)=='/'){
			u = u.substring(0,u.length()-1);
		}
		//如果url是以“http://”开头，即截取
		if(u.length()>7 && (u.substring(0,7).equals("http://") || u.substring(0,7).equals("HTTP://"))){
			uh = u.substring(0,7);
			u = u.substring(7,u.length());
		}
		//如果截取后的url中仍然包含“/”并且最后一个“/”后面的字符串中包含“.”，即删掉后面
		if(u.indexOf("/")!=-1 && u.substring(u.lastIndexOf("/"),u.length()).indexOf(".")!=-1){
			u = u.substring(0,u.lastIndexOf("/"));
		}

		//去除路径值p的双引号和单引号。
		if(p.charAt(0)== '"' || p.charAt(0)== '\''){
			p = p.substring(1,p.length());
		}
		if(p.charAt(p.length()-1) =='"' || p.charAt(p.length()-1) =='\''){
			p = p.substring(0,p.length()-1);
		}
		
		if(p.length()>6 && (p.substring(0,7).equals("http://") || p.substring(0,7).equals("HTTP://"))){
			return p;
		}else if(p.length()>6 && (p.substring(0,7).equals("mailto:") || p.substring(0,7).equals("MAILTO:"))){
			return "#";
		}else if(p.length()>10 && (p.substring(0,11).equals("javascript:") || p.substring(0,11).equals("JAVASCRIPT:"))){
			return "#";
		}else if(p.length()>1 && p.substring(0, 2).equals("/.")){
			return "#";
		}else if(p.length()>0 
			&& p.length()<128 
			&& p.charAt(0) != '#' 
			&& p.indexOf("/#")==-1 
			&& p.indexOf("/archiver")==-1 
			&& p.indexOf("/ARCHIVER")==-1 
			&& p.indexOf("//")==-1 
		){
			//如果是有效url，则有四种情况：
			//首字符为“/”的情况
			//首二字符为“./”的情况
			//首三字符为“../”的情况
			//首七字符为“http://”的情况
			if(p.charAt(0)== '/'){
				pu = uh+getDomain(u)+p;
			}else if(p.length()>1 && p.substring(0,2).equals("./")){
				pu = uh+u+"/"+p.substring(2);
			}else if(p.length()>2 && p.substring(0,3).equals("../")){
				//拆分组合url
				while(u.length()>3 && p.indexOf("../")!=-1){
					if(u.charAt(u.length()-1)=='/'){
						u = u.substring(0,u.length()-1);
					}
					if(u.indexOf("/")==-1){
						while(p.indexOf("../")!=-1){
							p = p.substring(3, p.length());
						}
					}else{
						u = u.substring(0,u.lastIndexOf("/")+1);
						p = p.substring(3, p.length());
					}
					if(u.charAt(u.length()-1)=='/'){
						u = u.substring(0,u.length()-1);
					}
				}
				pu = uh+u+"/"+p;
			}else{
				pu = uh+u+"/"+p;
			}
		}
		return pu;
	}
	
	/**
	 * 获得url的顶级域名
	 * 例：http://www.***.com/main/index.php
	 * 解析后结果为：www.***.com
	 * @param url
	 * @return
	 */
	public static String getDomain(String url){
		String u = url;
		if(u!=null && !u.equals("")){
			if(u.charAt(u.length()-1)=='/'){
				u = u.substring(0,u.length()-1);
			}
			if(u.length()>6 && (u.substring(0,7).equals("http://") || u.substring(0,7).equals("HTTP://"))){
				u = u.substring(7, u.length());
			}
			if(u.length()>2 && u.indexOf("/")!=-1){
				u = u.substring(0, u.indexOf("/"));
			}
		}
		return u;
	}
	
	
	/**
	 * 根据字符串获得无参数的纯HTML标记
	 * 无法获得如<h1 id=h1>...\</h1\>带参数的标记
	 * @return
	 */
	public static ArrayList<String> getTag(String HTMLSourceCode,String tagName){
		ArrayList<String> list = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			list = new ArrayList<String>();
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*["+tagName.toLowerCase()+"|"+tagName.toUpperCase()+"]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	Pattern pattern = Pattern.compile("(<["+tagName.toLowerCase()+"|"+tagName.toUpperCase()+"]+\\s*>.*)");
	    		Matcher matcher = pattern.matcher(strs[i].replaceAll("\r\n", ""));
	    		boolean result = matcher.find(); 
	            if(result){ 
	            	list.add(matcher.group(1)+"</"+tagName+">"); 
	    }}}return list;
	}
	
	
	
	/**
	 * 获得网页标题标签
	 * @return
	 */
	public static String getTagTitle(String HTMLSourceCode){
		String title = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			Pattern pattern = Pattern.compile("(<[t|T]+[i|I]+[t|T]+[l|L]+[e|E]+\\s*[^<>]*>[^<>]*<\\s*/\\s*[t|T]+[i|I]+[t|T]+[l|L]+[e|E]+\\s*>)");
			Matcher matcher = pattern.matcher(HTMLSourceCode);
			boolean result = matcher.find(); 
	        if(result){ 
	        	title = matcher.group(1); 
	        } 
		}
		return title;
	}
	
	/**
	 * 获得所有超链接标签
	 * @return
	 */
	public static ArrayList<String> getTagByA(String HTMLSourceCode){
		ArrayList<String> links = null;
		if(HTMLSourceCode!=null && HTMLSourceCode.length()>0){
			links = new ArrayList<String>();
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*[a|A]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	Pattern pattern = Pattern.compile("(<[a|A]+\\s+[^<>]*[h|H]+[r|R]+[e|E]+[f|F]+\\s*=\\s*[^<>]*>.*)");
	    		Matcher matcher = pattern.matcher(strs[i].replaceAll("\r\n", ""));
	    		boolean result = matcher.find(); 
	            if(result){ 
	                links.add(matcher.group(1)+"</a>"); 
	    }}}return links;
	}
	
	

	/**
	 * 在HTML中寻找以ss开头并且以es结尾的字符串值，注意！
	 * (此方法可以用于匹配特定HTML标签或特定的HTML文档区域)
	 * 注：参数值不益过长，32个字符以内
	 * ss:Start String 常规文本或正则表达式
	 * es:End String 常规文本或正则表达式
	 * 例：获得HTML源中所有h2标签：
	 * String ss = "<\\s*[h|H]2\\s*";
	 * String es = "<\\s*\/\\s*[h|H]2\\s*>";
	 * ArrayList\<String\> list = getMatcher(ss, es);
	 * for(String h2 : list){
	 *	System.out.println(h2);
	 * }
	 * @return 符合条件的字符串集合ArrayList
	 */
	public static ArrayList<String> getMatcher(String HTMLSourceCode,String start,String end){
		ArrayList<String> list = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			list = new ArrayList<String>();
			//为了防止绝对字符串导致分割后丢失内容，在HTML源前后加上非空字符串
			String[] group = (HTMLSourceCode+"p|+——）（").split(end);
			//System.out.println("end分割后结果个数为："+group.length);
			if(group.length>1){
				//System.out.println(group[0]);
		        for(int i=0;i<group.length-1;i++){
		        	//System.out.println("start："+start);
		        	String[] strs = ("|+——）（"+group[i]).split(start);
		        	//System.out.println("start分割后结果个数为："+strs.length);
		        	if(strs.length>1){
			        	String s = "";
			        	for(int j=1;j<strs.length;j++){
			        		s+=strs[j];
			        	}
			        	if(!s.equals("")){
				        	list.add(s);
			        	}
		        	}
		        	
		        }
			}
	    }
		return list;
	}
	
	
	
	
	/**
	 * 获得所有SCRIPT标签
	 * @return
	 */
	public static ArrayList<String> getTagByScript(String HTMLSourceCode){
		ArrayList<String> script = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			script = new ArrayList<String>();
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	Pattern pattern = Pattern.compile("(<\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+[^<>]*>.*)");
	    		Matcher matcher = pattern.matcher(strs[i].replaceAll("\r\n", ""));
	    		boolean result = matcher.find(); 
	            if(result){ 
	            	script.add(matcher.group(1)+"</script>"); 
	    }}}return script;
	}
	
	
	/**
	 * 获得所有STYLE标签
	 * @return
	 */
	public static ArrayList<String> getTagByStyle(String HTMLSourceCode){
		ArrayList<String> style = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			style = new ArrayList<String>();
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	Pattern pattern = Pattern.compile("(<\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+[^<>]*>.*)");
	    		Matcher matcher = pattern.matcher(strs[i].replaceAll("\r\n", ""));
	    		boolean result = matcher.find(); 
	            if(result){ 
	            	style.add(matcher.group(1)+"</style>"); 
	    }}}return style;
	}
	
	/**
	 * 获得所有图片标签
	 * @return
	 */
	public static ArrayList<String> getTagByImg(String HTMLSourceCode){
		ArrayList<String> img = new ArrayList<String>();
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			Pattern pattern = Pattern.compile("(<img\\s+[^<>]*>)");
			Matcher matcher = pattern.matcher(HTMLSourceCode);
			boolean result = matcher.find(); 
	        while(result){ 
	        	img.add(matcher.group(1));
	        	result=matcher.find(); 
	        } 
		}
		return img;
	}
	
	/**
	 * 获得所有图片URL及文本有BUG
	 * @return
	 */
	public static HashMap<String,String> getImgSrcAndAlt(String basicUrl,String HTMLSourceCode){
		ArrayList<String> img = getTagByImg(HTMLSourceCode);
		HashMap<String,String> img_text = new HashMap<String,String>();
		if(img!=null && img.size()>0){
			//去除结尾符'/'
			if(basicUrl.charAt(basicUrl.length()-1)=='/'){
				basicUrl = basicUrl.substring(0,basicUrl.length()-1);
			}
			String img_url = "";
			String img_txt = "";
			Pattern pattern =null;
			Matcher matcher = null;
			boolean result = false;
			for(int i=0;i<img.size();i++){//[h|H]+[r|R]+[e|E]+[f|F]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)
				pattern = Pattern.compile("[a|A]+[l|L]+[t|T]+\\s*=\\s*(\"\\s*(.*?)\\s*\"|\'\\s*(.*?)\\s*\'|(.*?))(\\s{1}|/\\s*>|>)");
				matcher = pattern.matcher(img.get(i));
				result = matcher.find(); 
		        if(result){ 
		        	img_txt = matcher.group(1); 
		        } 
		        pattern = Pattern.compile("[s|S]+[r|R]+[c|C]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|/\\s*>)");
				matcher = pattern.matcher(img.get(i));
				result = matcher.find(); 
		        if(result){ 
		        	img_url = matcher.group(1); 
		        } 
		        if(img_url.replaceAll(" ", "")!="" && img_txt.replaceAll(" ", "")!= "" && img_txt.length()>1){
		        	//去除双引号和单引号。
		        	if(img_url.substring(0,1).equals("\"") || img_url.substring(0,1).equals("'")){
						img_url = img_url.substring(1,img_url.length());
					}
					if(img_url.substring(img_url.length()-1).equals("\"") || img_url.substring(img_url.length()-1).equals("'")){
						img_url = img_url.substring(0,img_url.length()-1);
					}
					if(img_txt.substring(0,1).equals("\"") || img_txt.substring(0,1).equals("'")){
						img_txt = img_txt.substring(1,img_txt.length());
					}
					if(img_txt.substring(img_txt.length()-1).equals("\"") || img_txt.substring(img_txt.length()-1).equals("'")){
						img_txt = img_txt.substring(0,img_txt.length()-1);
					}
					if(img_url.substring(0,1).equals("/") && img_url.indexOf("//")==-1){
						img_url = basicUrl+img_url;
					}else if(!img_url.substring(0,1).equals("/") && img_url.length()>=11 && !img_url.substring(0,11).equals("javascript:") && !img_url.substring(0,7).equals("mailto:") && !img_url.substring(0,7).equals("http://") && img_url.indexOf("//")==-1/* && str.indexOf(".")>-1*/){
						img_url = basicUrl+"/"+img_url;
					}else if(!img_url.substring(0,1).equals("/") && img_url.length()>=7 &&  !img_url.substring(0,7).equals("mailto:") && !img_url.substring(0,7).equals("http://") && img_url.indexOf("//")==-1/* && str.indexOf(".")>-1*/){
						img_url = basicUrl+"/"+img_url;
					}else if(img_url.length()<7 && img_url.indexOf("//")==-1/* && str.indexOf(".")>-1*/){
						img_url = basicUrl+"/"+img_url;
					}
					//去除结尾符'/'
					if(img_url.charAt(img_url.length()-1)=='/'){
						img_url = img_url.substring(0,img_url.length()-1);
					}
					
		        }
		        if(img_url.length()>7 && img_url.substring(0,7).equals("http://") && img_txt.length()>1){
		        	img_text.put(img_url, img_txt);
				}else{
					System.out.println("["+img_url+" : "+img_txt+"] 无效！");
				}
			}
		}
		return img_text;
	}
	
	
	/**
	 * 获得网页标题文本
	 * @return
	 */
	public static String getValueByTitle(String HTMLSourceCode){
		String title = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			Pattern pattern = Pattern.compile("<[t|T]+[i|I]+[t|T]+[l|L]+[e|E]+\\s*[^<>]*>([^<>]*)<\\s*/\\s*[t|T]+[i|I]+[t|T]+[l|L]+[e|E]+\\s*>");
			Matcher matcher = pattern.matcher(HTMLSourceCode);
			boolean result = matcher.find(); 
	        if(result){ 
	        	title = matcher.group(1).replaceAll("\r\n", ""); 
	        } 
		}
		return title;
	}

	
	/**
	 * 去除无用标签(已过时)
	 * @return
	 */
	public static String getEliminated_2(String HTMLSourceCode){
		//去除无用标签：<srcipt>、<style>、<form>、<!--注释内容-->.
		StringBuffer sb = null;
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+\\s*>");
			sb = new StringBuffer();
			System.out.println("正在清除脚本...");
	        for(int i=0;i<strs.length;i++){
	        	sb.append(strs[i].replaceAll("<\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+(.*(\r\n)*.*)*", ""));
	        }
            /* - - - - - - - - - - - - - - - - - - - - - - */
	        strs = sb.toString().split("<\\s*/\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+\\s*>");
	        sb = new StringBuffer();
	        System.out.println("正在清除样式表...");
	        for(int i=0;i<strs.length;i++){
	        	sb.append(strs[i].replaceAll("<\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+(.*(\r\n)*.*)*", ""));
	        }
            /* - - - - - - - - - - - - - - - - - - - - - - */
	        strs = sb.toString().split("<\\s*/\\s*[f|F]+[o|O]+[r|R]+[m|M]+\\s*>");
	        sb = new StringBuffer();
	        System.out.println("正在清除form表单...");
	        for(int i=0;i<strs.length;i++){
	        	sb.append(strs[i].replaceAll("<\\s*[f|F]+[o|O]+[r|R]+[m|M]+(.*(\r\n)*.*)*", ""));
	        }
            /* - - - - - - - - - - - - - - - - - - - - - - */
	        strs = sb.toString().split("--\\s*>");
	        sb = new StringBuffer();
	        System.out.println("正在清除HTML注释...");
	        for(int i=0;i<strs.length;i++){
	        	sb.append(strs[i].replaceAll("<!--(.*(\r\n)*.*)*", ""));
	        }
	    }
		return sb.toString();
	}
	
	
	/**
	 * 去除字符串参数中无用标签(新)
	 * @return
	 */
	public static String getEliminated(String HTMLSourceCode){
		//去除无用标签：<srcipt>、<style>、<form>、<!--注释内容-->.
		StringBuffer sb = new StringBuffer();
		String htmls = HTMLSourceCode;
		if(htmls!=null && !htmls.equals("")){
			sb = new StringBuffer();
			String[] strs = htmls.split("<\\s*/\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	String[] sss = strs[i].split("\r\n");
	        	int row=sss.length-1;
	        	for(int j=0;j<sss.length;j++){
	        		Pattern pattern = Pattern.compile("<\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+.*");
		    		Matcher matcher = pattern.matcher(sss[j]);
		    		boolean find = matcher.find(); 
		            if(find){ 
		            	sss[j] = sss[j].replaceAll("<\\s*[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+.*", "");
		            	row = j;
		            	break;
		            }
	        	}
	        	strs[i]="";
	        	for(int k=0;k<=row;k++){
	        		strs[i]+=sss[k]+"\r\n";
	        	}
	        	sb.append(strs[i]);
	        }
            /* - - - - - - - - - - - - - - - - - - - - - - */
	        strs = sb.toString().split("<\\s*/\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+\\s*>");
	        sb = new StringBuffer();
	        for(int i=0;i<strs.length;i++){
	        	String[] sss = strs[i].split("\r\n");
	        	int row=sss.length-1;
	        	for(int j=0;j<sss.length;j++){
	        		Pattern pattern = Pattern.compile("<\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+.*");
		    		Matcher matcher = pattern.matcher(sss[j]);
		    		boolean find = matcher.find(); 
		            if(find){ 
		            	sss[j] = sss[j].replaceAll("<\\s*[s|S]+[t|T]+[y|Y]+[l|L]+[e|E]+.*", "");
		            	row = j;
		            	break;
		            }
	        	}
	        	strs[i]="";
	        	for(int k=0;k<=row;k++){
	        		strs[i]+=sss[k]+"\r\n";
	        	}
	        	sb.append(strs[i]);
	        }
	        /* - - - - - - - - - - - - - - - - - - - - - - */
	        
//	        strs = sb.toString().split("<\\s*/\\s*[f|F]+[o|O]+[r|R]+[m|M]+\\s*>");
//	        sb = new StringBuffer();
//	        for(int i=0;i<strs.length;i++){
//	        	String[] sss = strs[i].split("\r\n");
//	        	int row=sss.length-1;
//	        	for(int j=0;j<sss.length;j++){
//	        		Pattern pattern = Pattern.compile("<\\s*[f|F]+[o|O]+[r|R]+[m|M]+.*");
//		    		Matcher matcher = pattern.matcher(sss[j]);
//		    		boolean find = matcher.find(); 
//		            if(find){ 
//		            	sss[j] = sss[j].replaceAll("<\\s*[f|F]+[o|O]+[r|R]+[m|M]+.*", "");
//		            	row = j;
//		            	break;
//		            }
//	        	}
//	        	strs[i]="";
//	        	for(int k=0;k<=row;k++){
//	        		strs[i]+=sss[k]+"\r\n";
//	        	}
//	        	sb.append(strs[i]);
//	        }
	        
            /* - - - - - - - - - - - - - - - - - - - - - - */
//	        strs = sb.toString().split("--\\s*>");
//	        sb = new StringBuffer();
//	        for(int i=0;i<strs.length;i++){
//	        	sb.append(strs[i].replaceAll("<!--(.*(\r\n)*.*)*", ""));
//	        }
	    }
		return sb.toString();
	}
	
	
	
	
	/**
	 * 清除所有超链接标签
	 * @return
	 */
	public static String clearTagByA(String HTMLSourceCode){
		StringBuffer sb = new StringBuffer("");
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			String[] strs = HTMLSourceCode.split("<\\s*/\\s*[a|A]+\\s*>");
	        for(int i=0;i<strs.length;i++){
	        	sb.append(strs[i].replaceAll("(<[a|A]+\\s+[^<>]*>(.*(\r\n)*.*)*)",""));
	    	}
	    }
		return sb.toString();
	}
	
	/**
	 * 翻译前替换源内HTML标签为唯一标识，防止标签被翻译
	 * 2014-1-9 add
	 */
	public static String beforeTranslateTagReplace(String HTMLSource){
		
		
		HTMLSource = HTMLSource.replaceAll("<[a|A]+\\s+", " q9x3jskd837sd92jh7as ");
		HTMLSource = HTMLSource.replaceAll("<\\s*/\\s*[a|A]+\\s*>", " q9x3jskd837sd92jh7ae ");
		HTMLSource = HTMLSource.replaceAll("<\\s*[h1|H1][^<>]*>", " q9x3jskd837sd92jh7h1s ");
		HTMLSource = HTMLSource.replaceAll("<\\s*/\\s*[h1|H1]+\\s*>", " q9x3jskd837sd92jh7h1e ");
		HTMLSource = HTMLSource.replaceAll("<\\s*[h2|H2][^<>]*>", " q9x3jskd837sd92jh7h2s ");
		HTMLSource = HTMLSource.replaceAll("<\\s*/\\s*[h2|H2]+\\s*>", " q9x3jskd837sd92jh7h2e ");
		//HTMLSource = HTMLSource.replaceAll("<\\s*[i|I]+[m|M]+[g|G]+\\s+", " q9x3jskd837sd92jh7img ");//img标签在调用此方法前已经被替换成唯一标识串了
		HTMLSource = HTMLSource.replaceAll("(<\\s*[b|B]+[r|R]+\\s*>|<\\s*/\\s*[b|B]+[r|R]+\\s*>|<\\s*[b|B]+[r|R]+\\s*/\\s*>)", "\r\n");
		
		return HTMLSource;
	}
	
	

	
	/**
	 * 翻译后反替换各标签唯一标识为标签
	 * 2014-1-9 add
	 */
	public static String afterTranslateTagReplace(String HTMLSource){
		
		

		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7as", "<a ");
		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7ae", "</a>");
		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7h1s", "<h1>");
		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7h1e", "</h1>");
		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7h2s", "<h2>");
		HTMLSource = HTMLSource.replace("q9x3jskd837sd92jh7h2e", "</h2>");
		HTMLSource = HTMLSource.replace("\r\n", "<br>");
		
		
		return HTMLSource;
	}
	
	
	
	
	
	/**
	 * 获得网页纯文本get HTML Source Just Reservations
	 * 保留a、h1、h2、img、br标签
	 * @return
	 */
	public static String getHSJR(String HTMLSource){
		StringBuffer text = new StringBuffer();
		String etd =  HTMLSource;
		
		etd = etd.replaceAll("<[a|A]+\\s+", "`,a,`");
		etd = etd.replaceAll("<\\s*/\\s*[a|A]+\\s*>", "`,/a,`");
		etd = etd.replaceAll("<\\s*[h1|H1][^<>]*>", "`,h1,`");
		etd = etd.replaceAll("<\\s*/\\s*[h1|H1]+\\s*>", "`,/h1,`");
		etd = etd.replaceAll("<\\s*[h2|H2][^<>]*>", "`,h2,`");
		etd = etd.replaceAll("<\\s*/\\s*[h2|H2]+\\s*>", "`,/h2,`");
		etd = etd.replaceAll("<\\s*[i|I]+[m|M]+[g|G]+\\s+", "`,img,`");
		etd = etd.replaceAll("(<\\s*[b|B]+[r|R]+\\s*>|<\\s*/\\s*[b|B]+[r|R]+\\s*>|<\\s*[b|B]+[r|R]+\\s*/\\s*>)", "\r\n");
		etd = etd.replaceAll("<.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?>","").replaceAll("\t", " ");
		if(etd!=null && etd.length()>0){
			String[] strs = etd.split("\r\n");
			StringBuffer str = new StringBuffer();
			char[] chars1,chars2 = null; 
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars1 = strs[i].toCharArray();
					for(int j=0;j<chars1.length;j++){
						if(chars1[j]!=' '){
							str.append(strs[i].substring(j)+"\r\n");
							break;
			}}}}
			strs = str.toString().split("\r\n");
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars2 = strs[i].toCharArray();
					for(int j=chars2.length-1;j>=0;j--){
						if(chars2[j]!=' '){
							strs[i] = strs[i].substring(0,j+1)+"\r\n";
							break;
			}}}}
			for(int i = 0;i<strs.length;i++){
				if(strs[i].length()<8){
					strs[i] = strs[i].replaceAll("\r\n"," ");
				}
				text.append(strs[i]);
			}
		}
		etd = text.toString();
		etd = etd.replace("  ", "");
		etd = etd.replace("`,a,`", "<a ");
		etd = etd.replace("`,/a,`", "</a>");
		etd = etd.replace("`,h1,`", "<h1>");
		etd = etd.replace("`,/h1,`", "</h1>");
		etd = etd.replace("`,h2,`", "<h2>");
		etd = etd.replace("`,/h2,`", "</h2>");
		etd = etd.replace("`,img,`", "<img border=0 ");
		etd = etd.replace("\r\n", "<br>");
		etd = etd.replace("<br>/>", "/>");
		etd = etd.replaceAll("&(ensp|#8194);", " ").replaceAll("&(emsp|#8195);", " ")
		.replaceAll("&(amp|#38);", "&").replaceAll("&(lt|#60);", "<")
		.replaceAll("&(gt|#62);", ">").replaceAll("&(yen|#165);", "¥")
		.replaceAll("&(macr|#175);", "¯").replaceAll("&(acute|#180);", "´")
		.replaceAll("&(deg|#176);", "°").replaceAll("&(&ordm|#186);", "º")
		.replaceAll("&(quot|#34);", "\"").replaceAll("&(copy|#169);", "©")
		.replaceAll("&(reg|#174);", "®").replaceAll("&(™|#8482);", "™")
		.replaceAll("&(times|#215);", "×").replaceAll("&(divide|#247);", "÷")
		.replaceAll("&(curren|#164);", "¤").replaceAll("&(OElig|#338);", "Œ")
		.replaceAll("&(oelig|#339);", "œ").replaceAll("&(circ|#710);", "ˆ")
		.replaceAll("&(ndash|#8211);", "–").replaceAll("&(mdash|#8212);", "—")
		.replaceAll("&(lsquo|#8216);", "‘").replaceAll("&(rsquo|#8217);", "’")
		.replaceAll("&(sbquo|#8218);", "‚").replaceAll("&(ldquo|#8220);", "“")
		.replaceAll("&(rdquo|#8221);", "”").replaceAll("&(bdquo|#8222);", "„")
		.replaceAll("&(dagger|#8224);", "†").replaceAll("&(Dagger|#8225);", "‡")
		.replaceAll("&(permil|#8240);", "‰").replaceAll("&(nbsp|#160);", " ")
		.replaceAll("&(\\w+|\\d+);", " ").replaceAll("&#(\\d+);", " ");
		return etd;
	}
	
	/**
	 * 获得网页纯文本
	 * 保留h1、h2标签
	 * @return
	 */
	public static String getTextResHn(String HTMLSourceCode){
		StringBuffer text = new StringBuffer();
//		String finalText = null;
		String etd =  HTMLSourceCode;
		etd = etd.replaceAll("<\\s*[h1|H1]+\\s*>", "`,h1,`");
		etd = etd.replaceAll("<\\s*/\\s*[h1|H1]+\\s*>", "`,/h1,`");
		etd = etd.replaceAll("<\\s*[h2|H2]+\\s*>", "`,h2,`");
		etd = etd.replaceAll("<\\s*/\\s*[h2|H2]+\\s*>", "`,/h2,`");
		etd = etd.replaceAll("(<\\s*[b|B]+[r|R]+\\s*>|<\\s*/\\s*[b|B]+[r|R]+\\s*>|<\\s*[b|B]+[r|R]+\\s*/\\s*>)", "\r\n");
		etd = etd.replaceAll("<.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?>","").replaceAll("\t", " ");
		if(etd!=null && etd.length()>0){
			String[] strs = etd.split("\r\n");
			StringBuffer str = new StringBuffer();
			char[] chars1,chars2 = null; 
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars1 = strs[i].toCharArray();
					for(int j=0;j<chars1.length;j++){
						if(chars1[j]!=' '){
							str.append(strs[i].substring(j)+"\r\n");
							break;
			}}}}
			strs = str.toString().split("\r\n");
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars2 = strs[i].toCharArray();
					for(int j=chars2.length-1;j>=0;j--){
						if(chars2[j]!=' '){
							strs[i] = strs[i].substring(0,j+1)+"\r\n";
							break;
			}}}}
			for(int i = 0;i<strs.length;i++){
				if(strs[i].length()<8){
					strs[i] = strs[i].replaceAll("\r\n"," ");
				}
				text.append(strs[i]);
			}
		}
		etd = text.toString();
		etd = etd.replaceAll("  ", "");
		etd = etd.replaceAll("`,h1,`", "<h1>");
		etd = etd.replaceAll("`,/h1,`", "</h1>");
		etd = etd.replaceAll("`,h2,`", "<h2>");
		etd = etd.replaceAll("`,/h2,`", "</h2>");
		etd = etd.replaceAll("&(ensp|#8194);", " ").replaceAll("&(emsp|#8195);", " ")
		.replaceAll("&(amp|#38);", "&").replaceAll("&(lt|#60);", "<")
		.replaceAll("&(gt|#62);", ">").replaceAll("&(yen|#165);", "¥")
		.replaceAll("&(macr|#175);", "¯").replaceAll("&(acute|#180);", "´")
		.replaceAll("&(deg|#176);", "°").replaceAll("&(&ordm|#186);", "º")
		.replaceAll("&(quot|#34);", "\"").replaceAll("&(copy|#169);", "©")
		.replaceAll("&(reg|#174);", "®").replaceAll("&(™|#8482);", "™")
		.replaceAll("&(times|#215);", "×").replaceAll("&(divide|#247);", "÷")
		.replaceAll("&(curren|#164);", "¤").replaceAll("&(OElig|#338);", "Œ")
		.replaceAll("&(oelig|#339);", "œ").replaceAll("&(circ|#710);", "ˆ")
		.replaceAll("&(ndash|#8211);", "–").replaceAll("&(mdash|#8212);", "—")
		.replaceAll("&(lsquo|#8216);", "‘").replaceAll("&(rsquo|#8217);", "’")
		.replaceAll("&(sbquo|#8218);", "‚").replaceAll("&(ldquo|#8220);", "“")
		.replaceAll("&(rdquo|#8221);", "”").replaceAll("&(bdquo|#8222);", "„")
		.replaceAll("&(dagger|#8224);", "†").replaceAll("&(Dagger|#8225);", "‡")
		.replaceAll("&(permil|#8240);", "‰").replaceAll("&(nbsp|#160);", " ")
		.replaceAll("&(\\w+|\\d+);", " ").replaceAll("&#(\\d+);", " ");
		return etd;
	}
	
	
	/**
	 * 获得网页纯文本
	 * @return
	 */
	public static String getText(String HTMLSourceCode){
		StringBuffer text = new StringBuffer();
		String finalText = null;
		String etd =  HTMLSourceCode.replaceAll("(<\\s*[b|B]+[r|R]+\\s*>|<\\s*/\\s*[b|B]+[r|R]+\\s*>|<\\s*[b|B]+[r|R]+\\s*/\\s*>)", "\r\n");
		if(etd!=null && etd.length()>0){
			String string = etd.replaceAll("<.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?(\r\n)*.*?>","").replaceAll("\t", " ");//
			String[] strs = string.split("\r\n");
			StringBuffer str = new StringBuffer();
			char[] chars1,chars2 = null; 
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars1 = strs[i].toCharArray();
					for(int j=0;j<chars1.length;j++){
						if(chars1[j]!=' '){
							str.append(strs[i].substring(j)+"\r\n");
							break;
			}}}}
			strs = str.toString().split("\r\n");
			for(int i=0;i<strs.length;i++){
				if(strs[i].length()>0 && !strs[i].equals(" ")){
					chars2 = strs[i].toCharArray();
					for(int j=chars2.length-1;j>=0;j--){
						if(chars2[j]!=' '){
							strs[i] = strs[i].substring(0,j+1)+"\r\n";
							break;
			}}}}
			for(int i = 0;i<strs.length;i++){
				if(strs[i].length()<8){
					strs[i] = strs[i].replaceAll("\r\n"," ");
				}
				text.append(strs[i]);
			}
		}
		finalText =text.toString().replace("  ", " ").replace("   ", " ")
		.replaceAll("    ", " ").replaceAll("     ", " ")
		.replaceAll("&(ensp|#8194);", " ").replaceAll("&(emsp|#8195);", " ")
		.replaceAll("&(amp|#38);", "&").replaceAll("&(lt|#60);", "<")
		.replaceAll("&(gt|#62);", ">").replaceAll("&(yen|#165);", "¥")
		.replaceAll("&(macr|#175);", "¯").replaceAll("&(acute|#180);", "´")
		.replaceAll("&(deg|#176);", "°").replaceAll("&(&ordm|#186);", "º")
		.replaceAll("&(quot|#34);", "\"").replaceAll("&(copy|#169);", "©")
		.replaceAll("&(reg|#174);", "®").replaceAll("&(™|#8482);", "™")
		.replaceAll("&(times|#215);", "×").replaceAll("&(divide|#247);", "÷")
		.replaceAll("&(curren|#164);", "¤").replaceAll("&(OElig|#338);", "Œ")
		.replaceAll("&(oelig|#339);", "œ").replaceAll("&(circ|#710);", "ˆ")
		.replaceAll("&(ndash|#8211);", "–").replaceAll("&(mdash|#8212);", "—")
		.replaceAll("&(lsquo|#8216);", "‘").replaceAll("&(rsquo|#8217);", "’")
		.replaceAll("&(sbquo|#8218);", "‚").replaceAll("&(ldquo|#8220);", "“")
		.replaceAll("&(rdquo|#8221);", "”").replaceAll("&(bdquo|#8222);", "„")
		.replaceAll("&(dagger|#8224);", "†").replaceAll("&(Dagger|#8225);", "‡")
		.replaceAll("&(permil|#8240);", "‰").replaceAll("&(nbsp|#160);", " ")
		.replaceAll("&(\\w+|\\d+);", " ").replaceAll("&#(\\d+);", " ");
		return finalText;
	}
	
	/**
	 * 获得网页关键字
	 * @return
	 */
	public static String getValueByKeywords(String HTMLSourceCode){
		String keyword = "";
		String str = getTagByKeywords(HTMLSourceCode);
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
	        Pattern pattern = Pattern.compile("[c|C]+[o|O]+[n|N]+[t|T]+[e|E]+[n|N]+[t|T]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)");
	        Matcher matcher = pattern.matcher(str);//
			boolean result = matcher.find(); 
	        if(result){ 
	        	keyword = matcher.group(1); 
	        } 
	        if(keyword!=null && keyword.length()>2){
				//去除双引号和单引号。
				if(keyword.substring(0,1).equals("\"") || keyword.substring(0,1).equals("'")){
					keyword = keyword.substring(1,keyword.length());
				}
				if(keyword.substring(keyword.length()-1).equals("\"") || keyword.substring(keyword.length()-1).equals("'")){
					keyword = keyword.substring(0,keyword.length()-1);
				}
			}
		}
		return keyword;
	}
	
	/**
	 * 获得网页关键字标签
	 * @return
	 */
	public static String getTagByKeywords(String HTMLSourceCode){
		String keyword = "";
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			Pattern pattern = Pattern.compile("(<\\s*[m|M]+[e|E]+[t|T]+[a|A]+\\s*[^<>]*[n|N]+[a|A]+[m|M]+[e|E]+\\s*=\\s*[^<>]*[k|K]+[e|E]+[y|Y]+[w|W]+[o|O]+[r|R]+[d|D]+[s|S]+[^<>]*>)");
			Matcher matcher = pattern.matcher(HTMLSourceCode);
			boolean result = matcher.find(); 
	        if(result){ 
	        	keyword = matcher.group(1); 
	        } 
		}
		return keyword;
	}
	
	
	/**
	 * 获得网页关描述标签
	 * @return
	 */
	public static String getTagByDescription(String HTMLSourceCode){
		String Description = "";
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
			Pattern pattern = Pattern.compile("(<\\s*[m|M]+[e|E]+[t|T]+[a|A]+\\s*[^<>]*[n|N]+[a|A]+[m|M]+[e|E]+\\s*=\\s*[^<>]*[d|D]+[e|E]+[s|S]+[c|C]+[r|R]+[i|I]+[p|P]+[t|T]+[i|I]+[o|O]+[n|N]+[^<>]*>)");
			Matcher matcher = pattern.matcher(HTMLSourceCode);
			boolean result = matcher.find(); 
	        if(result){ 
	        	Description = matcher.group(1); 
	        } 
	        
		}
		return Description;
	}
	
	
	/**
	 * 获得网页关描述内容
	 * @return
	 */
	public static String getValueByDescription(String HTMLSourceCode){
		String Description = "";
		String str = getTagByDescription(HTMLSourceCode);
		if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
	        Pattern pattern = Pattern.compile("[c|C]+[o|O]+[n|N]+[t|T]+[e|E]+[n|N]+[t|T]+\\s*=\\s*(\"\\s*(.+?)\\s*\"|\'\\s*(.+?)\\s*\'|(.+?))(\\s{1}|>)");
	        Matcher matcher = pattern.matcher(str);//
			boolean result = matcher.find(); 
	        if(result){ 
	        	Description = matcher.group(1); 
	        } 
	        if(Description!=null && Description.length()>2){
				//去除双引号和单引号。
				if(Description.substring(0,1).equals("\"") || Description.substring(0,1).equals("'")){
					Description = Description.substring(1,Description.length());
				}
				if(Description.substring(Description.length()-1).equals("\"") || Description.substring(Description.length()-1).equals("'")){
					Description = Description.substring(0,Description.length()-1);
				}
			}
		}
		return Description;
	}
	
	/**
	 * 判断是否是有效的网页，以排除其他文件。
	 * @param url
	 * @return
	 */
	public static boolean canConnection(String url){
		boolean isHTML = false;
		URL Url = null;
		try{
			Url = new URL(url);
			 HttpURLConnection con = (HttpURLConnection) Url.openConnection(); 
			 con.getInputStream(); 
			isHTML = true;
		}catch(Exception e){
			isHTML = false;
		}
		return isHTML;
	}
	
	/**
	 * 判断URL是否是一级域名。
	 * 不健全的
	 * @return
	 */
	public static boolean isLevelDomain(String URL){
		String str = URL;
		if(str.charAt(str.length()-1)=='/'){
			str = str.substring(0,str.length()-1);
		}
		boolean isLevelDomain = false;
		char[] chars = str.toCharArray();
		int count=0;
		for(int i=0;i<chars.length;i++){
			if(chars[i]=='/'){
				count++;
			}
		}
		if(count==2){
			isLevelDomain = true;
		}
		return isLevelDomain;
	}
}
