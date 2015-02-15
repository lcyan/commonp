1.修改 mysql 密码
	$ mysqladmin -uroot -p 老密码 password 新密码

	注意：老密码即您 mysql 的 root 用户当前的密码，新密码即您修改后的密码。 -p 跟
	老密码之间没有空格。老密码跟“ password”之间有空格， password 跟新密码之间有
	空格。