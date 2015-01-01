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

