package ui;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;

import spider.analyze.HTMLAnalyze;
import spider.checkboxtree.CheckboxTree;
import spider.utility.Utility;

import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JEditorPane;
import javax.swing.JScrollBar;
import java.awt.Color;

public class MainFrame extends JFrame {

	private JPanel contentPane = null;
	private static CheckboxTree tree = null;
	private static List<Map<String, String>> treeDataList = null;
	private static Map<String,DefaultMutableTreeNode> firstLevelNodes = null;
	private boolean stop = false;
	private boolean stop_status_is_outputed = false;
	private static JScrollPane scrollPane = new JScrollPane();
	private JTextPane textPane = null;
	private SimpleAttributeSet sas = null;//属性集合对象，主要用于textPane多样化文字样式和颜色设置
	
	/***各目标资源采集成功的帖子数量（用于显示在jTextPane上做参考）***/
	private int successfullyCount = 0;
	
	private static Properties config = null;//采集程序参数配置信息对象
	private static MainFrame frame = null;//本窗体自己的引用，用于将本窗体对象传入到其他窗体，便于其他窗体调用关闭本窗体方法。
	
	private static int allSelectFlag = 1;//全选按钮切换值
	/***
	 * textPane文字填充方法
	 * @param text 文字内容
	 * @param fg 文字颜色
	 */
	public void putText(String text,Color color){
		

		/* 此方法执行频繁，以下代码比较消耗内存，SimpleAttributeSet对象已以全局变量方式创建
		 * 此注释请勿删除！
		SimpleAttributeSet sas = new SimpleAttributeSet();//属性集合对象
	    StyleConstants.setFontFamily(sas, "宋体");
	    StyleConstants.setFontSize(sas, 14);
	    */
		
	    StyleConstants.setForeground(sas,color);//设置此次填充内容的前景颜色
	    
		try {
			int textLength = textPane.getStyledDocument().getLength();
			if(textLength>256000){
				textPane.setText(textPane.getText().substring(textLength-256000, textLength-1));
			}
			//将文本填充到末尾
			textPane.getStyledDocument().insertString(textPane.getStyledDocument().getLength(), text,sas);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		//滚动移动至到最底端
		textPane.setCaretPosition(textPane.getStyledDocument().getLength());
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception{

		
		
		
		
		
		
		/**设置全局UI字体样式**/
		Font font = new Font("宋体", Font.PLAIN, 12);
	    Enumeration keys = UIManager.getLookAndFeelDefaults().keys();
	    while (keys.hasMoreElements()) {
	         Object key = keys.nextElement();
	         if (UIManager.get(key) instanceof Font) {
	             UIManager.put(key, font);
	         }
	   } 

		//指定界面主题皮肤
		//UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				
				
				
				
				
				//加载程序配置信息
		        try {
		        	File f = new File("config.properties");
		        	if(f.exists()){

		            	FileInputStream in = new FileInputStream(f);
		            	config = new Properties();
		            	config.load(in);//加载properties
		            	
		        	}else{
		        		JOptionPane.showMessageDialog(null,"无法加载程序配置文件config.properties，文件不存在！","提示",JOptionPane.ERROR_MESSAGE);
		        	}
		            
		        } catch (Exception e) {
		        	config = null;//如果配置文件读取失败，则将config全局变量对象赋值null，以便配置设置窗体对其进行判断
		        	JOptionPane.showMessageDialog(null,"配置文件读取异常，请检查config.properties！","提示",JOptionPane.ERROR_MESSAGE);
		        }
				
				
				
				
				try {
					frame = new MainFrame();
					//frame.setFont(new Font("宋体",Font.PLAIN,12));
					frame.setTitle("加载中…");
					frame.setVisible(true);
					frame.initialize();

					String domain_name = config.getProperty("domain_name");
					String site_name = config.getProperty("site_name");
					frame.setTitle(site_name+" - "+domain_name + " 采集器");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
			}
		});
	}
	
	
	public static void initialize(){
		
		
		/**动态构建树代码**/
		//查询所有类别（class表），循环遍历结果并创建Node
		
		try {
			treeDataList =Utility.getServerResultByJSON(config.getProperty("resource"));
		} catch (Exception e) {

			System.out.println("配置文件加载异常");
			JOptionPane.showMessageDialog(null,e.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		
		
		//System.out.println(treeDataList);
		/*
		//测试数据：
		List<Map<String, String>> treeDataList =new  ArrayList<Map<String, String>>();
		Map<String, String> node1 = new HashMap<String, String>();
		node1.put("res_id", "1");
		node1.put("class_name", "社区");
		node1.put("res_name", "天涯社区");
		treeDataList.add(node1);
		
		Map<String, String> node5 = new HashMap<String, String>();
		node5.put("res_id", "5");
		node5.put("class_name", "糗事");
		node5.put("res_name", "煎蛋网");
		treeDataList.add(node5);
		
		Map<String, String> node2 = new HashMap<String, String>();
		node2.put("res_id", "2");
		node2.put("class_name", "糗事");
		node2.put("res_name", "糗事百科");
		treeDataList.add(node2);
		
		Map<String, String> node3 = new HashMap<String, String>();
		node3.put("res_id", "3");
		node3.put("class_name", "社区");
		node3.put("res_name", "猫扑社区");
		treeDataList.add(node3);

		Map<String, String> node4 = new HashMap<String, String>();
		node4.put("res_id", "4");
		node4.put("class_name", "论坛");
		node4.put("res_name", "网易论坛");
		treeDataList.add(node4);
		
		Map<String, String> node6 = new HashMap<String, String>();
		node6.put("res_id", "6");
		node6.put("class_name", "论坛");
		node6.put("res_name", "杭州19楼论坛");
		treeDataList.add(node6);
		*/
		firstLevelNodes = new HashMap<String,DefaultMutableTreeNode> ();
		if(treeDataList!=null && treeDataList.size()>0){
			for(Map<String, String> nodeDataMap:treeDataList){
				DefaultMutableTreeNode dmt = (/*将得到的一级节点信息转换成DefaultMutableTreeNode对象*/(DefaultMutableTreeNode)firstLevelNodes.get(/*在一级节点Map中获得当前被遍历的节点的父节点（分类）*/nodeDataMap.get(/*得到为当前节点数据的className*/"class_name")));
				/**如果等于null，说明还没有这个一级节点，需要立刻创建**/
				if(dmt==null){
					//创建一个TreeNode对象并指定显示名：
					dmt = new DefaultMutableTreeNode(nodeDataMap.get(/*得到为当前节点数据的className*/"class_name")); 
				}
				//往dmt里添加当前被遍历的子节点对象：
				DefaultMutableTreeNode secondNode = new DefaultMutableTreeNode(nodeDataMap.get("res_name"));
				dmt.add(secondNode);
				
				//子节点添加后重新将一级节点装进一级节点Map里。
				firstLevelNodes.put(nodeDataMap.get(/*得到为当前节点数据的className*/"class_name"), dmt);
				
			}
			//所有节点添加完毕后，将firstLevelNodes一级节点添加到tree控件里
			DefaultMutableTreeNode rootNode  =   new  DefaultMutableTreeNode("树根"); 
			
			for(String key:firstLevelNodes.keySet()){
				rootNode.add(firstLevelNodes.get(key));
			}
			

			
			
			//创建复选框树控件并绑定到分割面板左侧
			tree = new CheckboxTree(rootNode);
			//tree.setFont(new Font("宋体", Font.PLAIN, 12));
			//设置树根复选框为“选中”状态（也就是所有节点为选中状态）
			tree.setCheckingPath(tree.getPathForRow(0));
			scrollPane.setViewportView(tree);
			tree.expandAll();//展开所有节点
			tree.setAutoscrolls(true);
	
			tree.setRootVisible(false);

		}else{
			JOptionPane.showMessageDialog(null,"没有获取到相关数据！","错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	
	//递归遍历Tree 获得所有被选中的二级tree封装并返回。【已被其他直接语句块代替】
	/*
	public void visitAllNodes(DefaultMutableTreeNode node,ArrayList<String> selectedNodes) {
	       // node is visited exactly once
	       //process(node);
	   
	       if (node.getChildCount() >= 0) {
	           for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
	        	   DefaultMutableTreeNode  n = (DefaultMutableTreeNode )e.nextElement();

	               System.out.println(n.getLevel());
	               if(n.getLevel()==2 ){
	            	   selectedNodes.add(n.toString());
	               }
	               visitAllNodes(n,selectedNodes);
	           }
	       }
	   }
	*/
	
	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public MainFrame() throws MalformedURLException, IOException {
		//setTitle("哥姐会社区客户端采集器");
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 100, 864, 598);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("资源");
		//mnNewMenu.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(mnNewMenu);
		
		
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setBackground(Color.BLACK);//设置textPane黑色背景
		
		
		
		
		
		

		/** 创建textPane属性集合对象，用于自定义方法putText()（为了避免在putText方法里引发性能问题，故将该对象的创建置于此处） **/
		sas = new SimpleAttributeSet();//属性集合对象
	    StyleConstants.setFontFamily(sas, "宋体");
	    StyleConstants.setFontSize(sas, 14);

        /*
        textPane.setParagraphAttributes(sas,true) ;
        try {
        
			StyledDocument doc = textPane.getStyledDocument();
        	//追加绿色字体：
            StyleConstants.setForeground(a, Color.GREEN);
			doc.insertString(doc.getLength(), "jTextPane测试。。。",a);
			//追加红色字体：
            StyleConstants.setForeground(a, Color.RED);
			doc.insertString(doc.getLength(), "\nGGGGGGGGGGGGGGGG",a);

            StyleConstants.setForeground(a, Color.YELLOW);
			doc.insertString(doc.getLength(), "\npublic static void main(String[] args){",a);
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         */
		
		
		
		
		JMenuItem mntmJmenuitem = new JMenuItem("添加采集源");
		//mntmJmenuitem.setFont(new Font("宋体", Font.PLAIN, 12));
		mntmJmenuitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							
							//弹出添加采集源窗体
							ResourceFrame  ctf = new ResourceFrame(null,config);
							ctf.setBounds(149, 10, 1068, 740);
							ctf.frame = ctf;
							ctf.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		mnNewMenu.add(mntmJmenuitem);
		
		JMenuItem mi_edit_res = new JMenuItem("修改采集源");
		mi_edit_res.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				//得到被selected的tree叶子节点
				//String seltd = tree.getSelectionModel().toString();
				TreePath tp = tree.getSelectionPath();
				if(tp!=null){
					DefaultMutableTreeNode selectedNode =  (DefaultMutableTreeNode )tp.getLastPathComponent();
					
					if(selectedNode.isLeaf()){
						//循环遍历树数据集合，判断当前被选中节点的text是否与当前被遍历的一样，如果一样则将该节点对应的数据作为参数发送给创建窗体CreateTargetFrame
						for(Map<String, String> nodeData:treeDataList){
							if(nodeData.get("res_name").equals(selectedNode.toString())){
								ResourceFrame  ctf = new ResourceFrame(nodeData,config);
								ctf.setBounds(149, 10, 1068, 740);
								ctf.frame = ctf;
								ctf.setVisible(true);
							}
						}
					}else{
						JOptionPane.showMessageDialog(null,"只能选择叶子，不能选择枝干","提示",JOptionPane.WARNING_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null,"在树中选择一个叶子再进行本操作！","提示",JOptionPane.WARNING_MESSAGE);
					
				}
				
				
				
				
			}
		});
		//mi_edit_res.setFont(new Font("宋体", Font.PLAIN, 12));
		mnNewMenu.add(mi_edit_res);
		
		JMenuItem menuItem_2 = new JMenuItem("删除采集源");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				//得到被selected的tree叶子节点
				//String seltd = tree.getSelectionModel().toString();
				TreePath tp = tree.getSelectionPath();
				if(tp!=null){
					DefaultMutableTreeNode selectedNode =  (DefaultMutableTreeNode )tp.getLastPathComponent();
					
					if(selectedNode.isLeaf()){
						//循环遍历树数据集合，判断当前被选中节点的text是否与当前被遍历的一样，如果一样则将该节点对应的数据作为参数发送给创建窗体CreateTargetFrame
						for(Map<String, String> nodeData:treeDataList){
							if(nodeData.get("res_name").equals(selectedNode.toString())){


								int yesorno = JOptionPane.showConfirmDialog(null, "你确认删除"+nodeData.get("res_name")+"采集源吗？","提示",JOptionPane.YES_NO_OPTION);
								if(yesorno == JOptionPane.YES_OPTION){

									String resUrl = nodeData.get("res_url");
									String resName = nodeData.get("res_name");
									
									Map<String,String> parMap = new HashMap<String,String>();
									parMap.put("resUrl", resUrl);
									parMap.put("resName", resName);
									
									String resultSet="0";
									try {
										//System.out.println(config.getProperty("delete_resource"));
										resultSet = Utility.serverPostRequest(config.getProperty("delete_resource"), parMap);
									} catch (Exception e) {
										JOptionPane.showMessageDialog(null,"删除资源出错！","异常",JOptionPane.ERROR_MESSAGE);
										// TODO Auto-generated catch block
										//e.printStackTrace();
									}
									
									System.out.println("resultSet:["+resultSet+"]");
									
									if(Integer.parseInt(resultSet)>0){

										//JOptionPane.showMessageDialog(null,"删除成功！","提示",JOptionPane.INFORMATION_MESSAGE);
										
										initialize();//重新加载树
										
									}else{

										JOptionPane.showMessageDialog(null,"删除失败！","提示",JOptionPane.ERROR_MESSAGE);
									}
									
									
									
								}
								
							}
						}
					}else{
						JOptionPane.showMessageDialog(null,"只能选择叶子，不能选择枝干","提示",JOptionPane.WARNING_MESSAGE);
					}
				}else{
					JOptionPane.showMessageDialog(null,"在树中选择一个叶子再进行本操作！","提示",JOptionPane.WARNING_MESSAGE);
					
				}
				
				
				
				
			}
		});
		mnNewMenu.add(menuItem_2);
		
		JMenu menu_1 = new JMenu("设置");
		menuBar.add(menu_1);
		
		JMenuItem menuItem_1 = new JMenuItem("配置");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//弹出ConfigFrame程序配置窗体，并传入配置信息对象
				ConfigFrame cf = new ConfigFrame(config,frame);
				cf.setVisible(true);
			}
		});
		menu_1.add(menuItem_1);
		
		JMenu menu = new JMenu("帮助");
		//menu.setFont(new Font("宋体", Font.PLAIN, 12));
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("关于");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"版本：1.0\r\n作者：陈建宇\r\nE-mail：thejava@163.com\r\n版权所有！本软件最终解释权归开发者所有。","关于本软件",JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		});
		//menuItem.setFont(new Font("宋体", Font.PLAIN, 12));
		menu.add(menuItem);
		
		//创建一个JPanel来承载各种Swing控件。
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane.setBounds(10, 35, 211, 502);
		contentPane.add(scrollPane);
		
		//创建分割面板，左边放树，右边放Panel
		//JSplitPane splitPane = new JSplitPane();
		//splitPane.setBounds(10, 35, 529, 502);
		//splitPane.setDividerSize(10);//设置分割线大小
		//splitPane.setDividerLocation(200);//设置分割线位置
		//contentPane.add(splitPane);//将分割面板添加至内容面板里
		
		
		
//		public void visitAllNodes(CheckboxTree cbt) {
//		       TreeNode root = (TreeNode)cbt.getModel().getRoot();
//		       visitAllNodesa(root);
//		}
		
		JButton button = new JButton("开始");
		//button.setFont(new Font("宋体", Font.PLAIN, 12));
		button.setFocusPainted(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				//实现暂停功能，新创建一个线程并调用启动方法。
				new Thread() { 
					//实现父类的默认运行方法。
		            public void run() { 
						////无限循环进行
						//while(true) { 
		            	
		            	
				//遍历树，获得所有被选中的二级节点。
				TreePath[] tps = tree.getCheckingModel().getCheckingPaths();//获得所有被选中的节点Path
				for(TreePath tp: tps){
					DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode )tp.getLastPathComponent();
					if(/*如果节点的级别为2级*/currentNode.getLevel()==2 ){
						//循环遍历节点的数据源，判断节点数据源的res_name是否与被选中的节点的text相等，如果相等，则获取该节点数据源的res_url值并抓取该URL。
						for(int res_index=0;res_index<treeDataList.size();res_index++){
							
							Map<String, String> nodeData = treeDataList.get(res_index);
							
							if(nodeData.get("res_name").equals(currentNode.toString())){
								//调用抓取器抓取url
								String resUrl = nodeData.get("res_url");
								
								
								/***
								 * 资源url有两种类型，一种固定的url，直接获取这个页面里的链接列表，然后抓取，
								 * 还有种是多链接列表页的模板url，递增页码，这种需要查询上次采集进度（已采集到了哪一页及哪个位置了）,然后接着采集。
								 * 
								 * 固定页url：http://content.com/article/3/2600.html
								 * 模板页url：http://content.com/article/3/[2600-1].html
								 * 
								 * [2600-1] 表示从2600页递减采集到1页。
								 * [1-2600] 表示从1页递增采集到2600页。
								 * 
								 * 如果是多链接列表页情况的话，每个内容采集都记录一次采集进度，记住页码和链接位置（index）
								 * 每页最后一个链接采集后，记录页码进度时，将页码根据情况记录为下一页，将链接index设置为-1
								 * 即表示下一页还未开始，循环进行下一页时，取到链接index后，+1后再抓取。
								 * 链接index表示已经采集了此位置，因此每页还未采集时此值不可以为0，为-1才妥。
								 * 
								 * 
								 */
								

								Pattern page_pattern = Pattern.compile("\\[(\\d+-\\d+)\\]");
								Matcher page_matcher = page_pattern.matcher(resUrl);//例如：http://dlie2.news.ldjie.com/a/2/dsff[1-2]b.html
								//Matcher matcher = pattern.matcher("http://dlie2.news.ldjie.com/a/2/dsff2b.html");
								boolean isMultiLinksPage = page_matcher.find(); 
								//System.out.println(page_result);

								//int 

								int leftPageNumber = 0;//多链接列表情况的开始页码，与结束页码比较后确定页码递增抓取还是递减抓取
								int rightPageNumber = 0;//多链接列表情况的结束页码，与结束页码比较后确定页码递增抓取还是递减抓取
								int pageNumber = -1;//多链接列表情况的链接列表页页码，数据库中初始默认值为-1，表示还未开始任何链接页的抓取。
								int linkIndex = -1;//多链接列表情况的已抓取链接位置，数据库中初始默认值为-1，表示当前链接列表页还未开始任何链接的抓取。
						        if(isMultiLinksPage){ 


						    		String all_matche = page_matcher.group(0);//得到[1-2]
						    		String parentheses_matche = page_matcher.group(1);//得到正则表达式小括里表达式匹配值1-2

						    		leftPageNumber = Integer.parseInt(parentheses_matche.split("-")[0]);
						    		rightPageNumber = Integer.parseInt(parentheses_matche.split("-")[1]);
						    		
						    		//查询该资源url的当前页码进度，替换resUrl为要采集的url（将[1-2]部分替换为具体页码数字）

									Map<String,String> pm = new HashMap<String,String>();
									pm.put("resUrl", resUrl);
									System.out.println("resUrl:"+resUrl);
									try{
										String info = Utility.serverPostRequest(config.getProperty("get_links_page_progress"), pm);
										System.out.println("get_links_page_progress:"+info);
										
										//info字符串内容为：pageNumber,linkIndex
										if(!"".equals(info) && info.indexOf(",")!=-1){
											pageNumber = Integer.parseInt(info.split(",")[0]);
											linkIndex = Integer.parseInt(info.split(",")[1]);
											
										}
										
										if(pageNumber==-1){
											/***
											 * 系统初始状态时，数据库中pageNumber值为-1，表示该url模板还未开始任何链接页的抓取。
											 * 第一次抓取时，pageNumber的值应为链接列表页页码开始值leftPageNumber。
											 */
											pageNumber=leftPageNumber;
										}
										
										System.out.println(all_matche+" to "+pageNumber);
										//多链接列表页情况，需要替换url为查询出的进度对应的url。例：将 http://xxx.xxx/x/[1-2].html 替换为 http://xxx.xxx/x/2.html
										resUrl = resUrl.replaceAll("\\["+parentheses_matche+"\\]", String.valueOf(pageNumber));

									}catch(Exception ex){
										putText( "查询多链接页采集进度时失败！重复尝试。\n",Color.RED);
										ex.printStackTrace();
										//res_index--;//重新抓取该链接列表页
										//continue;
									}
									

						        } 
								

								putText("开始抓取["+nodeData.get("res_name")+"]:"+resUrl+"\n",Color.GREEN);
								if(!resUrl.equals("") && resUrl!=null){
									
									
									/**       抓取URL开始     **/
									//创建抓取器对象
									String HTMLSourceCode = null;
									try {
										HTMLSourceCode = HTMLAnalyze.connection(resUrl);
										HTMLSourceCode = HTMLAnalyze.getEliminated(HTMLSourceCode);//去除无用标记，如<script>、<style>、<form>、注释等
										if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
											//System.out.println(alz.get_text());
											//System.out.println("1111111111111111111111111111111");
											//获得链接页开始和结束特征，并以此为参数得到两者之间的源码
											String resLinksStart = nodeData.get("res_links_start");
											String resLinksEnd = nodeData.get("res_links_end");
//											System.out.println("=========================================");
//											System.out.println(HTMLSourceCode);
//											System.out.println("=========================================");
//
											////为了防止正则表达式特殊字符的出现，必将特殊字符转义
											//resLinksStart = resLinksStart.replaceAll("\\(", "\\\\(");
											//resLinksStart = resLinksStart.replaceAll("\\)", "\\\\)");
											System.out.println("resLinksStart："+resLinksStart);
											
											//System.out.println("<div id=\"cityArea_0\" style=\"display:block;\">\r\n  <div class=\"list_cen\" id=\"热点推荐\" onclick=\"clickPartLink(event,'stat','热点推荐');\">".equals(resLinksStart));
											System.out.println("resLinksEnd："+resLinksEnd);
											
											System.out.println("HTMLSourceCode:\n"+HTMLSourceCode);
											
											//获得以resLinksStart开始以resLinksEnd结束的源码段
											ArrayList<String> LinksSources = HTMLAnalyze.getMatcher(HTMLSourceCode,resLinksStart,resLinksEnd);
											
											System.out.println(LinksSources.size());

											if(LinksSources==null || LinksSources.size()==0){
												putText("未匹配到链接，退出。",Color.RED);
												return;
											}
											//System.out.println(LinksSources);
											System.out.println(LinksSources.get(0));
											//System.out.println("2222222222222222222222222222222222222");
											
											//获得LinksSources里的所有超链接URL及文本
											HashMap<String,String> aTags = HTMLAnalyze.getAHrefAndText(resUrl, LinksSources.get(0));
											
											
											putText("["+nodeData.get("res_name")+"]共有"+aTags.keySet().size()+"个链接\n",Color.GREEN);
											
											if(isMultiLinksPage){
												putText("上次抓取到第"+(linkIndex+1)+"个链接\n",Color.GREEN);
											}
											
											
											int linkCount=0;
											ArrayList<HashMap<String,String>> targets = new ArrayList<HashMap<String,String>>();
											for(String aKey:aTags.keySet()){
												HashMap<String,String> target = new HashMap<String,String>();
												target.put(aKey, aTags.get(aKey));
												putText(aTags.get(aKey)+":"+aKey+"\n",Color.GREEN);
												targets.add(target);
											}
											
											
											successfullyCount = 0;//初始化目标采集成功帖子数量为0
											
											int startIndex=0;
											if(isMultiLinksPage){//如果是多链接列表页情况，开始采集下标应为上次采集到的地方+1
												startIndex=linkIndex+1;
												linkCount=linkIndex+1;
											}
											
											//循环抓取aTags里面的URL的文章（帖子）内容
											for(int x=startIndex;x<targets.size();x++){
												HashMap<String,String> target = targets.get(x);
												String aKey = "";
												for(String key:target.keySet()){
													aKey=key;
												}
												
													//如果没点击暂停按钮
							            			if(!stop){
												
												
												
												
												
												linkCount++;
												
												/**文章URL**/
												String articleUrl=aKey;

												try{
													putText("---------------------------------------------------------------------------------\n",Color.GREEN);
													putText("正在抓取：["+nodeData.get("res_name")+"]第"+linkCount+"个链接："+aKey+"\n",Color.GREEN);
													
													try{
														HTMLSourceCode = HTMLAnalyze.connection(articleUrl);
													}catch(Exception e){
														for(int iii=60;iii>0;iii--){

															//向EditPane里写异常报告并继续抓取下一个URL
															putText("连接超时，"+iii+"秒后重新连接\n",Color.RED);
															Thread.sleep(1000);
														}
														x=x-1;//重置抓取游标为上一个的，以便重新抓取上一个失败的链接。
														linkCount = linkCount-1;
														continue;
													}
													HTMLSourceCode = HTMLAnalyze.getEliminated(HTMLSourceCode);
													if(HTMLSourceCode!=null && !HTMLSourceCode.equals("")){
	
														/**文章标题**/
														String resTitleStart = nodeData.get("res_title_start");
														//System.out.println("resTitleStart:\n"+resTitleStart);
														String resTitleEnd = nodeData.get("res_title_end");
														//System.out.println("resTitleEnd:\n"+resTitleEnd);
														//System.out.println("HTMLSourceCode:"+HTMLSourceCode);
														String articleTitle=HTMLAnalyze.getMatcher(HTMLSourceCode,resTitleStart,resTitleEnd).get(0).replaceAll("<.+?>", "").trim();
														//System.out.println("articleTitle:"+articleTitle);

														//System.out.println("2222222222222222222222222222222222222");
														//String articleTitle = articleTitleSplited[articleTitleSplited.length-1];
														putText("文章标题：<<"+articleTitle+">>\n",Color.GREEN);
														//HTMLAnalyze.get_text(HTMLSourceCode)

														//判断文章是否已发布过，防止重复抓取。根据文章标题和来自URL进行逻辑并判断
														Map<String,String> pm = new HashMap<String,String>();
														pm.put("articleTitle", articleTitle);
														pm.put("articleFromUrl", articleUrl);
														
														String isExists = Utility.serverPostRequest(config.getProperty("content_is_exists"), pm);
														//System.out.println("已经POST到web");
														if(isExists.equals("true")){
															putText( "URL被重复抓取！web端文章已存在！自动放弃\n",Color.RED);
															continue;
														}
														
														
														/**文章作者**/
														String resAuthorStart = nodeData.get("res_author_start");
														String resAuthorEnd = nodeData.get("res_author_end");
														String articleAuthor=HTMLAnalyze.getMatcher(HTMLSourceCode,resAuthorStart,resAuthorEnd).get(0).replaceAll("<.+?>", "").trim();
														//String articleAuthor = articleAuthorSplited[articleAuthorSplited.length-1];
														
														putText("文章作者："+articleAuthor+"\n",Color.GREEN);

														/**发布日期**/
														String putTime = getDate();
														//System.out.println("发表时间："+putTime);

														/**文章内容**/
														String resContentStart = nodeData.get("res_content_start");
														String resContentEnd = nodeData.get("res_content_end");
														//System.out.println("文章内容开始和结束特征："+resContentStart+">>"+resContentEnd);
														//System.out.println("源：/r/n"+HTMLSourceCode+"");
														//String[] articleContentSplited=
														
														String articleContent = HTMLAnalyze.getHSJR(HTMLAnalyze.getMatcher(HTMLSourceCode,resContentStart,resContentEnd).get(0));//articleContentSplited[articleAuthorSplited.length-1];
														
														
														/** 图片上传、图片标签src替换、翻译流程 **/
														//1.查找源中img标签，拼装完整src地址
														String tempHTMLSC = articleContent;
														HashMap<String,String> hm = null;
														Pattern pattern = Pattern.compile("(<img\\s+[^<>]*>)");
														Matcher matcher = pattern.matcher(articleContent);
														
														boolean result = matcher.find();
														if(result){
															hm = new HashMap<String,String>();
														}
														int number=0;
														

														//2.生成img标签的替换唯一标识串，并替换掉img标签
														//3.创建key为生成的img的唯一标识串，value为拼装的完整src的串的map数据结构。
														while(result){ 
															number++;
															String str = matcher.group(1);
															
															
															//替换源为生成的唯一标识
															String unique = " imgsrc8x30cj9l1jx9zsj3xoa"+number+" ";//标识符+时间戳+序号+空格（空格为了翻译分割单词需要）
															tempHTMLSC = tempHTMLSC.replaceFirst(str, unique);

															//取得src并拼装完整src地址
															ArrayList<String> srclst = HTMLAnalyze.getSrc(str);
															String src = "";
															if(srclst!=null && srclst.size()>0){
																src = srclst.get(0);
															}
															String srcurl = HTMLAnalyze.buildURL(articleUrl, src);//取得完整URL
															
															hm.put(unique, srcurl);
															
														 	result=matcher.find(); 
														}
														articleContent = tempHTMLSC;//文章内容源=处理后的源
														
														//4.执行翻译、文件上传（当条件不满足时，此步骤可能会跳过）
														//如果系统开启了翻译，则翻译articleContent
														String isTranslate = nodeData.get("res_translate");
														if(!isTranslate.equals("无") && !isTranslate.equals("no")){
															
															//替换源中各标签为唯一标识，防止标签被翻译解析，翻译完成后将唯一标识反替换成标签
															articleContent = HTMLAnalyze.beforeTranslateTagReplace(articleContent);
															
															//执行翻译
															articleContent = Utility.translate(articleContent, isTranslate);
															
															//反替换
															articleContent = HTMLAnalyze.afterTranslateTagReplace(articleContent);
															
														}
														
														
														//如果系统开启了图片下载复制，则遍历map，下载并上传图片得到上传后的新的图片地址，并将新的地址覆盖到map对应的条目
														if(nodeData.get("res_image_method").equals("download")){
															if(hm!=null){
																for(String key:hm.keySet()){
																	String imgurl = hm.get(key);
																	try{
																		
																		byte[] file = Utility.fileDownload(imgurl);
																		
																		//上传并返回新的url地址
																		String newurl = Utility.fileUpload(file, config.getProperty("file_upload"));
																		if(!newurl.equals("")){
																			
																			//拼装完整url
																			newurl = HTMLAnalyze.buildURL(config.getProperty("file_upload"), newurl);
																			
																			hm.put(key, newurl);//修改图片url为新的url
																		}
																	}catch(Exception ex){
																		putText("图片下载/上传失败！\n",Color.red);
																	}
																}
																
															}
														}
														
														
														
														//5.遍历map，根据map结构里的value构建img标签并替换源中生成的相应的唯一标识（key），完成反替换。
														if(hm!=null){
															for(String key:hm.keySet()){
																articleContent = articleContent.replaceFirst(key.trim(), " <img border=\"0\" src=\""+hm.get(key)+"\" title=\""+articleTitle+"\"/>");
															}
														}
														
														
														
														
														
														//System.out.println("文章内容：\r\n"+articleContent+"");
														String proart = articleContent.trim();
														if(proart.length()>24){
															proart = proart.substring(0, 12)+"..."+proart.substring(proart.length()-12, proart.length());
														}
														putText("文章内容："+proart+"\n",Color.GREEN);
														HTMLSourceCode = HTMLSourceCode.split(resContentEnd)[1];
//														System.out.println("HTMLSourceCode:\n----------------------------------------------------------------------"+HTMLSourceCode);
//														System.out.println("\n----------------------------------------------------------------------");
														/**文章评论**/
														//判断是否需要抓取评论
														String getComment = nodeData.get("res_getComment");
														if(getComment.equals("true")){

															String commentContentAreaStarts = nodeData.get("res_comment_content_area_start");
															String commentContentAreaEnd = nodeData.get("res_comment_content_area_end");
//															System.out.println("commentContentAreaStarts:\n"+commentContentAreaStarts);
//															System.out.println("commentContentAreaEnd:\n"+commentContentAreaEnd);
															 ArrayList<String> ccl = HTMLAnalyze.getMatcher(HTMLSourceCode,commentContentAreaStarts,commentContentAreaEnd);
															//HTMLSourceCode = .get(0);
//															String[] splitedHTMLSourceCodes = HTMLSourceCode.split(commentContentAreaEnd);
//															splitedHTMLSourceCodes[splitedHTMLSourceCodes.length-1]="";
//															HTMLSourceCode="";
//															for(String splited:splitedHTMLSourceCodes){
//																HTMLSourceCode += splited;
//															}
															//HTMLSourceCode = commentContentAreaStarts+HTMLSourceCode+commentContentAreaEnd;
															//HTMLSourceCode = splitedHTMLSourceCodes.toString();
															//System.out.println("源：\r\n"+HTMLSourceCode);
															//String[] group = (HTMLSourceCode+"p|+——）（").split(commentContentAreaEnd);
															//commentContentAreaStarts = commentContentAreaStarts.replaceAll("\\%", "\\\\%");
															//commentContentAreaEnd = commentContentAreaEnd.replaceAll("\\-", "\\\\-");
//															System.out.println("commentContentAreaStarts>>\r\n"+commentContentAreaStarts);
//															System.out.println("commentContentAreaEnd>>\r\n"+commentContentAreaEnd);

															//String commentContents = HTMLAnalyze.getText(.get(0));
															//ArrayList<String> commentContents = HTMLAnalyze.getMatcher(HTMLSourceCode,commentContentAreaStarts,commentContentAreaEnd);
															

																putText("共有："+ccl.size()+"条评论\n",Color.GREEN);
															 //将评论内容也添加进文章
																StringBuilder sb = new StringBuilder();
																for(String ccs:ccl){
																	sb.append("<br>- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -<br>");
																	sb.append(HTMLAnalyze.getText(ccs));
																}
																articleContent = articleContent+sb;
														}
														putText("内容大小："+articleContent.getBytes().length+"字节\n",Color.GREEN);
															
															
														 
															//定义一个map存放参数名和值，最终这个map将发送给web将数据保存到数据库
															Map<String,String> parMap = new HashMap<String,String>();
															parMap.put("resId", nodeData.get("res_id"));
															parMap.put("articleTitle", articleTitle);
															parMap.put("articleAuthor", articleAuthor);
															parMap.put("articlePublishTime", putTime);
															parMap.put("articleFromUrl", articleUrl);
															parMap.put("articleContent", articleContent);
															
															String resultSet = Utility.serverPostRequest(config.getProperty("submit"), parMap);
															System.out.println(resultSet);
															if(Integer.parseInt(resultSet)>0){
																//成功提示
																//System.out.println("已保存到服务器！");
																putText("提交到web保存成功！\n",Color.GREEN);
																successfullyCount +=1;//设置目标采集成功帖子数量+1
																
																//如果是多链接列表情况，每篇文章保存时也要保存采集进度
																if(isMultiLinksPage){
																	try{
	
																		Map<String,String> update_links_page_progress_param = new HashMap<String,String>();

																		linkIndex = x;
																		//如果当前链接列表已经采集到最后一篇文章，则保存进度时，pageNumber应为下一页页码，linkIndex应为-1
																		if(x==targets.size()-1){
																			//如果模板页码范围，左边小于右边，那么就递增
																			if(leftPageNumber<rightPageNumber && pageNumber<rightPageNumber){
																				pageNumber = pageNumber + 1;
																			}else if(leftPageNumber>rightPageNumber && pageNumber>rightPageNumber){
																				pageNumber = pageNumber - 1;
																			}
																			linkIndex = -1;
																		}
																		
																		update_links_page_progress_param.put("resUrl", nodeData.get("res_url"));
																		update_links_page_progress_param.put("pageNumber", String.valueOf(pageNumber));
																		update_links_page_progress_param.put("linkIndex", String.valueOf(linkIndex));
																		String update_links_page_progress_result = Utility.serverPostRequest(config.getProperty("update_links_page_progress"), update_links_page_progress_param);

																		System.out.println("pageNumber:"+pageNumber);
																		System.out.println("linkIndex:"+linkIndex);
																		System.out.println("update_links_page_progress_result:"+update_links_page_progress_result);
																		if("true".equals(update_links_page_progress_result)){

																			putText( "进度保存成功，保存页码："+pageNumber+"，保存链接下标："+x+"\n",Color.GREEN);
																		}else{

																			putText( "进度保存失败！保存页码："+pageNumber+"，保存链接下标："+x+"\n",Color.RED);
																		}
																		

																		
																	}catch(Exception ex){

																		putText( "进度保存失败！保存页码："+pageNumber+"，保存链接下标："+x+"\n",Color.RED);
	
																	}
																}
																
															}else{
																//失败提示
																//System.out.println("保存到服务器失败！");

																putText("提交到web保存失败，请检查web服务器程序！\n",Color.GREEN);
															}
														
//															
//														for(int i=0;i<ccl.size();i++){
//
//
//															/**评论作者**/
//															String commentAuthorIdStart = nodeData.get("res_comment_authorid_start");
//															String commentAuthorIdEnd = nodeData.get("res_comment_authorid_end");
//															String commentAuthorBringUrl = HTMLAnalyze.getMatcher(ccl.get(i),commentAuthorIdStart,commentAuthorIdEnd).get(0);
//															String commentAuthorUrl = HTMLAnalyze.getHref(commentAuthorBringUrl).get(0);
//															commentAuthorUrl = HTMLAnalyze.buildURL(articleUrl, commentAuthorUrl);
//															String commentAuthor = commentAuthorBringUrl.replaceAll("<.+?>", "");
//															//作者带上个人空间的超链接。将超链接特征作为参数，获得完整URL并打上超链接标记。
//															//...code in here.
//															
//															
//															
//															/**评论时间**/
//															String commentTimeStart = nodeData.get("res_comment_time_start");
//															String commentTimeEnd = nodeData.get("res_comment_time_end");
//															String commentTime = HTMLAnalyze.getMatcher(ccl.get(i),commentTimeStart,commentTimeEnd).get(0);
//															
//															/**评论内容**/
//															String commentContentStart = nodeData.get("res_comment_content_start");
//															String commentContentEnd = nodeData.get("res_comment_content_end");
//															String commentConten = HTMLAnalyze.getMatcher(ccl.get(i),commentContentStart,commentContentEnd).get(0);
//															
//															
//															
//															
//															//System.out.println("commentContentStart>>\r\n"+commentContentStart);
//															//System.out.println("commentContentEnd>>\r\n"+commentContentEnd);
//															
//															//commentContentEnd = commentContentEnd.replaceAll("\\-", "\\\\-");
//															
//															
//															
//															//System.out.println((i+1)+"楼 <a href=\""+commentAuthorUrl+"\" target=\"_blank\">"+commentAuthor+"</a>  发表于："+commentTime+"\r\n\t"+HTMLAnalyze.getText(HTMLAnalyze.clearTagByA(commentConten)));
//															
//															
//														}
														
														
														
														
														
													}
													
													
												}catch(Exception e3){
													putText("采集["+nodeData.get("res_name")+"]：<<"+target.get(aKey)+">>失败！抓取规则与源码不匹配\n",Color.RED);
													e3.printStackTrace();
													continue;
												}/*finally{
													Thread.sleep(5000);
												}
												*/
												
												
												
												
												//如果点击了暂停按钮：
							            			}else{  
							            				try {
							            					//如果已经输出过“已停止”，那么就不再输出。（因为此线程程序是循环进行的）
							            					if(!stop_status_is_outputed){
							            						putText("已暂停。\n",Color.YELLOW);
							            						stop_status_is_outputed = true;
							            					}
										                	x=x-1;
															Thread.sleep(1000);
														} catch (InterruptedException e) {
															e.printStackTrace();
														}
							            			}
							            			
												
												
												
												
												
												
											}

		            						putText("["+nodeData.get("res_name")+"]采集结束，成功采集"+successfullyCount+"篇，失败"+(aTags.keySet().size()-successfullyCount)+"篇\n",Color.YELLOW);
											
										}
										
									} catch (Exception e1) {
										e1.printStackTrace();
										putText("采集["+nodeData.get("res_name")+"]的链接列表页失败，自动采集下一个站点\n",Color.RED);
										//JOptionPane.showMessageDialog(null,e1.getStackTrace()+":"+e1.getMessage(),"采集时程序发生异常",JOptionPane.ERROR_MESSAGE);
										
										continue;
									}
									
									

									
									//一轮抓取结束，执行JVM内存回收，释放内存中创建的多个已不用的对象
									
									//线程等待2分钟再进入下一轮抓取
									

									//如果是多链接列表页情况，要循环执行抓取模板url，直到所有页码抓完。所有页码抓完的标志是进度pageNumber应等于页码范围右边值，因为不管递增还是递减都是从左值往右值抓取。
									if(isMultiLinksPage && pageNumber != rightPageNumber){ 

										putText("采集["+resUrl+"]的链接结束，开始采集下一页链接。\n",Color.GREEN);
										res_index--;//重复进行
										continue;
									}
									
									

				                //} 
									
									
									
									
								}
								
								
								
								
								
								
								
//								int j=1;
//								for(String key:nodeData.keySet()){
//									System.out.println("= = = = = = = =特征"+j+"：= = = = = = = =");
//									System.out.println(key+"：\r\n"+nodeData.get(key));
//									
//								}
								
								
								
							}
							
							
							
							
							
							
						}
					}
				}
				
				
				
				
				
				
				
				
				
				

				putText("所有任务结束\n",Color.YELLOW);
				
				
				
				
				
				
				
				
				
			}
		            }.start();
				
				
			}
		});
		button.setBounds(231, 10, 95, 25);
		contentPane.add(button);
		
		
		JButton button_1 = new JButton("全部收拢");
		//button_1.setEnabled(false);
		button_1.setFocusPainted(false);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton jbt = (JButton)arg0.getSource();
				int rowCount = treeDataList.size()+firstLevelNodes.size();
				if(jbt.getText().equals("全部收拢")){
					//收拢所有
					
					for(int i=0;i<rowCount;i++){
						//System.out.println("rowCount"+rowCount);
						tree.collapsePath(tree.getPathForRow(i));
					}
					jbt.setText("全部展开");
				}else{
					tree.expandAll();
					for(int i=0;i<rowCount;i++){
						//System.out.println("rowCount"+rowCount);
						tree.expandPath(tree.getPathForRow(i));
					}
					//tree.expandPath(path)
					jbt.setText("全部收拢");
				}
			}
		});
		//button_1.setFont(new Font("宋体", Font.PLAIN, 12));
		button_1.setBounds(10, 10, 106, 25);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("全选");
		button_2.setFocusPainted(false);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton jbt = (JButton)arg0.getSource();
				if(allSelectFlag==1){
					tree.getCheckingModel().clearChecking();
					allSelectFlag=0;
				}else{
					int rowCount = tree.getRowCount();
					TreePath[] tps = new TreePath[rowCount];
					//选中所有
					for(int i=0;i<rowCount;i++){
						tps[i]=tree.getPathForRow(i);
					}
					tree.setCheckingPaths(tps);
					allSelectFlag=1;
				}
			}
		});
		//button_2.setFont(new Font("宋体", Font.PLAIN, 12));
		button_2.setBounds(115, 10, 106, 25);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("暂停");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton jbt = (JButton)arg0.getSource();
				if(jbt.getText().equals("暂停")){
					stop=true;
					putText("采集暂停中……\n",Color.YELLOW);
					jbt.setText("继续");
				}else{
					stop=false;
					putText("采集启动中……\n",Color.YELLOW);
					stop_status_is_outputed = false;
					jbt.setText("暂停");
				}
			}
		});
		//button_3.setFont(new Font("宋体", Font.PLAIN, 12));
		button_3.setBounds(336, 10, 95, 25);
		contentPane.add(button_3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(231, 45, 615, 495);
		contentPane.add(scrollPane_1);
		
        
		
		scrollPane_1.setViewportView(textPane);
		
		
		
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
	
	/***
	 * 得到系统日期
	 * @return
	 */
	public static String getDate(){
	    String dateStr="";
	    Date dt = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    dateStr=sdf.format(dt);
	    return dateStr;
	}
}
