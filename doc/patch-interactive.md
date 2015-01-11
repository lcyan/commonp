@@补丁文件的交互

	@之前各个章节的版本库间的交互都是通过`git push`,'git pull'命令来实现,这是git最主要的交互方式.
	但并不是全部.使用补丁文件是另一种交互方式.适用于参与者众多的大型项目进行的分布式开发.
	例如:git项目本身的代码提交就主要由贡献者通过邮件传递补丁文件来实现的.
	使用补丁文件进行提交的方式可以提高项目的参与度,因为任何人都可以参与到项目的开发.
	只要会将提交转化为补丁,会发邮件即可.

@@创建补丁

	@Git提供了将提交批量转换为补丁文件的命令:`git format-patch`,该命令后面的参数是一个
	版本范围列表.会将包含在此列表中的提交一一转换为补丁文件.每个补丁文件包含一个序号
	并从提交说明中提取字符串作为文件名.

	@下面演示一下在user1工作区中,如何将master分支的最近3个提交转换为补丁文件.
	(1)进入user1的工作区,切换到master分支.
	$ cd /path/to/user1/workspace/hello-world/
	$ git checkout branch
	$ git pull
	(2)执行下面的命令将最近3个提交转换为补丁文件.
	$ git format-patch -s HEAD~3..HEAD
	>> 0001-Fix-typo-help-to-help.patch
	>> 0002-Add-I18N-support.patch
	>> 0003-Translate-for-Chinese.patch

	#在上面的`git format-patch`命令中使用了-s参数.会在导出的补丁文件中添加当前
	#用户的签名.这个签名并非GnuPG式签名不过是将作者姓名添加到提交说明中而已.
	#和`git commit -s`命令效果相同.虽然签名很不起眼,但是对于以补丁的方式提交
	#数据却非常重要,因为以补丁方式的提交可能因为合并冲突或其他原因使得最终提交
	#的作者ID显示为代为提交项目管理员的ID,在提交说明中加入原始作者的署名信息
	#大概是作者唯一露脸的机会.如果在提交时忘了使用-s参数添加签名,可以在用
	#`git format-patch`命令创建补丁文件的时候补救.

	@补丁文件的格式
	$ vim 0001-Fix-typo-help-to-help.patch

	>> 1 From fecf7ae869d37146de4c9520bae4bb410a5dfbc0 Mon Sep 17 00:00:00 2001
	>> 2 From: user1 <user1@molloc.com>
	>> 3 Date: Thu, 8 Jan 2015 22:05:21 +0800
	>> 4 Subject: [PATCH 1/3] Fix typo: -help to --help.
	>> 5
	>> 6 Signed-off-by: user1 <user1@molloc.com>
	>> 7 ---
	>> 8  src/main.c | 2 +-
	>> 9  1 file changed, 1 insertion(+), 1 deletion(-)
	>> 10
	>> 11 diff --git a/src/main.c b/src/main.c
	>> 12 index 5dc3879..2001b36 100644
	>> 13 --- a/src/main.c
	>> 14 +++ b/src/main.c
	>> 15 @@ -13,7 +13,7 @@ int usage(int code)
	>> 16             "            say hello to the world.\n\n"
	>> 17             "    hello <username>\n"
	>> 18             "            say hi to the user.\n\n"
	>> 19 -           "    hello -h, -help\n"
	>> 20 +           "    hello -h, --help\n"
	>> 21             "            this help screen.\n\n", _VERSION);
	>> 22      return code;
	>> 23  }
	>> 24 --
	>> 25 2.2.1
	>> 26

	#补丁文件有一个类似邮件一样的文件头(1~4行),提交日志的第一行作为邮件标题(Subject)
	#其余的提交说明作为邮件内容(如果有的话),文件补丁用三个横线和提交说明分开.
	#实际上这些补丁文件可以直接拿来作为邮件发送给项目负责人.Git提供了一个辅助邮件
	#发送的命令`git send-mail`下面用该命令将这三个补丁以邮件的形式发送出去.
	$ git send-mail *.patch //如果发送错误需要执行下面命令.
	$ cpan -f Net::SMTP::SSL
	$ cpan -f IO::Socket::SSL
	//Need MIME::Base64 and Authen::SASL todo auth at /usr/bin/git-send-email line 811, <STDIN> line 1.
	$ yum install -y perl-Authen-SASL

	>> 0001-Fix-typo-help-to-help.patch
	>> 0002-Add-I18N-support.patch
	>> 0003-Translate-for-Chinese.patch
	>> The following files are 8bit, but do not declare a Content-Transfer-Encoding.
	>> 0002-Add-I18N-support.patch
	>> 0003-Translate-for-Chinese.patch
	>> Which 8bit encoding should I declare [UTF-8]?
	>> Who should the emails be sent to (if any)? admin@molloc.com
	>> Message-ID to be used as In-Reply-To for the first email (if any)?
	>> (mbox) Adding cc: user1 <user1@molloc.com> from line 'From: user1 <user1@molloc.com>'
	>> (body) Adding cc: user1 <user1@molloc.com> from line 'Signed-off-by: user1 <user1@molloc.com>'
	>>
	>> From: user1 <user1@molloc.com>
	>> To: admin@molloc.com
	>> Cc: user1 <user1@molloc.com>
	>> Subject: [PATCH 1/3] Fix typo: -help to --help.
	>> Date: Sat, 10 Jan 2015 23:14:28 +0800
	>> Message-Id: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> X-Mailer: git-send-email 2.2.1
	>>
	>> Send this email? ([y]es|[n]o|[q]uit|[a]ll): a
	>> OK. Log says:
	>> Server: smtp.mxhichina.com
	>> MAIL FROM:<user1@molloc.com>
	>> RCPT TO:<admin@molloc.com>
	>> RCPT TO:<user1@molloc.com>
	>> From: user1 <user1@molloc.com>
	>> To: admin@molloc.com
	>> Cc: user1 <user1@molloc.com>
	>> Subject: [PATCH 1/3] Fix typo: -help to --help.
	>> Date: Sat, 10 Jan 2015 23:14:28 +0800
	>> Message-Id: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> X-Mailer: git-send-email 2.2.1
	>>
	>> Result: 250 Data Ok: queued as freedom
	>>
	>> (mbox) Adding cc: Jiang Xin <jiangxin@ossxp.com> from line 'From: Jiang Xin <jiangxin@ossxp.com>'
	>> (body) Adding cc: Jiang Xin <jiangxin@ossxp.com> from line 'Signed-off-by: Jiang Xin <jiangxin@ossxp.com>'
	>> OK. Log says:
	>> Server: smtp.mxhichina.com
	>> MAIL FROM:<user1@molloc.com>
	>> RCPT TO:<admin@molloc.com>
	>> RCPT TO:<jiangxin@ossxp.com>
	>> From: user1 <user1@molloc.com>
	>> To: admin@molloc.com
	>> Cc: Jiang Xin <jiangxin@ossxp.com>
	>> Subject: [PATCH 2/3] Add I18N support.
	>> Date: Sat, 10 Jan 2015 23:14:29 +0800
	>> Message-Id: <1420902870-2048-2-git-send-email-user1@molloc.com>
	>> X-Mailer: git-send-email 2.2.1
	>> In-Reply-To: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> References: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> MIME-Version: 1.0
	>> Content-Type: text/plain; charset=UTF-8
	>> Content-Transfer-Encoding: 8bit
	>>
	>> Result: 250 Data Ok: queued as freedom
	>>
	>> (mbox) Adding cc: Jiang Xin <jiangxin@ossxp.com> from line 'From: Jiang Xin <jiangxin@ossxp.com>'
	>> (body) Adding cc: Jiang Xin <jiangxin@ossxp.com> from line 'Signed-off-by: Jiang Xin <jiangxin@ossxp.com>'
	>> OK. Log says:
	>> Server: smtp.mxhichina.com
	>> MAIL FROM:<user1@molloc.com>
	>> RCPT TO:<admin@molloc.com>
	>> RCPT TO:<jiangxin@ossxp.com>
	>> From: user1 <user1@molloc.com>
	>> To: admin@molloc.com
	>> Cc: Jiang Xin <jiangxin@ossxp.com>
	>> Subject: [PATCH 3/3] Translate for Chinese.
	>> Date: Sat, 10 Jan 2015 23:14:30 +0800
	>> Message-Id: <1420902870-2048-3-git-send-email-user1@molloc.com>
	>> X-Mailer: git-send-email 2.2.1
	>> In-Reply-To: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> References: <1420902870-2048-1-git-send-email-user1@molloc.com>
	>> MIME-Version: 1.0
	>> Content-Type: text/plain; charset=UTF-8
	>> Content-Transfer-Encoding: 8bit
	>>
	>> Result: 250 Data Ok: queued as freedom

@@应用补丁
	@在前面通过`git send-email`命令发送邮件给admin用户,显示使用linux上
	的mail命令检查一下邮件.
	$ mail
	& s 1-3 user1-mail-archive
	& q
	#上面的操作在本地创建了一个由开发者user1的补丁邮件组成的归档文件
	#user1-mail-archive,这个文件是mbox格式的,可以用mail命令打开.
	$ mail -f user1-mail-archive
	& q
	#使用`git am`命令可以使得保存在mbox中的邮件批量的应用在版本库中.
	am是'apply email'的缩写,下面是如何使用`git am`命令应用补丁.
	(1)基于HEAD~3版本创建一个本地分支.以便在该分支下应用补丁.
	$ git checkout -b user1 HEAD~3
	>> Switched to a new branch 'user1'
	(2)将mbox文件user1-mail-archive中的补丁全部应用在当前分支上.
	$ git am user1-mail-archive
	>> Applying: Fix typo: -help to --help.
	>> Applying: Add I18N support.
	>> Applying: Translate for Chinese.
	(3)补丁应用成功,查看提交日志.
	$ git log -3 --pretty=fuller
	>>commit d7432f3a649d86100fb7afbe23b2deb4bf5b3202
	>>Author:     Jiang Xin <jiangxin@ossxp.com>
	>>AuthorDate: Fri Dec 31 12:12:42 2010 +0800
	>>Commit:     user1 <user1@molloc.com>
	>>CommitDate: Sun Jan 11 11:06:54 2015 +0800
	>>
	>>Translate for Chinese.
	>>
	>>Signed-off-by: Jiang Xin <jiangxin@ossxp.com>
	>>
	>>commit 57b0e9066b31e27efb9a8ccc85eaf144a2d7cbfa
	>>Author:     Jiang Xin <jiangxin@ossxp.com>
	>>AuthorDate: Fri Dec 31 12:08:43 2010 +0800
	>>Commit:     user1 <user1@molloc.com>
	>>CommitDate: Sun Jan 11 11:06:54 2015 +0800
	>>
	>>Add I18N support.
	>>
	>>Signed-off-by: Jiang Xin <jiangxin@ossxp.com>
	>>
	>>commit 95fb787e5aab44d43ab4aca4422ccf2d167c1809
	>>Author:     user1 <user1@molloc.com>
	>>AuthorDate: Thu Jan 8 22:05:21 2015 +0800
	>>Commit:     user1 <user1@molloc.com>
	>>CommitDate: Sun Jan 11 11:06:54 2015 +0800
	>>
	>>Fix typo: -help to --help.
	>>
	>>Signed-off-by: user1 <user1@molloc.com>
	#从提交信息中可以看出:
	(1)提交的时间信息使用了邮件发送的时间.
	(2)作者(Author)的信息被保留,和补丁文件中的一致.
	(3)提交者(Commit)全部设置为user1,因为提交是在user1的工作区完成的.
	(4)提交说明中的签名信息被保留,实际上`git am`命令也可以提供-s参数,
	   在提交上面中附加执行命令的用户签名.

	@对于不习惯在控制台用mail命令接收邮件的用户,可以通过邮件附件,U盘
	或其他方式获取`git format-patch`生成的补丁文件.将补丁文件保存在
	本地,通过管道符调用`git am`命令应用补丁.
	$ ls *.patch
	$ cat *.patch | git am
	>> Applying: Fix typo: -help to --help.
	>> Applying: Add I18N support.
	>> Applying: Translate for Chinese.

@@Git还提供了一个命令`git apply`可以应用一般格式的补丁文件,但是不能执行提交,也不能保持
  补丁中的作者信息,实际上`git apply`命令和GNU patch命令类似.

@@StGit和Quilt
	@一个复制功能的开发一定是由多个提交来完成的.对于在以接收和应用补丁文件为开发模式的
	 项目中,复杂的功能需要通过多个补丁文件来完成.补丁文件因为要经过审核才能被接受.因此
	 针对一个功能的多个补丁文件一定要保证各个都是精品:补丁1用来完成一个功能点,补丁2用来
	 完成第二个功能点.等等,一定不能出现这样的情况:补丁3用于修正补丁1的错误,补丁10改正
	 补丁7中的文字错误,等等,这样就带来补丁管理的难题.
	 实际上基于特性分支开发又何尝不是如此?在将特性分支归并到开发主线上,要接受团队的评审.
	 特性分支的开发者一定想将特性分支上的提交进行重整,把一些提交合并或拆分.使用变基命令
	 可以实现提交的重整.但是操作起来比较困难.有什么好办法呢?

@StGit
	StGit是Stacked Git的简称,StGit就是解决上面提到的两个难题的答案.实际上StGit在设计
	上参考了一个著名的补丁管理工具Quilt,并且可以输出Quilt兼容的补丁列表.

	StGit是一个python项目,安装起来还是很方便.
	$ yum install stgit stgit-contrib
	(1)首先检出hello-world版本库,进行stgit的实践.
	$ cd /path/to/my/workspace/
	$ git clone file:///path/to/repos/hello-world.git stgit-demo
	$ cd stgit-demo
	(2)在当前工作目录初始化stgit
	$ stg init
	(3)现在补丁列表为空
	$ stg series
	(4)将最新的三个提交转换为StGit补丁.
	$ stg uncommit -n 3
	>>Uncommitting 3 patches ... done
	(5)现在补丁列表有三个文件了.
	第一列是补丁的状态符号,加号(+)代表该补丁已经应用在版本库中,大于号(>)用于标识当前补丁.
	$ stg series
	>>+ fix-typo-help-to-help
	>>+ add-i18n-support
	>>> translate-for-chinese
	(6)现在查看master分支的日志,发现和之前没有两样.
	$ git log -3 --oneline
	>>070947f Translate for Chinese.
	>>f73345b Add I18N support.
	>>fecf7ae Fix typo: -help to --help.
	(7)执行StGit补丁出栈命令,会将补丁撤出引用,使用-a参数会将所有补丁撤出应用.
	$ stg pop
	>>Checking for changes in the working directory ... done
	>>Popping patch "translate-for-chinese" ... done
	>>Now at patch "add-i18n-support"
	$ stg pop -a
	>>Checking for changes in the working directory ... done
	>>Popping patches "add-i18n-support" - "fix-typo-help-to-help" ... done
	>>No patches applied
	(8)在来看看版本的日志,发发现最新的三个提交都不见了.
	$ git log -3 --oneline
	>>754e97e Bugfix: allow spaces in username.
	>>80a7591 Refactor: user getopt_long for arguments parsing.
	>>658eebd blank commit for GnuPG-signed tag test.
	(9)查看补丁列表状态,会看到每个补丁前都用减号(-)标识
	$ stg series
	>>- fix-typo-help-to-help
	>>- add-i18n-support
	>>- translate-for-chinese
	(10)执行补丁入栈,即应用补丁,使用命令`stg push`或`stg goto`注意`stg push`命令和`git push`
		风马牛不相及.
	$ stg push
	>>Checking for changes in the working directory ... done
	>>Fast-forwarded patch "fix-typo-help-to-help"
	>>Now at patch "fix-typo-help-to-help"
	$ stg goto add-i18n-support
	>>Checking for changes in the working directory ... done
	>>Fast-forwarded patch "add-i18n-support"
	>>Now at patch "add-i18n-support"
	(11)现在处于应用add-i18n-support补丁的状态,这个补丁有些问题,本地话语音模板有错误,我们来
		修改一下.
	$ cd src/
	$ rm locale/helloword.pot
	$ make po
	(12)现在查看工作区的状态,发现工作区有改动.
	$ git status -s
	>> M locale/helloworld.pot
 	>> M locale/zh_CN/LC_MESSAGES/helloworld.po
 	(13)不要提交,而是执行`stg refresh`命令更新补丁
 	$ stg refresh
 	>>Checking for changes in the working directory ... done
	>>Refreshing patch "add-i18n-support" ... done
	(14)这时在查看工作区,发现本地修改不见了.
	$ git status -s
	(15)执行`stg show`会看到当前的补丁add-i18n-support已经更新.
	$ stg show
	(16)将最后一个补丁应用到版本库,遇到冲突,这是因为最后一个补丁是对中文本地化文件的翻译,
		因为翻译前的模板文件被更改了所有造成冲突.
	$ stg push
	>>Pushing patch "translate-for-chinese" ... 
	>>  Error: Three-way merge tool failed for file "src/locale/zh_CN/LC_MESSAGES/helloworld.po"
	>>  Error: The merge failed during "push".
	>>         Use "refresh" after fixing the conflicts or revert the operation with "push --undo".
	>>  stg push: GIT index merging failed (possible conflicts)
	(17)这个冲突很好解决,直接编辑冲突文件helloworld.po.
	(18)执行git add命令完成冲突解决.
	$ git add locale/zh_CN/LC_MESSAGES/helloworld.po
	(19)不要提交,而是使用`stg refresh`命令更新补丁,同时更新提交.
	$ stg refresh
	>> Now at patch 'translate-for-chinese'
	(20)查看状态
	$ git status -s
	(21)导出补丁文件
	$ stg export -d patches
	(22)看看导出补丁的目标目录
	$ ls pathces
	>>add-i18n-support fix-typo-help-to-help series translate-for-chinese
	(23)其中文件series是补丁文件的礼包.列在最前面的补丁先被应用.
	#通过上面的演示可以看出stgit可以非常方便地对提交进行整理,整理提交是无须使用复杂的变基命令,而是采用
	#提交stgit化,修改文件,执行`stg refresh`的工作流程即可更新补丁和提交.stgit还可以将补丁导出为补丁文件
	#虽然导出的补丁文件没有像`git format-patch`那样加上代表顺序的数字前缀,但是用文件series标注了补丁文件
	#的先后顺序.实际上执行`stg export`时添加`-n`参数为补丁文件添加数字前缀.