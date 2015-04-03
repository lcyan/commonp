@@Topgit协同模型

@@作者版本控制系统的三个里程碑

	(1)@Subversion和卖主分支
	从2005年开始我专心于开源软件的研究,定制开发和整合,在之后的这几年,
	一直使用subversion做版本控制,对于定制开发的工作,subversion有一种
	称为卖主分支(vendor Branch)的模式.
	@import doc/img/git-topgit-line-9.jpg

	#由左至右,提交随着时间而递增.
	#主线trunk用于对定制开发的过程进行跟踪.
	#主线的第一个提交v1.0是导入上游(该开源软件的官方版本库)发布的版本.
	#之后在v1.0提交之处建立分支,是为卖主分支(vendor branch).
	#主线上依次进行了c1,c2两次提交,是基于v1.0进行的定制开发.
	#上游有了新版本,提交到卖主分支上,即v2.0提交,和v1.0相比除了大量的文件更改外,
	 还可能有文件增加和删除.
	#然后在主线上执行从卖主分支到主线的合并,即提交M1.因为此时主线上的改动相对
	 较少.合并v2.0并不太费事.
	#主线继续开发.可能同时有针对不同需求的定制开发.在主线上会有越来越多的提交.
	#如果在卖主分支上导入上游的新版本v3.0,合并将非常痛苦,因为主线上针对不同
	 需求的定制开发已经混杂在一起了.

	@实践证明,Subversion的卖主分支对于大规模的定制开发非常不适合.向上游新版本的
	 迁移会随着定制功能和提交的增大变得越来越困难.

	(2)@HG和MQ
	在2008年,我们的版本库迁移到Mercurial(水银,又称为Hg),并工作在'Hg+MQ'模式下,
	我自以为找到了定制开发版本控制系统的终极解决方案,那时我们已被Subversion的
	卖主分支折磨的太久了.
	Hg和Git一样也是一种分布式版本控制系统,MQ是Hg的一个扩展,可以实现提交和补丁
	两种模式之间的切换.Hg版本库上的提交可以通过`hg qimport`命令转化为补丁列表,
	也可以通过`hg qpush, hg qpop`等命令在补丁列表上游移(出栈和入栈).入栈的补丁
	转化为Hg版本库的提交.补丁出栈会从Hg版本库移走最新的提交.

	@相比Subversion的卖主分支,使用`Hg+MQ`的好处在于:
	#针对不同需求的定制开发,其提交被限定在各自独立的补丁文件中而不是混杂在一起.
	#针对同一个需求的定制开发,无论多少次的更改都体现为补丁文件的变化,而补丁文件
	 本身也是被版本控制的.
	#各个补丁之间是顺序依赖关系,形成一个Quilt格式的补丁列表.
	#向上游新版本迁移过程的工作量降低了,除了因为针对各个功能的定制开发被隔离,
	 还有迁移过程也非常具有可操作性.
	#将定制开发迁移至上游新版本的过程是:先将所有补丁出栈,在将上游新版本提交到
	 主线,然后依次将补丁入栈,如果上游新版本的代码改动较大,补丁入栈可能会遇到
	 冲突,在冲突解决完毕后执行`hg gref`命令即可完成定制开发到新的上游版本的迁移.
	 但是如果需要在定制开发上进行多人协作,'Hg+MQ'的弊病就显现了.因为'Hg+MQ'工作
	 模式下,定制开发的成果是一个补丁库,在补丁库上进行协作的难度非常大,当发生
	 冲突的时候,补丁文件本身的冲突会让人眼花缭乱,这就引发了我们第三次版本控制
	 系统大迁移.

	(3)@Git和Topgit
	Topgit是'Topic Git'的简写,使用shell脚本语言开发的辅助工具,对Git功能进行了
	扩展,用于管理特性分支的开发,Topgit为特性分支引入了基准分支的概念,并以此管理
	特性分支间的依赖,让特性分支向上游新版本的迁移变得非常简单.

	@Topgit的主要特点有:

		#上游的原始代码位于开发主线(master分支),而每一个定制开发都对应于一条
		 git特性分支(refs/heads/t/feature_name)

		#特性分支之间的依赖关系不像'Hg+MQ'那样逐一依赖的,而是可以任意设定分支
		 之间的依赖:多重依赖,平行依赖等.

		#特性分支和其他依赖的分支可以导出为Quilt格式的补丁列表.

		#因为针对某一需求的定制开发限定在特定的特性分支中,可以多人协同参与,
		 和正常的Git开发别无二致.

@@Topgit原理

	@下图是一个近似的Topgit实现图(略去了重要的top-base)分支.
	@import doc/img/git-topgit-line-72.jpg
	上图中,主线上的v1.0是上游版本的一次提交.特性分支A和特性分支C都直接依赖
	主线master,而特性分支B则依赖特性分支A.提交M1是特性分支B因为特性分支A更新
	而做的一次迁移.提交M2和M4则分别是特性分支A和C因为上游出现了新版本v2.0而
	做的迁移,当然特性分支B也要做相应的迁移,是为M3.

	上述的图示非常粗糙,因为如果按照这样的设计很难将特性分支导出为补丁文件,
	例如特性分支B导出为补丁,实际上应该是M2和M3之间的差异.而绝不是a2和M3之间
	的差异.Topgit为了能够实现将分支导出为补丁,又为每个特性的开发引入了一个
	特殊的引用(refs/top-bases/\*),用于追踪特性分支的'变基',称为特性分支的基准
	分支.所有特性分支的基准分支也形成了复杂的分支关系图.
	@import doc/img/git-topgit-line-83.jpg

	@把图line-72和line-83两张分支图合并,重合点之间的差异就可用于将特性分支
	导出为补丁文件.
	@上面的特性分支B还只是依赖一个分支,如果出现了一个分支依赖多个特性分支的话,
	情况就会更加复杂,也更能体现出这种设计方案的精妙.

	@Topgit还在每个特性分支工作区的根目录引入两个文件,用于记录分支的依赖及关于此分支的说明:

	#文件.topdeps记录该分支所依赖的分支列表.
	 该文件通过`tg create`命令在创建特性分支时自动创建,或者通过`tg depend add`命令来添加新依赖.

	#文件.topmsg记录了该分支的描述信息,该文件通过`tg create`命令在创建特性分支时创建,可以手动编辑.

@@Linux下安装Topgit

$ git clone git://repo.or.cz/topgit.git

$ cd topgit

$ make prefix=/usr/local

$ make prefix=/usr/local install

安装改进版的Topgit.需要预先安装Quilt补丁管理工具.
$ yum install -y quilt

$ git clone git://github.com/ossxp-com/topgit.git

$ cd topgit

$ QUILT_PATCHES=debian/patches quilt push -a

>> Applying patch debian/patches/t/debian_locations.diff
>> patching file Makefile
>> patching file tg.sh
>>
>> Applying patch debian/patches/t/delete-remote-branch.diff
>> patching file tg-delete.sh
>>
>> Applying patch debian/patches/t/export_quilt_all.diff
>> patching file README
>> patching file tg-export.sh
>>
>> Applying patch debian/patches/t/fast_tg_summary.diff
>> patching file tg-summary.sh
>>
>> Applying patch debian/patches/t/git-merge-no-edit.diff
>> patching file tg.sh
>>
>> Applying patch debian/patches/t/graphviz_layout.diff
>> patching file tg-summary.sh
>>
>> Applying patch debian/patches/t/mac_os_x.diff
>> patching file tg-info.sh
>> patching file tg-patch.sh
>> patching file tg.sh
>> Hunk #1 succeeded at 434 with fuzz 1 (offset 2 lines).
>>
>> Applying patch debian/patches/t/prune-stale-remote-branch.diff
>> patching file tg-remote.sh
>>
>> Applying patch debian/patches/t/tg_completion_bugfix.diff
>> patching file contrib/tg-completion.bash
>>
>> Applying patch debian/patches/t/tg_graph_ascii_output.diff
>> patching file .gitignore
>> patching file Makefile
>> patching file contrib/tg-completion.bash
>> patching file share/graph.gvpr
>> patching file tg-graph.sh
>> patching file tg-summary.sh
>> Hunk #3 succeeded at 80 (offset 11 lines).
>> Hunk #4 succeeded at 141 (offset 11 lines).
>>
>> Applying patch debian/patches/t/tg_push_all.diff
>> patching file tg-push.sh
>>
>> Now at patch debian/patches/t/tg_push_all.diff

$ make prefix=/usr/local

$ make prefix=/usr/local install

@@注意如果克隆Topgit版本库后工作区文件的换行符是DOS格式换行符(CRLF),在安装过程
  中会遇到麻烦.克隆改进的Topgit则不会出现类似问题,这是因为在工作区根目录下存在
  一个`.gitattributes`文件,可以保证检出的工作区文件采用Unix格式的换行符(LF)

  @在Cygwin下安装改进后的Topgit使用方法如下:
  $ git clone git://github.com/ossxp-com/topgit.git
  $ cd topgit
  $ QUILT_PATCHES=debian/patches quilt push -a
  $ make prefix=/usr/local
  $ make prefix=/usr/local install

@@Topgit的使用

	@通过前面的原理部分,可以发现Topgit为管理特性分支引入的配置文件和基准分支都是和Git兼容的.

	#在refs/top-bases/命名空间下的引用,用于记录特性分支的基准分支.
	#在特性分支的工作区根目录下引入两个文件`.topdeps`和`.topmsg`,用于记录分支的依赖和说明.
	#引入新的钩子脚本`hooks/pre-commit`用于在提交时检查分支依赖有没有发生循环等.

	@Topgit的命令行的一般格式为:

	$ tg [global_option] <subcmd> [command_options...] [arguments...]

	Note:在子命令前的全局选项,目前可用的只有`-r <remote>`用于设定远程版本库,默认为origin.
		 在子命令后可以跟子命令相关的参数.

	<1>. tg help
		@`tg help`命令显示帮助信息,在`tg help`后面提供子命令名称,可以获得该子命令详细的帮助信息.
	<2>. tg create
		@`tg create`命令用于创建新的特性分支.用法如下:
		$ tg [...] create NAME [DEPS... | -r RNAME]
		其中:
		#NAME是新的特性分支的分支名,必须提供.一般约定俗成:NAME以`t/`前缀开头的分支表明此分支是一个Topgit特性分支.
		#DEPS...是可选的一个或多个依赖分支名.如果不提供依赖分支名.则使用当前分支作为新的特性分支的依赖分支.
		# -r RNAME选项,将远程分支作为依赖分支,不常用.
		@`tg create`命令会创建新的特性分支`refs/heads/NAME`,以及特性分支的基准分支`refs/top-bases/NAME`,并且
		 在项目的根目录下创建文件`.topdeps`和`.topmsg`还会提示用户编辑`.topmsg`文件,输入详细的特性分支的描述信息.
		为了试验Topgit命令,找一个示例版本库或干脆创建一个版本库.在示例版本库的master分支下输入如下命令创建一个
		名为`t/featurel`的特性分支:
		$ mkdir tgtest
		$ cd tgtest
		$ git init
		$ vim README
		$ git add -A
		$ git commit -s -m 'first initial.'
		$ tg create t/feature1
		>>tg: Automatically marking dependency on master
		>>tg: Creating t/feature1 base from master...
		>>Switched to a new branch 't/feature1'
		>>tg: Topic branch t/feature1 set up. Please fill .topmsg now and make initial commit.
		>>tg: To abort: git rm -f .top* && git checkout master && tg delete t/feature1

		@提示信息以`tg:`开头的是Topgit产生的说明.其中提示用户编辑`.topmsg`文件,然后执行一次提交操作完成Topgit特性分支的创建.

		@如果想撤销此次操作,删除项目根目录下`.top*`文件,切换到master分支,然后执行`tg delete t/feature1`命令删除`t/feature1`
		 分支及特性分支的基准分支`refs/top-bases/t/feature1`

		@输入`git status`可以看到当前已经切换到`t/feature1`分支,并且Topgit已经创建了`.topdeps`和`.topmsg`文件,并已将这两个
		 文件加入到暂存区.
		$ git status
		>>On branch t/feature1
		>>Changes to be committed:
		>>  (use "git reset HEAD <file>..." to unstage)

		>>	new file:   .topdeps
		>>	new file:   .topmsg
		$ cat .topdeps
		>> master

		@打开`.topmsg`文件,会看到下面的内容(前面增加了行号):
		$ vim .topmsg
		>>  1 From: cherry <m290236573@gmail.com>
		>>  2 Subject: [PATCH] t/feature1
		>>  3
		>>  4 <patch description>
		>>  5
		>>  6 Signed-off-by: cherry <m290236573@gmail.com>

		@其中第二行是关于该特性分支的简短描述.第4行是详细描述,可以写多行.编辑完成,别忘了提交,提交之后才完成Topgit分支的创建.
		$ git add -u
		$ git commit -m 'create tg branch t/feature1'

		@如果这时想创建一个新的特性分支`t/feature2`,并且也要依赖master,注意需要在命令行中提供master作为第二个参数,以设定
		 依赖分支.因为当前所处的分支为`t/feature1`,如果不提供指定的依赖分支就会自动依赖当前分支.
		$ tg create t/feature2 master
		$ git commit -m 'create tg branch t/feature2'
		@下面的命令将创建`t/feature3`分支,该分支依赖`t/feature1`和`t/feature2`.
		$ tg create t/feature3 t/feature1 t/feature2
		$ git commit -m 'create tg branch t/feature3'

	<3>.tg info

		@`tg info`命令用于显示当前分支或指定的Topgit分支的信息.用法如下:
		$ tg [...] info [NAME]

		@其中NAME是可选的Topgit分支名,例如执行下面的命令会显示`t/feature3`的信息.
		$ tg info
		>>Topic Branch: t/feature3 (1/1 commit)
		>>Subject: [PATCH] t/feature3
		>>Base: 0bdd8e5
		>>Depends: t/feature1
		>>t/feature2
		>>Up-to-date.
		@切换到`t/feature1`分支,做一些修改并提交.
		$ git checkout t/feature1
		$ echo Hi > hacks.txt
		$ git add hackes.txt
		$ git commit -m 'hacks in t/feature1.'
		@然后在来看分支`t/feature3`的状态.
		$ tg info t/feature3
		>>Topic Branch: t/feature3 (1/1 commit)
		>>Subject: [PATCH] t/feature3
		>>Base: 0bdd8e5
		>>Depends: t/feature1
		>>         t/feature2
		>>Needs update from:
		>>	t/feature1 (1/1 commit)
		@状态信息显示`t/feature3`不再是最新的状态(Up-to-date)因为依赖的`t/feature1`分支
		 包含新的提交,而需要从`t/feature1`获取更新.

	<4>. tg update

		@tg update命令用于更新分支,即从依赖的分支获得最新的提交合并到当前分支,同时在
		 `refs/top-bases/`命名空间下的特性分支的基准分支也会更新.
		$ tg [...] update [NAME]
		@其中NAME是可选的Topgit分支名.下面就对需要更新的`t/feature3`分支执行`tg update`.
		$ git checkout t/feature3
		$ tg update
		>>tg: Updating base with t/feature1 changes...
		>>Merge made by the 'recursive' strategy.
		>> hacks.txt | 1 +
		>> 1 file changed, 1 insertion(+)
		>> create mode 100644 hacks.txt
		>>tg: Updating t/feature3 against new base...
		>>Merge made by the 'recursive' strategy.
		>> hacks.txt | 1 +
		>> 1 file changed, 1 insertion(+)
		>> create mode 100644 hacks.txt
		@从上面的输出信息可以看出执行了两次分支的合并操作.一次是针对`refs/top-bases/t/feature3`
		 引用指向的特性分支的基准分支,一次是针对的是`refs/heads/t/feature3`特性分支.
		@执行`tg update`命令因为要涉及到分支的合并,因此并非每次都会成功,例如在`t/feature3`和
		 `t/feature1`中同时对一个文件(如hacks.txt)进行修改.然后在`t/feature3`中再次执行`tg update`
		 可能就会报错,进入冲突解决的状态.
		$ git checkout t/feature1
		$ vim hacks.txt
		$ git add -u
		$ git commit -m 'upate hackes.txt file.'
		$ git checkout t/feature3
		$ vim hacks.txt
		$ git add -u
		# git commit -m 'update hackes.txt file.'
		$ tg info t/feature3
		>>Topic Branch: t/feature3 (2/1 commits)
		>>Subject: [PATCH] t/feature3
		>>Base: e05bc7b
		>>Depends: t/feature1
		>>         t/feature2
		>>Needs update from:
		>>	t/feature1 (1/1 commit)
		$ tg update
		>>tg: Updating base with t/feature1 changes...
		>>Merge made by the 'recursive' strategy.
		>>hacks.txt | 1 +
		>>1 file changed, 1 insertion(+)
		>>tg: Updating t/feature3 against new base...
		>>Auto-merging hacks.txt
		>>CONFLICT (content): Merge conflict in hacks.txt
		>>Automatic merge failed; fix conflicts and then commit the result.
		>>tg: Please commit merge resolution. No need to do anything else
		>>tg: You can abort this operation using `git reset --hard` now
		>>tg: and retry this merge later using `tg update`.
		@执行`tg info`命令查看一下当前分支`t/feature3`的状态
		$ tg info
		>>Topic Branch: t/feature3 (3/2 commits)
		>>Subject: [PATCH] t/feature3
		>>Base: 394228b
		>>* Base is newer than head! Please run `tg update`.
		>>Depends: t/feature1
		>>         t/feature2
		>>Up-to-date.
		从上面`tg info`命令的输出可以看出当前分支的状态是'Up to date',但是输出
		包含了一个提示:分支的基(Base)要比头(HEAD)新,请执行`tg update`.
		这是如果执行`tg summary`命令的话,可以看到`t/feature3`处于'B(Break)'状态.
		$ tg summary
		>>			t/feature1                     	[PATCH] t/feature1
		>> 0     	t/feature2                     	[PATCH] t/feature2
		>>>     B	t/feature3                     	[PATCH] t/feature3
		@执行`git status`命令,可以看出因为两个分支同时修改了文件'hacks.txt'而导致冲突.
		$ git status
		>>On branch t/feature3
		>>You have unmerged paths.
		>>  (fix conflicts and run "git commit")

		>>Unmerged paths:
		>>  (use "git add <file>..." to mark resolution)

		>>	both modified:   hacks.txt

		>>no changes added to commit (use "git add" and/or "git commit -a")
		可以变基hacks.txt文件,或者调用冲突解决工具解决冲突,之后在提交,这才真正
		完成了此次`tg update`
		@编辑hacks.txt解决冲突.
		$ git mergetool
		$ git commit -m 'resolved conflict with t/feature1.'
		$ tg info
		>>Topic Branch: t/feature3 (4/2 commits)
		>>Subject: [PATCH] t/feature3
		>>Base: 394228b
		>>Depends: t/feature1
		>>         t/feature2
		>>Up-to-date.
	<5>.tg summary
		@tg summary命令用于显示Topgit管理的特性分支的列表及各个分支的状态.用法如下:
		$ tg [...] summary [-t | --sort | --deps | --graphviz]
		@不带任何参数执行`git summary`是最常用的Topgit命令.
		`-t` //显示特性分支列表.
		$ git summary -t
		>>t/feature1
		>>t/feature2
		>>t/feature3
		`--deps`参数除了显示Topgit特性分支外,还是显示特性分支的依赖分支.
		$ tg summary --deps
		>>t/feature1 master
		>>t/feature2 master
		>>t/feature3 t/feature1
		>>t/feature3 t/feature2
		`--sort`参数按照分支依赖的顺序显示分支列表,除了显示Topic分支外,还会显示依赖的非Topgit分支.
		$ tg summary --sort
		>>t/feature3
		>>t/feature2
		>>t/feature1
		>>master
		`--graphviz`会输出GraphViz格式文件,可以用于显示特性分支之间的关系.
		$ tg summary --graphviz | dot -T png -o topgit.png //yum install -y graphviz
		@import doc/img/git-topgit-line-357.png

		不带任何参数执行`tg summary`显示分支列表及状态.也是最常用的Topgit命令之一.
		$ tg summary
		       	t/feature1                     	[PATCH] t/feature1
		 0     	t/feature2                     	[PATCH] t/feature2
		>     	t/feature3                     	[PATCH] t/feature3
		@标识'>':(t/feature3分支之前的大于号),用于标记当前所处的特性分支.
		@标识'0':(t/feature2分支前的数字0)含义是该分支中没有的提交,这是一个建立后尚未使用的废弃的分支.
		@标记'D'表明该分支处于过时(out-of-date)状态,可能是一个或多个依赖的分支包含了新的提交,尚未合并到
		 此特性分支.可以用`tg info`命令看出到底是由那个依赖分支的改动导致该特性分支处于过时状态.
		@标记'B':之前演示中出现过,表明该分支处于Break状态,即可能由于冲突未解决或其他原因导致该特性分支的
		 基准分支(base)相对该分支的头(head)不匹配,例如`refs/top-bases`下的特性分支的基准分支迁走了,但是
		 特性分支未完成迁移.
		@标记'!':表明该分支所依赖的分支不存在.
		@标记'l':表明该特性分支只存在于本地,不存在于远程跟踪服务器.
		@标记'r':表明该特性分支既存在于本地,又存在与远程跟踪服务器,并且两者匹配.
		@标记'L':表明该特性分支,本地比远程跟踪服务器的新.
		@如果没有传销'l/r/L/R':表明该版本库尚未设置远程版本库.
		@一般带有标记'r'的是最常见的,也是最正常的.

@@下面要介绍`tg remote`命令为测试版本库建立一个对应的远程版本库,然后就能在`tg srmmary`的输出中看到'l/r/L/R'等标识符了.

	<6>:tg remote

		`tg remote`命令用于为远程版本库增加Topgit的相关设置,以便在和该远程版本库进行`git fetch`,`git pull`等操作时
		能够同步Topgit的相关分支,命令用法如下:

		$ tg [...] remote [--populate] [REMOTE]

		@其中REMOTE为远程版本库的名称,默认为origin.执行`tg remote`命令会自动在版本的配置文件增加`refs/top-bases`下引用
		 同步表达式,下面的示例中最后一行就是执行`tg remote origin`后增加的设置.

		 	[remote 'origin']
		 		url = /path/to/repos/tgtest.git
		 		fetch = +refs/heads/*:refs/remotes/origin/*
		 		fetch = +refs/top-bases/*:refs/remotes/origin/top-bases/*
		@如果使用--populate参数,除了会向上面那么设置默认的Topgit远程版本库外,还会自动执行`git fetch`命令,然后在本地
		建立Topgit特性分支和对应的基准分支.

		*/

		@当执行tg命令时,如果不用`-r remote`全局参数,则默认使用`origin`远程版本库.

		下面为前面测试的Topgit版本库设置一个远程版本库,具体操作如下:

		(1):先建立一个裸版本库tgtest.git

		$ git init --bare /path/to/repos/tgtest.git
		>>Initialized empty Git repository in /root/source/gitrepo/tgtest.git/

		(2):然后执行`git remote`,将刚刚创建的版本库以origin为名注册为远程版本库.

		$ git remote add origin /path/to/repos/tgtest.git

		(3):执行`git push`将当前版本库的master分支推送到刚刚创建好的远程版本库.

		$ git push origin master

		>>Counting objects: 3, done.
		>>Writing objects: 100% (3/3), 237 bytes | 0 bytes/s, done.
		>>Total 3 (delta 0), reused 0 (delta 0)
		>>To file:///root/source/gitrepo/tgtest.git/
		>> * [new branch]      master -> master

		(4):之后运行`tg remote`命令为远程版本库添加额外的配置,以便该远程版本库能够跟踪Topgit分支.

		$ tg remote --polulate origin
		>>tg: Remote origin can now follow TopGit topic branches.
		>>tg: Populating local topic branches from remote 'origin'...
		>>tg: The remote 'origin' is now the default source of topic branches.

		(5):当执行上面的`tg remote`命令后,会在当前版本库的`.git/config`文件中添加设置(以加号开头的行):
		>> 9 [remote "origin"]
		>> 10     url = file:///root/source/gitrepo/tgtest.git/
		>> 11     fetch = +refs/heads/*:refs/remotes/origin/*
		>> 12     fetch = +refs/top-bases/*:refs/remotes/origin/top-bases/*
		>> 13 [topgit]
		>> 14     remote = origin
		(6)这时在执行`tg summary`会看到分支前面都有标记`l`,即本地分支提交比远程版本库新.
		>>  l    	t/feature1                     	[PATCH] t/feature1
		>> 0l    	t/feature2                     	[PATCH] t/feature2
		>>> l    	t/feature3                     	[PATCH] t/feature3
		(7)执行`tg push`命令将特性分支`t/feature2`及其基准分支推送到远程版本库.
		$ tg push t/feature2
		>>Counting objects: 4, done.
		>>Compressing objects: 100% (3/3), done.
		>>Writing objects: 100% (4/4), 442 bytes | 0 bytes/s, done.
		>>Total 4 (delta 0), reused 0 (delta 0)
		>>To file:///root/source/gitrepo/tgtest.git/
		>> * [new branch]      t/feature2 -> t/feature2
		>> * [new branch]      refs/top-bases/t/feature2 -> refs/top-bases/t/feature2
		(8)再来看看`tg summary`,会看到`t/feature2`的标识变为`r`,即远程和本地同步.
		>>  l    	t/feature1                     	[PATCH] t/feature1
		>> 0r    	t/feature2                     	[PATCH] t/feature2
		>>>0l    	t/feature3                     	[PATCH] t/feature3
		(9)运行`tg push --all`会将所有的Topgit分支推送到远程版本库,之后再来看`tg summary`的输出.
			会看到所有分支都带上了`r`的标识.
		$ tg push --all
		>>Counting objects: 9, done.
		>>Compressing objects: 100% (7/7), done.
		>>Writing objects: 100% (9/9), 839 bytes | 0 bytes/s, done.
		>>Total 9 (delta 3), reused 0 (delta 0)
		>>To file:///root/source/gitrepo/tgtest.git/
		>> * [new branch]      t/feature1 -> t/feature1
		>> * [new branch]      refs/top-bases/t/feature1 -> refs/top-bases/t/feature1
		>>Everything up-to-date
		>>Counting objects: 16, done.
		>>Compressing objects: 100% (13/13), done.
		>>Writing objects: 100% (16/16), 1.79 KiB | 0 bytes/s, done.
		>>Total 16 (delta 5), reused 0 (delta 0)
		>>To file:///root/source/gitrepo/tgtest.git/
		>> * [new branch]      t/feature3 -> t/feature3
		>> * [new branch]      refs/top-bases/t/feature3 -> refs/top-bases/t/feature3
		$ tg summary
		>>  r    	t/feature1                     	[PATCH] t/feature1
		>> 0r    	t/feature2                     	[PATCH] t/feature2
		>>>0r    	t/feature3                     	[PATCH] t/feature3

		@如果版本库设置了多个远程版本库,要针对每一个远程版本库执行`tg remote <REMOTE>`,但只有
		 一个远程的源用`--populate`参数调用`tg remote`将其设置为默认的远程版本库.

		 */
	<7>:tg push

		@在前面`tg remote`的介绍中,已经看到过`tg push`命令,`tg push`命令用于将Topgit特性分支
		 及对应的基准分支推送到远程版本库.用法如下:

		$ tg [...] push [--dry-run] [--no-deps] [--tgish-only] [--all|branch*]

		'tg push'命令后面的参数指定要推送给远程服务器的分支列表,如果省略了则推送当前分支.
		改进的`tg push`命令支持通过`--all`参数将所有'Topgit'特性分支推送到远程版本库.

		参数'--dry-run'用于测试推送的执行效果,而不是真正的执行.
		参数'--no-deps'的含义是不推送依赖的分支,默认推送.
		参数'--tgish-noly'的含义是只推送Topgit特性分支,默认推送指定的所有分支.

	<8>:tg depend

		'tg depend'命令目前仅实现了为当前的Topgit特性分支增加了新的依赖,用法如下:
		$ tg [...] depend add NAME
		将NAME加入到文件`.topdeps`中,并将NAME分支向该特性分支及特性分支的基准分支进行合并
		操作.虽然Topgit可以检查到分支的循环依赖,但还是要注意合理地设置分支的依赖.合并重复
		的依赖.

	<9>:tg base

		'tg base'命令用于显示特性分支的基准分支的提交ID(精简格式)

	<10>:tg delete

		'tg delete'命令用于删除Topgit特性分支及其对应的基准分支.用法如下:

		$ tg [...] delete [-f] NAME

		默认只删除没有改动的分支,即标记为'0'的分支,除非使用'-f'参数.
		目前此命令尚不能自动清除其分支中对删除分支的依赖.还需要手工调整'.topdeps'文件,
		删除对不存在的分支的依赖.

	<11>: tg patch

		'tg patch'命令通过比较特性分支及其基准分支的差异,显示该特性分支的补丁,用法如下:

		$ tg [...] patch [-i | -w] [NAME]

		其中'-i'参数显示暂存区和基准分支的差异.'-w'参数显示工作区和基准分支的差异.

		'tg patch'命令存在的一个问题是只能正确显示工作区中的根执行.这个缺陷已经在我的改进的Topgit
		中被改正.

	<12>:tg export

		'tg export'命令用于导出特性分支及其依赖,便于向上游贡献.可以导出Quilt格式的补丁列表.或者顺序
		提交到另外的分支中.用法如下:

		$ tg [...] export ([--collapse] NEWBRANCH | [--all | -b BRANCH1,BRANCH2...] --quilt DIRECTORY | --linearize NEWBRANCH)

		这个命令有三种导出的方法:

		(1):将所有的Topgit特性分支压缩为一个,提交到新的分支.

		$ tg [...] export --collapse NEWBRANCH

		(2):将所有的Topgit特性分支按照顺序提交到一个新的分支中.

		$ tg [...] export --linearize NEWBRANCH

		(3):将指定的Topgit分支(一个或多个)及其依赖分支转换为Quilt格式的斌定.保存到指定的目录中.

		$ tg [...] export -b BRANCH1,BRANCH2... --quilt DIRECTORY

		在导出为Quilt格式补丁的时候,如果将所有的分支导出,
		必须使用'-b'参数将分支全部罗列(或者分支的依赖关系将所有分支囊括),这对于需要导出所有
		分支的操作非常不方便.我改进的Topgit通过'--all'参数实现了所有分支的导出.

	<13>: tg import

		'tg import'命令将分支的提交转换为'Topgit'特性分支,指定范围的每个提交都转换为一个特性分支,
		各个特性分支之间线性依赖.用法如下:

		$ tg [...] import [-d BASE_BRANCH] {[-p PREFIX] range...| -s NAME COMMIT}

		如果不使用'-d'参数,特性分支则以当前分支为依赖.特性分支名称自动生成.使用约定俗成的't/'作为前缀.
		也可以通过'-p'参数指定其他前缀.可以通过'-s'参数设定特性分支的名称.

	<14>: tg log

		'tg log'命令实际上是对'git log'命令的封装.这个命令通过'--no-merges'和'--first-parent'参数调用
		'git log'虽然屏蔽了大量因和依赖分支合并而引入的依赖分支的提交日志,但是同时也屏蔽了合并到该特性
		分支的其他贡献者的提交.

	<15>: tg mail

		'tg mail'调用'git send-mail'发送邮件,参数'-s'用于向该命令传递参数(需要用双引号括起来).邮件中
		的目的地址从补丁文件头中的'To','Cc','Bcc'等字段获取.参数'-r'引用回复邮件的ID以便正确地生成
		'in-reply-to'邮件头.
		Note:此命令可能会发送多封邮件,可以通过如下设置在调用'git send-email'命令发送邮件是进行确认.

		$ git config sendemail.confirm always

	<16>:tg files

		'tg files'命令用于显示当前或指定的特性分支改动了那些文件.

	<17>:tg prev

		'tg prev'命令用于显示当前或指定的特性分支所依赖的分支名.

	<18>:tg next

		'tg next'命令用于显示当前或指定的特性分支被其他那些特性分支所依赖.

	<19>:tg graph

		'tg graph'命令并非官方提供的命令.而是源自一个补丁,实现文本方式的Topgit分支图.当然这个
		文本分支图没有'tg summary --graphviz'生成的那么漂亮.

@@用Topgit方式改造Topgit

	在Topgit的使用中陆续发现了一些不合用的地方,于是便以Topgit特性分支的方式来对Topgit进行改造.

	<1>:Topgit(1):tg push 全部分支.
	<2>:Topgit(2):tg导出全部分支.
	<3>:Topgit(3):更灵活的'tg patch'
	<4>:Topgit(4):tg的命令补齐
	<5>:Topgit(5):'tg summary'执行的更快.

	下面就以Topgit改造过程为例,来介绍如何参与一个Topgit管理下的项目的开发,改造后Topgit版本库
	的地址为:'git://github.com/ossxp-com/topgit.git'

	1.首先克隆该版本库.

	$ git clone git://github.com/ossxp-com/topgit.git
	$ cd topgit
	查看远程分支
	$ git branch -r
	>>origin/HEAD -> origin/master
	>>origin/master
	>>origin/t/debian_locations
	>>origin/t/delete-remote-branch
	>>origin/t/export_quilt_all
	>>origin/t/fast_tg_summary
	>>origin/t/git-merge-no-edit
	>>origin/t/graphviz_layout
	>>origin/t/mac_os_x
	>>origin/t/prune-stale-remote-branch
	>>origin/t/tg_completion_bugfix
	>>origin/t/tg_graph_ascii_output
	>>origin/t/tg_patch_binary
	>>origin/t/tg_patch_cdup
	>>origin/t/tg_push_all
	>>origin/tgmaster

	看到了远程分支中出现了属性的以't/'为前缀的Topgit分支.说明这个版本库是一个用Topgit进行管理
	的版本库.为了能够获取Topgit各个特性分支的基准分支,需要用'tg remote'命令对默认的origin远程
	版本库注册一下.

	$ tg remote --polulate origin
	>>tg: Remote origin can now follow TopGit topic branches.
	>>tg: Populating local topic branches from remote 'origin'...
	>>From git://github.com/ossxp-com/topgit
	>>* [new ref]         refs/top-bases/t/debian_locations -> origin/top-bases/t/debian_locations
	>>* [new ref]         refs/top-bases/t/delete-remote-branch -> origin/top-bases/t/delete-remote-branch
	>>* [new ref]         refs/top-bases/t/export_quilt_all -> origin/top-bases/t/export_quilt_all
	>>* [new ref]         refs/top-bases/t/fast_tg_summary -> origin/top-bases/t/fast_tg_summary
	>>* [new ref]         refs/top-bases/t/git-merge-no-edit -> origin/top-bases/t/git-merge-no-edit
	>>* [new ref]         refs/top-bases/t/graphviz_layout -> origin/top-bases/t/graphviz_layout
	>>* [new ref]         refs/top-bases/t/mac_os_x -> origin/top-bases/t/mac_os_x
	>>* [new ref]         refs/top-bases/t/prune-stale-remote-branch -> origin/top-bases/t/prune-stale-remote-branch
	>>* [new ref]         refs/top-bases/t/tg_completion_bugfix -> origin/top-bases/t/tg_completion_bugfix
	>>* [new ref]         refs/top-bases/t/tg_graph_ascii_output -> origin/top-bases/t/tg_graph_ascii_output
	>>* [new ref]         refs/top-bases/t/tg_patch_binary -> origin/top-bases/t/tg_patch_binary
	>>* [new ref]         refs/top-bases/t/tg_patch_cdup -> origin/top-bases/t/tg_patch_cdup
	>>* [new ref]         refs/top-bases/t/tg_push_all -> origin/top-bases/t/tg_push_all
	>>tg: Adding branch t/debian_locations...
	>>tg: Adding branch t/delete-remote-branch...
	>>tg: Adding branch t/export_quilt_all...
	>>tg: Adding branch t/fast_tg_summary...
	>>tg: Adding branch t/git-merge-no-edit...
	>>tg: Adding branch t/graphviz_layout...
	>>tg: Adding branch t/mac_os_x...
	>>tg: Adding branch t/prune-stale-remote-branch...
	>>tg: Adding branch t/tg_completion_bugfix...
	>>tg: Adding branch t/tg_graph_ascii_output...
	>>tg: Adding branch t/tg_patch_binary...
	>>tg: Adding branch t/tg_patch_cdup...
	>>tg: Adding branch t/tg_push_all...
	>>tg: The remote 'origin' is now the default source of topic branches.

	@@执行'tg summary'查看一下本地Topgit特性分支的状态.

	$ tg summary
	>>  r  ! 	t/debian_locations             	[PATCH] make file locations Debian-compatible
	>>  r  ! 	t/delete-remote-branch         	[PATCH] When delete branch, delete it's remote couterparts
	>>  r  ! 	t/export_quilt_all             	[PATCH] t/export_quilt_all
	>>  r  ! 	t/fast_tg_summary              	[PATCH] t/fast_tg_summary
	>>  r  ! 	t/git-merge-no-edit            	[PATCH] No stop to edit for the new merge behavior of git
	>>  r  ! 	t/graphviz_layout              	[PATCH] t/graphviz_layout
	>>  r  ! 	t/mac_os_x                     	[PATCH] t/mac_os_x
	>>  r  ! 	t/prune-stale-remote-branch    	[PATCH] prune stale remote branch when update
	>>  r  ! 	t/tg_completion_bugfix         	[PATCH] t/tg_completion_bugfix
	>>  r    	t/tg_graph_ascii_output        	[PATCH] t/tg_graph_ascii_output
	>>  r  ! 	t/tg_patch_binary              	[PATCH] Save diff of binary files in patch
	>> 0r  ! 	t/tg_patch_cdup                	[PATCH] t/tg_patch_cdup
	>>  r  ! 	t/tg_push_all                  	[PATCH] t/tg_push_all

	怎么?出现了感叹号?记得前面在介绍'tg summary'命令的章节中提到过,感叹号的出现说明该特性分支所依赖的分支丢失了.
	用'tg info'查看一下其中的某个特性分支.

	$ tg info e/export_quilt_all
	>>Topic Branch: t/export_quilt_all (7/4 commits)
	>>Subject: [PATCH] t/export_quilt_all
	>>Base: d279e29
	>>Remote Mate: origin/t/export_quilt_all
	>>Depends: tgmaster
	>>MISSING: tgmaster
	>>Up-to-date.

	@原来该特性分支依赖'tgmaster'分支,而不是'master'分支.远程存在'tgmaster'分支而本地不存在.于是在本地建立'tgmaster'跟踪分支.

	$ git checkout tgmaster //若版本小于1.6.6 执行 $ git checkout -b tgmaster origin/master

	>>Branch tgmaster set up to track remote branch tgmaster from origin.
	>>Switched to a new branch 'tgmaster'

	@再次执行'tg summary'命令,这次的输出就正常了.

	$ tg summary
	>>  r    	t/debian_locations             	[PATCH] make file locations Debian-compatible
	>>  r    	t/delete-remote-branch         	[PATCH] When delete branch, delete it's remote couterparts
	>>  r    	t/export_quilt_all             	[PATCH] t/export_quilt_all
	>>  r    	t/fast_tg_summary              	[PATCH] t/fast_tg_summary
	>>  r    	t/git-merge-no-edit            	[PATCH] No stop to edit for the new merge behavior of git
	>>  r    	t/graphviz_layout              	[PATCH] t/graphviz_layout
	>>  r    	t/mac_os_x                     	[PATCH] t/mac_os_x
	>>  r    	t/prune-stale-remote-branch    	[PATCH] prune stale remote branch when update
	>>  r    	t/tg_completion_bugfix         	[PATCH] t/tg_completion_bugfix
	>>  r    	t/tg_graph_ascii_output        	[PATCH] t/tg_graph_ascii_output
	>>  r    	t/tg_patch_binary              	[PATCH] Save diff of binary files in patch
	>> 0r    	t/tg_patch_cdup                	[PATCH] t/tg_patch_cdup
	>>  r    	t/tg_push_all                  	[PATCH] t/tg_push_all

	@通过下面的命令创建图形化分支图:

	$ tg summary --graphviz | dot -T png -o topgit.png
	@import doc/img/git-topgit-line-766.png

	@其中:
		1.特性分支't/export_quilt_all'.为'tg export --quilt'命令增加'--all'选项,以便导出所有的特性分支.
		2.特性分支't/fast_tg_summary',主要是改进'tg'命令补齐时分支的显示进度.当特性分支接近上百个时差异非常明显.
		3.特性分支't/graphviz_layout',改进了分支的图形输出格式.
		4.特性分支't/tg_completion_bugfix',解决了命令补齐的一个bug.
		5.特性分支't/tg_graph_ascii_output',源自'Bert Wesarg'的贡献,非常巧妙地实现了文本话分支图显示.展示了'gvpr'命令的强大功能.
		6.特性分支't/tg_patch_cdup',解决了在项目的子目录下无法执行'tg patch'的问题.
		7.特性分支't/tg_push_all',通过为'tg push'增加一个'--all'选项,解决了当'tg'从0.7升级到0.8版后,无法批量向上游推送特性分支的问题.

	@@下面展示一下如何跟踪上游的最新改动,并迁移到新的上游版本.分支'tgmaster'用于跟踪上游的'Topgit'分支.以't/'开头的分支是对'Topgit'
	  改进的特性分支,而'master'分支实际上是导出'Topgit'补丁文件并负责编译特定Linux平台发行包的分支.

	  (1)把官方的Topgit版本库以'upstream'的名称加入作为新的远程版本库.

	  $ git remote add upstream git://repo.or.cz/topgit.git

	  (2)然后将'upstream'远程版本的'master'分支合并到本地的'tgmaster'分支.

	  $ git pull upstream master:tgmaster
	  >>remote: Counting objects: 113, done.
	  >>remote: Compressing objects: 100% (50/50), done.
	  >>remote: Total 94 (delta 60), reused 78 (delta 44)
	  >>Unpacking objects: 100% (94/94), done.
	  >>From git://repo.or.cz/topgit
	  >>d279e29..f2815f4  master     -> tgmaster
	  >>* [new tag]         topgit-0.1 -> topgit-0.1
	  >>* [new tag]         topgit-0.2 -> topgit-0.2
	  >>* [new tag]         topgit-0.3 -> topgit-0.3
	  >>* [new tag]         topgit-0.4 -> topgit-0.4
	  >>* [new tag]         topgit-0.5 -> topgit-0.5
	  >>* [new tag]         topgit-0.6 -> topgit-0.6
	  >>* [new tag]         topgit-0.7 -> topgit-0.7
	  >>* [new tag]         topgit-0.8 -> topgit-0.8
	  >>* [new tag]         topgit-0.9 -> topgit-0.9
	  >>* [new branch]      master     -> upstream/master
	  >>Warning: fetch updated the current branch head.
	  >>Warning: fast-forwarding your working tree from
	  >>Warning: commit d279e292a787fa733746c3d15209c70e5596ab10.
	  >>Already up-to-date.

	  (3)此时在执行'tg summary'会发现所有的Topgit分支都多了一个标记'D',表明因为依赖分支
	  的更新而导致'Topgit'特性分支过时了.
	  $ tg summary
	  >> r D  	t/debian_locations             	[PATCH] make file locations Debian-compatible
	  >> r D  	t/delete-remote-branch         	[PATCH] When delete branch, delete it's remote couterparts
	  >> r D  	t/export_quilt_all             	[PATCH] t/export_quilt_all
	  >> r D  	t/fast_tg_summary              	[PATCH] t/fast_tg_summary
	  >> r D  	t/git-merge-no-edit            	[PATCH] No stop to edit for the new merge behavior of git
	  >> r D  	t/graphviz_layout              	[PATCH] t/graphviz_layout
	  >> r D  	t/mac_os_x                     	[PATCH] t/mac_os_x
	  >> r D  	t/prune-stale-remote-branch    	[PATCH] prune stale remote branch when update
	  >> r D  	t/tg_completion_bugfix         	[PATCH] t/tg_completion_bugfix
	  >> r D  	t/tg_graph_ascii_output        	[PATCH] t/tg_graph_ascii_output
	  >> r D  	t/tg_patch_binary              	[PATCH] Save diff of binary files in patch
	  >>0r D  	t/tg_patch_cdup                	[PATCH] t/tg_patch_cdup
	  >> r D  	t/tg_push_all                  	[PATCH] t/tg_push_all

	  (4)依次对各个分支执行'tg update',完成对更新的依赖分支的合并.
	  $ tg update t/export_quilt_all
	  ...

	  (5)对各个分支完成更新后,会发现'tg summary'的输出中,标识过时的'D'标记变为'L',即本地分支要比远程服务器分支要新.

	  (6)执行'tg push --all'就可以实现将所有的特性分支推送到远程服务器上.

@@Topgit使用中注意事项

	1.经常运行'tg remote --polulate'获取他人创建的特性分支.
	@运行命令'git fetch'或命令'git pull'和远程版本库同步,只能将他人创建的Topgit特性分支在本地以远程分支(refs/remote/origin/t/<branch-name>)
	 的方式保存.而不能自动在本地建立分支.

	@如果确认版本库是使用Topgit维护的话,应该在和远程版本库同步的时候执行'tg remote --populate origin'这条命令会做两件事情.

		1.自动调用'git fetch origin'获取远程'origin'版本库新的提交和引用.
		2.检查'refs/remotes/origin/top-bases/'下的所有引用,如果是新的,在本地(refs/top-bases)尚不存在,Topgit会据此自动在本地创建新的特性分支.

	2.适时的调整特性分支的依赖关系
	@例如之前用于Topgit演示的版本库,各个特性分支的依赖文件内容如下:
	>分支't/feature1'的'.topdeps'文件.
	>master
	>分支t/feature2的.'topdeps'文件
	>master
	>分支t/feature3的.'topdeps'文件
	>t/feature1
	>t/feature2

	@@如果分支't/feature3'的'.topdeps'文件是这样的.可能就会存在问题
	>master
	>t/feature1
	>t/feature2
	@@问题在于t/featue3依赖的其他分支已经依赖了master分支,虽然不会造成致命的影响,但是在特定的情况下这种重复会造成不便.例如在master分支更新后,
	  可以由于代码重构的比较厉害,在特性分支迁移时会造成冲突,如在't/feature1'分支中执行'tg update'会遇到冲突,当辛苦完成冲突解决并提交后,
	  在't/feature3'中执行'tg update'会因为先依赖的是master分支,所以现在master分支上对't/feature3'分支进行合并,这样就肯定会遇到和't/feature1'
	  相同的冲突,还要在重复解决一次.
	  @@如果在.topdeps中删除了对'master'分支的重复依赖,就不会出现上面的重复解决冲突的问题了.
	  @@依赖的顺序不同会造成合并的顺序也不用,同样会产生重复的冲突问题.因此当发现重复的冲突是,可以取消合并操作,修改特性分支'.topdeps'文件,
	    调整文件内容(删除重复分支,调整分支的顺序)并提交,然后在执行'tg update'继续合并操作.

	3.Topgit特性分支的里程碑和分支管理
	@@Topgit本身就是管理特性分支的软件,Topgit某个时刻的开发状态是Topgit管理下的所有分支(包括基准分支)整体的状态.
	@@思考一下:能够用里程碑来标记Topgit管理的版本库的开发状态吗?

	@所有使用里程碑管理是不可能的,因为Git里程碑只能针对一个分支做标记而不能标记所有的分支.使用克隆是唯一的方法.克隆不但用于标记Topgit版本库
	 的开发状态,也可以用于Topgit版本库的分支管理,例如一旦上游出现了新版本,就从当前版本库建立一个克隆,原来的版本库用于维护原有上游版本的定制
	 开发,新的克隆版本库针对新的上游版本进行迁移,用于新的上游版本的特性开发.
	 也许还可以通过其他的方法实现,例如将Topgit所有相关的分支都复制到一个特定的引用目录中或记录在文件中,以实现特性分支的状态记录.