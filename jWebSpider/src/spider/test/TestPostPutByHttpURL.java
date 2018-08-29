package spider.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class TestPostPutByHttpURL {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
//		URL url = new URL("http://fjnx.v084.10000net.cn/process/collection/AddClass");
//		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
//		
//		// 设置允许output
//        huc.setDoOutput(true);
//        // 设置为post方式
//        huc.setRequestMethod("POST");
//        String className="城市之夜2";
//        OutputStream os = huc.getOutputStream();
//        os.write(("className="+className).getBytes("utf-8"));
//        os.close();
//        
//        BufferedReader br = new BufferedReader(new InputStreamReader(huc .getInputStream()));
//        huc.connect();
//        String line = br.readLine();
//        
//        
//        
//        huc.disconnect();
//        
        
		Map map = new HashMap<String,Object>();
		HashMap<String,Object> aha = (HashMap<String,Object>)map.get("aaa");
		System.out.println((aha==null));
		
		
		
		
		
	}

}
