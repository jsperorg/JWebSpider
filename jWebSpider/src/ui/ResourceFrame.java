package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalExclusionType;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import spider.utility.Utility;

import java.awt.Component;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Checkbox;
import javax.swing.JCheckBox;

public class ResourceFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txf_resUrl;
	private JTextField txf_resName;
	private List<Map<String, String>> classList;
	public static ResourceFrame frame = null;
	private Properties config = null;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	/**
	 * Create the frame.
	 */
	public ResourceFrame(final Map<String, String> nodeData,Properties cfg) {
		config = cfg;
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if(nodeData!=null){
			setTitle("修改源 - 修改各项务必转义正则表达式特殊意义的字符，如（(、[、{、)、]、}、^、$、.、*、+、|、?、\\），务必在特殊意义符号前加上一个反斜杠表示转义，如\"\\(\"");
		}else{
			setTitle("添加源 - 填写各项务必转义正则表达式特殊意义的字符，如（(、[、{、)、]、}、^、$、.、*、+、|、?、\\），务必在特殊意义符号前加上一个反斜杠表示转义，如\"\\(\"");
		}
		setBounds(0, 31, 1068, 735);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("网址：");
		//label.setFont(new Font("黑体", Font.PLAIN, 12));
		label.setBounds(11, 13, 43, 15);
		contentPane.add(label);
		
		txf_resUrl = new JTextField();
		txf_resUrl.setBounds(49, 12, 212, 21);
		contentPane.add(txf_resUrl);
		txf_resUrl.setColumns(10);
		
		JLabel label_1 = new JLabel("链接页开始特征：");
		//label_1.setFont(new Font("黑体", Font.PLAIN, 12));
		label_1.setBounds(11, 96, 119, 15);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("内容页标题开始特征：");
		//label_2.setFont(new Font("黑体", Font.PLAIN, 12));
		label_2.setBounds(10, 243, 120, 15);
		contentPane.add(label_2);
		
		
		
		
		JButton btn_checkExists = new JButton("验证");
		//btn_checkExists.setFont(new Font("黑体", Font.PLAIN, 12));
		btn_checkExists.setFocusPainted(false);
		btn_checkExists.setBounds(272, 10, 71, 25);
		contentPane.add(btn_checkExists);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(11, 121, 250, 100);
		contentPane.add(scrollPane);
		
		final JTextArea txa_resLinksStart = new JTextArea();
		scrollPane.setViewportView(txa_resLinksStart);
		
		JLabel label_3 = new JLabel("描述名：");
		//label_3.setFont(new Font("黑体", Font.PLAIN, 12));
		label_3.setBounds(657, 16, 56, 15);
		contentPane.add(label_3);
		
		txf_resName = new JTextField();
		txf_resName.setColumns(10);
		txf_resName.setBounds(711, 13, 109, 21);
		contentPane.add(txf_resName);
		
		JLabel label_4 = new JLabel("所属分类：");
		//label_4.setFont(new Font("黑体", Font.PLAIN, 12));
		label_4.setBounds(380, 17, 66, 15);
		contentPane.add(label_4);
		
		final JComboBox cbx_class = new JComboBox();
		//cbx_class.setFont(new Font("黑体", Font.PLAIN, 12));
		cbx_class.setBounds(444, 14, 95, 21);
		try {
			classList = Utility.getServerResultByJSON(config.getProperty("class"));
		} catch (Exception e1) {

			JOptionPane.showMessageDialog(null,e1.getMessage(),"错误",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		/*
		 final List<Map<String, String>> classList = new ArrayList<Map<String, String>>();
		Map<String, String> map1 = new HashMap<String,String>();
		map1.put("class_id", "1");
		map1.put("class_name", "选项1");
		classList.add(map1);
		Map<String, String> map2 = new HashMap<String,String>();
		map2.put("class_id", "2");
		map2.put("class_name", "选项2");
		classList.add(map2);
		Map<String, String> map3 = new HashMap<String,String>();
		map3.put("class_id", "3");
		map3.put("class_name", "选项3");
		classList.add(map3);
		*/
		for(Map<String, String> classMap:classList){
			cbx_class.addItem(classMap.get("class_name"));
			//System.out.println(classMap.get("class_name"));
		}

		//nodeData;
		cbx_class.setSelectedIndex(0);
		contentPane.add(cbx_class);
		
		JButton btn_addClass = new JButton("添加");
		//btn_addClass.setFont(new Font("黑体", Font.PLAIN, 12));
		btn_addClass.setFocusPainted(false);
		btn_addClass.setBounds(542, 12, 66, 25);
		contentPane.add(btn_addClass);
		
		JLabel label_5 = new JLabel("链接页结束特征：");
		//label_5.setFont(new Font("黑体", Font.PLAIN, 12));
		label_5.setBounds(272, 96, 141, 15);
		contentPane.add(label_5);
		
		JLabel label_6 = new JLabel("内容页标题结束特征：");
		//label_6.setFont(new Font("黑体", Font.PLAIN, 12));
		label_6.setBounds(271, 243, 141, 15);
		contentPane.add(label_6);
		
		JLabel label_7 = new JLabel("内容作者结束特征：");
		//label_7.setFont(new Font("黑体", Font.PLAIN, 12));                                 
		label_7.setBounds(272, 390, 141, 15);
		contentPane.add(label_7);
		
		JLabel label_8 = new JLabel("内容作者开始特征：");
		//label_8.setFont(new Font("黑体", Font.PLAIN, 12));
		label_8.setBounds(11, 390, 141, 15);
		contentPane.add(label_8);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 268, 250, 100);
		contentPane.add(scrollPane_1);
		
		final JTextArea txa_resTitleStart = new JTextArea();
		scrollPane_1.setViewportView(txa_resTitleStart);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(11, 415, 250, 100);
		contentPane.add(scrollPane_2);
		
		final JTextArea txa_resAuthorStart = new JTextArea();
		scrollPane_2.setViewportView(txa_resAuthorStart);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 562, 250, 100);
		contentPane.add(scrollPane_3);
		
		final JTextArea txa_resContentStart = new JTextArea();
		scrollPane_3.setViewportView(txa_resContentStart);
		
		JLabel label_9 = new JLabel("内容正文开始特征：");
		//label_9.setFont(new Font("黑体", Font.PLAIN, 12));
		label_9.setBounds(10, 537, 141, 15);
		contentPane.add(label_9);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(272, 121, 250, 100);
		contentPane.add(scrollPane_4);
		
		final JTextArea txa_resLinksEnd = new JTextArea();
		scrollPane_4.setViewportView(txa_resLinksEnd);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(271, 268, 250, 100);
		contentPane.add(scrollPane_5);
		
		final JTextArea txa_resTitleEnd = new JTextArea();
		scrollPane_5.setViewportView(txa_resTitleEnd);
		
		JScrollPane scrollPane_6 = new JScrollPane();
		scrollPane_6.setBounds(272, 415, 250, 100);
		contentPane.add(scrollPane_6);
		
		final JTextArea txa_resAuthorEnd = new JTextArea();
		scrollPane_6.setViewportView(txa_resAuthorEnd);
		
		JScrollPane scrollPane_7 = new JScrollPane();
		scrollPane_7.setBounds(271, 562, 250, 100);
		contentPane.add(scrollPane_7);
		
		final JTextArea txa_resContentEnd = new JTextArea();
		scrollPane_7.setViewportView(txa_resContentEnd);
		
		JLabel label_10 = new JLabel("内容正文结束特征：");
		//label_10.setFont(new Font("黑体", Font.PLAIN, 12));
		label_10.setBounds(271, 537, 141, 15);
		contentPane.add(label_10);
		

		final JScrollPane scrollPane_8 = new JScrollPane();
		scrollPane_8.setBounds(542, 121, 250, 100);
		contentPane.add(scrollPane_8);
		

		JScrollPane scrollPane_10 = new JScrollPane();
		scrollPane_10.setBounds(541, 268, 250, 100);
		contentPane.add(scrollPane_10);
		
		final JTextArea txa_CommentAuthorIdStart = new JTextArea();
		scrollPane_10.setViewportView(txa_CommentAuthorIdStart);
		
		JScrollPane scrollPane_11 = new JScrollPane();
		scrollPane_11.setBounds(802, 268, 250, 100);
		contentPane.add(scrollPane_11);
		
		final JTextArea txa_CommentAuthorIdEnd = new JTextArea();
		scrollPane_11.setViewportView(txa_CommentAuthorIdEnd);
		
		JScrollPane scrollPane_12 = new JScrollPane();
		scrollPane_12.setBounds(542, 415, 250, 100);
		contentPane.add(scrollPane_12);
		
		final JTextArea txa_CommentTimeStart = new JTextArea();
		scrollPane_12.setViewportView(txa_CommentTimeStart);
		
		JScrollPane scrollPane_13 = new JScrollPane();
		scrollPane_13.setBounds(803, 415, 250, 100);
		contentPane.add(scrollPane_13);
		

		final JTextArea txa_CommentContentAreaStart = new JTextArea();
		scrollPane_8.setViewportView(txa_CommentContentAreaStart);
		
		JScrollPane scrollPane_9 = new JScrollPane();
		scrollPane_9.setBounds(803, 121, 250, 100);
		contentPane.add(scrollPane_9);
		
		final JTextArea txa_CommentContentAreaEnd = new JTextArea();
		scrollPane_9.setViewportView(txa_CommentContentAreaEnd);
		

		final JRadioButton rad_commentYes = new JRadioButton("是");
		rad_commentYes.setSelected(true);
		final JRadioButton rad_commentNo = new JRadioButton("否");

		final ButtonGroup commentBg = new ButtonGroup();
		commentBg.add(rad_commentYes);
		commentBg.add(rad_commentNo);
		//cbx_commentYes.setSelected(true);
		//rad_commentYes.setFont(new Font("黑体", Font.PLAIN, 12));
		rad_commentYes.setBounds(915, 13, 55, 23);
		contentPane.add(rad_commentYes);
		
		//rad_commentNo.setFont(new Font("黑体", Font.PLAIN, 12));
		rad_commentNo.setBounds(972, 12, 57, 23);
		contentPane.add(rad_commentNo);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{contentPane}));
		
		
		
		
		
		
		
		
		
		

		JLabel lblNewLabel = new JLabel("图片采集方式：");
		lblNewLabel.setBounds(11, 45, 95, 15);
		contentPane.add(lblNewLabel);
		
		final JRadioButton rad_image_download = new JRadioButton("下载复制");
		rad_image_download.setSelected(true);
		buttonGroup.add(rad_image_download);
		rad_image_download.setBounds(99, 41, 80, 23);
		contentPane.add(rad_image_download);
		
		JRadioButton rad_image_reference = new JRadioButton("外链引用");
		buttonGroup.add(rad_image_reference);
		rad_image_reference.setBounds(181, 41, 80, 23);
		contentPane.add(rad_image_reference);
		
		JLabel label_13 = new JLabel("翻译：");
		label_13.setBounds(272, 45, 54, 15);
		contentPane.add(label_13);
		
		final JComboBox cbx_translate = new JComboBox();

		cbx_translate.addItem("无");
		cbx_translate.addItem("youdao");
		cbx_translate.addItem("google");
		cbx_translate.addItem("bing");
		cbx_translate.addItem("baidu");
		cbx_translate.setSelectedIndex(0);
		
		cbx_translate.setBounds(316, 41, 71, 23);
		contentPane.add(cbx_translate);

		
		
		
		
		
		
		
		
		
		
		
		JLabel lblid = new JLabel("评论者ID开始特征：");
		//lblid.setFont(new Font("黑体", Font.PLAIN, 12));
		lblid.setBounds(541, 243, 119, 15);
		contentPane.add(lblid);
		
		JLabel lblid_1 = new JLabel("评论者ID结束特征：");
		//lblid_1.setFont(new Font("黑体", Font.PLAIN, 12));
		lblid_1.setBounds(802, 243, 141, 15);
		contentPane.add(lblid_1);
		
		JLabel label_15 = new JLabel("评论时间结束特征：");
		//label_15.setFont(new Font("黑体", Font.PLAIN, 12));
		label_15.setBounds(803, 390, 141, 15);
		contentPane.add(label_15);
		
		JLabel label_16 = new JLabel("评论时间开始特征：");
		//label_16.setFont(new Font("黑体", Font.PLAIN, 12));
		label_16.setBounds(542, 390, 141, 15);
		contentPane.add(label_16);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(542, 231, 511, 2);
		contentPane.add(separator_3);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(542, 378, 511, 2);
		contentPane.add(separator_4);
		
		final JTextArea txa_CommentTimeEnd = new JTextArea();
		scrollPane_13.setViewportView(txa_CommentTimeEnd);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(542, 525, 511, 2);
		contentPane.add(separator_5);
		
		JLabel label_17 = new JLabel("评论内容开始特征：");
		//label_17.setFont(new Font("黑体", Font.PLAIN, 12));
		label_17.setBounds(541, 537, 141, 15);
		contentPane.add(label_17);
		
		JLabel label_18 = new JLabel("评论内容结束特征：");
		//label_18.setFont(new Font("黑体", Font.PLAIN, 12));
		label_18.setBounds(802, 537, 141, 15);
		contentPane.add(label_18);
		
		JScrollPane scrollPane_14 = new JScrollPane();
		scrollPane_14.setBounds(541, 562, 250, 100);
		contentPane.add(scrollPane_14);
		
		final JTextArea txa_CommentContentStart = new JTextArea();
		scrollPane_14.setViewportView(txa_CommentContentStart);
		
		JScrollPane scrollPane_15 = new JScrollPane();
		scrollPane_15.setBounds(802, 562, 250, 100);
		contentPane.add(scrollPane_15);
		
		final JTextArea txa_CommentContentEnd = new JTextArea();
		scrollPane_15.setViewportView(txa_CommentContentEnd);
		
		
		
		
		
		
		
		
		
		
		JButton btn_save = new JButton("保存");
		//btn_save.setFont(new Font("黑体", Font.PLAIN, 12));
		btn_save.setFocusPainted(false);
		//添加按钮被点击后做添加源业务：
		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				//定义一个map存放参数名和值，最终这个map将发送给web将数据保存到数据库
				Map<String,String> parMap = new HashMap<String,String>();
				
				
				//获得各输入项的
				String resUrl = txf_resUrl.getText();
				String className = cbx_class.getSelectedItem().toString();
				String classId=null;
				//循环classList，判断里面的Map的value是否equals被选中的JCombox的item.toString。如果是则这个Map的get("id")获得该项的id。
				for(Map<String,String> map:classList){
					if(map.get("class_name").equals(className)){
						classId=map.get("class_id");
						break;
					}
				}
				
				String resTranslate = cbx_translate.getSelectedItem().toString();
				String resImageMethod = "download";
				if(buttonGroup.getSelection()==rad_image_download.getModel()){
					resImageMethod="download";//下载复制
				}else{
					resImageMethod="reference";//外链引用
				}
				
				String resName = txf_resName.getText();
				String resLinksStart = txa_resLinksStart.getText();
				String resLinksEnd =txa_resLinksEnd.getText();
				String resTitleStart = txa_resTitleStart.getText();
				String resTitleEnd = txa_resTitleEnd.getText();
				String resAuthorStart = txa_resAuthorStart.getText();
				String resAuthorEnd = txa_resAuthorEnd.getText();
				String resContentStart = txa_resContentStart.getText();
				String resContentEnd = txa_resContentEnd.getText();
				//如果客户端参数验证通过，就提交到服务器保存：
				if(classId!=null && !resUrl.equals("") && !resName.equals("") && !resLinksStart.equals("") && !resLinksEnd.equals("") && !resTitleStart.equals("") && !resTitleEnd.equals("") && !resAuthorStart.equals("") && !resAuthorEnd.equals("") && !resContentStart.equals("") && !resContentEnd.equals("")){
					
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
					
					

					parMap.put("resTranslate", resTranslate);
					parMap.put("resImageMethod", resImageMethod);
					
				}else{
					JOptionPane.showMessageDialog(null,"有输入项不符合保存要求，请检查。","消息",JOptionPane.ERROR_MESSAGE);
					return;
				}
				

				//如果”抓取评论“的”是“单选是被选中的：
				if(commentBg.getSelection() == rad_commentYes.getModel()){
					String commentContentAreaStarts = txa_CommentContentAreaStart.getText();
					String commentContentAreaEnd = txa_CommentContentAreaEnd.getText();
					String commentAuthorIdStart = txa_CommentAuthorIdStart.getText();
					String commentAuthorIdEnd = txa_CommentAuthorIdEnd.getText();
					String commentTimeStart = txa_CommentTimeStart.getText();
					String commentTimeEnd = txa_CommentTimeEnd.getText();
					String commentContentStart = txa_CommentContentStart.getText();
					String commentContentEnd = txa_CommentContentEnd.getText();
					if(!commentContentAreaStarts.equals("") && !commentContentAreaEnd.equals("") && !commentAuthorIdStart.equals("") && !commentAuthorIdEnd.equals("") && !commentTimeStart.equals("") && !commentTimeEnd.equals("") && !commentContentStart.equals("") && !commentContentEnd.equals("")){
						
						parMap.put("getComment", "true");
						parMap.put("commentContentAreaStarts", commentContentAreaStarts);
						parMap.put("commentContentAreaEnd", commentContentAreaEnd);
						parMap.put("commentAuthorIdStart", commentAuthorIdStart);
						parMap.put("commentAuthorIdEnd", commentAuthorIdEnd);
						parMap.put("commentTimeStart", commentTimeStart);
						parMap.put("commentTimeEnd", commentTimeEnd);
						parMap.put("commentContentStart", commentContentStart);
						parMap.put("commentContentEnd", commentContentEnd);
						
					}else{
						JOptionPane.showMessageDialog(null,"输入的抓取评论的各项不符合保存要求，请检查。","消息",JOptionPane.ERROR_MESSAGE);
						return;
					}
				}else{
					//如果不抓取评论，则设置getComment参数值为false,提供给服务端对此值的判断做出相应的业务逻辑。
					parMap.put("getComment", "false");
				}
				//System.out.println(parMap);
				String resultSet = null;
				if(nodeData!=null){
					resultSet = Utility.serverPostRequest(config.getProperty("update_resource"), parMap);
				}else{
					resultSet = Utility.serverPostRequest(config.getProperty("add_resource"), parMap);
				}
				System.out.println(resultSet);
				if(Integer.parseInt(resultSet)>0){
					//成功提示
					JOptionPane.showMessageDialog(null,"已保存到服务器！","消息",JOptionPane.INFORMATION_MESSAGE);
					MainFrame.initialize();//刷新主窗体左边的树
				}else{
					//失败提示
					JOptionPane.showMessageDialog(null,"保存到服务器失败！","消息",JOptionPane.ERROR_MESSAGE);
				}
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null,"保存失败，错误信息："+ex.getMessage(),"消息",JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		
		
		
		
		btn_save.setBounds(316, 675, 95, 25);
		contentPane.add(btn_save);
		
		
		
		
		
		//点击取消按钮：
		JButton btn_cancel = new JButton("取消");
		btn_cancel.setFocusPainted(false);
		//btn_cancel.setFont(new Font("黑体", Font.PLAIN, 12));
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.setVisible(false);
				frame.dispose();
			}
		});
		btn_cancel.setBounds(421, 675, 95, 25);
		contentPane.add(btn_cancel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(11, 231, 511, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(11, 378, 511, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();                          
		separator_2.setBounds(11, 525, 511, 2);
		contentPane.add(separator_2);
		
		JLabel label_11 = new JLabel("评论区域结束特征：");
		//label_11.setFont(new Font("黑体", Font.PLAIN, 12));
		label_11.setBounds(803, 96, 141, 15);
		contentPane.add(label_11);
		                                                  
		JLabel label_12 = new JLabel("评论区域开始特征：");
		//label_12.setFont(new Font("黑体", Font.PLAIN, 12));
		label_12.setBounds(542, 96, 119, 15);
		contentPane.add(label_12);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setOrientation(SwingConstants.VERTICAL);
		separator_6.setBounds(530, 121, 2, 541);
		contentPane.add(separator_6);
		
		JLabel label_19 = new JLabel("抓取评论：");
		//label_19.setFont(new Font("黑体", Font.PLAIN, 12));
		label_19.setBounds(856, 16, 66, 15);
		contentPane.add(label_19);
		
		rad_commentYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color rgb = txa_resLinksStart.getBackground();

				
				txa_CommentContentAreaStart.setBackground(rgb);
				txa_CommentContentAreaEnd.setBackground(rgb);
				txa_CommentAuthorIdStart.setBackground(rgb);
				txa_CommentAuthorIdEnd.setBackground(rgb);
				txa_CommentTimeStart.setBackground(rgb);
				txa_CommentTimeEnd.setBackground(rgb);
				txa_CommentContentStart.setBackground(rgb);
				txa_CommentContentEnd.setBackground(rgb);
				
				txa_CommentContentAreaStart.setEnabled(true);
				txa_CommentContentAreaEnd.setEnabled(true);
				txa_CommentAuthorIdStart.setEnabled(true);
				txa_CommentAuthorIdEnd.setEnabled(true);
				txa_CommentTimeStart.setEnabled(true);
				txa_CommentTimeEnd.setEnabled(true);
				txa_CommentContentStart.setEnabled(true);
				txa_CommentContentEnd.setEnabled(true);
			}
		});
		rad_commentNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//设置抓取评论的表单不可用...
				//scrollPane_8.setEnabled(false);
				txa_CommentContentAreaStart.setBackground(new Color(50,50,50));
				txa_CommentContentAreaEnd.setBackground(new Color(50,50,50));
				txa_CommentAuthorIdStart.setBackground(new Color(50,50,50));
				txa_CommentAuthorIdEnd.setBackground(new Color(50,50,50));
				txa_CommentTimeStart.setBackground(new Color(50,50,50));
				txa_CommentTimeEnd.setBackground(new Color(50,50,50));
				txa_CommentContentStart.setBackground(new Color(50,50,50));
				txa_CommentContentEnd.setBackground(new Color(50,50,50));

				txa_CommentContentAreaStart.setEnabled(false);
				txa_CommentContentAreaEnd.setEnabled(false);
				txa_CommentAuthorIdStart.setEnabled(false);
				txa_CommentAuthorIdEnd.setEnabled(false);
				txa_CommentTimeStart.setEnabled(false);
				txa_CommentTimeEnd.setEnabled(false);
				txa_CommentContentStart.setEnabled(false);
				txa_CommentContentEnd.setEnabled(false);
			}
		});
		
		//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		if(nodeData!=null){
			

			//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbb");

			String className1 = nodeData.get("class_name");
			for(int i=0;i<cbx_class.getItemCount();i++){
				String className2 = cbx_class.getItemAt(i).toString();

				//System.out.println("for item "+i+" "+className1+":"+className2 );
				if(className2.equals(className1)){
					cbx_class.setSelectedIndex(i);
				}
			}
			
			String resImageMethod = nodeData.get("res_image_method");
			if(resImageMethod.equals("download")){
				rad_image_download.setSelected(true);
				rad_image_reference.setSelected(false);
			}else{
				rad_image_download.setSelected(false);
				rad_image_reference.setSelected(true);
			}

			String resTranslate = nodeData.get("res_translate");
			for(int i=0;i<cbx_translate.getItemCount();i++){
				//System.out.println("for item "+i+" "+className1+":"+className2 );
				if(cbx_translate.getItemAt(i).toString().equals(resTranslate)){
					cbx_translate.setSelectedIndex(i);
				}
			}
			
			
			
			
			
			txf_resUrl.setText(nodeData.get("res_url"));
			txf_resName.setText(nodeData.get("res_name"));
			txa_resLinksStart.setText(nodeData.get("res_links_start"));
			txa_resLinksEnd.setText(nodeData.get("res_links_end"));
			txa_resTitleStart.setText(nodeData.get("res_title_start"));
			txa_resTitleEnd.setText(nodeData.get("res_title_end"));
			txa_resAuthorStart.setText(nodeData.get("res_author_start"));
			txa_resAuthorEnd.setText(nodeData.get("res_author_end"));
			txa_resContentStart.setText(nodeData.get("res_content_start"));
			txa_resContentEnd.setText(nodeData.get("res_content_end"));
			
			if(nodeData.get("res_getComment").equals("true")){
				rad_commentNo.setSelected(false);
				rad_commentYes.setSelected(true);
				//将抓取评论的规则赋值到各文本控件。

				txa_CommentContentAreaStart.setText(nodeData.get("res_comment_content_area_start"));
				txa_CommentContentAreaEnd.setText(nodeData.get("res_comment_content_area_end"));
				txa_CommentAuthorIdStart.setText(nodeData.get("res_comment_authorid_start"));
				txa_CommentAuthorIdEnd.setText(nodeData.get("res_comment_authorid_end"));
				txa_CommentTimeStart.setText(nodeData.get("res_comment_time_start"));
				txa_CommentTimeEnd.setText(nodeData.get("res_comment_time_end"));
				txa_CommentContentStart.setText(nodeData.get("res_comment_content_start"));
				txa_CommentContentEnd.setText(nodeData.get("res_comment_content_end"));
				
			}else{
				rad_commentYes.setSelected(false);
				rad_commentNo.setSelected(true);
				//将抓取评论的规则赋值到各文本控件。

				txa_CommentContentAreaStart.setText(nodeData.get("res_comment_content_area_start"));
				txa_CommentContentAreaEnd.setText(nodeData.get("res_comment_content_area_end"));
				txa_CommentAuthorIdStart.setText(nodeData.get("res_comment_authorid_start"));
				txa_CommentAuthorIdEnd.setText(nodeData.get("res_comment_authorid_end"));
				txa_CommentTimeStart.setText(nodeData.get("res_comment_time_start"));
				txa_CommentTimeEnd.setText(nodeData.get("res_comment_time_end"));
				txa_CommentContentStart.setText(nodeData.get("res_comment_content_start"));
				txa_CommentContentEnd.setText(nodeData.get("res_comment_content_end"));
				
				
				txa_CommentContentAreaStart.setBackground(new Color(50,50,50));
				txa_CommentContentAreaEnd.setBackground(new Color(50,50,50));
				txa_CommentAuthorIdStart.setBackground(new Color(50,50,50));
				txa_CommentAuthorIdEnd.setBackground(new Color(50,50,50));
				txa_CommentTimeStart.setBackground(new Color(50,50,50));
				txa_CommentTimeEnd.setBackground(new Color(50,50,50));
				txa_CommentContentStart.setBackground(new Color(50,50,50));
				txa_CommentContentEnd.setBackground(new Color(50,50,50));

				txa_CommentContentAreaStart.setEnabled(false);
				txa_CommentContentAreaEnd.setEnabled(false);
				txa_CommentAuthorIdStart.setEnabled(false);
				txa_CommentAuthorIdEnd.setEnabled(false);
				txa_CommentTimeStart.setEnabled(false);
				txa_CommentTimeEnd.setEnabled(false);
				txa_CommentContentStart.setEnabled(false);
				txa_CommentContentEnd.setEnabled(false);
			}
		}
		
	}
}
