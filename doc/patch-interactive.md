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