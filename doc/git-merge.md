
NOTE:实际上`git pull`由两个步骤组成的:一个是获取操作(git fetch),另一个是合并操作(git merge)
	'git pull = git fetch + git merge'

1.合并操作的命令格式:
	$ git merge [options...] <commit>...

NOTE:合并操作的大多数情况,只需提供一个<commit>(提交ID或对应的引用:分支,里程碑等)作为参数.
	 合并操作将<commit>对应的目录树和当前工作分支的目录树的内容进行合并,合并后的提交以当
	 前分支的提交作为第一个父提交,以<commit>为第二个父提交,合并还支持将多个<commit>代表的
	 分支和当前分支进行合并,过程类似.

	 默认情况下,合并后的结果会自动提交,但是如果提供--no-commit选项,则合并后的结果会放入
	 暂存区,用户可以对合并结果进行检查,更改,然后手动提交.

	 合并提交并非总会成功,因为合并的不同提交核能同事修改了同一文件相同区域的内容,导致冲突.
	 冲突会造成合并操作的中断,冲突的文件被标识,用户可以对标识的为冲突的文件进行冲突解决操作.
	 然后更新暂存区,再提交,最终完成合并操作.

	 根据合并操作是否遇到冲突,以及不同的冲突类型,可以分为以下几种情况:
	 	1.成功的自动合并.
	 	2.逻辑冲突.
	 	3.真正的冲突.
	 	4.树冲突.
<1>: 自动合并.
	--保证两个用户的本地版本库和共享版本库状态一致,需要在两个用户的本地版本库执行
	--$ git fetch
	--$ git reset --hard origin/master

	--两个用户分别修改不同的文件,其中一个用户要尝试合并操作将本地提交和另一个用户的提交合并.
	(1):用户use1修改`team/user1.txt`文件,提交并推送到共享服务器.
	$ cd /root/source/usersgitrepo/user1/workspace/project
	$ echo "hack by user1 at `date`" >> team/user1.txt
	$ git add -u
	$ git commit -m 'update team/user1.txt'
	$ git push
	(2):用户user2修改team/user2.txt文件.提交.
	$ cd /root/source/usersgitrepo/user2/workspace/project
	$ echo "hack by user2 at `date`" >> team/user2.txt
	$ git add -u
	$ git commit -m 'update team/user2.txt'
	(3):用户user2在推送的时候,会遇到非快进式推送的错误而被终止.
	$ git push
	'To file:///root/source/gitrepo/shared.git'
	' ! [rejected]        master -> master (fetch first)'
	'error: failed to push some refs to \'file:///root/source/gitrepo/shared.git'
	'hint: Updates were rejected because the remote contains work that you do'
	'hint: not have locally. This is usually caused by another repository pushing'
	'hint: to the same ref. You may want to first integrate the remote changes'
	'hint: (e.g., \'git pull ...\') before pushing again.'
	'hint: See the \'Note about fast-forwards\' in \'git push --help\' for details.'
	(4):用户user2执行获取(git fetch)操作,获取到的提交更新到本地用于跟踪共享版本库master
		分支的本地引用`origin/master`中.
	$ git fetch
	(5):用户user2执行合并操作,完成自动合并.
	$ git merge origin/master
	(6):用户user2推送合并后的本地版本库到共享库.
	$ git push
	---------------------------------------------------------------------------------------
	--修改相同文件的不同区域.
	(1):为了确保两个用户的本地版本库和共享版本库一致,先分别对两个用户的本地版本库执行拉回操作.
	$ git pull
	(2):用户user1在自己的工作区修改README文件.在文件第一行插入内容,更改或的文件内容如下.
	$ User1 hacked.
	$ Hello.
	(3):用户user1对修改进行本地提交并推送到共享版本库.
	$ git add -u
	$ git commit -m "User1 hack at the beginning."
	(4):用户user2在自己的工作区中修改README文件,在文件的最后插入内容,更改后的文件内容如下.
	$ Hello.
	$ User2 hacked.
	(5):用户user2对修改进行本地提交.
	$ git add -u
	$ git commit -m "User2 hack at the end."
	(6):用户user2执行获取(git fetch)操作,获取到的提交更新到本地用于跟踪共享版本库master
		分支的本地引用`origin/master`中.
	$ git fetch
	(7):用户user2执行合并操作,完成自动合并.
	$ git merge refs/remotes/origin/master
	(8):用户user2推送合并后的本地版本库到共享库.
	$ git push
	(9):如果追溯一下README文件每一行的来源,可以看到分别是user1,user2更改的最前和最后一行.
	$ git blame README
	'4f5bfd58 (user1 2015-01-03 15:21:17 +0800 1) User1 hacked.'
	'^5a27027 (user1 2015-01-02 23:19:44 +0800 2) Hello.'
	'f609db9c (user2 2015-01-03 15:22:05 +0800 3) User2 hacked.'
	------------------------------------------------------------------------------------------
	--同时更改文件名和文件内容
	如果一个用户将文件移动到其他目录(或修改文件名),另外一个用户针对重命名前的文件进行了修改,
	还能够自动合并么?这对于其他版本控制系统可能是一个难题,Git对于此类冲突能够很好地处理,
	可以自动解决冲突实现自动合并.
	(1):为了确保两个用户的本地版本库和共享版本库一致,先分别对两个用户的本地版本库执行拉回操作.
	$ git pull
	(2):用户user1在自己的工作区中将文件README重命名,本地提交并推送到共享版本库.
	$ cd /root/source/usersgitrepo/user1/workspace/project
	$ mkdir doc
	$ git mv README doc/README.txt
	$ git commit -m 'move document to doc/.'
	$ git push
	(3):用户user2在自己工作区中修改README文件,在文件的最后插入内容,并本地提交.
	$ cd /root/source/usersgitrepo/user2/workspace/project
	$ echo "User2 hacked again." >> README
	$ git add -u
	$ git commit -m 'User2 hack README again.'
	(4):用户user2执行获取(git fetch)操作,获取到的提交更新到本地用于跟踪共享版本库master
		分支的本地引用`origin/master`中.
	(5):用户user2执行合并操作,完成自动合并.
	$ git merge refs/remotes/origin/master
	(6):用户user2推送合并后的本地版本库到共享库.
	$ git push
	(7):使用-m参数可以查看合并操作所做出的修改.
	$ git log -1 -m --stat
-------------------------------------------------------------------------------------------

<2>:逻辑冲突
	--自动合并如果成功地执行,则大多数情况下就意味这完事大吉,但是在某些特殊情况下,合并后的结果
	虽然在Git看来是完美的合并,实际上却存在这逻辑冲突.
	一个典型的逻辑冲突是一个用户修改了一个文件的文件名,而另外的用户在其他文件追踪引用旧的文件名.
	这样的合并虽然能够成功但是包含着逻辑冲突.
	eg:一个C语音的项目组存在头文件hello.h,该文件定义了一些函数声明.
	   用户user1将hello.h文件改名为api.h
	   用户user2谢了一个新的源码文件foo.c并在该文件中包含了hello.h
	   两个用户的提交合并后,会因为源码文件foo.c找不到所包含的文件hello.h而导致项目编译失败.
	解决方法,每次编译时都要执行自动化测试.捕捉潜在bug.

---------------------------------------------------------------------------------------------

<3>:冲突解决
	--如果两个用户修改了同一个文件的同一区域,则在合并的时候会遇到冲突而导致合并过程中断.
	Git把决定权交给用户,在这种情况下,git标识出合并冲突,等待用户对冲突做出抉择.
	(1):为了确保两个用户的本地版本库和共享版本库一致,先分别对两个用户的本地版本库执行拉回操作.
	$ git pull
	(2):用户user1在自己的工作区中修改doc/README.txt文件(仅改动第二行).修改后的内容如下:
	User1 hacked.
	Hello, user1.
	User2 hacked.
	User2 hacked again.
	(3):用户user1对修改进行本地提交并推送到共享版本库.
	$ git add -u
	$ git commit -m 'Say hello to user1.'
	$ git push
	(4):用户user2在自己的工作区中修改doc/README.txt文件(仅改动第二行).修改后的内容如下:
	User1 hacked.
	Hello, user2.
	User2 hacked.
	User2 hacked again.
	(5):用户user2对修改进行了本地提交.
	$ git add -u
	$ git commit -m 'Say hello to user2.'
	(6):用户user2执行拉回操作,遇到冲突
	$ git pull
	'Auto-merging doc/README.txt'
	'CONFLICT (content): Merge conflict in doc/README.txt'
	'Automatic merge failed; fix conflicts and then commit the result.'
	$ git status
	'On branch master'
	'Your branch and \'origin/master\' have diverged,'
	'and have 1 and 1 different commit each, respectively.'
	'  (use "git pull" to merge the remote branch into yours)'
	'You have unmerged paths.'
	'  (fix conflicts and run "git commit")'

	'Unmerged paths:'
	'  (use \"git add <file>...\" to mark resolution)'

	'	both modified:   doc/README.txt'

	'no changes added to commit (use \"git add\" and/or \"git commit -a\")'
	--Git是如何记录合并过程及冲突的呢?实际上合并过程是通过.git目录下的几个文件进行记录的.
		1>:文件.git/MERGE_HEAD记录所合并的提交ID.
		2>:文件.git/MERGE_MSG记录合并失败的信息.
		3>:文件.git/MERGE_MODE标识合并状态.
	--版本库暂存区中则会记录冲突文件的多个不同版本,可以使用git ls-files命令查看:
	$ git ls-files -s
	'100644 ea501534d70a13b47b3b4b85c39ab487fa6471c2 1	doc/README.txt'
	'100644 5fe2f6b290330f7cf0efa9dd6c0cbf1b8231bfad 2	doc/README.txt'
	'100644 dc9c3da77b0e0c99ed9779903872a3ea1a608eac 3	doc/README.txt'
	'100644 9111b6cccec673a83e114c76abb66668e2114f55 0	team/user1.txt'
	'100644 553fae1dfa4220e24728dd504ebb25610bbd29c3 0	team/user2.txt'
	/*在上面的输出中,每一行分为四个字段,前两个是文件的属性和SHA1值,第三个字段是
	暂存区编号,当合并冲突发生后,会用0以上的暂存区编号.*/
	1>:编号为1的暂存区用于保存冲突文件修改之前的副本,即冲突双方共同的祖先版本.可以用`:1:<filename>`访问.
	$ git show :1:doc/README.txt
	'User1 hacked.'
	'Hello.'
	'User2 hacked.'
	'User2 hacked again.'
	2>:编号为2的暂存区用于保存当前冲突文件在当前分支中修改的副本.可以用`:2:<filename>`访问.
	$ git show :2:doc/README.txt
	'User1 hacked.'
	'Hello user2.'
	'User2 hacked.'
	'User2 hacked again.'
	3>:编号为3的暂存区用于保存当前冲突文件在合并版本(分支)中修改的副本.可以用`:3:<filename>`访问.
	$ git show :3:doc/README.txt
	'User1 hacked.'
	'Hello user1.'
	'User2 hacked.'
	'User2 hacked again.'

	--对暂存区中冲突文件的上述三个副本无须了解太多,这三个副本实际上是提供冲突解决工具,用于实现三向文件合并的.
	工作区的版本则可能同时包含了成功的合并及冲突的合并,其中冲突的合并会用特殊的标记(<<<===>>>)进行标识.
	查看当前工作中的冲突文件.

	$ cat doc/README.txt
	'User1 hacked.'
	'<<<<<<< HEAD'
	'Hello user2.'
	'======='
	'Hello user1.'
	'>>>>>>> 0bcb071bea62b03f4a2fd85686b47ddd56ba5a56'
	'User2 hacked.'
	'User2 hacked again.'
	NOTE: 特殊标识`<<<<<<`和`=======`之间的内容是当前分支所更改的内容.
		  特殊标识`======`和`>>>>>>>`之间的内容是所合并的版本更改的内容.
	冲突解决的实质就是通过编辑操作,将冲突标识符所标识的冲突内容替换为合适的内容,并去掉冲突标识符.
	编辑完毕后执行`git add`命令将文件添加到暂存区(标号为0),然后再提交就完成冲突解决了.

	当工作区处于合并冲突状态时,无法再执行提交操作.此时有2个选择:或者对合并冲突进行冲突解决操作.
	放弃合并操作非常简单,只须执行`git reset`将暂存区重置即可.

	###########################################################################
	<1>:手工编辑完成冲突解决.
	$ vim doc/README.txt 修改后的内容如下
	'User1 hacked.'
 	'Hello user1 and user2.'
 	'User2 hacked.'
 	'User2 hacked again.'
 	$ git add -u
 	$ git commit -m 'Merge completed: say hello to all users.'
 	<2>:使用`git mergetool`工具解决.
 	$ git mergetool //kdiff3

<4>:树冲突
	如果一个用户将某个文件改名,另外一个用户将同意的文件改为另外的名字,当这两个用户的提交进行
	合并操作时,Git显示无法替用户做出裁决,于是就产生了冲突.这种因为文件名修改造成的冲突,称为
	树冲突,这种树冲突的解决方式比较特别:
	(1):为了确保两个用户的本地版本库和共享版本库一致,先分别对两个用户的本地版本库执行拉回操作.
	$ git pull
	(2):用户user1将文件doc/README.txt改名为readme.txt提交并推送到共享版本库.
	$ cd /root/source/usersgitrepo/user1/workspace/project
	$ git mv doc/README.txt readme.txt
	$ git commit -m 'rename doc/README.txt to readme.txt.'
	$ git push
	(3):用户user2将文件doc/README.txt 改名为README.并做本地提交.
	$ cd /root/source/usersgitrepo/user2/workspace/project
	$ git mv doc/README.txt README
	$ git commit -m 'rename doc/README.txt to README.'
	(4):用户user2执行`git pull`操作,遇到合并冲突.
	$ git pull
	>>CONFLICT (rename/rename): 
	>>Rename "doc/README.txt"->"README" in branch "HEAD"
	>>rename "doc/README.txt"->"readme.txt" in "c3f6772f322862cc6627be5ee7bb35588d4eccc0"
	因为两个用户同时更改了同一文件的文件名并且改成了不同的名字,于是就产生了冲突.此时查看
	状态会看到:
	$ git status
	>>On branch master
	>>Your branch and 'origin/master' have diverged,
	>>and have 1 and 1 different commit each, respectively.
	>>  (use "git pull" to merge the remote branch into yours)
	>>You have unmerged paths.
	>>  (fix conflicts and run "git commit")

	>>Unmerged paths:
	>>  (use "git add/rm <file>..." as appropriate to mark resolution)

	>>	added by us:     README
	>>	both deleted:    doc/README.txt
	>>	added by them:   readme.txt

	>>no changes added to commit (use "git add" and/or "git commit -a")
	此时查看一下用户user2本地版本库的暂存区,可以看到冲突在编号1,2,3的暂存区
	出现了相同SHA1哈希值对象,但是文件名各不相同.
	$ git ls-files -s
	>>100644 463dd451d94832f196096bbc0c9cf9f2d0f82527 2	README
	>>100644 463dd451d94832f196096bbc0c9cf9f2d0f82527 1	doc/README.txt
	>>100644 463dd451d94832f196096bbc0c9cf9f2d0f82527 3	readme.txt
	>>100644 9111b6cccec673a83e114c76abb66668e2114f55 0	team/user1.txt
	>>100644 553fae1dfa4220e24728dd504ebb25610bbd29c3 0	team/user2.txt
	其中暂存区1是改名之前的`doc/README.txt`,
		暂存区2是用户user2改名后的文件名`README`,
		暂存区3是其他用户(user1)改名后的文件名`readme.txt`
	此时的工作区中存在两个相同的文件REAME和readme.txt分别是用户user2和user1
	对doc/README.txt重命名之后的文件.
	<1>:手工操作解决树冲突.
		这时user2应该和user1商量一下到底将该文件改成什么名字,如果双方最终确认
		应该采用user2的重命名规则.则user2应该进行下面的操作完成冲突解决.
		1>:删除文件 readme.txt
		在执行`git rm`操作过程会弹出三条警告,说共有三个文件待合并.
		$ git rm read.txt
		>>readme.txt: needs merge
		>>rm 'readme.txt'
		$ git rm doc/README.txt
		>>doc/README.txt: needs merge
		>>rm 'doc/README.txt'
		$ git add README
		这时查看一下暂存区,会发现所有文件都在暂存区0中.
		$ git ls-files -s
		>>100644 463dd451d94832f196096bbc0c9cf9f2d0f82527 0	README
		>>100644 9111b6cccec673a83e114c76abb66668e2114f55 0	team/user1.txt
		>>100644 553fae1dfa4220e24728dd504ebb25610bbd29c3 0	team/user2.txt
		提交完成冲突解决.
		$ git commit -m 'fixed tree conflict.'
	<2>:交互式解决树冲突.
		树冲突虽然不能像文件冲突那样使用图形工具进行冲突解决,但还是可以使用
		git mergetool命名,通过交互式问答快速解决此类问题.
		首先将user2的工作区重置到前一次的提交,在执行git merge引发树冲突.
		1>:重置到前一次提交
		$ cd /root/source/usersgitrepo/user2/workspace/project
		$ git reset --hard HEAD^
		2>:执行git merge引发树冲突
		$ git merge refs/remotes/origin/master
		$ git status -s
		>>AU README
		>>DD doc/README.txt
		>>UA readme.txt
		3>:执行git mergetool
		$ git mergetool
		>>opendiff kdiff3 tkdiff xxdiff meld tortoisemerge gvimdiff diffuse diffmerge ecmerge p4merge araxis bc codecompare emerge vimdiff
		>>Merging:
		>>doc/README.txt
		>>README
		>>readme.txt

		>>mv: cannot stat `doc/README.txt': No such file or directory
		>>cp: cannot stat `./doc/README_BACKUP_8347.txt': No such file or directory
		>>mv: cannot move `.merge_file_DPdJar' to `./doc/README_BASE_8347.txt': No such file or directory
		>>/usr/local/libexec/git-core/git-mergetool: line 229: ./doc/README_LOCAL_8347.txt: No such file or directory
		>>/usr/local/libexec/git-core/git-mergetool: line 229: ./doc/README_REMOTE_8347.txt: No such file or directory
		>>Deleted merge conflict for 'doc/README.txt':
		>>  {local}: deleted
		>>  {remote}: deleted
		>>Use (m)odified or (d)eleted file, or (a)bort?
		4>:询问对文件doc/README.txt的处理方式,输入d选择将该文件删除.
		$ d
		>>Deleted merge conflict for 'README':
		>>{local}: created file
		>>{remote}: deleted
		5>:询问对文件README的处理方式,输入c选择将该文件保留(创建).
		$ c
		6>:询问对文件README的处理方式,输入d选择将该文件删除.
		>>Deleted merge conflict for 'readme.txt':
	    >>{local}: deleted
	    >>{remote}: created file
	    $ d
	    7>:提交完成冲突解决
	    $ git ci -m 'fixed tree conflict.'
	    8>:向服务器推送
	    $ git push

@@合并策略
Git合并操作支持很多合并策略,默认会选择最合适的合并策略,例如,和一个分支
进行合并是会选择recursive合并策略,当和两个或两个以上的其他分支合并时
采用octopus合并策略,可以通过传递参数使用知道的合并策略.

$ git merge [-s <strategy>] [-X <strategy-option>] <commit>...

其中参数 -s 用于设定合并策略,参数-X用户为所选的合并策略提交附近的参数.
下面分别介绍不同的合并策略:
(1):resolve
	该合并策略只能用于合并两个头(即当前分支和另外一个分支)使用三向合并策略.
	这个合并策略被认为是最安全,最快的合并策略.
(2):recursive
	该合并策略只能用于合并两个头(即当前分支和另外一个分支)使用三向合并策略.
	这个合并策略是合并两个头指针时的默认合并策略.
	当合并的头指针拥有一个以上的祖先的时候,会针对多个公共祖先创建一个合并的树,
	并以此作为三向合并的参照.这个合并策略被认为可以实现冲突的最小化,而且可以发现
	和处理由于重命名导致的合并冲突.
	这个合并策略可以使用下列选项:
	--ours:在遇到冲突的时候,选择我们的版本(当前分支的版本)而忽略他人的版本.如果他人
	的改动和本地改动不冲突,会将他们的改动合并进来.
	Note:不要将此模式和后面介绍的单纯的ours合并策略相混淆,后面介绍的ours合并策略
	直接丢弃其他分支的变更,无论冲突与否.
	--theirs
	和ours选项相反,遇到冲突时选择他人的版本,丢弃我们的版本.
	--subtree[=path]
	这个选项使用子树合并策略,比下面介绍的subtree(子树合并)策略的定制能力更强.
	下面的subtree合并策略要对两个树的目录移动进行猜测.而recursive合并策略可以通过
	此参数直接对子树目录进行设置.
(3):octopus
	可以合并两个以上的头指针,但是拒绝执行需要手动解决的复杂合并,主要的用途是将多个
	主题分支合并到一起.这个合并策略是对三个及三个以上的头指针进行合并时的默认合并策略.
(4):ours
	可以合并任意数量的头指针,但是合并的结果总是使用当前分支的内容,丢弃其他分支的内容.
(5):subtree
	这是一个经过调整的recursive策略,当合并树A和B时,如果B和A的一个子树相同,B首先进行调整
	以匹配A的树的结构,以免两颗树在同一级别进行合并.同事也针对两个树的共同祖先进行调整.

@@合并的相关设置

可以通过git config 命令设置与合并相关的配置变量,对合并进行配置.
(1):merge.conflicstyle
	该配置变量定义冲突文件中冲突的标记风格.有两个可用的风格.默认的'merge'或'diff3'
	默认的'merge'风格使用标准的冲突分界符(<<<<<<<=======>>>>>>>)对冲突的内容进行标识.
	其中的两个文字块分别是本地的修改和他人的修改.
	如果使用'diff3'风格,则会在冲突中出现三个文字块,分别是<<<<<<<和|||||||之间的本地
	更改版本,||||||和=======之间的原始(共同祖先)版本和======和>>>>>>>之间的是他人更改的版本.
	User1 hacked.
	<<<<<<<< HEAD
	Hello user2.
	||||||| merged common ancestors
	Hello
	==========
	Hello user1.
	>>>>>>>> a4343435347fd323ac324324
	User2 hacked.
	User2 hacked again.
(2):merge.tool
	设定执行`git mergetool`进行冲突解决时调用的图形化工具,配置变量'merge.tool'可以设置为如下
	内置支持的工具:'kdiff3','tkdiff','meld','meld','xxdiff','emerge','vimdiff','gvimdiff',
	'p4merge','opendiff'
	$ git config --global merge.tool kdiff3

	如果将merge.tool设置为其他值,则使用自定义工具进行冲突解决.自定义工具需要通过mergetool.<tool>.cmd
	对自定义工具的命令行进行设置.
(3):mergetool.<tool>.path
	如果git mergetool支持的冲突解决工具安装在在特殊位置,可以使用mergetool.<tool>.path对工具<tool>的
	安装位置进行设置.
	$ git config --global mergetool.kdiff3.path /path/to/kdiff3
(4):mergetool.<tool>.cmd
	如果所用的冲突解决工具不在内置的工具列表中,还可以使用mergetool.<tool>.cmd对自定义工具的命令行
	进行设置,同时要将merge.tool设置为<tool>
	$ git config --global merge.tool mykdiff3
	$ git config --global mergetool.mykdiff3.cmd '/usr/bin/kdiff3 -L1 "$MERGED (Base)" -L2 "$MERGED (Local)"
	-L3 "$MERGED (Remote)" --auto -o "$MERGED" "$BASE" "$LOBAL" "$REMOTE"'
(5):merge.log
	是否在合并提交的提交说明中包含合并提交的概要信息,默认为false
