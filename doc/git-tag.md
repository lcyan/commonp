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
	如果工作区对文件有修改,还可以通过后缀-dirty标识出来.
	$ echo hacked >> README
	$ git describe --dirty
	$ git checkout -- README
	如果提交本身没有包含里程碑,可以通过传递参数`--always`参数显示精简提交ID,否则会出错.
	$ git describe master^ --always
	命令git name-rev和git describe类似,会显示提交ID及其对应的一个引用.默认优先使用分支名,除非使用--tags
	参数.还有一个显著的不同就是,如果提交上没有相对应的引用,则会使用最新提交上的引用名称并加上向上的回溯
	符号~<num>.
	$ git name-rev HEAD
	>>HEAD master
	使用--tags优先使用里程碑.
		之所以在对应的里程碑引用名称加上后缀^0,是因为该引用指向的是一个tag对象而非提交.用^0后缀指向对应的提交.
	$ git name-rev HEAD^ --tags
	>>HEAD^ tags/jx/v1.0^0
	如果提交上没有对应的引用名称,则会使用新提交上的引用名称并加上后缀~<num>.后缀的含义是第<num>个祖先提交.
	$ git name-rev --tags 610e78
	>>610e78 tags/jx/v2.3~1
	命令git name-rev可以对标准输入中的提交ID进行改写,使用管道符号对前一个命令的输入进行改写,会显示神奇的效果.
	$ git log --pretty=oneline origin/helper/master | git name-rev --tags --stdin
	>>bb4fef88fee435bfac04b8389cf193d9c04105a6 (tags/jx/v2.3^0) Translate for Chinese.
	>>610e78fc95bf2324dc5595fa684e08e1089f5757 (tags/jx/v2.3~1) Add I18N support.
	>>384f1e0d5106c9c6033311a608b91c69332fe0a8 (tags/jx/v2.2^0) Bugfix: allow spaces in username.
	>>e5e62107f8f8d0a5358c3aff993cf874935bb7fb (tags/jx/v2.1^0) fixed typo: -help to --help
	>>5d7657b2f1a8e595c01c812dd5b2f67ea133f456 (tags/jx/v2.0^0) Parse arguments using getopt_long.
	>>3e6070eb2062746861b20e1e6235fed6f6d15609 (tags/jx/v1.0^0) Show version.
	>>75346b3283da5d8117f3fe66815f8aaaf5387321 (tags/jx/v1.0~1) Hello world initialized.

@@创建里程碑.

<1>: git tag 	<tagname> [<commit>]
<2>: git tag -a <tagname> [<commit>]
<3>: git tag -m <msg> <tagname> [<commit>]
<4>: git tag -s <tagname> [<commit>]
<5>: git tag -u <key-id> <tagname> [<commit>]

其中用法<1>是创建轻量级里程碑.
	用法<2>和<3>相同,都是创建带说明的里程碑,其中用法<3>直接通过-m参数提交里程碑的创建说明.
	用法<4>和<5>相同,都是创建带GnuPG签名的里程碑,其中用法<5>用 -u参数指定的私钥进行签名.
	创建里程碑需要输入里程碑的名字(<tagname>)和一个可选的提交ID(<commit>)
	如果没有提供提交ID,则基于头指针HEAD创建里程碑.

<1>轻量级里程碑
	轻量级里程碑最简单,创建时无须输入描述信息.我们来看看如何创建轻量级里程碑.
	(1)先创建一个空提交.
	$ git ci --allow-empty -m 'blank commit'.
	(2)在刚刚创建的空提交上创建一个轻量级里程碑,名为mytag.
	省略了<commit>参数,相当于在HEAD上即最新的空提交上创建里程碑.
	$ git tag mytag
	(3)查看里程碑,可以看到该里程碑已经创建.
	$ git tag -l my*
	轻量级里程碑被的奥秘:
		当创建了里程碑mytag后,会在版本库.git/refs/tags目录创建一个新文件.
		查看下这个引用文件的内容,会发现是一个40位的SHA1哈希值.
		$ cat .git/refs/tags/mytag
		>>aebc86c490cd68614a6c2ee8a797c97c83a2bdb1
		用git cat-file命令检查轻量级里程碑指向的对象.轻量级里程碑实际上指向的是一个提交.
		$ git cat-file -t mytag
		>> commit
		查看该提交的内容,发现就是刚刚进行的空提交.
		$ git cat-file -p mytag
		>>tree 6d736b5a3287bae0d0d7a73ab792925038023cd5
		>>parent d901dd8170f67fec607828905d5fbd91e3272400
		>>author user1 <user1@molloc.com> 1420467101 +0800
		>>committer user1 <user1@molloc.com> 1420467101 +0800

		>>blank commit.

		>>Signed-off-by: user1 <user1@molloc.com>
	轻量级里程碑的缺点:
		轻量级里程碑的创建过程没有记录.因此我发知道是谁创建的里程碑.何时创建的里程碑.
		在团队协同开发时,尽量不要采用此种偷懒的方式创建里程碑.而是采用后两种方式.
		还有git describe命令默认不使用轻量级里程碑生成版本描述字符串.
		执行git describe命令,发现生成的版本描述字符串,使用的是前一个版本上的里程碑名字.
		$ git describe
		>>jx/v1.0-3-gaebc86c
		使用--tags参数,也可以将轻量级里程碑用作版本描述符.
		$ git describe --tags
		>>mytag
<2>带说明的里程碑
	带说明的里程碑,就是使用参数-a或-m <msg>调用git tag命令,在创建里程碑的时候提供一个关于
	该里程碑的说明.
	(1)还是先创建一个空提交.
	$ git commit --allow-empty -m 'blank commit for annotated tag test.'
	(2)在刚刚创建的空提交上创建一个带说明的里程碑,名为mytag2
	下面的命令使用了-m <msg>参数,在命令行给出了新建里程碑的说明.
	$ git tag -m 'My first annotated tag.' mytag2
	(3)查看里程碑,可以看到该里程碑已经创建.
	$ git tag -l my* -n1
	>>mytag           blank commit.
	>>mytag2          My first annotated tag.

	下面来看看带说明里程碑的奥秘,当创建了带说明的里程碑mytag2后,会在版本库的.git/refs/tags目录下
	创建一个新的引用文件.
	1.查看下这个引用文件的内容
	$ cat .git/refs/tags/mytag2
	>>34ca4ca0cc4ed8a69491935135937b626c711ac3
	2.用git cat-file命令检查该里程碑(带说明的里程碑)指向的对象,会发现指向的不在是一个提交,
	  而是一个tag对象.
	  $ git cat-file -t mytag2
	  >>tag
	3.查看该提交的内容,会发现mytag2对象的内容不是之前我们熟悉的提交对象的内容,而是包含了创建里程碑
	时的说明,以及对应的提交ID等信息.
	$ git cat-file -p mytag2
	>>object 5c2b1f0872594d77471308eb68788c061848a7aa
	>>type commit
	>>tag mytag2
	>>tagger user1 <user1@molloc.com> 1420468395 +0800

	>>My first annotated tag.
	由此可见使用了带说明的里程碑,会在版本库中建立一个新的对象(tag对象),这个对象会记录创建里程碑的用户(tagger)
	创建里程碑的时间,以及为什么要创建里程碑.这就避免了轻量级里程碑因为匿名创建而无法追踪的缺点.
	带说明的里程碑是一个tag对象,在版本库中以一个对象的方式存在,并用一个40位的SHA1哈希值来表示,这个哈希值的
	生成方法和前面介绍的commit对象,tree对象,blob对象一样.至此,Git对象库的四类对象我们就都已经研究到了.
	$ git cat-file tag mytag2 | wc -c
	$(printf "tag 148\000"; git cat-file tag mytag2) | sha1sum
	虽然mytag2本身是一个tag对象,但是很多git命令中,可以直接将其视为一个提交.下面git log命令,显示mytag2指向
	的提交日志.
	$ git log -1 --pretty=oneline mytag2
	>>5c2b1f0872594d77471308eb68788c061848a7aa blank commit for annotated tag test.
	有时需要得到里程碑指向的提交对象的SHA1.
	$ gir rev-parse mytag2 //看到的并不是提交对象的ID,而是tag对象的ID.
	$ git rev-parse mytag2^0 //可以获得mytag2对象所指向的提交对象的ID.
	$ git rev-parse mytag^{} | git rev-parse mytag2~0 | git rev-parse mytag2^{commit} //可以获得mytag2对象所指向的提交对象的ID.
@@
<3>带签名的里程碑.
	带签名的里程碑和上面介绍的带说明的里程碑本质上是一样的.都是在创建里程碑的时候在git对象库中生成一个tag
	对象.只不过带签名的里程碑多做了一个工作.为里程碑对象添加GnuPG签名.
	创建带签名的里程碑也非常简单,使用参数-s或-u <key-id>即可.还可以使用-m <msg>参数直接在命令行中提交里程碑的描述.
	创建带签名的里程碑的一个前提是需要安装GnuPG,并且奖励相应的公钥/私钥对.
	$ yum install -y gunpg
	(1)先创建一个空提交
	$ git commit --allow-empty -m 'blank commit for GnuPG-signed tag test.'
	(2)直接在刚创建的空提交上创建一个带签名的里程碑mytag3可能会保存.
	$ git tag -s -m 'My first GPG-signed tag.' mytag3
	>>gpg: skipped "user1 <user1@molloc.com>": No secret key
	>>gpg: signing failed: No secret key
	>>error: gpg failed to sign the data
	>>error: unable to sign the tag
	之所以签名失败,是因为找不到签名可用的公钥/私钥对.使用下面的命令可以查看当前可以的GnuPG公钥.
	$ gpg --list-keys
	时间上创建带签名的里程碑时,并非一定要使用签名者本人的公钥/私钥对进行签名.使用-u <key-id>
	参数调用就可以指定的公钥/私钥对进行签名,对于此例可以使用FBC49D01作为<key-id>.当如果没有
	可以的公钥/私钥对,或者希望使用提交者本人的公钥/私钥对进行签名,就需要为提交者:
	user1 <user1@molloc.com>创建对于的公钥/私钥.
	使用gpg --gen-key来创建公钥/私钥对.
	$ gpg --gen-key
	Note:在创建公钥/私钥对时,会提示输入用户名,输入user1,提示输入邮件地址,输入user1@molloc.com
	其他可以采用默认值.
	Note:在提示输入密码时,为了简单起见可以直接按下回车.即使用空口令.
	Note:在生成公钥/私钥对过程中,会提示用户做一些随机操作以便产生更好的随机数,这个不停的摇晃鼠标就可以了.
	$ gpg --list-keys
	>>/root/.gnupg/pubring.gpg
	>>------------------------
	>>pub   2048R/22C76F3A 2015-01-05
	>>uid                  user1 <user1@molloc.com>
	>>sub   2048R/EDB1E9FD 2015-01-05
	$ git tag -s -m 'My first GPG-signed tag.' mytag3
	和带说明的里程碑一样,在Git对象库中也建立了一个tag对象,查看该tag对象可以卡卡南道其中包含的GnuPG签名.
	>>object 658eebd9a6583015434a8fdcd29b471e57acc62e
	>>type commit
	>>tag mytag3
	>>tagger user1 <user1@molloc.com> 1420472883 +0800

	>>My first GPG-signed tag.
	>>-----BEGIN PGP SIGNATURE-----
	>>Version: GnuPG v2.0.14 (GNU/Linux)

	>>iQEcBAABAgAGBQJUqrIzAAoJEBBicIgix286KdMH/0PLYXJoq6Hz8V0/MKnKr71h
	>>lGCzUPsIimmgtTk17NwxtFXj+fS0obQQ7AIi6a0vz80eoQ3ZUuMuRmgPFx7vurkj
	>>22FdwoZZxxXqF/dByqA8NHqzHWGtu6Fi8usj0H3teRbtvveCkG/7QU4UhgrlwfZ0
	>>1AIGw/N2F53bPnQRrIpG6eaahic664prbCwvi4Gqfk1/soytCxIF0XIpgJdpqKU7
	>>6JHLY+5mO7VhZVOImQywTWuGazXtWo5BTeNhwLwmz20K4QLdgXescRGIdtRLsf20
	>>AzcBmxOUe3H4rJZZXEAYNN4k5MC4/d+np8jMgVeoPXZ47UT/a31UAzUQtY+3MD0=
	>>=lcbA
	>>-----END PGP SIGNATURE-----
	要验证签名的有效性,如果直接使用gpg命令会比较麻烦,因为需要将这个文件拆分为两个,一个是不包含
	签名的里程碑内容,另外一个是签名本身,还好可以使用命令git tag -v来验证里程碑签名的有效性.
	$ git tag -v mytag3
	>>object 658eebd9a6583015434a8fdcd29b471e57acc62e
	>>type commit
	>>tag mytag3
	>>tagger user1 <user1@molloc.com> 1420472883 +0800

	>>My first GPG-signed tag.
	>>gpg: Signature made Mon 05 Jan 2015 11:48:03 PM CST using RSA key ID 22C76F3A
	>>gpg: Good signature from "user1 <user1@molloc.com>"

@@
删除里程碑:
	如果里程碑建立在错误的提交上,或者对里程碑的命名不满意,可以删除里程碑,删除里程碑使用
	命名`git tag -d`西面用命令删除里程碑mytag.
	$ git tag -d mytag
	>>Deleted tag 'mytag' (was aebc86c)
	里程碑没有类似reflog的变更记录机制,一旦删除了不易恢复,慎用,在删除里程碑mytag的命令
	输出中,会显示该里程碑所对应的提交ID,一旦发现删除错误,赶紧补救还来得及,下面的命令实现
	对里程碑mytag的重建.
	$ git tag mytag aebc86c

	Git没有提供对里程碑重命名的命令,如果对里程碑名字不满意的话,可以删除旧的里程碑,然后重新
	用新的名字创建里程碑.
	为什么没有提供重命名里程碑的命令呢?按理只要将.git/refs/tags下的引用的文件改名就可以了
	.这是因为里程碑的名字不但反应在.git/refs/tags引用目录下的文件名,而且对于带说明或签名的
	里程碑,里程碑的名字还反应在tag对象的内容中.尤其是带签名的里程碑,如果修改里程碑的名字,不但
	里程碑对象内容势必要变化,而且里程碑也要重新进行签名.这显示难以实现.

	`git filter-branch`命令实现对里程碑自动重命名方法,但是那个方法也不能毫发无损地实现对
	签名里程碑的重命名,被重命名的签名里程碑中的签名会被去除,从而成为带说明的里程碑.

@@不要随意更改里程碑.
	里程碑建立后,如果需要更改,可以使用同样的里程碑名称重新建立,不要需要加上-f或--force
	参数强制覆盖已有的里程碑.
	更改里程碑要慎用,一个原因是里程碑从概念上将是对历史提交的一个标记,不应该随意变动.
	另外一个原因是里程碑一旦被他人同步,如果修改里程碑,已经同步该里程碑的用户并不会自动
	更新,这就导致一个相同名称里程碑在不同用户的版本库中的指向不同.

@@共享里程碑
	现在看看用户user1的工作区状态,可以看出现在的工作区相比上游有三个新的提交.
	$ git status
	>>On branch master
	>>Your branch is ahead of 'origin/master' by 3 commits.
	>>  (use "git push" to publish your local commits)
	>>nothing to commit, working directory clean
	那么如果执行`git push`命令向上游推送,会将本地创建的三个里程碑推送到上游吗?
	想上游推送.
	$ git push
	>>Counting objects: 3, done.
	>>Compressing objects: 100% (3/3), done.
	>>Writing objects: 100% (3/3), 463 bytes | 0 bytes/s, done.
	>>Total 3 (delta 1), reused 0 (delta 0)
	>>To file:///root/source/gitrepo/hello-world.git
    >>d901dd8..658eebd  master -> master
    通过执行`git ls-remote`可以查看上游版本库的引用,会发现本地建立的三个里程碑,并没有
    推送到上游.
    $ git ls-remote orgin my*
    创建的里程碑,默认只在本地版本库中可见,不会因为对分支执行推送而将里程碑也推送到
    远程版本库.这样的设计显然更为合理,否则的话,每个用户本地创建的里程碑都自动向
    上游推送,那么上游的里程碑将有多么杂乱,而且不同用户创建的相同名称的里程碑会相互
    覆盖.

    <1>:显示推送以共享里程碑
    如果用户确实需要将某些本地建立的里程碑推送到远程版本库.需要在`git push`中
    明确地表示出来.下面在用户user1的工作区中执行命令,将mytag里程碑共享到上游版本库.
    $ git push origin mytag
    >>Total 0 (delta 0), reused 0 (delta 0)
	>>To file:///root/source/gitrepo/hello-world.git
	>> * [new tag]         mytag -> mytag
	如果需要将本地建立的所有里程碑全部推送到远程版本库,可以使用通配符.
	$ git push origin refs/tags/\*
	>>Counting objects: 2, done.
	>>Compressing objects: 100% (2/2), done.
	>>Writing objects: 100% (2/2), 681 bytes | 0 bytes/s, done.
	>>Total 2 (delta 0), reused 0 (delta 0)
	>>To file:///root/source/gitrepo/hello-world.git
	>> * [new tag]         mytag2 -> mytag2
	>> * [new tag]         mytag3 -> mytag3
	再用命令`git ls-remote`查看上游版本库的引用,会发现本地建立的三个里程碑,已经能够
	在上游中看到了.
	$ git ls-remote origin my*
	>>aebc86c490cd68614a6c2ee8a797c97c83a2bdb1	refs/tags/mytag
	>>34ca4ca0cc4ed8a69491935135937b626c711ac3	refs/tags/mytag2
	>>5c2b1f0872594d77471308eb68788c061848a7aa	refs/tags/mytag2^{}
	>>9c463b68319543b3e3ddcc736bd6a4abf9218f9a	refs/tags/mytag3
	>>658eebd9a6583015434a8fdcd29b471e57acc62e	refs/tags/mytag3^{}
	用户从版本库执行拉回操作,会自动获取里程碑么?
	用户user2的工作区中如果执行`git fetch`或`git pull`操作,能自动将用户user1推送到
	共享版本库中的里程碑获取到本地版本库吗?
	$ cd/path/to/user2/workspace/hello-world
	执行git pull,从上游版本库中获取提交.
	$ git pull
	remote: Counting objects: 5, done.
	>>remote: Compressing objects: 100% (5/5), done.
	>>remote: Total 5 (delta 1), reused 0 (delta 0)
	>>Unpacking objects: 100% (5/5), done.
	>>From file:///root/source/gitrepo/hello-world
	>>   d901dd8..658eebd  master     -> origin/master
	>> * [new tag]         mytag3     -> mytag3
	>> * [new tag]         mytag      -> mytag
	>> * [new tag]         mytag2     -> mytag2
	>>Updating d901dd8..658eebd
	>>Fast-forward
	可见执行`git pull`操作,能够在获取远程共享版本库的提交的同时,获取新的里程碑.
	$ git tag -n1 -l my*
	>>mytag           blank commit.
	>>mytag2          My first annotated tag.
	>>mytag3          My first GPG-signed tag.
	?里程碑变更能自动同步吗?
	里程碑可以强制更新,当里程碑改变后,已经获取到里程碑的版本库再次使用获取或拉回操作.
	能够自动更新里程碑吗?答案是不能,可以看看下面的操作.
	1>用户user2强制更新里程碑mytag2
	$ git tag -f -m 'user2 update this annotated tag.' mytag2 HEAD^
	>>Updated tag 'mytag2' (was 34ca4ca)
	2>里程碑mytag2已经是不同的对象了.
	$ git cat-file -p mytag2
	>>object 5c2b1f0872594d77471308eb68788c061848a7aa
	>>type commit
	>>tag mytag2
	>>tagger user2 <user2@molloc.com> 1420553545 +0800

	>>user2 update this annotated tag.

	3>为了更改远程共享服务器中的里程碑,需要需要显示推送,即在推送时写上要推送的里程碑名称.
	$ git push origin mytag2
	>>To file:///root/source/gitrepo/hello-world.git
	>> ! [rejected]        mytag2 -> mytag2 (already exists)
	>>error: failed to push some refs to 'file:///root/source/gitrepo/hello-world.git'
	>>hint: Updates were rejected because the tag already exists in the remote.
	$ git push -f origin mytag2
	>>Counting objects: 1, done.
	>>Writing objects: 100% (1/1), 168 bytes | 0 bytes/s, done.
	>>Total 1 (delta 0), reused 0 (delta 0)
	>>To file:///root/source/gitrepo/hello-world.git
	>> + 34ca4ca...876f9ce mytag2 -> mytag2 (forced update)
	4>切换到用户user1的工作区,执行拉回操作,没有获取到新的里程碑.
	$ git pull
	>>Already up-to-date.
	5>用户user1必须显式地执行拉回操作,即要在`git pull`的参数中使用`引用表达式`.
	所谓引用表达式就是用冒号分割的引用名称或通配符,用在这里代表用户远程共享
	版本库的引用refs/tags/mytag2覆盖本地同名引用.
	$ git pull orgin refs/tags/mytag2:refs/tags/mytag2
	>>remote: Counting objects: 1, done.
	>>remote: Total 1 (delta 0), reused 0 (delta 0)
	>>Unpacking objects: 100% (1/1), done.
	>>From file:///root/source/gitrepo/hello-world
	>> - [tag update]      mytag2     -> mytag2
	>>Already up-to-date.

@@关于里程碑的共享和同步操作,看似很繁琐,但是用心体会就会感觉到Git关于里程碑的共享的设计是非常
合理人性化的.
	(1):里程碑共享,必须显式的推送.即在推送命令的参数中.标明要推送的那个里程碑.
	显式推送是防止用户随意推送里程碑导致共享版本库中里程碑泛滥的情况.当然还可以参考
	`Gitolite服务架设`为共享版本库添加授权,只允许部分用户向服务器推送里程碑.
	(2):执行获取或拉回操作,自动从远程版本库获取新里程碑,并在本地版本库中创建.
	获取或拉回操作,只会将获取的远程分支所包含的新里程碑同步到本地,而不会将
	远程版本库的其他分支中的里程碑获取到本地.这既方便了里程碑的取得.又防止本地
	里程碑因同步远程版本库而泛滥.
	(3):如果本地已有同名的里程碑,默认不会从上游同步里程碑,即使两者里程碑的指向是不同的.
	理解这一点非常重要,这也就要求里程碑一旦共享.就不要在修改.

@@删除远程版本库中的里程碑.
	假如向远程版本库推送里程碑后,忽然发现里程碑创建在了错误的提交上.为了防止其他人获取到错误
	的里程碑.应该尽快将里程碑删除.
	删除本地里程碑非常简单,使用`git tag -d <tagname>`就可以了,但是如何撤销已经推送到远程
	版本库的里程碑呢?需要登陆到服务器上吗?或者需要麻烦管理员?不必,可以直接在本地版本库
	执行命令删除远程版本库中的里程碑.
	命令: git push <remote_url> :<tagname>
	该命令的最后一个参数实际上是一个引用表达式,引用表达式一般的格式是:<refs>:<ref>.
	该推送命令使用的是引用表达式冒号前的引用被省略,其含义是将一个空值推送到
	远程版本库对应的引用中.亦即删除远程版本库中的相关的引用.这个命令不但可以用于删除
	里程碑.还可以删除远程版本库中的分支.
	(1):用户user1 执行推送操作删除远程共享版本库中的里程碑.
	$ git push origin :mytag2
	>>To file:///root/source/gitrepo/hello-world.git
    >>- [deleted]         mytag2

@@里程碑的命名规范
	在正式项目的版本库管理中,要为里程碑创建订立一些规则:
	1.对创建里程碑进程权限控制.
	2.不要使用轻量级里程碑(只用于本地临时性的里程碑)而是要使用带说明的里程碑.甚至
	要求必须使用带签名的里程碑.
	3.如果使用带签名的里程碑,可以考虑设置专用账户,使用专用的私钥创建签名.
	4.里程碑的命名要使用统一的风格,并很容易和最终产品显示的版本号相对应.
	5.不要以`-`开头,以免在命令行中被当成命令的选项.
	6.可以包含路径分隔符`/`但是路径分隔符不能位于最后.
	使用路径分隔符创建tag实际上会在引用目录下创建子目录.例如名为`demo/v1.2.1`的里程碑
	就会创建目录`.git/refs/tags/demo`并在该目录下创建引用文件v1.2.1
	7.不能出现两个连续的`..`.因为两个连续的点被用于表示版本范围,当然更不能是用`...`
	8.如果在里程碑命名中是用了路径分隔符`/`,就不能在任何一个分隔路径中以点`.`开头.
	这是因为里程碑在用简写格式表达式时,可能造成以一个点`.`开头这样的引用名称在用
	作版本范围的最后一个版本时,本来两点操作变成了三点操作符.从而造成歧义.
	9.不能在里程碑名称的最后出现`.`否则作为第一个参数出现在表示版本范围的表达式中时,
	本来版本范围表达式可能用的是两点操作符,结果被误做三点操作符.
	10.不能使用特殊字符:eg:'~','^',':','?','*','[',以及字符\177(删除字符)或小于
	\040(32)的Ascii码都不能使用.
	11.不能以'.lock'结尾,因为'.lock'结尾的文件是里程碑操作过程中的临时文件.
	12.不能包含'@{'字串,否则易和'@{<num>'(reflog)语法相混淆.
	13.不能包含反斜线'\\'.因为反斜线用于命令或shell脚本会造成意外.
	Git还专门为检查引用名称是否符合规范提供了一个命令:`git check-ref-format`.
	若该命令返回值为0,则引用名称符合规范.若返回值为1,则不符合规范.
	$ git check-ref-format refs/tags/.name || echo "返回 $? 不合法"

@@linux的里程碑.
	$ git ls-remote --tags git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-2.6-stable.git v2.6.36*

	以-rc<num>为后缀的是先于正式版发布的预发布版本.

	正式版发布就去掉了-rc后缀

@@Android项目
	同一个项目要在里程碑命名上遵照统一标准,并能够和软件的版本号正确地对应.

	(1):Android项目中的里程碑命名,会发现其里程碑的命名格式android-<大版本号>_r<小版本号>
	$ git ls-remote --tags git://android.git.kernel.org/platform/manifest.git android-2.2*
	(2):里程碑的创建过程中使用了专用账号和GnuPG签名.
