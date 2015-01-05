里程碑即tag,是人为对提交进行的命名.这和Git的提交ID是否太长无关.使用任何
数字版本号无论长短,都没有使用一个直观的表意的字符串来的方便.
例如:'v2.1',对应与软件'2.1'发布版本就比使用提交ID要直观的多.

$ git describe //显示里程碑和提交ID的组合来代表软件的版本号.

@@详细介绍里程碑的创建,删除,和共享.
@@里程碑的三种状态:轻量级里程碑,带注释的里程碑和带签名的里程碑

1.在本地创建一个镜像,用作本地用户的共享版本库.
$ cd /root/source/gitrepo
$ git clone --mirror git://github.com/ossxp-com/hello-world.git //在本地建立了一个裸版本库.

2.用户user1和user2分别在各自的工作区克隆这个裸版本库.
$ git clone file:///path/torepos/hello-world.git /path/to/user1/workspace/hello-world
$ git clone file:///path/torepos/hello-world.git /path/to/user2/workspace/hello-world
$ git --git-dir=/path/to/user1/workspace/hello-world config user.name user1
$ git --git-dir=/path/to/user1/workspace/hello-world config user.email user1@molloc.com
$ git --git-dir=/path/to/user2/workspace/hello-world config user.name user2
$ git --git-dir=/path/to/user2/workspace/hello-world config user.email user2@molloc.com

@@显示里程碑
	里程碑可以用`git tag`命令来显示,里程碑还可以在其他命令的输出中出现.
	$ git tag //不带任何参数执行`git tag`命令,则可显示当前版本库的里程碑列表.
	$ cd /path/to/user1/workspace/hello-world
	$ git tag
	>>x/v1.0
	>>jx/v1.0-i18n
	>>jx/v1.1
	>>jx/v1.2
	>>jx/v1.3
	>>jx/v2.0
	>>jx/v2.1
	>>jx/v2.2
	>>jx/v2.3
	里程碑创建的时候可能包含了一个说明,在显示里程碑的时候同事显示说明,使用-n<num>
	显示最多<num>行里程碑说明.
	$ git tag -n1
	>>jx/v1.0         Version 1.0
	>>jx/v1.0-i18n    i18n support for v1.0
	>>jx/v1.1         Version 1.1
	>>jx/v1.2         Version 1.2: allow spaces in username.
	>>jx/v1.3         Version 1.3: Hello world speaks in Chinese now.
	>>jx/v2.0         Version 2.0
	>>jx/v2.1         Version 2.1: fixed typo.
	>>jx/v2.2         Version 2.2: allow spaces in username.
	>>jx/v2.3         Version 2.3: Hello world speaks in Chinese now.
	还可以使用通配符对输出进行过滤,只显示名称和通配符相符的里程碑.
	$ git tag -l jx/v2*
	>>jx/v2.0
	>>jx/v2.1
	>>jx/v2.2
	>>jx/v2.3
	命令git log --decorate可以看到提交对应的里程碑及其他引用.
	>>d901dd8 (HEAD, origin/master, origin/HEAD, master) Merge pull request #1 from gotgithub/patch-1
	>>96fc4d4 Bugfix: build target when version.h changed.
	>>3e6070e (tag: jx/v1.0) Show version.
	>>75346b3 Hello world initialized.
	使用命令git describe将提交显示为一个易记的名字,这个易记的名称来自于建立在该提交的里程碑,
	若该提交乜有里程碑则使用该提交历史版本上的里程碑并加上可理解的寻址信息.
	$ git describe 3e6070eb206
	>>jx/v1.0
	若提交没有对应的里程碑,但是在其祖先版本上建有里程碑,则使用类似<tag>-<num>-g<commit>的格式显示.
	其中<tag>是最接近的祖先提交的里程碑数字,<num>是该里程碑和提交直接的距离.<commit>是该提交的精简ID.
	$ git describe
	>>jx/v1.0-2-gd901dd8

