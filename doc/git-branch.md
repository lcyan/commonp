@@Git分支

应用的角都介绍分支的几种不同类型:发布分支,特性分支,卖主分支.
?如何创建分支,切换分支,以及分支间的合并,变基.

分支是代码管理的利器,如果没有有效的分支管理,代码管理就适应不了复杂的开发过程
和项目的需要,在实际的项目实践中,单一分支的单线开发模式远远不够.因为:

<1>:成功的软件项目大多要经过多个开发周期,发布多个软件版本.每个已经发布的版本
	都可能发现Bug,这就需要对历史版本进行更改.
<2>:有前瞻性的项目管理.新版本的开发往往是和当前版本同步进行的.如果两个版本的开发都
	混杂在master分支中,肯定会是异常灾难.
<3>:如果产品要针对不同的客户定制,肯定是希望客户越多越好,如果所有的客户定制都
	混杂在一个分支中,必定会带来混乱.如果使用多个分支管理不同的定制,但若是管
	理不善.分支直接定制功能的迁移就会成为头痛的问题.
<4>:即便是所有成员都在为同一个项目的同一个版本进行工作,每个人领受任务却不尽
	相同,有的任务开发周期长,有的任务需要对软件架构进行较大的修改.如果所有人
	都工作在同一个分支.就会因为过多过频的冲突导致效率低下.
<5>:敏捷开发(不管是极限编程XP还是scrum或其他)是最有效的项目管理模式,其最有效的
	一个实践就是快速迭代,每晚编译.如果不能将项目的各个功能模块的开发通过分支
	进行隔离,在软件集成上就会遭遇困难.

@@使用版本控制系统的分支功能,可以避免对已发布的软件版本进行Bug修正是引入新
功能的代码,或者因误删其他Bug修正代码导致已修复的问题重现.在这种情况下创建
分支有一个专有的名称:Bugfix分支或发布分支(Release Branch)之所以成为发布分支,
是因为在软件新版本发布后经常使用此技术进行软件维护,发布升级版本.

@import doc/img/git-branch-line-27.jpg

1>图2,可以看到开发者创建了一个发布分支(Bugfix),在分支中提交修正代码'fix1'
  .注意此分支是自上次软件发布时最后一次提交进行创建的,因为此分支中没有
  包含开发者为新功能所做的提交'F1.1',是一个干净的分支.
2>图3可以看出从发布分支向主线做了一次合并,这是因为在主线上也同样存在该Bug,
  需要在主线上也做出相应的更改.
3>图4,开发这继续开发,针对功能1执行了一个新的提交,编号'F1.2'这时,客户报告有新的Bug.
4>继续在发布分支上进行Bug修正,参考图示5,当修正完成(提交'fix2')时,基于发布分支
  创建一个新的软件版本发给客户.不要忘了想主线合并,因为同样的bug可能在主线上也存在.

  Note:关于如何基于一个历史提交创建分支,以及如何在分支之间进行合并.


@@特性分支
@import doc/img/git-branch-line-43.jpg

1>图1,用圆圈代表功能1的历史提交,用三角代替功能2的历史提交.因为所有开发者都在主线上
	工作,所以提交混杂在一起.
2>当决定功能2不在这一版的产品中发布,延期到下一个版本,功能2 的开发者做了一个(或者若干个)
  反向提交,即图2中的倒三角(代号为F2.x)标识的反向提交.将功能2的所有历史提交全部撤销.
3>图3表示除了功能2外的其他开发继续进行.
	那么负责开发功能2的开发者干什么呢,或者放一个长假,或者在本地开发,与版本库隔离,
	即不向版本库提交,直到延期的项目终于发布之后在将代码提交,这两种方法都是不可取的,
	尤其是后一种隔离开发最微信,如果因为病毒感染,文件误删,磁盘损坏,就会导致全部工作
	损失殆尽.
	采用分支将某个功能或模块的开发与开发主线独立出来,是及解决类似问题的办法,这种用途
	的分支被成为特性分支(Feature Branch)或主题分支(Topic Branch).下图展示了如何使用
	特性分支帮助纠正要延期的项目.

	@@import doc/img/git-branch-line-57.jpg
	图1和签名的一样,都是过个开发者的提交混杂在开发主线上.
	图2是当得知功能2不在此次产品发布中后,功能2的开发者所做的操作.
	首先,功能2的开发者提交一个(或若干个)反向提交,将功能2的相关代码全部撤销,图中倒三角
	(代号为F2.X)的提交就是一个反向提交.

	接着,功能2的开发者从反向提交开始创建一个特性分支.
	最后,功能2的开发者将功能2的历史提交拣选到特性分支上,对于git可以使用拣选命令`git cherry-pick`
	图3中可以看出包括功能2在内的所有功能和模块都继续提交,但是提交的分支各不相同,功能2的开发者将
	代码提交到特性分支上,其他开发者还提交到主线上.

@@在什么情况下使用特性分支?
	试验性,探索性的功能开发应该为其建立特性分支,功能复杂,开发周期长(有可能在本次发布中取消)的模块
	应该为建立特性分支.会更改软件提交架构,破坏软件集成,或者容易导致冲突,影响他人开发进度的模块,应该为
	其建立特性分支

	在使用svn等版本控制系统建立分支时,或者因为太慢,或者因为授权原因需要找管理员进行操作,非常的不方便.
	Git的分支管理就方便多了,一室开发这可以在本地版本库中随心所欲地创建分支,而是管理员可以对共享版本库
	进行设置允许开发者创建特定名称的分支,这样开发者的本地分支可以推送到服务器实现数据的备份.


@@卖主分支
	有的项目需要引用第三方的代码模块并且需要对其进行定制,有的项目甚至整个就是基于某个开源项目进行的
	定制,如何有效地管理本地定制和第三方(上游)代码的变更就成为了一个难题,卖主分支(Vendor Branch)
	可以部分解决这个难题.

	所谓卖主分支,就是在版本库中创建一个专门和上游代码进行同步的分支,一旦有上游代码发布就捡入到
	卖主分支中.下图是一个典型的卖主分支工作流程.
	@@import doc/img/git-branch-line-86.jpg

	>在主线上捡入上游软件版本1.0的代码,在图中标记为v1.0的提交即是.
	>然后在主线上进行定制开发,c1,c2分别代表历次定制提交.
	>当上游有了新版本发布后,例如2.0版本,就将上游新版本的源代码提交到卖主分支中.图中标记v2.0的提交即是.
	>然后在主线上合并卖主分支上的新提交,合并后的提交显示为M1.
	>如果定制较少,使用卖主分支可以工作的很好,但是如果定制的内容非常多,在合并分支的时候就是遇到非常多的
	冲突.定制代码越多,混杂的越厉害,冲突的解决越困难.
	>本章的内容尚不能针对复杂的定制开发给我满意的版本控制解决方案.'Topgit协同模型'会介绍一个针对复杂定制
	开发的更好的解决方法.


@分支命令概述

	<1>: git branch
	<2>: git branch <branchname>
	<3>: git branch <branchname> <start-point>
	<4>: git branch -d <branchname>
	<5>: git branch -D <branchname>
	<6>: git branch -m <oldbranch> <newbranch>
	<7>: git branch -M <oldbranch> <newbranch>

	说明:@<1>用于显示本地分支列表,当前分支在输出中会显示为特别的演示,并用星号'*'标识出来.
		 <2>和<3>用于创建分支,用法<2>基于当前头指针(HEAD)指向的提交创建分支,新分支的
		 @分支名为<branchname>.用法<3>基于提交<start-point>创建新分支,新分支的分支名为<branchname>
		 @<4>和<5>用于删除分支.<4>在删除分支<branchname>时会检查所要删除的分支是否已经合并到其他
		 分支中,否则拒绝删除.用法<5>会强制删除分支<branchname>,即使该分支没有合并到任何一个分支.
		 @<6>和<7>用于重命名分支,如果版本库中已经存在名为<newbranch>的分支,用法<6>拒绝执行重命名,
		 而用法<7>会强制执行.

@@Hello-world的开发计划.

	(1)为hello-world创建里程碑v1.0
		$ cd /path/to/user1/workspace/hello-world/
		$ git tag -m 'Release 1.0' v1.0
	(2)将新建的里程碑推送到远程共享版本库库.
		$ git push origin refs/tags/v1.0
		>>Counting objects: 1, done.
		>>Writing objects: 100% (1/1), 154 bytes | 0 bytes/s, done.
		>>Total 1 (delta 0), reused 0 (delta 0)
		>>To file:///root/source/gitrepo/hello-world.git
		>> * [new tag]         v1.0 -> v1.0
	(3)在user1的工作区运行程序.
		<1>.进入src目录,编译程序.
			$ cd src
			$ make
			>>version.h.in => version.h
			>>cc    -c -o main.o main.c
			>>cc -o hello main.o
		<2>.使用参数--help运行hello程序,可以查看帮助信息
			说明:hello程序的帮助输出中有一个拼写错误,本应该是--help的地方写成了-help,这是有意之为.
			$ ./hello --help
			>>Copyright Jiang Xin <jiangxin AT ossxp DOT com>, 2009.

			>>Usage:
			>>    hello
			>>            say hello to the world.

			>>    hello <username>
			>>            say hi to the user.

			>>    hello -h, -help
			>>            this help screen.
		<3>.不带参数,向全世界问候.
			说明,最后一行显示版本为'v1.0',这显然是来自于新建立里程碑'v1.0'
			$ ./hello
			>>Hello world.
			>>(version: v1.0)
		<4>.执行命令的时候,后面添加用户名作为参数,则向该用户问候.
			说明:下面在运行hello的时候,显然出现了一个Bug,即用户名中间如果出现了空格,输出的欢迎信息
				 只包含了部分的用户名.这个Bug也是有意之为.
				$ ./hello Jiang Xin
				>> Hi, Jiang.
				>>(version: v1.0)
			既然1.0版本已经发布了,现在是时候制订下一个版本2.0的开发计划.计划如下:
			(1)多语言支持.
				为hello-world添加多语种支持,使得软件运行的时候能够使用中午或其他本地化语音进行问候.
			(2)用getopt进行命令解析.
				对命令行参数解析框架进行改造,以便更灵活,更易扩展的命令行处理,在1.0版本中,程序内容
				解析命令行参数使用了简单的字符串比较,非常不灵活,从源文件`src/main.c`中可以看到.
				$ git grep -n argv
				>>main.c:20:main(int argc, char **argv)
				>>main.c:24:    } else if ( strcmp(argv[1],"-h") == 0 ||
				>>main.c:25:                strcmp(argv[1],"--help") == 0 ) {
				>>main.c:28:        printf ("Hi, %s.\n", argv[1]);

			最终决定由开发者user2负责多语种支持的功能,有开发者user1负责用getopt进行命令行解析的功能.

@@基于特性分支的开发.


@@创建分支user1/getopt
	开发者user1负责用getopt进行命令行解析功能,因为这个功能用到getopt函数,于是将这个分支命名为`user1/getopt`
	开发者user1使用`git branch`命令创建该特性分支.
	(1):确保是在开发者user1的工作区.
	$cd /path/to/user1/workspace/hello-world/
	(2):开发者user1基于当前HEAD创建分支user1/getopt
	$ git branch user1/getopt
	(3)使用git branch穿件分支,并不会自动切换,查看当前分支仍然工作在master中.
	$ git branch
	>>* master
    >>user1/getopt
    (4)执行`git checkout`命令切换到新分支上去.
    $ git checkout usre1/getopt
    >>Switched to branch 'user1/getopt'
    $ git branch
    >>  master
	>>* user1/getopt

	@@分支实际上是创建在目录`.git/refs/heads`下的引用,版本库初始时创建的master分支就是在该目录下.
		<1>.查看一下`.git/refs/heads`目录下的引用,可以在该目录下看到master文件和一个user1的目录.
			而在user1目录下是文件getopt
			$ls -F .git/refs/heads/
			>>master  user1/
			$ls -F .git/refs/heads/user1/
			>>getopt
		<2>.引用文件`.git/refs/heads/user1/getopt`记录的是一个提交ID.
			$ cat .git/refs/heads/user1/getopt
			>>658eebd9a6583015434a8fdcd29b471e57acc62e
		<3>.因为分支`user1/getopt`是基于HEAD创建的,因此当前该分支和master分支的指向是一致的.
			$ cat .git/refs/heads/master
			>>658eebd9a6583015434a8fdcd29b471e57acc62e


@@创建分支user2/i18n
	开发者user2要完成多语种支持的工作任务,于是决定将分支定名为`user2/i18n`每一次创建分支通常
	都需要完成以下两个工作.
	1.创建分支,执行`git branch <branchname>`命令创建分支.
	2.切换分支,执行`git checkout <branchname>`命令切换分支.
	`git checkout -b <new_branch> [<start-point>]` //可以将上述两条命令所执行的操作一次性完成.

	@@开发者2的操作过程.
	(1)进入到开发者user2的工作目录,并和上游同步一次.
	$ cd /path/to/user2/workspace/hello-world/
	$ git pull
	>>remote: Counting objects: 1, done.
	>>remote: Total 1 (delta 0), reused 0 (delta 0)
	>>Unpacking objects: 100% (1/1), done.
	>>From file:///root/source/gitrepo/hello-world
	>> * [new tag]         v1.0       -> v1.0
	>>Already up-to-date.
	(2)执行`git checkout -b`命令,创建并切换到新分支`user2/i18n`上.
	$ git checkout -b user2/i18n
	>>Switched to a new branch 'user2/i18n'
	$ git branch
	>>  master
	>>* user2/i18n


@@开发者user1完成功能开发.
	说明:您可以试着更改,不过在hello-world中已经保存了一份改好的代码,可以直接检出.
	(1):确保是在开发者user1的工作区.
	$cd /path/to/user1/workspace/hello-world/
	(2):执行下面的命令,用里程碑jx/v2.0标记的内容(以实现getopt进行命令行解析的功能)替换暂存区和工作区.
	下面的'git checkout'命令的最后一个是'.',因此检出只更改了暂存区和工作区.而没有更改头指针.
	$ git checkout jx/v2.0 -- .
	(3):查看状态,会看到分支仍保持为`user1/getopt`,当文件`src/main.o`被修改了.
	$ git status
	>>On branch user1/getopt
	>>Changes to be committed:
	>>  (use "git reset HEAD <file>..." to unstage)

	>>	modified:   src/Makefile
	>>	modified:   src/main.c
	(4):比较暂存区和HEAD文件的差异,可以看到为实现用getopt进行命令行解析功能而对代码的改动.
	$ git diff --cached
	(5)开发者user1提交代码
	$ git commit -m 'Refactor: user getopt_long for arguments parsing.'
	>>[user1/getopt 80a7591] Refactor: user getopt_long for arguments parsing.
 	>>2 files changed, 38 insertions(+), 6 deletions(-)
