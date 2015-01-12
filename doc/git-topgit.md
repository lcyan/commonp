@@Topgit协同模型

@@作者版本控制系统的三个里程碑

	(1)@Subversion和卖主分支
	从2005年开始我专心于开源软件的研究,定制开发和整合,在这几年的几年,
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

	上述的图示非常粗糙,因为如果安装这样的设计很难将特性分支导出为补丁文件,
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
		$ tg create t/feature1
		>>tg: Automatically marking dependency on master
		>>tg: Creating t/feature1 base from master...
		>>Switched to a new branch 't/feature1'
		>>tg: Topic branch t/feature1 set up. Please fill .topmsg now and make initial commit.
		>>tg: To abort: git rm -f .top* && git checkout master && tg delete t/feature1

		@提示信息以`tg:`开头的是Topgit产生的说明.其中提示用户变基`.topmsg`文件,然后执行一次提交操作完成Topgit特性分支的创建.

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
		>>Auto-merging hacks.txt
		>>CONFLICT (content): Merge conflict in hacks.txt
		>>Automatic merge failed; fix conflicts and then commit the result.
		>>tg: Please commit merge resolution and call `git checkout t/feature3 && tg update` again.
		>>tg: It is also safe to abort this operation using `git reset --hard`,
		>>tg: but please remember that you are on the base branch now;
		>>tg: you will want to switch to some normal branch afterwards.
		@编辑hacks.txt解决冲突.
		$ git mergetool
		$ git commit -m 'resolved conflict with t/feature1.'
		$ tg info

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
		`--sort`参数安装分支依赖的顺序显示分支列表,除了显示Topic分支外,还会显示依赖的非Topgit分支.
		$ tg summary --sort
		>>t/feature3
		>>t/feature2
		>>t/feature1
		>>master
		`--graphviz`会输出GraphViz格式文件,可以用于显示特性分支之间的关系.
		$ tg summary --graphviz | dot -T png -o topgit.png //yum install -y graphviz
		@import doc/img/git-topgit-line-357.png