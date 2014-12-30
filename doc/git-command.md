git add -u --将所有修改过的文件加入暂存区.
git add -A --将本地删除文件和新增文件都登记到提交暂存区.
git add -p --对一个文件内的修改进行有选择性的添加.
git commit --直接将登记在暂存区中的内容提交.
git diff --word-diff --逐字比较.

//修改过后的文件在执行`git diff`将会看到修改造成的差异.
//修改后的文件通过`git add`命令提交到暂存区后,再执行`git diff`命令将看不到该文件的差异.
//继续对此文件进行修改,再次执行`git diff`命令将会看到新的修改显示是在差异中,而看不到旧的修改.
//执行`git diff --cached`命令才可以看到添加到暂存区中的文件所做的修改.

git stash --可以保存和恢复工作进度(将工作区和暂存区的改动全部封存起来)。
//在切换到新的工作分支之前执行`git stash`保存工作进度,工作区就会变得非常干净,然后就可以切换到新的分子中了.
$ git stash
$ git checkout <new_branch>
$ git stash pop --则可以恢复之前保存的工作进度.

#####################################################################################
//@@使用git访问svn
$ git svn cloen <svn_repos_url>
//当能过通过网络连接到svn服务器的时候,并想将本地提交同步到SVN服务器时,先获取SVN服务器上的最新的提交,然后在执行变基操作,最后再将本地提交推送给SVN服务器.

$ git svn fetch
$ git svn rebase
$ git svn dcommit

$ git log
// q:退出分页器
// h:显示帮助.
// 空格下翻一页,b上翻一页.
// j:上翻一行,k:下翻一行.
// d:下翻半页,u:上翻半页.

######################################################################################
//@@install git
$ git fetch
//执行清理工作,避免前一次编译的遗留文件对编译造成影响.
//Note:下面的操作将丢弃本地对git代码的改动.
$ git clean -fdx
$ git reset --hard
//查看Git的里程碑,选中最新的版本进行安装.
$ git tag
//检出该版本的代码
$ git checkout v2.2.1
//执行安装
$ make prefix=/usr/local all doc info
$ make prefix=/usr/local install install-doc install-html install-info


#########################################################################################
//@@ git core
$ git config --global user.name 'cherry'
$ git config --global user.emal 'm290236573@gmail.com'
$ git config --global alias.ci commit
// git config -e //打开的是 `/root/source/gitrepo/demo/.git/config`文件进行编辑.
// git config -e --global //打开的是`/root/.gitconfig`全局配置文件进行编辑.
// git config -e --system //打开`/usr/local/etc/gitconfig`系统配置文件进行编辑.
// 其中版本库级别的配置文件优先级最高,全局配置文件次之,系统级配置文件优先级最低.
$ git commit --allow-empty -m 'who does commit?' //--allow-empty //允许空白提交.

$ git config --unset --global user.name
$ git config --unset --global user.email //删除配置文件
$ git commit --amend --author='Your Name<you@example.com>'
**************************************************************
概念:
	1. `.git/refs`是保存引用的命名空间,其中 `.git/refs/heads`目录下的引用又称为分支.
	2. 对于分支,既可以使用正规的长格式的表示法,eg:`refs/heads/master`,
				也可以去掉前面的两级目录用`master`来表示.(git rev-parse 用于显示引用对应的提交ID)
HEAD 当前版本库的头指针
符号^可以指代父提交
	1. `HEAD^`代表版本库中的上一次提交,即最近一次提交的父提交.
	2. `HEAD^^`则代表`HEAD^`的父提交.
NOTE: 对于一个提交有多个父提交,可以在符号`^`后面用数字表示是第几个父提交.
	G
	1. `a573106^2`的含义是提交a573106的多个父提交中的第二个父提交.
	2. `HEAD^1`相当于HEAD^,含义是HEAD的多个父提交中的第一个父提交.
	3. `HEAD^^2`的含义是HEAD^(HEAD父提交)的多个父提交中的第二个父提交.
符号~<n>也可以用于指代祖先提交.eg:`a573106~5`相当于`a573106^^^^^`.
NOTE: 提交所对应的树对象,可以用类似如下的语法访问:
	1. `a573106^{tree}`
	2. `a573106:path/to/file` //某一次提交对应的文件对象.
	3. `:path/to/file` //暂存区中的文件对象.
NOTE: 可以通过git reset命令改变分支的引用,即实现分支的重置.使用重置命令很危险,会彻底丢弃历史,不能通过浏览提交历史的方法找到丢弃的提交ID.
**************************************************************
$ git log
			--fuller //显示作者,提交者,提交信息.
			--stat   //可以看到每次提交的文件变更统计.
			--oneline --decorate -4 //--decorate 可以在提交ID的旁边显示该提交关联的引用(里程碑或分支).

$ git describe //的输出可以作为软件的版本号,因为这样可以很容易地实现将发布的软件包版本和版本库中的代码对应在一起.
			   //当发现软件包Bug时,可以最快,最准确的对应到代码上.

Note: git 的删除.
	  //直接在工作区删除(`rm -rf *`)对暂存区和版本库没有任何影响.
$ git commit
			--ammend       //修改最新的提交
			--allow-empty  //允许空提交.
			--reset-author //重置作者.
			-s //在提交说明自动添加上包含提交者姓名和邮件地址的签名标识,类似于Signed-off-by: User Name <email@address>
			-a //对本地所有变更的文件执行提交操作.包括本地修改的文件和删除的文件,但不包括未被版本库跟踪的文件.
$ git clone 
			demo demo-step-1 把demo备份到demo-step-1

$ git add
			-u //可以将(被版本库追踪的)本地文件的变更(修改,删除)全部记录到暂存区中.
			-A //将工作区中的所有改动及新增文件添加到暂存区.

$ git stash //保存当前的工作进度,会分别对暂存区和工作区的状态进行保存.(本地没有被系统跟踪的文件并不能保存进度.)
			// Note:保存进度时,如果提供说明则更容易通过进度列表找到保存的进度.(`git stash save 'message...'`)
			list //查看保存进度.此命令暗示了git stash可以多次保存工作进度,并且在恢复的时候可以选择.
			pop [--index] [<stash>]
				 //如果不使用任何参数,会恢复最新保存的工作进度,并将恢复的工作进度从存储的工作进度列表中删除.
				 //如果提供<stash>参数(来自于`git stash list`显示的列表),则从该<stash>中恢复进度.恢复完毕后也将从进度列表中删除<stash>
				 //选项--index除了恢复工作区的文件外,还尝试恢复暂存区,这也就是为什么在本章一开始恢复进度的时候显示的状态和保存进度前的略有不同.
			[save [--patch] [-k|--[no-]keep-index] [-q|--quiet] [<message>]]
				 //这条命令实际上是`git stash`的完整版,即如果需要在保存工作进度的时候使用指定的说明,必须使用如下的格式`git stash save 'mseeage....'`
				 //使用参数--patch会显示工作区和HEAD的差异,
				 //通过对差异文件的编辑决定在进度中最终要保存的工作区的内容.
				 //通过编辑差异文件可以在进度中排除无关的内容.

				 //使用参数-k或--keep-index参数,在保存进度后不会将暂存区重置.默认会将暂存区和工作区强制重置.

			apply [--index] [<stash>]
				 //除了不删除恢复的进度之外,其他和`git stash pop`命令一样.

			drop [<stash>]
				 //删除一个存储的进度,默认删除最新的进度.

			clear //删除所有存储的进度.

			branch <branchname> <stash>
				 //基于进度创建分支.
		
$ git checkout //改命令的实质就是修改HEAD本身的指向,该命令不会影响分支'游标'(如:master),这条命令会重写工作区.

	用法:<1>:`git checkout [-q] [<commit>] [--] <paths>...`
		 <2>:`git checkout [<branch>]`
		 <3>:`git checkout [-m] [[-b] --orphan <new_branch>] [<sart_point>]`
	<1>与<2>区别在于:<1>在命令中包含路径<paths>.为了避免路径和引用(提交ID)同名而发生冲突.可以在<paths>前用2个连续的短线作为分隔符.
					 <1>用法的<commit>是可选项,如果省略则相当于从当前暂存区(index)进行检出.这和上一章的重置命令大不相同:
					 <1>重置的默认值是HEAD,而检出的默认值是暂存区.因此重置一般是重置暂存区<除非使用--hard参数,否则不重置工作区>
					 <1>而检出命令主要是覆盖工作区(如<commit>不省略,也会替换暂存区中相应的文件.)
					 <1>(包含了路径<paths>的用法)不会改变HEAD头指针,主要是用于指定版本的文件覆盖工作区中对应的文件.如果省略<commit>
					 <1>则会用暂存区的文件覆盖工作区的文件,否则用指定提交中的文件覆盖暂存区和工作区中对应的文件.
					 <2>(不使用路径<paths>的用法)则会改变头指针,之所以后面的参数写作<branch>,是因为只有HEAD切换到一个分支才可以对
					 <2>提交进行跟踪,否则仍然会进入'分离头指针'的状态.在'分离头指针'状态下的提交不能被引用关联到,从而可能丢失.
					 <2>所以用法<2>最主要的作用就是切换分支.如果省略<branch>则相当于对工作区进行状态检查.
					 <3>主要用于创建和切换到新的分支(<new_branch>),新的分支从<start_point>指定的提交开始创建.新分支和我们熟悉的
					 <3>master分支没有什么实质的不同,都是refs/heads命名空间下的引用.
	
	NOTE: $ git checkout branch 
			//检出branch分支.(更新HEAD以指向branch分支,以及用branch指向的树更新暂存区和工作区)
		  $ git checkout
		    //汇总显示工作区,暂存区与HEAD的差异.
		  $ git checkout HEAD
		    //同上
		  $ git checkout -- filename
		    //用暂存区中filename文件来覆盖工作区中的filename文件.相当于取消自上次执行`git add filename`以来(如果执行过)的本地修改.
			//这个命令很危险,因为对于本地的修改会悄无声息的覆盖,毫不留情.
		  $ git checkout branch -- filename
			//维持HEAD的指向不变,用branch所指向的提交中的filename替换暂存区和工作区中相应的文件.注意会将暂存区和工作区的filename文件直接覆盖.
		  $ git checkout -- ./git checkout .
			//git checkout 命令后的参数为一个`.`,这条命令最危险!会取消所有本地的修改(相对于暂存区).相当于用暂存区的所有文件直接覆盖本地文件,
			//不给用户确认的机会.

$ git reset 
			--hard //NOTE:会破坏工作区未提交的改动,慎用.//eg:`git reset --hard HEAD^/git reset --hard <commitid>` 相当于将master重置到上一个老的提交上.
					master@{2} //master分支之前的第2次改变的SHA1

			-- filename 仅将文件filename的改动撤出暂存区,暂存区中的其他文件不改变,相当于对命令`git add filename`的反向操作.

			--soft HEAD^ 工作区和暂存区不改变,但是引用向前回退一次.当对最新提交的提交说明或提交的更改不满意时,撤销最新的提交以便重新提交.
			//NOTE:`git commit --amend`用于对最新的提交进行重新提交以修补错误的提交说明或错误的提交文件.修补提交命令实际上相当于执行了下
			面两条命令(Note: 文件.git/COMMIT_EDITMSG保存了上次的提交日志.)
				$ git reset --soft HEAD^
				$ git commit -e -F .git/COMMIT_EDITMSG

			--mixed HEAD^(相当于`git reset HEAD^`)工作区不改变,但是暂存区会回退到上一次提交之前,引用也会回退一次.

			--hard HEAD^ 彻底撤销最近的提交,引用回退到前一次,而且工作区和暂存区都会回退到上一次提交的状态.自上一次以来的提交全部丢失.

			--hard <commit> 会执行动作{1},{2},{3}.

			--soft <commit> 会执行动作{1}.

			--mixed <commit> (默认为--mixed),会执行动作{1},{2}.
			
			{1}:替换引用的指向.引用指向新的提交ID.
			{2}:替换暂存区.替换后,暂存区的内容和引用指向的目录树一致.
			{3}:替换工作区.替换后,工作区的内容变得和暂存区一致,也和HEAD所指向的目录树一致.

			命令`git reset`仅用HEAD指向的目录树重置暂存区,工作区不会受到影响.相当于之前用`git add`命令更新到暂存区的内容撤出暂存区.引用也没有改变.
							因为重置到HEAD相当于没有重置.

			<1>: `git rest [-q] [<commit>] [--] <paths>...`
			<2>: `git rest [--sort | --mixed | --hard | --merge | --keep] [-q] [<commit>]`
			NOTE://<1><2>的区别:
								<1>在命令中包含路径<paths>.为了避免路径和引用(或者提交ID)同名而发生冲突,可以在<paths>前用两个连续的短线(减号)作为分隔符.
								<1>不会重置引用,更不会改变工作区,而是用指定的提交状态(<commit>)下的文件(<paths>)替换掉暂存区中的文件.
									eg:`git reset HEAD <paths>`相当于取消之前执行的git add <paths>命令时改变的暂存区.
								<2>则会重置引用,根据不同的选项,可以对暂存区或工作区进行重置.

$ git diff
			//看到修改后的文件与版本库中的文件的差异.(Note:与本地比较的不是版本库中的文件,而是一个中间状态的文件.)
			//Note:显示工作区的最新改动,即工作区与提交任务(提交暂存区,stage)中相比的差异.
			HEAD //将工作区(当前工作分支)相比,会看到更多差异.
			--cached/--staged //看到的是提交暂存区(提交任务,stage(Note:.git目录下的index文件(用于跟踪工作区文件的)))和版本库中文件的差异.

$ git status 
			-s //显示精简输出.
			-b //显示当前工作分支的名称.

$ git reflog show master //可以显示.git/logs/refs/heads/master中的内容.`97c0580 master@{0}`://每次改变最终的SHA1,是引用<refname>之前第<n>次改变时的SHA1.


$ git commit //暂存区的目录树会写到版本库(对象库(.git/objects))中,master分支会做相应的更新,即master最新指向的目录树就是提交时原暂存区的目录树.
$ git reset HEAD //暂存区的目录树会被重写,会被master分支指向的目录树所替换,但是工作区不收影响.
$ git rm --cached <file> //会直接从暂存区删除文件,工作区则不作出改变.
$ git checkout ./git checkout -- <file> //会用暂存区全部的文件或指定的文件替换工作区的文件.这个操作很危险,会清除工作区中未添加到暂存区的改动.
$ git checkout HEAD ./git checkout HEAD <file> //会用HEAD指向的master分支中的全部或部分文件替换暂存区和工作区中的文件.
											   //这个命令不但会清除工作区中未提交的改动,也会清除暂存区中未提交的改动.

$ git clean
			-fd //清除当前工作区中没有加入版本库的文件和目录(非跟踪文件和目录)

$ git ls-files -s //显示暂存区的目录树.

$ git status
			-s //显示精简格式的状态输出.
//初始化git目录
$ git init demo
Initialized empty Git repository in /root/source/gitrepo/demo/.git/
//.git --Git版本库,又叫仓库,repository
//.git 所在的目录为工作区.

$ git add filename //将文件添加到版本库.
$ git commit -m 'initialized.'
[master (root-commit) 97c0580] initialized.
 1 file changed, 1 insertion(+)
  create mode 100644 welcome.txt))]
//此次提交在master分支上,且是该分支的第一个提交`root-commit`,提交ID为:`97c0580`
$ git rev-parse --git-dir //显示版本库.git目录所在的位置.
/root/source/gitrepo/demo/.git

$ git rev-parse --show-toplevel //显示工作区跟目录.
/root/source/gitrepo/demo

$ git rev-parse --show-prefix //相对于工作区根目录的相对目录.
a/b/c/

end

