# JWebSpider
A simple web spider based on the Java. Contains a Spider Client and PHP website.

JWebSpider是一个网站内容抓取器，基于java语言，抓取器是一个用java swing开发的桌面应用。   
本源码包含一个抓取器，一个php+MySQL的配套web程序。  

### 使用方法
> 1. 运行php web程序，执行程序附带的SQL脚本。采集器的采集资源表在SQL脚本中，采集器依赖该SQL建的数据库，存储用户配置的采集资源，采集器将采集的数据也使用post提交到该web程序，保存到数据库中。  
> 2. 运行抓取器，配置抓取器，将抓取器与web连接起来。添加采集源及规则后便可开始采集。抓取器从web请求得到采集源并显示为树，开始采集时又将采集结果post提交给web程序。  

抓取器主要可配置列表页、内容页规则。抓取的内容包括文本、图片。文本可翻译，图片可下载上传到web。  

抓取器的核心由作者自行设计实现，未使用第三方组件，因此请自酌可用性。核心是HTML解析器jWebSpider/src/spider/analyze/HTMLAnalyze.java。该解析器提供了一堆公共方法，可单独作为爬虫模块。   
用法示例：  
```java
//获取某个url的HTML文本
String HTML = HTMLAnalyze.connection("https://news.qq.com/");

//去噪（去除script、style等无意义内容）
HTML = HTMLAnalyze.getEliminated(HTML);

//获得匹配的源码段
ArrayList<String> machedSources = HTMLAnalyze.getMatcher(HTML,startExp,endExp);

//获得html中的超链接及对应文本,key为url,value为文本，并转换相对路径到http url
HashMap<String,String> aTags = HTMLAnalyze.getAHrefAndText(basePath, html);

...

```

### 界面截图
![Configration](http://jsper.org/JWebSpider/config.png)  

![Set resource.](http://jsper.org/JWebSpider/res_update.png)  

![Main frame.](http://jsper.org/JWebSpider/picker.png)


