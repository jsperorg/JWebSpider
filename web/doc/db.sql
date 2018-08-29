drop database if exists `shehui9`;
create database `shehui9` character set 'utf8';
use `shehui9`;

#职位表
drop table if exists `sys_major`;
create table sys_major
(
	`major_id` int auto_increment primary key,
	`major_name` varchar(20) not null
);
insert into sys_major values(null,'CEO');
insert into sys_major values(null,'CTO');
insert into sys_major values(null,'网站编辑');

select * from sys_major;


#管理员表
drop table if exists `sys_admin_group`;
create table `sys_admin_group`(
	`admin_id` int auto_increment,	#主键ID
	`admin_account` varchar(1024) not null,	#用户名
	`admin_password` varchar(1024) not null,	#用户密码
	`major_id` int,	#所属职位
	`admin_state` int default -1,	#状态（-1.锁定 0.正常）
	primary key (`admin_id`),
	foreign key (major_id) references sys_major(major_id)
);

insert into sys_admin_group values(null,'admin','123456',1,0);
insert into sys_admin_group values(null,'Guest','123456',1,0);
#--insert into admin_group values(null,'admin','|J�	�7b�a� �=�d��',1,0);
select * from `sys_admin_group`;

update sys_admin_group set major_id = 1 where admin_account = 'Guest';

#菜单表
drop table if exists `sys_menu`;
create table sys_menu
(
	`menu_id` int auto_increment primary key,				#菜单ID
	`menu_title` varchar(50) not null,					#菜单显示标题
	`menu_superior` int default -1  not null,				#父菜单ID（顶级菜单为-1）
	`menu_url` varchar(500) not null						#指定URL
);

insert into sys_menu values(null,'采集源管理',-1,'biz_js/res_management.js');
	insert into sys_menu values(null,'添加采集源',1,'biz_js/add_res.js');
	insert into sys_menu values(null,'采集源列表',1,'biz_js/res_list.js');
	
insert into sys_menu values(null,'文章管理',-1,'biz_js/article_management.js');
	insert into sys_menu values(null,'发布文章',4,'biz_js/add_article.js');
	insert into sys_menu values(null,'文章列表',4,'biz_js/article_list.js');
	
insert into sys_menu values(null,'网站相关',-1,'biz_js/site_about.js');
	insert into sys_menu values(null,'基本设置',7,'biz_js/basic_set.js');
	insert into sys_menu values(null,'流量统计',7,'biz_js/tip.js');
	
insert into sys_menu values(null,'其他管理',-1,'biz_js/tip.js');
	insert into sys_menu values(null,'其他管理一',10,'biz_js/tip.js');
	insert into sys_menu values(null,'其他管理二',10,'biz_js/tip.js');
	insert into sys_menu values(null,'其他管理三',10,'biz_js/tip.js');
	
select * from sys_menu;

update sys_menu set menu_title='其他管理一' where menu_id=11;
update sys_menu set menu_title='其他管理二' where menu_id=12;



#权限表（权限表表达了职位与菜单的从属关系，一个职位对于各自的权限菜单）
drop table if exists `sys_purview`;
create table sys_purview
(
	`purview_id` int auto_increment primary key,
	`major_id` int,  #所属职位
	`menu_id` int,  #所属职位
	foreign key (major_id) references sys_major(major_id),
	foreign key (menu_id) references sys_menu(menu_id)
);

insert into sys_purview values(null,1,1);
insert into sys_purview values(null,1,2);
insert into sys_purview values(null,1,3);
insert into sys_purview values(null,1,4);
insert into sys_purview values(null,1,5);
insert into sys_purview values(null,1,6);
insert into sys_purview values(null,1,7);
insert into sys_purview values(null,1,8);
insert into sys_purview values(null,1,9);
insert into sys_purview values(null,1,10);
insert into sys_purview values(null,1,11);
insert into sys_purview values(null,1,12);
insert into sys_purview values(null,1,13);

select * from sys_purview;

#查询职位ID为1（CEO）的所有菜单：
select * from sys_menu where menu_id in (select menu_id from sys_purview where major_id = 1);

#查询管理员admin_account为"admin"，admin_password为"123456"的所有菜单：
select * from sys_menu join sys_purview on sys_menu.menu_id = sys_purview.menu_id join sys_admin_group on sys_purview.major_id = sys_admin_group.major_id where sys_admin_group.admin_account = 'admin' and sys_admin_group.admin_password = '123456';

#查询父节点ID标识为5且职位ID标识为1的所有节点（此SQL主要用于控制端递归获取某职位下的所有菜单及子菜单）
select menu_id from sys_menu where menu_id in(select menu_id from sys_purview where major_id = 1) and menu_superior=-1;

select menu_id from sys_menu where menu_superior=-1;



#分类表（比如：新闻、资讯、论坛、图文、糗事、人气帖，主要用于构建客户端采集器主窗体左边的树菜单和web索引页的选项卡）
drop table if exists `class`;
create table class
(
	`class_id` int auto_increment primary key,
	`class_name` varchar(1024) not null#类别名称
);

insert into class values(null,'新闻');
insert into class values(null,'社区');
insert into class values(null,'趣闻');

select * from class;
delete from class where class_id=6;
update class set class_name='社区' where class_id=2;


#目标资源表（采集目标，通常是某个网站的栏目，此表主要用于构建客户端采集器主窗体左边的树菜单和web索引页的选项卡，比如：网易论坛、天涯情感频道、优酷搞笑视频）
drop table if exists `res`;
create table res
(
	`res_id` int auto_increment primary key,			#主键ID
	`res_url` varchar(128) not null,					#目标url
	`class_id` int not null,							#所属分类ID
	`res_name` varchar(64) not null,					#描述名
	`res_links_start` varchar(512) not null,			#链接页开始特征
	`res_links_end` varchar(512) not null,				#链接页结束特征
	`res_title_start` varchar(512) not null,			#内容标题开始特征
	`res_title_end` varchar(512) not null,				#内容标题结束特征
	`res_author_start` varchar(512) not null,			#内容作者开始特征
	`res_author_end` varchar(512) not null,				#内容作者结束特征
	`res_content_start` varchar(512) not null,			#内容正文开始特征
	`res_content_end` varchar(512) not null,			#内容正文结束特征
	`res_getComment` varchar(512) not null,			#是否抓取评论
	`res_comment_content_area_start` varchar(512),			#评论区内容开始特征
	`res_comment_content_area_end` varchar(512),			#评论区类容结束特征
	`res_comment_authorid_start` varchar(512),			#评论者ID开始特征
	`res_comment_authorid_end` varchar(512),			#评论者ID结束特征
	`res_comment_time_start` varchar(512),			#评论时间开始特征
	`res_comment_time_end` varchar(512),			#评论时间结束特征
	`res_comment_content_start` varchar(512),			#评论内容开始特征
	`res_comment_content_end` varchar(512),			#评论内容结束特征
	`res_image_method` varchar(512) default 'download',			#图片处理方法（download=下载复制，reference=外链引用）
	`res_translate` varchar(512) default '无',				#翻译 （否）
	foreign key (class_id) references class(class_id)
);

delete from res; where res_id = 12;

insert into res values(null,'http://news.baidu.com',2,'百度新闻','<links-start>','</links-end>','<title>','</title>','<author>','</author>','<content>','</content>','true','<comment-content-area-start>','<comment-content-area-end>','111','222','333','444','555','666','download','无');

insert into res values(null,'http://news.163.com',2,'网易新闻','<links-start>','</links-end>','<title>','</title>','<author>','</author>','<content>','</content>','true','<comment-content-area-start>','<comment-content-area-end>','111','222','333','444','555','666','download','无');

select * from res;




#文章（帖子）存储表‘；g///////////6
drop table if exists `article`;
create table article
(
	`article_id` int auto_increment primary key,			#主键ID
	`res_id` int not null,			#文章分类ID
	`article_title` varchar(64),			#文章标题
	`article_author` varchar(32),			#文章作者
	`article_publish_time` varchar(32),			#发表时间
	`article_from_url` varchar(512),			#来自地址
	`article_content` mediumtext,			#文章内容
	foreign key (res_id) references res(res_id)
);

#insert into article values(null,1,'文章标题','作者','发布时间','原文地址','文章内容');
#insert into article values(null,1,'2文章标题','2作者','2发布时间','2原文地址','2文章内容');


delete from article;
select * from article where article_title='C罗即将对阵捷克――暴走大预测';

select article_id,article_title,article_author,article_publish_time,res_name from Article join res on article.res_id=res.res_id order by article_id desc limit 0,50;
select count(article_id) as counts from article;



#文章回复表
drop table if exists `article_comment`;
create table article_comment
(
	`comment_id` int auto_increment primary key,			#主键ID
	`article_id` int not null,			#文章ID
	`comment_author` varchar(32),			#评论者ID
	`comment_publish_time` varchar(32),			#发表时间
	`comment_content` text,			#评论内容
	foreign key (article_id) references article(article_id)
);

select * from article_comment;

select * from class;


select res_id,res_name from res where class_id=2;

#查询所有类型文章：
select article_title,article_publish_time,res_name,class_name from article join res on article.res_id = res.res_id join class on res.class_id=class.class_id;

#根据条件分页查询文章：
select article_id,article_title,article_author,article_publish_time,res_name from Article join res on article.res_id=res.res_id where article_title like '%我%' or article_author like '%风花雪月%' order by article_id desc limit 0,20;

#根据采集源名称查询
select article_title,article_publish_time,res_name,class_name from article join res on article.res_id = res.res_id join class on res.class_id=class.class_id where res_name='华生时评';

#根据分类查询
select article_title,article_publish_time,res_name,class_name from article join res on article.res_id = res.res_id join class on res.class_id=class.class_id where class_name='论坛';

select article_title,article_publish_time,res_name,class_name,res.class_id from article join res on article.res_id = res.res_id join class on res.class_id=class.class_id where class.class_id=2;

select res_id,res_name from res where (select count(article_id) from article where res_id in (select res_id from res where class_id=2)>0) and class_id=2;

select * from article where res_id = 7;
select article_id from article where res_id = 7;
select * from class;
select res_id,res_name from res where class_id=2;

select article_title,article_publish_time from article where res_id=9;

UPDATE article SET article_publish_time=REPLACE(article_publish_time, '112年5月3日', '2012-06-21 ');



select article_id,article_title,article_author,article_publish_time,article_content,res.res_id,res.res_name from article join res on article.res_id= res.res_id where article_id=168;





create table test(
	test_id int auto_increment primary key,
	test_name varchar(50) not null,
	test_content varchar(500) not null
);

delete from test;

insert into test values(null,'啊啊啊啊啊','叭叭叭巴巴');
insert into test values(null,'ddgg','dsfhrefefwecxvbxczfghtnbsdvb');
insert into test values(null,'阿卡卡卡卡将长vorjgn','无哈无哈殇帝刘玢进欧冠vn');
insert into test values(null,'龙卷风工具无人敢个发链接工具vjdgo ','vo扔垃圾等vob教哦山东房间哦就别弄诶哦将深v两厢车距哦了个 ');
insert into test values(null,'vjoe鬼灵精风红v红v 皇帝神圣','如果防大红花他如何的虚构');
insert into test values(null,'哇哇哇哇啊哈哈据偶家 vjo','fuck有龙jojo啊好 ');
insert into test values(null,'sdfg东风公司','蝴蝶飞过 放到');
insert into test values(null,'vrh个小舒服 给我 更人工湖喝个是在 ','给我口交擦擦擦擦擦');
insert into test values(null,'herg vd 发给vrg3f ','狂金花股份的风光好金花股份的风光好');
insert into test values(null,'破iu影天热','儿童语');

delete from test where test_id in(-999999,8);

select * from test;


drop table if exists `progress`;
create table progress(
	progress_id int auto_increment primary key,
	res_url varchar(128) not null,					#资源url
	page_number int default -1 not null,
	link_index int default -1 not null
);

select * from progress;
select * from res;
select * from article;


delete from article;

show variables like '%char%';