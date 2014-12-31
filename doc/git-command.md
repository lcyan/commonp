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
	3. git的大部分命令可以使用提交版本作为参数.(如:git diff <commitid>)
	4. 有的命令则使用一个版本范围作为参数(如: git log <rev1>..<rev2>)
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

编写.gitignore文件命令: cat > .gitignore << EOF
Note:忽略只对未跟踪文件有效,对应已加入版本库的文件无效.
	 独享式忽略的两种方式:
		<1>:针对具体版本库的'独享式'忽略,即在版本库.git目录下的一个文件 `.git/info/exclude`来设置文件忽略.
		<2>:全局的独享式忽略,即通过Git的配置变量`core.excludesfile`指定的一个忽略文件,其设置对所有本地版本库均有效.

Git忽略语法:
	<1>:忽略文件中的空行或已#号开始的行会被忽略.
	<2>:可以使用通配符:`*`代表任意多字符,`?`代表一个字符.[abc]代表可选字符范围等.
	<3>:如果名称的最前面是一个路径分隔符`/`,表明要忽略的文件在此目录下,而非子目录的文件.
	<4>:如果名称的最后面是一个路径分隔符`/`,表明要忽略的是整个目录,同名文件不忽略,否则同名的文件和目录都忽略.
	<5>:通过在名称前面添加一个`!`,代表不忽略.
	 eg:
		<1>: # 这是注释行 -- 被忽略
		<2>: *.a		#忽略所有以`.a`为扩展名的文件.
		<3>: !lib.a		#但是lib.a文件或目录不要忽略,即使前面设置了对*.a的忽略.
		<4>: /TODO		#只忽略此目录下的TODO文件,子目录的TODO文件不忽略.
		<5>: build/		#忽略所有build/目录下的文件.
		<6>: doc/\*.txt  #忽略文件如doc/notes.txt 但是文件如 doc/server/arch.txt.不被忽略.

Git时光机
	<A>--<B>--<C>--<D>--<E>--<F(master)>
	case 1:移除提交<D>,把<E>,<F>重新嫁接到<C>.
		<1>: $ git checkout C //执行`git checkout`暂时将HEAD指针切换到C
		<2>: $ git cherry-pick master^ //执行拣选操作,将E提交在当前HEAD上重放.
		<3>: $ git cherry-pick master  //将F提交在当前HEAD上重放.
		<4>: $ git log --pretty=fuller --decorate -2 //通过日志查看,最新2此提交的原始创作时间(AuthorDate)和提交时间(CommitDate)不同.
													 //AuthorDate是拣选提交的原始更改时间.
													 //CommitDate是拣选操作时的时间.因此拣选后的新提交的SHA1也不同于所拣选的原提交的SHA1.
		<5>: $ git checkout master //最重要的一步操作,将master分支重置到新的提交ID,切换操作用了reflog语法,即HEAD@{1}相当于切换回master分支前的HEAD指向.
		<6>: $ git rest --hard HEAD@{1}

		重新恢复场景: $ git rest --hard F
	case 2:<C>,<D>组合成<CD>复合体,<E>,<F>重新嫁接到<CD>复合体
		 <1>: $ git checkout D //暂时将HEAD头指针切换到D.
		 <2>: $ git rest --soft HEAD^^//后退2步,以便C和D融合.
		 <3>: $ git ci -C C//执行提交,提交说明重用C提交的提交说明
		 <3>: $ git cherry-pick E//执行拣选操作将E提交在当前HEAD上重放.
		 <4>: $ git cherry-pick F//执行拣选操作将F提交在当前HEAD上重放.
		 <5>: $ git checkout master //最重要的一步操作,将master分支重置到新的提交ID,切换操作用了reflog语法,即HEAD@{1}相当于切换回master分支前的HEAD指向.
		 <6>: $ git rest --hard HEAD@{1}
/**************************************************************/

$ git cherry-pick //实现提交在新分支上的'重放'
				  //是从众多的提交中挑选出一个提交应用在当前的工作分支中.需要一个提交ID作为参数.
				  //操作过程相当于将该提交导出为补丁文件,然后在当前HEAD上重放,形成无视内容还是提交说明都一致的提交.

$ git rebase
				usage<1>: $ git rebase --onto <newbase> <since> <till>
				usage<2>: $ git rebase --onto <newbase> <since>
				usage<3>: $ git rebase					<since> <till>
				usage<4>: $ git rebase 					<since>
				usage<5>: $ git rebase -i ...
				usage<6>: $ git rebase --continue
				usage<7>: $ git rebase --skip
				usage<8>: $ git rebase --abort

$ git rev-parse //是git的一个底层命令,其功能非常丰富,很多git脚本后工具都会用到这条命令.

				--git-dir	//显示git版本库的位置.
				--show-cdup	//当前工作区目录的深度.
				--parseopt  //可以被Git无关的应用用于解析命令行参数.
				--symbolic --branches	//显示分支.
				--symbolic --tags	//显示里程碑.
				--symbolic --glob=refs/\*	//显示定义的所有引用.
				<git_object_express>	//可以将一个git对象表达式表示为对应的SHA1.支持显示多个表达式的SHA1.
										//eg:git rev-parse master refs/heads/master
										//	 git rev-parse A refs/tags/A 	(里程碑A实际上指向的是一个Tag对象而非提交,用以下命令显示提交而非里程碑对象)
										//		git rev-parse A^{} A^0 A^{commit}
										//	 git rev-parse 4443 332555(ID的前几位)
				//'^'代表父提交.当一个提交有多个父提交时,可以通过在符号'^'后面跟上一个数字表示第几个父提交.
				//'A^' <=> 'A^1'
				//而'B^0'代表了B所指向的一个commit对象(因为B是Tag对象)
				//eg: git rev-parse A^ A^1 B^0

				A^{tree} :A 	//显示里程碑A对应的目录树
				A^{tree}:src/MakeFile A:src/MakeFile //显示树里面的文件.
				:gitg.png	HEAD:gitg.png	//暂存区里的文件和HEAD中的文件相同.
				:\/'Commit A'	//还可以通过提交日志中查找字符串的方式显示提交
				HEAD@{0} master@{0} //reflog中的语法

$ git rev-list //版本范围表示法

				--oneline	A 	//一个提交ID实际上就可以代表一个版本列表.含义是该版本开始的所有历史提交.
				--oneline	D 	F 	//相当于每个版本单独使用时指代列表的并集.
				--oneline	^G 	D 	//在一个版本前面加上符号'^'含义是取反,即排除这个版本及其历史记录.
				--oneline 	G..D 	//和上面等价的'点点'表示法,使用两个点连接连个版本eg:'G..D'就相当于'^G D'
				//版本取反参数的顺序不重要,但是'点点'表示法前后的版本顺序很重要.
					//syntax:^B C 	等价于 C ^B
					//syntax:B..C 	不等价于 C..B

			   --oneline	B...C
			   //三点表示法的含义是两个版本共同能够访问到的除外,eg:'B...C'将B和C共同能过访问到的F,I,J排除在外.
			   //两个版本的前后顺序没有关系.实际上r1...r2相当于'r1 r2 --not' $(git merge-base --all r1 r2).
			   //
			   --oneline	B^@		//某提交的历史提交,自身除外,用语法'r1^@'
			   --oneline 	B^!		//提交本身不包括其历史提交,用语法'r1^!'

$ git archive
				-o latest.zip HEAD //基于最新的提交建立归档文件.
				-o partial.tar HEAD src doc //只将目录src和doc建立到归档partial.tar中.
				--format=tar --prefix=1.0/ v1.0 | gzip > foo-1.0.tar.gz //基于里程碑V1.0建立归档,并且为归档中的文件添加目录前缀1.0

	//在建立归档时,如果使用树对象ID进行归档,则使用当前时间作为归档中文件的修改时间,而如果使用提交ID或里程碑等,则使用提交建立时间
	//作为归档中文件的修改时间.
	//如果使用tar格式建立归档,并且时间提交ID或者里程碑ID,还会把提交ID记录在归档文件的文件头里.
	//记录在文件头中的提交ID可以通过`git tar-commit-id`命令获取.
	//如果希望在建立归档是忽略某些文件和目录,可以通过为相应文件或目录建立`export-ignore`属性加以实现.

$ git log
			--fuller //显示作者,提交者,提交信息.
			--stat   //可以看到每次提交的文件变更统计.
			--oneline --decorate -4 //--decorate 可以在提交ID的旁边显示该提交关联的引用(里程碑或分支).
			--oneline F^! D 	//显示F本身的提交和D以及D提交历史的并集.
			--graph		//显示分支图
			-<n> 	//n为数字,显示最近<n>条记录.
			-p 		//在显示日志的时候同时显示改动.
			--stat 	//显示每次提交的变更概要.
			--pretty=raw 	//显示commit的原始数据,可以显示提交对应的树ID.
			--pretty=fuller //会同时显示作者和提交者.两者可以不同.

$ git show 	//如果只查看和分析某一个提交,可以使用git show/git cat-file
			D --stat //显示里程碑D及其提交.

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
			-C C//提交说明重用C提交的提交说明.
$ git clone
			demo demo-step-1 把demo备份到demo-step-1

$ git add
			-u //可以将(被版本库追踪的)本地文件的变更(修改,删除)全部记录到暂存区中.
			-A //将工作区中的所有改动及新增文件添加到暂存区.
			-i //可以选择性添加文件,进入一个交互页面,首先显示的是工作区状态.
			-f //强制添加忽略的文件到暂存区.

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
			//扩展了GNU的差异比较语法,提供了对重命名,二进制文件,文件权限变更的支持.
			//看到修改后的文件与版本库中的文件的差异.(Note:与本地比较的不是版本库中的文件,而是一个中间状态的文件.)
			//Note:显示工作区的最新改动,即工作区与提交任务(提交暂存区,stage)中相比的差异.
			HEAD //将工作区(当前工作分支)相比,会看到更多差异.
			--cached/--staged //看到的是提交暂存区(提交任务,stage(Note:.git目录下的index文件(用于跟踪工作区文件的)))和版本库中文件的差异.
			B A 	//比较里程碑B和里程碑A
			A 		//比较工作区和里程碑A
			--cached A 	//比较暂存区和里程碑A
			no args		//比较工作区和暂存区
			--cached 	//比较暂存区和HEAD
			HEAD 		//比较工作区和HEAD
			<commit1> <commit2> --<paths> //显示不同版本间该路径下文件的差异.
			--word-diff 	//逐词比较.

$ git blame	//在软件开发的过程当发现bug并定位到具体的代码时,Git的文件追溯命令可以指出是谁在什么时候,以及什么版本引入此bug.

			README
			-L 6,+5 README	//查看某几行

$ git bisect //是基于版本库的,自动化问题查找和定位工具.取代传统的软件测试中针对软件发布版本的,无法定位到代码的,粗放式的测试方法.
			 //执行二分查找,在发现问题后,首先要找到一个正确的版本,如果所发现的问题从软件最早的版本句是错的,那么就没有必要执行二分
			 //查找了,还是老老实实的Debug吧,但是如果能够找到一个正确的版本,记载这个正确的版本上问题没有发生,阿么就可以开始使用
			 //`git bisect`命令在版本库中进行二分查找了.
			 <1>: 工作区切换到已知的'好版本'和'坏版本'的中间的一个版本.
			 <2>: 执行测试,如果问题重现,则将版本库当前版本标记为'坏版本',如果问题没有重现,则将当前版本标记为'好版本'
			 <3>: 重复<1>-<2>,直至最终找到第一个导致问题出现的版本.
			 start 开始二分查找
			 good 标记为好的版本
			 bad 标记问坏的版本
			 //最后执行`git checkout bisect/bad`,解决bug提交.
			 //版本库切回执行二分查找之前所在的分支.
			 reset

			 @把好提交标记为坏提交怎么办?
			 	//Git的二分查找提供了一个恢复查找进度的办法.
			 	//$ git bisect bad
			 	//用`git bisect log > logfile`查看二分查找的日志记录.
			 	//编辑logfile,以#号开头的是在注释.删除记录了错误动作的行.
			 	//结束正在进行的出错的二分查找.`git bisect reset`
			 	//通过日志文件恢复进度,重启二分查找`git bisect replay logfile`
			 	//git describe
			 	//git bisect good

			 //二分查找使用自动化测试
			 //Git的二分查找支持run子命令,可以运行一个自动化测试脚本.实现自动的二分查找.
			 //如果脚本的退出码是0,正在测试的版本是一个好版本.
			 //如果脚本的退出码是125,正在测试的版本被跳过.
			 //如果脚本的退出码是1到127,125除外,正在测试的版本是一个坏版本.
			 //git bisect run sh good-or-bad.sh
			 //git describe refs/bisect/bad

$ git status
			-s //显示精简输出.
			-b //显示当前工作分支的名称.
			--ignore //会在状态显示中看到被忽略的文件.

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

