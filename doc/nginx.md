server {
	listen 80 default;  #默认监听 80 端口
	server_name _;  #默认 ip 访问
	index index.html index.htm index.jsp;  #定义默认首页的名称
	root /alidata/www/default;  #定义 nginx 的默认站点根目录位置
	location ~ \.jsp$ { #截取所有 jsp 的请求
		proxy_pass http://127.0.0.1:8080;  #反向代理给后端 tomcat
	}
	location ~ .*\.(gif|jpg|jpeg|png|bmp|swf)${
		expires 30d;
	}
	location ~ .*\.(js|css)?${
		expires 1h;
	}
	access_log /alidata/log/nginx/access/default.log;
}