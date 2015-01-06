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

