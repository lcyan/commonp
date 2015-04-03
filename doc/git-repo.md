git repo 相关操作

$ git clone
			usage<1>: git clone <repository> <directory>
			usage<2>: git clone --bare <repository> <directory.git>
			usage<3>: git clone --mirror <repository> <directory.git>

	//三种用法的区别:
	1. 	用法<1>将<repository>指向的版本库创建一个克隆到<directory>目录.
	   	目录<directory>相当于克隆版本库的工作区,文件都会检出,版本库位于
	   	工作区的.git目录下.
	2.  用法<2>和用法<3>创建的克隆版本库都不包含工作区,直接就是版本库内容,
		这样的版本库成为裸版本库.一般约定俗成裸版本库的目录以`.git`为后缀,
		所以上面示例中将克隆出来的裸版本库目录名写作<directory.git>
	3.	用法<3>区别于用法<2>在于用法<3>克隆出来的裸版本对上游版本库进行了
		注册,这样可以在裸版本库中使用`git fetch`命令和上游版本库进行持续同步.

	Note:不使用`--bare`,`--mirror`创建粗来的克隆包含工作区,这样就产生两个
		 包含工作区的版本库,这两个版本库是对等的.这两个工作区本质是没有区别
		 的,但是往往提交是在一个版本(A)中进行,另外一个(B)作为备份.
		 对应这种对等的工作区模式,版本库的同步只有一种可行的操作模式,
		 就是备份库(B)执行`git pull`命令从源版本库(A)中拉回新的提交实现版本库同步
		 为什么不能从版本库A向版本库B执行`git push`推送呢?
		 //默认更新非裸版本库的当前分支是不被允许的,因为这将导致暂存区和工作区与你推送至
		 //版本库的新提交不一致.

$ git push/pull

			$ git push [<remote-repos> [<refspec>]]
			$ git pull [<remote-repos> [<refspec>]]


		NOTE:其中方括号的还以是参数可以省略,<remote-repos>是远程版本库的地址或名称,
			 <refspec>是引用表达式,暂时理解为引用即可.

$ git clone --bare <repository> <directory>
	//实现版本库本地镜像显然是更好的方法,因为可以直接在工作区修改和提交,然后执行`git push`命令实现推送,
	//稍有一点遗憾的是推送命令还需要加上裸版本库的路径//FIXME?TODO

@@裸版本库不但可以通过克隆的方式创建,还可以通过`git init`命令以初始化的方式创建.

$ git init --bare /root/source/gitrepo/demo-init.git

$ cd /root/source/gitrepo/demo

$ git push /root/source/gitrepo/demo-init.git


<1>: 从网上克隆来的版本库,为什么对象库中找到不对象文件?而且引用目录里也看不到所有的引用文件?

$ git show-ref //看看所包含的引用.

@@答案是这些文件被打包了,放到了一个文本文件,`.git/packed-refs`中了.

$ head -5 .git/packed-refs

$ find .git/objects/ -type f
.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.idx //索引文件
.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.pack //打包文件
//打包文件和对应的索引文件只是扩展名不同,都保存与`.git/objects/pack/`目录下.
//Git对于以SHA1哈希值作为目录名和文件名保存的对象有一个术语,成为松散对象.
//松散对象打包后会提高访问效率,而且不同的对象可以通过增量存储节省磁盘空间.
//
@@通过Git一个底层的命令可以查看索引中包含的对象.
$ git show-index < .git/objects/pack/pack-*.idx | head -5

@@为什么克隆远程版本库就可以产生对象库打包及引用打包的效果呢?

>>这是因为克隆远程版本库时,使用了`智能`的通信协议,远程Git服务器将对象打包后传输给本地,形成本地版本库的对象库中的一个包含
>>所有对象的包及索引文件.无疑这样的传输方式--按需传输,打包传输--效率极高.
>>克隆之后的版本库在日常的提交中,产生的新对象仍旧以松散对象存在,而不是以打包的形式,日积月累会在本地版本库的对象库中形成了
>>大量的松散文件.松散对象只是进行了压缩,而没有(打包文件才有的)增量存储功能,会浪费磁盘空间,也会降低访问效率.更为严重的是
>>一些非正式的临时对象(暂存区操作中产生的临时对象)也以松散对象的形式保存在对象库中.造成了磁盘空间的浪费.

<2>: 不小心添加了一个大文件到Git库中,用重置命令丢弃了包含大文件的提交,可是版本库不见效,大文件仍在对象库中?

$ cp initramfs-2.6.32-504.3.3.el6.x86_64.img /tmp/bigfile //19M bigfile
$ cp bigfile /root/source/gitrepo/i-am-admin/
$ cp bigfile bigfile.dup
$ git add -A
$ du -sh .//工作区连同版本库共占用57M
$ du -sh .git/ //版本库占用19M
$ find .git/objects/ -type f //发现版本库的文件对象中,多出了一个松散对象,之所以添加两个文件而只有一个松散对象,因为Git对应文件的保存是将
							 //内容保存为blob对象中,和文件名无关,相同内容的不同文件会共享同一个blob对象.
$ git reset HEAD //将添加的文件撤出暂存区.
$ du -sh .git/ //版本库占用还是19M,在对象库中产生的blob对象仍然存在,通过查看版本库的磁盘占用就可以看出来.
$ git fsck //可以查看到版本库中包含的没有被任何引用关联的松散对象.标识为dangling的对象就是没有被任何引用直接或间接关联的对象.
'Checking object directories: 100% (256/256), done.'
'Checking objects: 100% (65/65), done.'
'dangling blob 40284b8ae5248ededa08268799d3c9ab33d7e1c0'
$ git prune //清理之后,发现.
$ git fsck //没有发现无关联的松散对象.
'Checking object directories: 100% (256/256), done.'
'Checking objects: 100% (65/65), done.'
$ du -sh .git/
'236K	.git/' //版本库的空间占用也小于19M,证明大的临时对象已经从版本库中删除了.


@@重置操作引入的对象.

$ git add bigfile bigfile.dup

$ git commit -m 'add bigfiles.'

$ du -sh .git/ //19M

$ git reset --hard HEAD^ //做一个重置操作,抛弃刚刚针对两个大文件做的操作.

$ du -sh .git/ //19M,版本库的空间占用没有变化,还是那么'庞大'.

$ find .git/objects/ -type f //查看版本库发现三个松散对象.这三个对象分别对应撤销的提交,目录树,已经大文件对应的blob对象.用git cat-file -t <id>查看
'.git/objects/6c/bce47593abf57f24cc4f2747a89b44656162fe'
'.git/objects/40/284b8ae5248ededa08268799d3c9ab33d7e1c0'
'.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.idx'
'.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.pack'
'.git/objects/68/b18d42c51491033745c710edcdbbcc3021d45f'

$ git prune //发现版本库的大小没有变化

$ git fsck //也看不到未被关联的对象.

$ git fsck --no-reflogs //reflog是防止误操作的最后一道闸门
'Checking object directories: 100% (256/256), done.'
'Checking objects: 100% (65/65), done.'
'dangling commit 68b18d42c51491033745c710edcdbbcc3021d45f'

$ git reflog
'6652a0d HEAD@{0}: reset: moving to HEAD^'
'68b18d4 HEAD@{1}: commit: add bigfiles.'
'6652a0d HEAD@{2}: clone: from git://github.com/ossxp-com/gitdemo-commit-tree.git'
//可以看到撤销的操作仍然记录在relog中,正因为如此,Git任务撤销的提交和大文件都可以被追踪到,还是使用者,所以无法用`git prune`删除.
//如果确认真的要丢弃不想要的对象,需要读版本库的reglog做过期操作,相当于将`.git/logs`下的文件情况.
//
$ git reflog expire --all //这条命令做不到让刚刚撤销的提交过期,因为reflog的过期操作默认只会让90天钱的数据过期.

$ git reflog expire --expire=<date> -all //强制要<date>之前的记录全部过期.

$ git prune

$ du -sh ./git //244k //版本库变小了.

Git管家 - git-gc

$ git gc
1: 对分散在`.git/refs`下的文件进行打包,打包到文件`.git/packed-refs`中.
   如果没有将配置`gc.packrefs`关闭,就会执行命令`git pack-refs --all --prune`实现对引用打包.
2: 丢弃90天前的reflog记录.
   会运行`git reflog expire --all`
3: 对松散对象进行打包.
   运行`git repack`命令,凡是有引用关联的对象都被打在包里,未被关联的对象仍旧以松散的形式保存.
4: 清除未被关联的对象,默认只清除2周以前未被关联的对象.可以向`git gc`提供`--prune=<date>`参数,其中的时间参数传递
   给git prune --expire <date>,实现对指定日期之前的未被关联的松散对象进行清理.
5: 其他清理,如运行`git rerere gc`对合并冲突的历史记录进行过期操作.

//查看`git gc`打包所实现的对象增量存储的效果
1.复制两个大文件到工作区

$ cp /tmp/bigfile bigfile //19M
$ cp /tmp/bigfile bigfile.dup

2.在文件bigfile.dup后面追加内容,以造成bigfile与bigfile.dup内容不同
$ echo 'hello git' >> bigfle.dup

3.将这两个稍有不同的文件提交到版本库
$ git add bigfle bigfile.dup
$ git commit -m 'add bigfiles.'

5.查看版本库提交进来的两个不同的大文件对象.
$ git ls-tree HEAD | grep bigfile
'100644 blob 40284b8ae5248ededa08268799d3c9ab33d7e1c0	bigfile'
'100644 blob 3f5b79f716b29358adef626b69db2af405f0040a	bigfile.dup'

6.做版本库重置,抛弃最新的提交,即抛弃添加两个大文件的提交.
$ git reset --hard HEAD^

7.查看此时版本库的大小
$ du -sh .git/ //38M

8.在对象库中查看对象的大小
$ find .git/objects -type f -printf '%-20p\t%s\n' //前面是文件名,后面是以字节为单位的文件大小
'.git/objects/6c/bce47593abf57f24cc4f2747a89b44656162fe	214'
'.git/objects/0b/e997a111537ad36f3f8c8f81f4472a851ca065	234'
'.git/objects/40/284b8ae5248ededa08268799d3c9ab33d7e1c0	19408648'
'.git/objects/bb/8eede10ff3b1dd5c07df4a0961167e7ea140ee	173'
'.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.idx	2892'
'.git/objects/pack/pack-ceb2f84b8a9336c4b1f7996af66e4b07f7878c8a.pack	80014'
'.git/objects/68/b18d42c51491033745c710edcdbbcc3021d45f	176'
'.git/objects/3f/5b79f716b29358adef626b69db2af405f0040a	19408658'

9.执行`git gc`并不会删除任何对象,因为这些对象都没过期,但是发现版本库的占用空间变小了
$ git gc
'Counting objects: 69, done.'
'Compressing objects: 100% (49/49), done.'
'Writing objects: 100% (69/69), done.'
'Total 69 (delta 11), reused 63 (delta 8)'
10.查看版本库现在的大小
$ du -sh .git/
'19M	.git/'//发现小了一半,原来是因为对象库重新打包,两个大文件采用了增量存储使得版本库变小.

//原来是因为对象库重新打包,两个大文件采用了增量存储使得版本库变小.
$ find .git/objects -type f -printf '%-20p\t%s\n' | sort
'.git/objects/info/packs	54'
'.git/objects/pack/pack-777870cb6f0593eb91d2468d47c2691927908ced.idx	3004'
'.git/objects/pack/pack-777870cb6f0593eb91d2468d47c2691927908ced.pack	19483608'
11.如果想将抛弃的历史数据彻底丢弃,进行如下操作
	1>:不在保留90天的reflog,而是将所有的reflog全部即时过期
	  $ git reflog expire --expire=now --all
	2>:通过`git fsck`可以看到有提交成为了未被关联的提交.
	  $ git fsck
	3>:git show <id> //这个未被关联的提交就是添加大文件的提交.
	4>:不带参数调用`git gc`虽然不会清除尚未过期(未到2周)的大文件,但是会将未被关联的对象从打包文件中移出,成为松散文件.
	  $ git gc
	5>:未被关联的对象重新成为松散文件,所以.git版本库的空间占用又反弹了
	  $ du -sh .git/ //38M
	6>:实际上完全可以调用`git gc --prune=now`就不用在等二周了.直接就可以完成对未关联对象的清理.
	  $ git gc --prune=now
	    'Counting objects: 65, done.'
		'Compressing objects: 100% (45/45), done.'
		'Writing objects: 100% (65/65), done.'
		'Total 65 (delta 8), reused 65 (delta 8)'
	7. 清理过后,版本库的空间占用降了下来.
	  $ du -sh .git/ //234K

<3>: 本地版本库的对象库里的文件越来越多,这可能导致Git性能的降低.

@@所谓共享式写操作,就是版本库作为一个裸版本库放在服务器上,团队成员可以通过PUSH(推送)操作将提交推送到共享的裸版本中.
  每一次推送操作都会出发`git gc --auto`命令,对版本库进行按需整理.

@@在实际操作中只有在特定的条件下才会触发真正的版本库整理.主要的触发条件:
	松散对象只有超过一定的数量时才会执行.在统计松散对象数量时,为了降低在`.git/objects`目录下
	搜索松散对象对系统造成的负担,实际采取了取样搜索,即只会对对象库下的一个子目录`.git/objects/17`进行文件搜索.
	在默认的配置下,只有该目录中的对象数目超过27个才会触发版本库的整理.至于为什么只在对象库中选择一个子目录进行
	松散对象的搜索,这是因为SHA1哈希值是完全随机的.

@@可以通过设置配置变量gc.auto的值,调整Git关键自动运行时触发版本库的整理操作的频率.默认的gc.auto的值为6700,
是触发版本库整理的全部松散对象数的阈值,对于单个取样目录`.git/objects/17`来说,超过`(6700+255)/256`个文件时
即开始版本库整理,但是注意不要将`git.auto`设置为0,否则`git gc --auto`命令永远不会触发版本库整理.
