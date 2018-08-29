package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConfigFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txf_domain_name;
	private JTextField txf_site_name;
	private JTextField txf_resource;
	private JTextField txf_add_resource;
	private JTextField txf_update_resource;
	private JTextField txf_submit;
	private MainFrame mf = null;
	private JTextField txf_delete_resource;
	private JTextField txf_content_is_exists;
	private JTextField txf_class;
	private JTextField txf_file_upload;
	private JTextField txf_get_links_page_progress;
	private JTextField txf_update_links_page_progress;
	/**
	 * Create the frame.
	 */
	public ConfigFrame(Properties config,MainFrame manfrm) {
		mf = manfrm;
		setTitle("配置");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 416);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("网站网址：");
		label.setBounds(10, 10, 60, 15);
		contentPane.add(label);
		
		txf_domain_name = new JTextField();
		txf_domain_name.setBounds(94, 7, 308, 21);
		contentPane.add(txf_domain_name);
		txf_domain_name.setColumns(10);
		
		JLabel label_1 = new JLabel("网站名称：");
		label_1.setBounds(10, 35, 60, 15);
		contentPane.add(label_1);
		
		txf_site_name = new JTextField();
		txf_site_name.setBounds(94, 32, 308, 21);
		contentPane.add(txf_site_name);
		txf_site_name.setColumns(10);
		
		JLabel label_2 = new JLabel("采集源获取地址：");
		label_2.setBounds(10, 60, 115, 15);
		contentPane.add(label_2);
		
		txf_resource = new JTextField();
		txf_resource.setBounds(135, 57, 267, 21);
		contentPane.add(txf_resource);
		txf_resource.setColumns(10);
		
		JLabel label_3 = new JLabel("添加采集源提交地址：");
		label_3.setBounds(10, 85, 120, 15);
		contentPane.add(label_3);
		
		txf_add_resource = new JTextField();
		txf_add_resource.setBounds(135, 82, 267, 21);
		contentPane.add(txf_add_resource);
		txf_add_resource.setColumns(10);
		
		JLabel label_4 = new JLabel("修改采集源提交地址：");
		label_4.setBounds(10, 110, 120, 15);
		contentPane.add(label_4);
		
		txf_update_resource = new JTextField();
		txf_update_resource.setBounds(135, 107, 267, 21);
		contentPane.add(txf_update_resource);
		txf_update_resource.setColumns(10);
		

		JLabel label_5 = new JLabel("采集结果提交地址：");
		label_5.setBounds(10, 183, 115, 15);
		contentPane.add(label_5);
		
		txf_submit = new JTextField();
		txf_submit.setBounds(135, 180, 267, 21);
		contentPane.add(txf_submit);
		txf_submit.setColumns(10);
		
		
		txf_delete_resource = new JTextField();
		txf_delete_resource.setColumns(10);
		txf_delete_resource.setBounds(135, 132, 267, 21);
		contentPane.add(txf_delete_resource);
		
		JLabel label_6 = new JLabel("删除采集源提交地址：");
		label_6.setBounds(10, 135, 120, 15);
		contentPane.add(label_6);
		
		txf_content_is_exists = new JTextField();
		txf_content_is_exists.setColumns(10);
		txf_content_is_exists.setBounds(135, 156, 267, 21);
		contentPane.add(txf_content_is_exists);
		
		JLabel label_7 = new JLabel("内容重复判断地址：");
		label_7.setBounds(10, 159, 115, 15);
		contentPane.add(label_7);
		
		txf_class = new JTextField();
		txf_class.setColumns(10);
		txf_class.setBounds(135, 205, 267, 21);
		contentPane.add(txf_class);
		
		JLabel label_8 = new JLabel("分类获取地址：");
		label_8.setBounds(10, 208, 115, 15);
		contentPane.add(label_8);
		

		
		txf_file_upload = new JTextField();
		txf_file_upload.setColumns(10);
		txf_file_upload.setBounds(135, 228, 267, 21);
		contentPane.add(txf_file_upload);
		
		JLabel label_9 = new JLabel("图片/文件上传地址：");
		label_9.setBounds(10, 231, 115, 15);
		contentPane.add(label_9);
		

		
		JButton button = new JButton("保存");

		button.setBounds(307, 343, 95, 25);
		contentPane.add(button);
		
		JLabel label_10 = new JLabel("采集进度查询接口：");
		label_10.setBounds(10, 258, 115, 15);
		contentPane.add(label_10);
		
		txf_get_links_page_progress = new JTextField();
		txf_get_links_page_progress.setColumns(10);
		txf_get_links_page_progress.setBounds(135, 255, 267, 21);
		contentPane.add(txf_get_links_page_progress);
		
		JLabel label_11 = new JLabel("采集进度更新接口：");
		label_11.setBounds(10, 281, 115, 15);
		contentPane.add(label_11);
		
		txf_update_links_page_progress = new JTextField();
		txf_update_links_page_progress.setColumns(10);
		txf_update_links_page_progress.setBounds(135, 278, 267, 21);
		contentPane.add(txf_update_links_page_progress);
		
		
		
		//赋值各项配置数据，由于打开主界面时会自动装在入配置对象，此处直接判断
		if(config!=null){
			txf_domain_name.setText(config.getProperty("domain_name"));
			txf_site_name.setText(config.getProperty("site_name"));
			txf_resource.setText(config.getProperty("resource"));
			txf_add_resource.setText(config.getProperty("add_resource"));
			txf_update_resource.setText(config.getProperty("update_resource"));
			txf_delete_resource.setText(config.getProperty("delete_resource"));
			txf_content_is_exists.setText(config.getProperty("content_is_exists"));
			txf_submit.setText(config.getProperty("submit"));
			txf_class.setText(config.getProperty("class"));
			txf_file_upload.setText(config.getProperty("file_upload"));
			
			txf_get_links_page_progress.setText(config.getProperty("get_links_page_progress"));
			txf_update_links_page_progress.setText(config.getProperty("update_links_page_progress"));
		}
		
		
		
		
		
		
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//保存配置到config.properties文件
				try {
					
					String domain_name = txf_domain_name.getText();
					String site_name = txf_site_name.getText();
					String resource = txf_resource.getText();
					String add_resource = txf_add_resource.getText();
					String update_resource = txf_update_resource.getText();
					String delete_resource = txf_delete_resource.getText();
					String content_is_exists = txf_content_is_exists.getText();
					String submit = txf_submit.getText();
					String clas = txf_class.getText();
					String file_upload = txf_file_upload.getText();

					String get_links_page_progress = txf_get_links_page_progress.getText();
					String update_links_page_progress = txf_update_links_page_progress.getText();
					
					

					Properties config=new Properties();
					FileOutputStream fos = new FileOutputStream("config.properties");
					config.setProperty("domain_name", domain_name);
					config.setProperty("site_name", site_name);
					config.setProperty("resource", resource);
					config.setProperty("add_resource", add_resource);
					config.setProperty("update_resource", update_resource);
					config.setProperty("delete_resource", delete_resource);
					config.setProperty("content_is_exists", content_is_exists);
					config.setProperty("submit", submit);
					config.setProperty("class", clas);
					config.setProperty("file_upload", file_upload);

					config.setProperty("get_links_page_progress", get_links_page_progress);
					config.setProperty("update_links_page_progress", update_links_page_progress);
					
					config.store(fos, "the picker configuration");
					
					JOptionPane.showMessageDialog(null,"保存后请重新运行本程序。","提示",JOptionPane.INFORMATION_MESSAGE);

					//退出并重新启动程序
					System.exit(0);
					/*以下代码无效
					//退出并重新启动程序
					dispose();
					mf.dispose();
					Thread.sleep(6000);//休眠三秒中，以确保重新打开MainFrame后能读取完properties文件
					mf = new MainFrame();
					mf.setVisible(true);
					*/
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
		});

	}
}
