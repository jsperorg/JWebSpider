package spider.test;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spider.analyze.HTMLAnalyze;
import spider.utility.Utility;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		String resUrl = "aaa[1-4]aaa";
		int pageNumber = 1;
		String all_matche = "[1-4]";//"\\[1-4\\]";

		//resUrl = resUrl.replaceAll("\\[","[");
		//resUrl = resUrl.replaceAll("\\]","]");
		resUrl = resUrl.replaceAll(all_matche, String.valueOf(pageNumber));

		String a= "A123456A";
		a = a.substring(1);
		a = a.substring(0, a.length()-1);
		System.out.println(a);
			
		
		//System.out.println(resUrl);
		
		
		
		
//		String s="jcoekgl&";
//		s=s.substring(0,s.length()-1);
//		System.out.println(s);
		//System.out.print("   aaa\r\n     ".trim().equals("aaa"));
//		String[] strsf ="yagjgtrgeiegftyuewxvcgfgfddsdfgrehjhggft".split("b");
//
//		System.out.println(strsf.length);
//		
//		for(String sf:strsf){
//			System.out.println(sf);
//			String[] strss = sf.split("n");
//			for(String ss:strss){
//				System.out.println(ss);
//			}
//		}
		
		
//		String[] strs ="斯蒂芬金((大佛精度高jlsdfgjdogjdogrj上)电股广泛地舒服".split("\\u0029");
//		
//		for(String s:strs){
//			System.out.println(s);
//		}
		
//		String[] s={"dsfsg-sfskhck","dfsfsdfsd"};
//
//		System.out.println();
		//System.out.println(s.replaceAll("\\-", "\\\\-"));
		
//		for(int i=0;i<5;i++){
//			System.out.println(i);
//			i=i;
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
//		String hss="链接上佛<div>第三方的<h1>AAAd df的 方法后发给我 </h1><span>的方法gre问题</span></div>啊啊啊方法<div>第三方的<h1>AAAd df的 方法后发给我 </h1><span>的方法gre问题</span></div>dcgfdgre地方";
//		String ss="<div>第三方的<h1>";
//		String es="</span></div>";
//		System.out.println(HTMLAnalyze.getMatcher(hss,ss,es).get(0));
		
//
//		System.out.println("<a href='http://www.g.cn/'><table id='sdfsdf' class='csdcs'>sdlfjog</table></a>".replaceAll("<table.+?>", ""));
//		System.out.println("<a href='http://www.g.cn/'><table id='sdfsdf' class='csdcs'>sdlfjog</table></a>".replaceAll("<table[^<>]*>", ""));
//		
//		
//		String source = "AAA<a href='http://www.aaaa.com/'>BBB<span id='mytable' class='tableStyle'>CCC</span></a>DDD";
//		System.out.println(source.replaceAll("<.*?>", ""));
//		System.out.println(source.replaceAll("<.*>", ""));
//		
//		System.out.println(source.replaceAll("<span.*?>", ""));
//		System.out.println(source.replaceAll("<span[^<>]*>", ""));
//		
		//在上面的HTML代码中，我要清除掉所有HTML标记，运行上面的代码得出以下结果：
		
		//根据结果表明，在java正则表达式中 问号 表示惰性匹配，即匹配的内容仅在问号后面表达式第一次出现的位置止。如果不带上问号，即表示匹配文本中最后一个">"。
		
		/*
		String source = "AAA<a href='/mop_go.do?sid=43625504' target='_blank' style='color:#FE0000' id='title'>BBB</a>CCC";
		String ss = "<a href='/mop_go\\.do\\?sid=[0-9]+' target='_blank' style='color:[^']+' id='title'>";
		String es = "</a>";
		
		ArrayList<String> res = HTMLAnalyze.getMatcher(source,ss,es);
		
		for(String s:res){
			System.out.println(s);
		}
		*/
		//System.out.println(GetNowDate());
		
		/*
		
		System.out.println("wocaoamabide".indexOf("dfcao"));
		
		
		List<Map<String, String>> list = null;
		try {
			list = Utility.getServerResultByJavaObject("http://localhost/gejiehui/process/GetAllRes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(list!=null && list.size()>0){
			for(Map<String,String> res:list){

				String resUrl = res.get("res_url");
				String classId = res.get("class_id");
				String resName = res.get("res_name");
				String resLinksStart = res.get("res_links_start");
				String resLinksEnd =res.get("res_links_end");
				String resTitleStart = res.get("res_title_start");
				String resTitleEnd = res.get("res_title_end");
				String resAuthorStart = res.get("res_author_start");
				String resAuthorEnd = res.get("res_author_end");
				String resContentStart = res.get("res_content_start");
				String resContentEnd = res.get("res_content_end");
				
				String resGetComment = res.get("res_getComment");
				String commentContentAreaStarts = res.get("res_comment_content_area_start");
				String commentContentAreaEnd = res.get("res_comment_content_area_end");
				String commentAuthorIdStart = res.get("res_comment_authorid_start");
				String commentAuthorIdEnd = res.get("res_comment_authorid_end");
				String commentTimeStart = res.get("res_comment_time_start");
				String commentTimeEnd = res.get("res_comment_time_end");
				String commentContentStart = res.get("res_comment_content_start");
				String commentContentEnd = res.get("res_comment_content_end");
				
				
				
				Map<String,String> parMap = new HashMap<String,String>();
				

				parMap.put("resUrl", resUrl);
					parMap.put("classId", classId);
					parMap.put("resName", resName);
					parMap.put("resLinksStart", resLinksStart);
					parMap.put("resLinksEnd", resLinksEnd);
					parMap.put("resTitleStart", resTitleStart);
					parMap.put("resTitleEnd", resTitleEnd);
					parMap.put("resAuthorStart", resAuthorStart);
					parMap.put("resAuthorEnd", resAuthorEnd);
					parMap.put("resContentStart", resContentStart);
					parMap.put("resContentEnd", resContentEnd);
					
						parMap.put("getComment",resGetComment);
						parMap.put("commentContentAreaStarts", commentContentAreaStarts);
						parMap.put("commentContentAreaEnd", commentContentAreaEnd);
						parMap.put("commentAuthorIdStart", commentAuthorIdStart);
						parMap.put("commentAuthorIdEnd", commentAuthorIdEnd);
						parMap.put("commentTimeStart", commentTimeStart);
						parMap.put("commentTimeEnd", commentTimeEnd);
						parMap.put("commentContentStart", commentContentStart);
						parMap.put("commentContentEnd", commentContentEnd);

						String resultSet =null;
						try {
							resultSet = Utility.serverPostRequest("http://fjnx.v084.10000net.cn/process/NewRes", parMap);
						} catch (Exception e) {
							e.printStackTrace();
						}
				
				System.out.println(resultSet);
				if(Integer.parseInt(resultSet)>0){
					System.out.println("已保存到服务器！");
				}else{
					System.out.println("保存失败！！！！");
				}
				
				
				
			}
		}
		*/
		
	}
	
	
}
