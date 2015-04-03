@@项目的版本库在某些情况情况下需要引用其他版本库中的文件,例如公司积累了一套常用的函数库,被多个项目调用,
  显然这个函数库的代码不能直接放到某个项目的代码中,而是要独立为一个代码库,那么其他项目要调用公共函数库
  该怎么处理呢?分别把公共函数库的文件拷贝到各自的项目中会造成冗余,丢弃了公共函数库的维护历史,这显示不是
  好的方法,本章要讨论的子模组协同模型,就是解决这个问题的一个方法.

@@属性svn的用户马上会想起'svn:externals'属性可以实现对代码库的引用.Git的子模组(submodule)是类似的一个实现
  不过因为'Git'的特殊性,二者的区别还是蛮大的.见下图:
  @import doc/img/git-submodule-line-8.png

@@创建子模组

	在演示子模组的创建和使用之前,先做些准备工作,先尝试建立两个公共函数库(libA.git和libB.git),以及一个
	引用函数库的主版本库(super.git)

	$ git --git-dir=/path/to/repos/libA.git init --bare
	$ git --git-dir=/path/to/repos/libB.git init --bare
	$ git --git-dir=/path/to/repos/super.git init --bare

	@向两个公共的函数库填充些数据.这就需要在工作区克隆两个函数库,提交数据并推送.克隆libA.git版本库,
	添加一些数据,然后提交并推送,说明:示例中显示为hack...的地方做了一些改动(如创建新文件等.)并将改动
	添加到暂存区.
	$ git clone /path/to/repos/libA.git /path/to/my/workspace/libA
	$ cd /path/to/my/workspace/libA
	hack...
	$ git push origin master

	克隆libB.git版本库,添加一些数据,然后提交并推送
	$ git clone /path/to/repos/libB.git /path/to/my/workspace/libB
	$ cd /path/to/my/workspace/libB
	hack...
	$ git push origin master

	@版本库super是准备在其中创建子模组的.super版本库刚刚初始化还未包含提交,master分支尚未有正确的引用.
	需要在super版本库中至少建立一个提交.下面就克隆super版本库,在其中完成一个提交(空提交即可)并推送.
	$ git clone /path/to/repos/super.git /path/to/my/workspace/super
	$ cd /path/to/my/workspace/super
	$ git commit --allow-empty -m 'initialized.'
	$ git push origin master

	@现在可以在super版本库中使用'git submodule add'命令添加子模组了.
	$ git submodule add /path/to/repos/libA.git lib/lib_a
	>>Cloning into 'lib/lib_a'...
	>>done.
	$ git submodule add /path/to/repos/libB.git lib/lib_b
	>>Cloning into 'lib/lib_b'...
	>>done.
	@至此看一下super版本库工作区的目录结构.在根目录下多了一个'.gitmodules'文件,并且两个函数库分别被
	 克隆到lib/lib_a目录和lib/lib_b目录下.
	$ ls -Fa
	>>./  ../  .git/  .gitmodules  lib/
	$ cat .gitmodules
	>>[submodule "lib/lib_a"]
	>>	path = lib/lib_a
	>>	url = /root/source/gitrepo/libA.git
	>>[submodule "lib/lib_b"]
	>>	path = lib/lib_b
	>>	url = /root/source/gitrepo/libB.git
	@此时super的工作区尚未提交.
	$ git status
	>>On branch master
	>>Your branch is up-to-date with 'origin/master'.
	>>Changes to be committed:
	>>  (use "git reset HEAD <file>..." to unstage)

	>>	new file:   .gitmodules
	>>	new file:   lib/lib_a
	>>	new file:   lib/lib_b
	@完成提交后,子模组才算正式在super版本库中建立,运行'git push'把建立了新模组的本地版本库推送到远程版本库.
	$ git commit -m 'add modules in lib/lib_a and lib_b.'
	$ git push
	@在提交的过程中,发现作为子模组方式添加的版本库实际上并没有添加版本库内容,实际上只是以'gitlink'的方式
	添加了一个链接.至于子模组的实际地址,是由文件'.gitmodules'指定的.
	可以通过查看补丁的方式看到'lib/lib_a'和'lib/lib_b'子模组的存在方式(即gitlink)
	$ git show HEAD
	>>commit 9bf51443f4702e560b4c22ed724b0009c40bb7cc
	>>Author: cherry <m290236573@gmail.com>
	>>Date:   Mon Jan 19 23:00:13 2015 +0800

	>>    add modules in lib/lib_a and lib/lib_b.
	>>
	>>    Signed-off-by: cherry <m290236573@gmail.com>

	>>diff --git a/.gitmodules b/.gitmodules
	>>new file mode 100644
	>>index 0000000..87c63f8
	>>--- /dev/null
	>>+++ b/.gitmodules
	>>@@ -0,0 +1,6 @@
	>>+[submodule "lib/lib_a"]
	>>+       path = lib/lib_a
	>>+       url = /root/source/gitrepo/libA.git
	>>+[submodule "lib/lib_b"]
	>>+       path = lib/lib_b
	>>+       url = /root/source/gitrepo/libB.git
	>>diff --git a/lib/lib_a b/lib/lib_a
	>>new file mode 160000
	>>index 0000000..5030826
	>>--- /dev/null
	>>+++ b/lib/lib_a
	>>@@ -0,0 +1 @@
	>>+Subproject commit 50308269123a924a45416c3d8a937c658e704f62
	>>diff --git a/lib/lib_b b/lib/lib_b
	>>new file mode 160000
	>>index 0000000..8ddb0a3
	>>--- /dev/null
	>>+++ b/lib/lib_b
	>>@@ -0,0 +1 @@
	>>+Subproject commit 8ddb0a3f034e4f941b026bca3b95048d36550fee

@@克隆带子模组的版本库
	之前的图在对比subversion的'svn:externals'属性和'git'的子模组实现差异时,提到过
	克隆带子模组的Git库,并不能自动将子模组的版本库克隆出来,对于只关心项目本身的数据,
	而不关心项目引用的外部项目数据的用户,这个功能非常好,数据没有冗余而且克隆速度也更快.

	@下面在另外的位置克隆super版本库,会发现'lib/lib_a'和'lib/lib_b'并未克隆.
	$ git clone /path/to/repos/super.git /path/to/my/workspace/super-clone
	$ cd /path/to/my/workspace/super-clone
	$ ls -aF
	>>./  ../  .git/  .gitmodules  lib/
	$ find lib
	>>lib
	>>lib/lib_b
	>>lib/lib_a
	@这是如果运行'git submodule status'可以查看子模组的状态.
	$ git submodule status
	>>-50308269123a924a45416c3d8a937c658e704f62 lib/lib_a
	>>-8ddb0a3f034e4f941b026bca3b95048d36550fee lib/lib_b
	@可以看到,每个子模组的目录前面都是40位的提交ID,最前面的是一个减号,减号的含义是该子模组尚未检出.
	如果需要克隆出子模组形式引用的外部看,首先需要执行'git submodule init'
	$git submodule init
	>>Submodule 'lib/lib_a' (/root/source/gitrepo/libA.git) registered for path 'lib/lib_a'
	>>Submodule 'lib/lib_b' (/root/source/gitrepo/libB.git) registered for path 'lib/lib_b'
	@执行'git submodule init'实际上修改了'.git/config'文件,对子模组进行了注册.
	文件'.git/config'的修改示例如下(以加号开头的行代表新增的行)
	>>[core]
	>>    repositoryformatversion = 0
	>>    filemode = true
	>>    bare = false
	>>    logallrefupdates = true
	>>[remote "origin"]
	>>    url = /root/source/gitrepo/super.git
	>>    fetch = +refs/heads/\*:refs/remotes/origin/\*
	>>[branch "master"]
	>>    remote = origin
	>>    merge = refs/heads/master
	>>+[submodule "lib/lib_a"]
	>>+    url = /root/source/gitrepo/libA.git
	>>+[submodule "lib/lib_b"]
	>>+    url = /root/source/gitrepo/libB.git
	@然后执行'git submodule update'完成子模组版本库的克隆.
	$ git submodule update
	>>Cloning into 'lib/lib_a'...
	>>done.
	>>Submodule path 'lib/lib_a': checked out '50308269123a924a45416c3d8a937c658e704f62'
	>>Cloning into 'lib/lib_b'...
	>>done.
	>>Submodule path 'lib/lib_b': checked out '8ddb0a3f034e4f941b026bca3b95048d36550fee'

@@在子模组中修改和子模组的更新

	执行'git submodule update'更新出来的子模组,都以某个具体的提交版本进行检出,进入某个
	子模组目录,会发现其处于非跟踪状态(分离头指针状态)

	$ cd /path/to/my/workspace/super-clone/lib/lib_a
	$ git branch
	>>* (detached from 5030826)
	>>  master

	@显然这种情况下,如果修改'lib/lib_a'下的文件,提交就会丢失,下面介绍如何在检出的子模组
	 中进行修改,以及如何更新子模组.

	 在子模组中切换到master分支(或其他想要修改的分支)后再进行修改.
	 (1)切换到master分支,然后在工作区做一些改动.
	 $ cd /path/to/my/workspace/super-clone/lib/lib_a
	 $ git checkout master
	 hack...
	 (2)提交执行.
	 git commit -m 'add function `version` in libA.js.'
	 (3)查看状态,会看到相对于远程分支领先一个提交.
	 $ git status
	 >>On branch master
	 >>Your branch is ahead of 'origin/master' by 1 commit.
	 >>  (use "git push" to publish your local commits)
	 >>nothing to commit, working directory clean
	 @在git stauts的状态输出中,可以看出新提交尚未推送到远程版本库,现在暂时不推送,看看在
	  super版本库中执行'git submodule update'对子模组的影响,具体操作过程如下.
	  (4)先到'super-clone'版本库查看一下状态,可以看到子模组已修改,包含了更新提交.
	 $ cd /path/to/my/workspace/super-clone
	 $ git stauts
	 >>On branch master
	 >>	Your branch is up-to-date with 'origin/master'.
	 >>	Changes not staged for commit:
	 >>	  (use "git add <file>..." to update what will be committed)
	 >>	  (use "git checkout -- <file>..." to discard changes in working directory)

	 >>		modified:   lib/lib_a (new commits)

	 >>	no changes added to commit (use "git add" and/or "git commit -a")
	 (5)通过'git submodule status'命令可以看出'lib/lib_a'子模组指向了新的提交ID(前面一个+号)
	 ,而'lib/lib_b'子模组状态正常(提交ID前是一个空格,不是加号也不是减号)
	 $ git submodule stauts
	 >>+cf158cf2693d581b3356d26f12663f5837915eb9 lib/lib_a (heads/master)
 	 >> 8ddb0a3f034e4f941b026bca3b95048d36550fee lib/lib_b (heads/master)
 	 (6)这时如果不小心执行了一次'git submodule update'命令,会将'lib/lib_a'重新切换到旧的执行.
 	 $ git submodule update
 	 >>Submodule path 'lib/lib_a': checked out '50308269123a924a45416c3d8a937c658e704f62'
 	 (7)执行'git submodule status'命令查看子模组的状态,可以看到'lib/lib_a'子模组被重置了.
 	 $ git submodule status
 	 >> 50308269123a924a45416c3d8a937c658e704f62 lib/lib_a(remotes/origin/HEAD)
	 >> 8ddb0a3f034e4f941b026bca3b95048d36550fee lib/lib_b(heads/master)
	 @那么刚才在'lib/lib_a'中的提交丢失了么?实际上因为已经提交到了master主线,因此提交没有丢失,
	 但是如果数据没有提交,就会造成未提交的数据的丢失.
	 (1)进入'lib/lib_a'目录,看到工作区再一次进入分离头指针状态.
	 $ cd lib/lib_a
	 $ git branch
	 >>* (detached from 5030826)
	 >>  master
	 (2)重新检出master分支找回之前的提交.
	 $ git checkout master
	 >>Previous HEAD position was 5030826... add file `libA.js`.
	 >>Switched to branch 'master'
	 >>Your branch is ahead of 'origin/master' by 1 commit.
	 >>  (use "git push" to publish your local commits)
	 @现在如果要将'lib/lib_a'目录下的子模组的改动记录到父项目(super版本库)中,就需要在父项目中
	  进行一次提交才能实现.
	  (1)进入父项目根目录查看状态.因为'lib/lib_a'的提交已经恢复,因此再次显示为有改动.
	  $ cd /path/to/my/workspace/super-clon/
	  % git status -s
	  >> M lib/lib_a
	  (2)查看差异比较,会看到执行子模组的gitlink有改动.
	  $ git diff
	  >>diff --git a/lib/lib_a b/lib/lib_a
	  >>index 5030826..cf158cf 160000
	  >>--- a/lib/lib_a
	  >>+++ b/lib/lib_a
	  >>@@ -1 +1 @@
	  >>-Subproject commit 50308269123a924a45416c3d8a937c658e704f62
	  >>+Subproject commit cf158cf2693d581b3356d26f12663f5837915eb9
	  (3)将gitlink的改动添加到暂存区,然后提交.
	  $ git add -u
	  $ git commit -m 'submodule lib/lib_a upgrade to new version.'

	  @此时先不要忙着推送,因为如果此时执行'git push'将super版本库推送到远程版本库,会引发一个问题,
	  即推送后的远程'super'版本库的子模组'lib/lib_a'指向了一个新的提交,而该提交还是本地的'lib/lib_a'
	  版本库(尚未向上游推送),这会导致其他人克隆super版本库和更新模组时因为找不到该子模组版本库相应的
	  提交而出错.下面就是这类错误信息:
	  >>fatal: refernce is not a tree: 50308269123a924a45416c3d8a937c658e704f62
	  Unable to checkout 'cf158cf2693d581b3356d26f12663f5837915eb9' in submodule path 'lib/lib_a'

	  @为了避免这种可能性的发生,最好先推送'lib/lib_a'中的新提交,然后再向super版本库推送更新的子模组
	  'gitlink'改动,即:
	  (1)先推送子模组
	  $ cd /path/to/my/workspace/super-clone/lib/lib_a
	  $ git push
	  (2)再推送父版本库.
	  $ cd /path/to/my/workspace/super-clone
	  $ git push