@@Git提供了分开的协议支持
	--SSH,GIT,HTTP,HTTPS,FTP,FTPS,RSYNC以及前面已经看到的本地协议.

	<1> :SSH协议(1):	--> ssh://[user@]example.com[port]/path/to/repo.git/ //可在URL中设置用户名和端口,默认端口22.

	<2>: SSH协议(2):	-->	[user@]example.com:path/to/repo.git/ //SCP格式的写法,更简洁,但是非默认端口需要通过其他方式(如主机别名的方式)设定.

	<3>: GIT协议:   	--> git://example.com[:port]/path/to/repo.git/ //最常用的只读协议.

	<4>: HTTP[S]协议:   --> http[s]://example.com[:port]/path/to/repo.git/ //兼有只能协议和哑协议.

	<5>: FTP[S]协议:    --> ftp[s]://example.com[:port]/path/to/repo.git/ //哑协议.

	<6>: RSYNC协议:     --> rsync://example.com/path/to.repo.git/ //哑协议.

	<7>: 本地协议(1):   --> file:///path/to/repo.git

	<8>: 本地协议(2):   --> /path/to/repo.git/ //和file://格式的本地协议类似,但有细微差别.例如克隆不支持浅克隆,且采用直接的硬连接实现克隆.

@@智能协议和哑协议

	<1>:智能协议:在会话时使用只能协议,会在会话的两个版本库的各自一端打开相应的程序进行数据交换.
		使用智能协议最直观的印象就是在数据传输过程中会有清晰的进度显示,而且因为是按需传输所有
		传输量更小,速度更快.

		-----------本地版本库--------------------------------------远程版本库----------------
		-																					-
		-	---git-send-pack------------git push----------------->git-receive-pack---   	-
		-																					-
		-	---git-fetch-pack<----------git pull------------------git-upload-pack----		-
		-																					-
		-------------------------------------------------------------------------------------
		上述协议中SSH,GIT及本地协议(file://)属于智能协议,HTTP协议需要特殊的配置,用`git-http-backend`
		配置CGI,并且客户端需要使用Git1.6.6或更高的版本才能够使用智能协议.
	<2>:哑协议,在使用哑协议访问远程版本库的时候,远程版本库不会运行辅助程序,而是完全依靠客户端去
		主动'发现'.客户端需要访问文件`.git/info/refs`获取当前版本库的引用列表.并根据引用对应的
		提交ID直接访问对象库目录下的文件.如果对象文件被打包而不是以松散对象的形式存在,则Git客户端
		还要去访问文件`.git/objects/info/packs`以获得打包文件列表,并据此读取完整的打包文件,从打包
		文件中获取完整的对象.由此可见哑协议效率非常之低,甚至为了要获取一个对象而去访问整个pack包.

		使用哑协议最直观的感受,传输速度非常慢,而且传输进度不可见,不知道什么时候才能够完成数据传输,
		上述协议中,FTP和RSYNC都是哑协议,没有通过`git-http-backend`或类似CGI程序配置的HTTP服务器提供
		的也是哑协议.因为哑协议需要索引文件`.git/info/refs`和`.git/objects/info/packs`以获取引用和
		包列表,因此要在版本库的钩子脚本post-update中设置运行`git update-server-info`以确保及时更新
		哑协议需要的索引文件.不过如果不使用哑协议,运行`git update-server-info`就没有必要了.

		GIT协议(智能协议):
			$ git clone git://git.kernel.org/pub/scm/git.git.git
		HTTPS[S](哑协议):
			$ git clone http://www.kernel.org/pub/scm/git/git.git
		HTTPS(智能协议):
			$ git clone https://github.com/git/git.git

@@多用户协同本地模拟

1.于/path/to/repos/shared.git中创建一个共享版本库.
$ git init --bare /path/to/repos/shared.git
2.用户user1克隆版本库.
$ cd /root/source/usersgitrepo/user1/workspace
$ git clone file:///root/source/gitrepo/shared.git project
3.设置版本库级别的user.name,user.email
$ cd project
$ git config user.name user1
$ git config user.email user1@molloc.com
4.用户user1创建初始数据并提交.
$ echo Heelo. > README
$ git add README
$ git commit -m 'initial commit.'
5.用户user1将本地版本库的提交推送到上游.
Note:在下面的推送指令中使用origin别名,其实际上指向就是'file:///root/source/gitrepo/shared.git'
	 可以从.git/config配置文件中看到是如何实现对origin远程版本库注册的.
$ git push origin master

###########
6.用户user2克隆版本库.
$ cd /root/source/usersgitrepo/user2/workspace
$ git clone file:///root/source/gitrepo/shared.git project
7.为用户user2设置版本库级别的user.name,user.email
$ cd project
$ git config user.name user2
$ git config user.email user2@molloc.com
8.用户user2的本地版本库现在拥有和user1用户同样的提交
$ git log

@@强制非快进式推送
现在用户user1和user2的工作区是相同的.思考一个问题:如果两人各自在本地版本库
中进行独立的提交,然后再分别想共享版本库推送,会互相覆盖吗?
试验:
1>首先,用户user1先在本地版本库中进行提交,然后将本地的提交推送到'远程'共享库
	1.用户user1创建team/user1.txt文件.
		Note:假设这个项目约定,每个开发者在team目录下写一个自述文件.于是用户user1创建文件`team/user1.txt`
	$ cd /root/source/usersgitrepo/user1/workspace/project
	$ mkdir team
	$ echo 'I\'m user1.' > team/user1.txt
	$ git add team
	$ git commit -m 'user1\'s profile.'
	$ git log --oneline --graph
通过上面的日志,可以看到用户user1成功的更新了'远程'共享版本库,如果用户user2不知道用户user1所做的上述操作,
仍然在基于'远程'版本库旧数据同步而来的本地版本库中进行改动,然后用户user2向远程共享版本库推送,会有什么结果?
用下面的操作验证:

2>用户user2创建team/user2.txt
	1.$ cd /root/source/usersgitrepo/user2/workspace/project
	  $ mkdir team
	  $ echo 'I\'m user1?' > team/user1.txt
	  $ git add team
	  $ git commit -m 'user2\'s profile.'
	2.用户user2将本地提交推送到服务器时出错.
	  $ git push

