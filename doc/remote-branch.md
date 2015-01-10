@@如何避免因为用户把所有的本地分支都推送到共享版本库,从而造成共享版本库上分支的混乱?
@@如何避免用户随意在共享版本库中创建里程碑而导致里程碑名称上的混乱和冲突?
@@如何避免不同用户针对不同特性开发创建了相同名字的分支而造成分支名称冲突?
@@当用户向共享版本库及其他版本库推送时,每次都需要输入长长的版本库URL,太不方便了.
@@当用户需要经常从多个不同的他人版本库获取提交时,有没有办法不要总是输入长长的版本库URL.
@@如果不带任何其他参数执行`git fetch, git pull, git push`到底是和那个远程版本库及那个分支进行交互?

@@-------------------------------git remote----------------------------------------------

@19 section
上一章介绍Git分支的时候,每一个版本库最多纸盒一个上游版本库(远程共享版本库)进行交互,实际上git允许
一个版本库和任意多的版本库进行交互,首先执行下面的命令,基于`hello-world.git`版本库在创建几个新的版本库.

$ cd /path/to/repos

$ git clone --bare hello-world.git hello-user1.git
>>Cloning into bare repository 'hello-user1.git'...
>>done
$ git clone --bare hello-world.git hello-user2.git
>>Cloning into bare repository 'hello-user2.git'...
>>done.

@@现在有三个共享版本库:`hello-world.git, hello-user1.git, hello-user2.git`现在有一个疑问,如果一个本地
版本库需要和上面三个版本库进行交互,三个共享版本库都存在一个master分支,会不会互相干扰冲突?

先看看hello-world远程共享版本库中包含的分支有那些:
$ git ls-remote --heads file://path/to/repos/hello-world.git
>>From file:///root/source/gitrepo/hello-world.git
>>0aada8364572e656b116bc20ef71c2f0383d4f55	refs/heads/hello-1.x
>>bb4fef88fee435bfac04b8389cf193d9c04105a6	refs/heads/helper/master
>>cf71ae3515e36a59c7f98b9db825fd0f2a318350	refs/heads/helper/v1.x
>>070947f328dc22b4c094cb88c224ec8639e42696	refs/heads/master

一共有四个分支,其中hello-1.x分支是开发者user1创建的.现在重新克隆该版本库

$ cd /path/to/my/workspace

$ git clone file:///path/to/repos/hello-world.git

$ cd /path/to/my/workspace/hello-world

执行`git branch`命令查看分支,会吃惊地看到只有一个分支master

$ git branch

>>* master

那么远程分支的其他分支哪里去了,为什么本地只有一个分支呢,执行`git show-ref`命令可以看到全部的本地引用.
$ git show-ref
>>070947f328dc22b4c094cb88c224ec8639e42696 refs/heads/master
>>070947f328dc22b4c094cb88c224ec8639e42696 refs/remotes/origin/HEAD
>>0aada8364572e656b116bc20ef71c2f0383d4f55 refs/remotes/origin/hello-1.x
>>bb4fef88fee435bfac04b8389cf193d9c04105a6 refs/remotes/origin/helper/master
>>cf71ae3515e36a59c7f98b9db825fd0f2a318350 refs/remotes/origin/helper/v1.x
>>070947f328dc22b4c094cb88c224ec8639e42696 refs/remotes/origin/master
>>3171561b2c9c57024f7d748a1a5cfd755a26054a refs/tags/jx/v1.0
>>aaff5676a7c3ae7712af61dfb9ba05618c74bbab refs/tags/jx/v1.0-i18n
>>e153f83ee75d25408f7e2fd8236ab18c0abf0ec4 refs/tags/jx/v1.1
>>83f59c7a88c04ceb703e490a86dde9af41de8bcb refs/tags/jx/v1.2
>>1581768ec71166d540e662d90290cb6f82a43bb0 refs/tags/jx/v1.3
>>ccca267c98380ea7fffb241f103d1e6f34d8bc01 refs/tags/jx/v2.0
>>8a5b9934aacdebb72341dcadbb2650cf626d83da refs/tags/jx/v2.1
>>89b74222363e8cbdf91aab30d005e697196bd964 refs/tags/jx/v2.2
>>0b4ec63aea44b96d498528dcf3e72e1255d79440 refs/tags/jx/v2.3
>>aebc86c490cd68614a6c2ee8a797c97c83a2bdb1 refs/tags/mytag
>>9c463b68319543b3e3ddcc736bd6a4abf9218f9a refs/tags/mytag3
>>e169057b2a8e474aa9cc0fca4da2a94522c873a6 refs/tags/v1.0

@@从`git show-ref`的输出中发现了几个不寻常的引用,这些引用以`refs/remotes/origin`为前缀,并且名称和
远程版本库的分支名称一一对应,这些引用实际上就是从远程版本库的分支复制过来的.

@@Git的`git branch`命令也可以查看这些远程分支,不过要加上`-r`参数:
$ git branch -r
>>	origin/HEAD -> origin/master
>>	origin/hello-1.x
>>	origin/helper/master
>>	origin/helper/v1.x
>>	origin/master

@@Git这样的设计是非常巧妙的,在从远程版本库执行获取操作时,不是把远程版本库的分支原封不动地复制到本地
版本库的分支中,而是复制到另外的命名空间.如在克隆一个版本库时,会将远程分支都复制在目录`.git/refs/remotes/origin/`
下,这样从不同的远程版本库执行获取操作,因为通过命名空间相互隔离,所以就避免了在本地的相互覆盖.

@@那么克隆操作产生的远程分支为什么都有一个名为`origin/`的前缀呢,奥秘就在配置文件`.git/config`中.
下面的几行内容出自该配置文件,为了说明方便显示了行号.
$ vim .git/config
>>	6 [remote "origin"]
>>	7     url = file:///root/source/gitrepo/hello-world.git
>>	8     fetch = +refs/heads/*:refs/remotes/origin/*

*/
@@这个小节可以称为[remote]小节,该小节以origin为名注册了一个远程版本库.该版本库的URL由第7行给出,
会发现这个 URL地址就是执行`git clone`命令时所用的地址,最具魔法的配置是第八行,这一行设置了执行
`git fetch origin`操作时使用的默认引用表达式:
	>该引用表达式以加号(+)开头,含义是强制进行引用的替换,即使即将进行的替换是非快进式的.
	>引用表达式中使用了通配符,冒号前面的含有通配符的引用值得是远程版本库的所有分支,
	 冒号后面的引用含义是复制到本地的远程分支目录中.

@@远程分支不是真正意义上的分支,是类似于里程碑一样的引用,如果针对远程分支执行检出命令,会看到大段的错误警告.
$ git checkout origin/hello-1.x
>>Note: checking out 'origin/hello-1.x'.

>>You are in 'detached HEAD' state. You can look around, make experimental
>>changes and commit them, and you can discard any commits you make in this
>>state without impacting any branches by performing another checkout.

>>If you want to create a new branch to retain commits you create, you may
>>do so (now or later) by using -b with the checkout command again. Example:

>>  git checkout -b new_branch_name

>>HEAD is now at 0aada83... Merge branch 'hello-1.x' of file:///root/source/gitrepo/hello-world into hello-1.x

@@上面大段的错误信息实际上告诉我们一件事,远程分支类似于里程碑,如果检出就会使得头指针
HEAD处于分离头指针状态,实际上除了以`refs/head`为前缀的引用之外,如果检出任何其他分支
,都将使工作区处于分离头指针状态.如果对远程分支进行修改就需要创建新的本地分支.


@@分支跟踪
	@为了能够在远程分支`refs/remotes/origin/hello-1.x`上进行工作,需要基于该远程分支创建
	本地分支,远程分支可以使用简写`origin/hello-1.x`,如果Git的版本是1.6.6或更新的版本,
	可以使用下面的命令同时完成分支的创建和切换.
	$ git checkout hello-1.x
	>>Branch hello-1.x set up to track remote branch hello-1.x from origin.
	>>Switched to a new branch 'hello-1.x'
	如果git的版本库比较老,或注册了多个远程版本库,因此存在多个名为hello-1.x的分支,
	就不能使用上面简洁的分支创建和切换命令,而需要使用在上一章学习到的分支创建命令,
	显示地从远程分支中创建本地分支.
	$ git checkout -b hello-1.x origin/hello-1.x
	@在上面基于远程分支创建的本地分支的过程中,命令输出的第一行说的是建立了本地分支
	和远程分支的跟踪,和远程分支建立跟踪后,本地分支就具有下列特征:
	>检查工作区状态时,会显示本地分支和被跟踪远程分支提交之间的关系.
	>当执行`git pull`命令时,会和被跟踪的远程分支进行合并(或者变基),如果两者出现版本偏离的话.
	>当执行`git push`命令时,会推送到远程版本库的同名分支中.

	@下面就在基于远程分支创建的本地跟踪分支中进行操作,看看本地分支是如何与远程分支建立关联的.
	(1)先将本地hello-1.x分支向后重置2个版本
	$ git reset --hard HEAD^^
	>>HEAD is now at 658eebd blank commit for GnuPG-signed tag test.
	(2)然后查看状态,显示当前分支相比跟踪分支落后了三个版本.
	#之所以落后三个版本而非两个版本库是因为hello-1.x的最新提交是一个合并提交,包含两个父提交,
	印象上面的重置命令会丢弃掉了三个提交.
	$ git status
	>> On branch hello-1.x
	>> Your branch is behind 'origin/hello-1.x' by 3 commits, and can be fast-forwarded.
	>>   (use "git pull" to update your local branch)
	>> nothing to commit, working directory clean
	(3)执行`git pull`命名,会自动与跟踪的远程分支进行合并,相当于找回最新的3个提交.
	$ git pull
	>> Updating 658eebd..0aada83
	>> Fast-forward
	>>  src/main.c | 11 +++++++++--
	>>  1 file changed, 9 insertions(+), 2 deletions(-)
	#但是如果基于本地分支创建另外一个本地分支则没有分支跟踪的功能.下面就从本地的hello-1.x分支
	#中穿件hello-jx分支.
	(1)从hello-1.x分支中创建新的本地分支hello-jx
	$ git checkout -b hello-jx hello-1.x
	>> Switched to a new branch 'hello-jx'
	(2)将hello-jx分支重置
	$ git reset --hard HEAD^^
	>> HEAD is now at 658eebd blank commit for GnuPG-signed tag test.
	(3)检查状态看不到分之间的跟踪信息.
	$ git status
	>> On branch hello-jx
	>> nothing to commit, working directory clean
	(4)执行`git pull`会报错.
	$ git pull
	>> There is no tracking information for the current branch.
	>> Please specify which branch you want to merge with.
	>> See git-pull(1) for details

	>>     git pull <remote> <branch>

	>> If you wish to set tracking information for this branch you can do so with:

	>>     git branch --set-upstream-to=origin/<branch> hello-jx

	#为什么同样方法建立的分支`hello-1.x`和`hello-jx`差距这么大,奥秘就是在于从远程
	#分支创建本地分支,自动建立了分支间的跟踪,而从一个本地分支创建另外一个本地分支
	#则没有.看看配置文件`.git/config`中是不是专门为分支`hello-1.x`创建了相应的配置信息?
	$ vim .git/config
	>> 9 [branch "master"]
	>> 10     remote = origin
	>> 11     merge  = refs/heads/master
	>> 12 [branch "hello-1.x"]
	>> 13     remote = origin
	>> 14     merge  = refs/heads/hello-1.x
	#第9~11行是针对master分支设置的分支间跟踪,是在版本库克隆的时候自动建立的.
	#第12~14行是前面基于远程分支创建本地分支时建立的.
	#至于分支`hello-jx`则没有建立相关的配置.
	#如果希望在基于一个本地分支创建另外一个本地分支时也能过使用分支间的跟踪功能,
	#就要在创建分支的时候提供`--track`参数,下面实践一下:
	(1)删除之前创建的hello-jx分支
	$ git checkout master
	>> Switched to branch 'master'
	>> Your branch is up-to-date with 'origin/master'.
	$ git branch -d hello-jx
	>> Deleted branch hello-jx (was 658eebd).
	(2)使用参数`--track`重新基于hello-1.x创建hello-jx分支.
	$ git checkout --track -b hello-jx hello-1.x
	>> Branch hello-jx set up to track local branch hello-1.x.
	>> Switched to a new branch 'hello-jx'
	(3)从git库的配置文件中会看为hello-jx分支设置的跟踪.
	#因为跟踪的是本版本库的本地分支,所以第16行设置的远程版本库的名字为一个点.
	$ vim .git/config
	>> 15 [branch "hello-jx"]
	>> 16     remote = .
	>> 17     merge  = refs/heads/hello-1.x

@@远程版本库
	@名为origin的远程版本库是在版本库克隆时注册的,那么如何注册新的远程版本库呢?

@@注册远程版本库
	#下面将版本库file:///path/o/repos/hello-user1.git以new-remote为名进行注册.
	$ git remote add new-remote file:///path/o/repos/hello-user1.git
	$ vim .git/config
	>> 18 [remote "new-remote"]
	>> 19     url = file:///root/source/gitrepo/hello-user1.git/
	>> 20     fetch = +refs/heads/*:refs/remotes/new-remote/*
*/
	#执行`git remote`敏玲,可以更为方便地显示已经注册的远程版本库.
	$ git remote -v
	>> new-remote	file:///root/source/gitrepo/hello-user1.git/ (fetch)
	>> new-remote	file:///root/source/gitrepo/hello-user1.git/ (push)
	>> origin	file:///root/source/gitrepo/hello-world.git (fetch)
	>> origin	file:///root/source/gitrepo/hello-world.git (push)
	#现在执行`git fetch`并不会从新注册的`new_remote`远程版本库中获取,
	#因为当前分支设置的默认远程版本库为origin.要想从new-remote远程版本
	#库中获取,需要为`git fetch`命令增加一个参数`new-remote`
	$ git fetch new_remote
	>> From file:///root/source/gitrepo/hello-user1
	>> * [new branch]      hello-1.x  -> new-remote/hello-1.x
	>> * [new branch]      helper/master -> new-remote/helper/master
	>> * [new branch]      helper/v1.x -> new-remote/helper/v1.x
	>> * [new branch]      master     -> new-remote/master
	#从上面的命令输出可以看出,远程版本库的分支复制到本地版本库前缀为
	#new-remote的远程分支中去了.用'git branch -r'命令可以查看新增了几
	#个远程分支.

@@更改远程版本库的地址

	@如果远程版本库的URL地址改变,需要更换,该如何处理?手工修改`.git/config`
	文件是一种方法,用`git config`命令进行更改是第二种方法,还有一种方法是
	用`git remote`命令:
	$ git remote set-url new-remote file:///path/to/repos/hello-user2.git
	$ git remote -v
	>> new-remote	file:///root/source/gitrepo/hello-user2.git/ (fetch)
	>> new-remote	file:///root/source/gitrepo/hello-user2.git/ (push)
	>> origin	file:///root/source/gitrepo/hello-world.git (fetch)
	>> origin	file:///root/source/gitrepo/hello-world.git (push)

	#从上面的输出可以发现每一个远程版本库都显示两个URL地址,分别是执行
	#`git fetch`和`git push`命令是用到的URL地址,既然有两个地址,就意味
	#着这两个地址可以不同,用下面的命令可以为推送操作设置单独的URL地址.
	$ git remote set-url --push new-remote /path/to/repos/hello-user2.git
	$ git remote -v
	>> new-remote	file:///root/source/gitrepo/hello-user2.git/ (fetch)
	>> new-remote	/root/source/gitrepo/hello-user2.git/ (push)
	>> origin	file:///root/source/gitrepo/hello-world.git (fetch)
	>> origin	file:///root/source/gitrepo/hello-world.git (push)
	#当单独为推送设置了URL后,配置文件`.git/config`的对应[remote]小节也会
	#增加一条新的名为pushurl的配置:
	$ vim .git/config
	>> 18 [remote "new-remote"]
	>> 19     url = file:///root/source/gitrepo/hello-user2.git/
	>> 20     fetch = +refs/heads/*:refs/remotes/new-remote/*
	>> 21     pushurl = /root/source/gitrepo/hello-user2.git/

*/
@@更改远程版本库的名称

	$ git remote rename new-remote user2 //将new-remote名称修改为user2
	#完成改名后,不但远程版本库的注册名称更改过来了,就连远程分支的名称都
	#会自动进行相应的更改,可以通过执行`git remote`和`git branch -r`查看.
	$ git branch -r
	>> origin/HEAD -> origin/master
	>> origin/hello-1.x
	>> origin/helper/master
	>> origin/helper/v1.x
	>> origin/master
	>> user2/hello-1.x
	>> user2/helper/master
	>> user2/helper/v1.x
	>> user2/master

@@远程版本库更新
	@当注册了多个远程版本库并希望获取所有远程版本库更新时,git提供了一个简单
	的命令`git remote update`
	$ git remote update
	>> Fetching origin
	>> Fetching user2
	#如果某个远程版本库不想在执行`git remote update`时获得更新,可以通过参数
	#关闭自动更新.
	$ git config remote.user2.skipDefaultUpdate true //关闭远程版本库user2的自动更新.
	$ git remote update
	>> Fetching origin

@@删除远程版本库
	@如果想删除注册的远程版本库,用git remote的rm子命令可以实现.
	$ git remote rm user2

@@PUSH和PULL操作与远程版本库

	@Git分支一章已经介绍锅对于新建的本地分支(没有建立和远程分支的跟踪),执行git push命令
	是不会被推送到远程版本库中的,这样的设置是非常安全的,避免了因为误操作将本地分支创建
	到远程版本库中.当不带任何参数执行`git push`,实际执行的过程是:

	#如果为当前分支设置了<remote>,即有配置branch.<branchname>.remote给出了远程版本库
	   代号,则不带参数执行`git push`相当于执行了`git push <remote>`
	#如果没有为当前分支设置<remote>,则不带参数执行`git push`相当于执行了`git push origin`
	#要获取的远程版本库的URL地址有remote.<remote>.url给出.
	#如果为注册的远程版本库设置了fetch参数,即通过remote.<remote>.fetch配置了一个引用
	 表达式,则使用该引用表达式执行获取操作.
	#接下来要确定合并的分支.如果设定了branch.<branchname>.merge则对其设定的分支执行合并,
	 否则报错退出.

	 @在执行`git pull`操作的时候可以通过参数`--rebase`设置使用变基操作而非合并操作,
	  将本地分支的改动变基到跟踪分支上去.为了避免因为忘记使用`--rebase`参数导致分支
	  的合并,可以执行如下命令进行设置.注意将<branchname>替换为对应的分支名称.
	  $ git config branch.<branchname>.rebase true

	  #有了这个设置之后,如果是在<branchname>工作分支中执行`git pull`命令,在遇到本地
	  分支和远程分支出现偏离的情况下,会采用变基操作,而不是默认的合并操作.
	  #如果为本地版本库设置参数`branch.autosetuprebase`值为true,则在基于远程分支建立
	  本地追踪分支时,会自动配置branch.<branchname>.rebase参数,在执行`git pull`命令时
	  使用变基操作取到默认的合并操作.

@@里程碑和远程版本库
	@远程版本库中的里程碑同步到本地版本库,会使用同样的名称,而不会想分支那样移动到
	另外的命名空间(远程分支)中,这可能会给本地版本库中的里程碑带来混乱.当合多个远程
	版本库交互式,这个问题更为严重.
	@前面已经介绍了当执行`git push`命令推送时,默认不会将本地创建的里程碑带入远程
	版本库.这样可以避免远程版本库上的里程碑泛滥.但是执行`git fetch`命令从远程版本库
	获取分支的最新提交的时,如果获取的提交上建有里程碑,这些里程碑会被获取到本地
	版本库.当删除注册的远程版本库是,远程分支会被删除,但是该远程版本库引入的里程碑
	不会被删除,日积月累本地版本库中的里程碑可能会变得愈加混乱.
	可以在执行`git fetch`命令的时候,设置不获取里程碑只获取分支及提交.通过提供参数
	-n或--no-tags参数可以实现.
	$ git fetch --no-tags file:///path/to/repos/hello-world.git refs/head/*:refs/remotes/hello-world/*
	@在注册远程版本库的时候,也可以使用--no-tags参数,避免将远程版本库的里程碑引入本地版本库.
	$ git remote add -no-tags hello-world file:///path/to/repos/hello-world.git

@@分支和里程碑的安全性
	@感觉到git的使用真是太方便,太灵活了,但是需要掌握的知识点和窍门也太多了.为了避免没有
	经验的用户在退订共享的git版本库中误操作,就需要对版本库进行一些安全上的设置.实际上git
	版本库本身也提供了一些安全机制避免对版本库的破坏.

	#用reflog记录对分支的操作历史.
	默认创建的带工作区的版本库都会包含core.logallrefupdates为true的配置,这样在版本库中
	建立的每个分支都会创建对应的reflog.但是创建的裸版本库默认不包含这个设置.也就不会为
	每个分支设置reflog.如果团队规模教下,可以因为分支误操作导致数据丢失.可以考虑为裸版本
	库添加core.logallrefupdates的相关配置.
	#关闭非快进式推送
	如果将配置receive.denyNonFasetForwards设置为true,则禁止一切非快进式推送.但这个配置
	有些矫枉过正,更好的方法是搭建基于SSH协议的Git服务器,通过钩子脚本更灵活地进行配置.
	例如:允许来自某些用户的强制提交,而其他用户不能执行非快进式推送.
	#关闭分支删除功能
	如果将配置receive.denyDeletes设置为true,则禁止删除分支,同样更好的方式是通过架设
	基于SSH协议的git服务器,配置分支删除的用户权限.




