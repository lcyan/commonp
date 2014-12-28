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
**************************************************************
$ git log
			--fuller //显示作者,提交者,提交信息.
			--stat   //可以看到每次提交的文件变更统计.
$ git commit
			--ammend       //修改最新的提交
			--allow-empty  //允许空提交.
			--reset-author //重置作者.
			-s //在提交说明自动添加上包含提交者姓名和邮件地址的签名标识,类似于Signed-off-by: User Name <email@address>
			-a //对本地所有变更的文件执行提交操作.包括本地修改的文件和删除的文件,但不包括未被版本库跟踪的文件.
$ git clone 
			demo demo-step-1 把demo备份到demo-step-1

$ git diff
			//看到修改后的文件与版本库中的文件的差异.(Note:与本地比较的不是版本库中的文件,而是一个中间状态的文件.)
			//Note:显示工作区的最新改动,即工作区与提交任务(提交暂存区,stage)中相比的差异.
			HEAD //将工作区(当前工作分支)相比,会看到更多差异.
			--cached/--staged //看到的是提交暂存区(提交任务,stage(Note:.git目录下的index文件(用于跟踪工作区文件的)))和版本库中文件的差异.

$ git status 
			-s //显示精简输出.
			-b //显示当前工作分支的名称.

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



