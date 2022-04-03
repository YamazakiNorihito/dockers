## docker alpine

### search

```bash
$ docker search alpine -f is-official=true -f stars=1
NAME      DESCRIPTION                                     STARS     OFFICIAL   AUTOMATED
alpine    A minimal Docker image based on Alpine Linux…   7752      [OK]
```

### pull

```bash
$ docker pull alpine

```

### ash シェル

```bash
docker run -it --name AL02 alpine:latest /bin/ash

/ # ash --help
BusyBox v1.33.1 () multi-call binary.

Usage: ash [-/+OPTIONS] [-/+o OPT]... [-c 'SCRIPT' [ARG0 [ARGS]] / FILE [ARGS] / -s [ARGS]]

Unix shell interpreter
```

### 一般ユーザー作成

ユーザー作成後、ユーザーリストにあるか確認

adduser コマンドで自動でホームディレクトリが作成される

```bash
/ # adduser alpine
Changing password for alpine
New password:
Bad password: too short
Retype password:
passwd: password for alpine changed by root

/ # cat /etc/passwd
root:x:0:0:root:/root:/bin/ash
bin:x:1:1:bin:/bin:/sbin/nologin
daemon:x:2:2:daemon:/sbin:/sbin/nologin
adm:x:3:4:adm:/var/adm:/sbin/nologin
lp:x:4:7:lp:/var/spool/lpd:/sbin/nologin
sync:x:5:0:sync:/sbin:/bin/sync
shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
halt:x:7:0:halt:/sbin:/sbin/halt
mail:x:8:12:mail:/var/mail:/sbin/nologin
news:x:9:13:news:/usr/lib/news:/sbin/nologin
uucp:x:10:14:uucp:/var/spool/uucppublic:/sbin/nologin
operator:x:11:0:operator:/root:/sbin/nologin
man:x:13:15:man:/usr/man:/sbin/nologin
postmaster:x:14:12:postmaster:/var/mail:/sbin/nologin
cron:x:16:16:cron:/var/spool/cron:/sbin/nologin
ftp:x:21:21::/var/lib/ftp:/sbin/nologin
sshd:x:22:22:sshd:/dev/null:/sbin/nologin
at:x:25:25:at:/var/spool/cron/atjobs:/sbin/nologin
squid:x:31:31:Squid:/var/cache/squid:/sbin/nologin
xfs:x:33:33:X Font Server:/etc/X11/fs:/sbin/nologin
games:x:35:35:games:/usr/games:/sbin/nologin
cyrus:x:85:12::/usr/cyrus:/sbin/nologin
vpopmail:x:89:89::/var/vpopmail:/sbin/nologin
ntp:x:123:123:NTP:/var/empty:/sbin/nologin
smmsp:x:209:209:smmsp:/var/spool/mqueue:/sbin/nologin
guest:x:405:100:guest:/dev/null:/sbin/nologin
nobody:x:65534:65534:nobody:/:/sbin/nologin
alpine:x:1000:1000:Linux User,,,:/home/alpine:/bin/ash
```

### 一般ユーザー切り替え

```bash
/ # su - alpine
188b92de8384:~$
```

### パッケージ検索

1. vi エディタ

```bash
188b92de8384:~$ which vim
188b92de8384:~$ which vi
/usr/bin/vi

188b92de8384:~$ ls /usr/bin/vi -l
lrwxrwxrwx    1 root     root            12 Aug  5 12:25 /usr/bin/vi -> /bin/busybox
```

### ネットワーク情報

```bash
188b92de8384:~$ ip a
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
2: sit0@NONE: <NOARP> mtu 1480 qdisc noop state DOWN qlen 1000
    link/sit 0.0.0.0 brd 0.0.0.0
20: eth0@if21: <BROADCAST,MULTICAST,UP,LOWER_UP,M-DOWN> mtu 1500 qdisc noqueue state UP
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever

```

```bash
188b92de8384:~$ ifconfig
eth0      Link encap:Ethernet  HWaddr 02:42:AC:11:00:02
          inet addr:172.17.0.2  Bcast:172.17.255.255  Mask:255.255.0.0
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:16 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0
          RX bytes:1256 (1.2 KiB)  TX bytes:0 (0.0 B)

lo        Link encap:Local Loopback
          inet addr:127.0.0.1  Mask:255.0.0.0
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:0 errors:0 dropped:0 overruns:0 frame:0
          TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000
          RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
```

### ファイルシステム構造

```bash
188b92de8384:~$ ls -1 /
bin
dev
etc
home
lib
media
mnt
opt
proc
root
run
sbin
srv
sys
tmp
usr
var
```

```bash
188b92de8384:~$ ls /bin -F
arch@          cp@            egrep@         gzip@          linux64@       more@          ping6@         run-parts@     sync@
ash@           date@          false@         hostname@      ln@            mount@         pipe_progress@ sed@           tar@
base64@        dd@            fatattr@       ionice@        login@         mountpoint@    printenv@      setpriv@       touch@
bbconfig@      df@            fdflush@       iostat@        ls@            mpstat@        ps@            setserial@     true@
busybox*       dmesg@         fgrep@         ipcalc@        lzop@          mv@            pwd@           sh@            umount@
cat@           dnsdomainname@ fsync@         kbd_mode@      makemime@      netstat@       reformime@     sleep@         uname@
chgrp@         dumpkmap@      getopt@        kill@          mkdir@         nice@          rev@           stat@          usleep@
chmod@         echo@          grep@          link@          mknod@         pidof@         rm@            stty@          watch@
chown@         ed@            gunzip@        linux32@       mktemp@        ping@          rmdir@         su@            zcat@

```

名前の最後@記号がついているものは、シンボリックリンク

### パケージ管理コマンド

1. コンテナにインストールされているパッケージ一覧表示

```bash
188b92de8384:~$ apk info -a
WARNING: Ignoring https://dl-cdn.alpinelinux.org/alpine/v3.14/main: No such file or directory
WARNING: Ignoring https://dl-cdn.alpinelinux.org/alpine/v3.14/community: No such file or directory
musl
busybox
alpine-baselayout
alpine-keys
libcrypto1.1
libssl1.1
ca-certificates-bundle
libretls
ssl_client
zlib
apk-tools
scanelf
musl-utils
libc-utils

```

### ユーザー切り替え

1. 元のユーザーに戻す場合

```bash
188b92de8384:~$ exit
/ # apk info -a
```

### パケージ更新

```bash
/ # apk search samba
WARNING: Ignoring https://dl-cdn.alpinelinux.org/alpine/v3.14/main: No such file or directory
WARNING: Ignoring https://dl-cdn.alpinelinux.org/alpine/v3.14/community: No such file or directory
/ # apk update
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/community/x86_64/APKINDEX.tar.gz
v3.14.1-26-g1460b8f70f [https://dl-cdn.alpinelinux.org/alpine/v3.14/main]
v3.14.1-43-gbd086f4217 [https://dl-cdn.alpinelinux.org/alpine/v3.14/community]
OK: 14934 distinct packages available
/ # apk search samba
samba-libs-4.14.5-r0
samba-util-libs-4.14.5-r0
py3-impacket-0.9.22-r1
samba-client-libs-4.14.5-r0
samba-dc-libs-4.14.5-r0
samba-winbind-clients-4.14.5-r0
samba-test-4.14.5-r0
samba-server-openrc-4.14.5-r0
samba-server-4.14.5-r0
samba-dc-4.14.5-r0
samba-libs-py3-4.14.5-r0
samba-server-libs-4.14.5-r0
samba-libnss-winbind-4.14.5-r0
samba-dev-4.14.5-r0
samba-common-tools-4.14.5-r0
samba-common-server-libs-4.14.5-r0
samba-client-4.14.5-r0
samba-4.14.5-r0
libwbclient-4.14.5-r0
samba-pidl-4.14.5-r0
samba-winbind-krb5-locator-4.14.5-r0
samba-doc-4.14.5-r0
samba-winbind-4.14.5-r0
py3-samba-4.14.5-r0
samba-common-4.14.5-r0
acf-samba-0.10.0-r4
```

- apk update
  利用可能パッケージのインデックスを更新

  - これを実行しても、インストールされたパッケージは更新されない
  - これから取ってくるパッケージの参照先を最新にする
  - Docker で使う場合、最初の方に書く

- apk upgrade
  現在インストールされているパッケージをアップグレード

  - 別のパッケージ管理システムの話ではあるが、 Docker ではなるべく使わずに、親 image に upgrade を依頼することを推奨している

### パッケージ Install

```bash
/ # apk -U add bash
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/community/x86_64/APKINDEX.tar.gz
(1/4) Installing ncurses-terminfo-base (6.2_p20210612-r0)
(2/4) Installing ncurses-libs (6.2_p20210612-r0)
(3/4) Installing readline (8.1.0-r0)
(4/4) Installing bash (5.1.4-r0)
Executing bash-5.1.4-r0.post-install
Executing busybox-1.33.1-r3.trigger
OK: 8 MiB in 18 packages

/ # apk info bash
bash-5.1.4-r0 description:
The GNU Bourne Again shell

bash-5.1.4-r0 webpage:
https://www.gnu.org/software/bash/bash.html

bash-5.1.4-r0 installed size:
1296 KiB

```

## linux

### 基礎

1. ファイル名
   使える文字は以下の通り

- アルファベット
- 数字
- 記号（「\_」[-]「.」）

  「\*」「!」「?」のような記号（メタキャラクタ）はファイル名やディレクトリに使用してはいけない。
  「.」で始まるファイルは隠しファイル（ユーザー環境やアプリケーションの設定ファイルでよく使用される

2. ファイルの種類

- 通常ファイル
- ディレクトリ
- リンクファイル
  - ファイル名とファイルの実態を紐づけたファイル
  - シンボリックリンクとハードリンクがある
- 特殊ファイル
  - あらゆるデバイスをファイルとして抽象かして扱う
  - プリンタに対応するデバイスファイルにデータを書き込むとプリンタから出力されるなど、ファイルを通じてデバイスを扱える

3. ファイル一覧

```bash
/tmp # ls -l
total 0
-rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
```

ls コマンドで表示された項目は以下の通り。

| 項目         | 意味                   |
| ------------ | ---------------------- |
| -rw-r--r--   | ファイルモード         |
| 1            | リンク数               |
| root         | ファイルの所有者       |
| root         | ファイルの所有グループ |
| 0            | ファイルサイズ         |
| Aug 18 14:56 | 最終更新日時           |
| test.txt     | ファイル名             |

ファイルモードの左端の１文字目はファイルの種類
| 文字 | 説明 |
| ------------ | ---------------------- |
|-|通常ファイル|
|d|ディレクトリ|
|l|リンクファイル|
|c|特殊ファイル（キャラクデバイスファイル|
|b|特殊ファイル（ブロックデバイスファイル|

3. 相対パス
   | 記号 | 説明 |
   | ------------ | ---------------------- |
   |.|ドット カレントディレクトリ|
   |..|１つ上のディレクトリ|
   |~|ホームディレクトリ|

4. ディレクトリ構成

| ディレクトリ | 説明                                           |
| ------------ | ---------------------------------------------- |
| bin          | 一般ユーザーが使える基本コマンド               |
| boot         | Linux カーネルなど軌道に必要なファイル         |
| dev          | デバイスファイル                               |
| etc          | システム設定ファイルやサービス起動ファイル     |
| home         | ユーザーのホームディレクトリ                   |
| lib          | ライブラリファイル                             |
| lost+found   | 破損ファイルの断片を格納                       |
| media        | 外付けストレージやメディア用のマウントポイント |
| mnt          | 一時的に使われるマウントポイント               |
| opt          | オプション的なソフトウェアのインストール先     |
| proc         | プロセス情報を扱う特殊ディレクトリ             |
| root         | root ユーザーのホームディレクトリ              |
| run          | 実行中のプログラムやシステム情報のデータ       |
| sbin         | システム管理者が利用する基本コマンド           |
| srv          | ホスト固有のサービス情報データ                 |
| swap         | スワップ用                                     |
| sys          | システム情報を扱う特殊なディレクトリ           |
| tmp          | 一時ファイル置き場                             |
| usr          | プログラムやライブラリなど                     |
| var          | ログファイルなど頻繁に更新されるファイル       |

5. ファイル表示

- cat(conCATenate) コマンド

  - text ファイルの内容を表示する

  ```bash
  / # cat etc/motd
  Welcome to Alpine!

  The Alpine Wiki contains a large amount of how-to guides and general
  information about administrating Alpine systems.
  See <http://wiki.alpinelinux.org/>.

  You can setup the system with the command: setup-alpine

  You may change this message by editing /etc/motd.

  ```

- less

  - 1 画面では収まりきらない大きなファイルを表示する場合使用する
  - テキストファイルの内容を１ページずつ表示
    ```bash
      /# less /etc/services
    ```
  - コマンドのキー操作
    - ↓ :1 行下にスクロール
    - ↑:1 行 ↑ にスクロール
    - スペース :1 画面下方向に進める
    - ctrl + b :1 画面 ↑ 方向に進める
    - q :less 終了

- env

  - 環境変数参照
    ```bash
      / # env
      HOSTNAME=d02147933e8e
      SHLVL=1
      HOME=/root
      TERM=xterm
      PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
      PWD=/
    ```

- os inf
  ```bash
    / # cat /etc/issue
    Welcome to Alpine Linux 3.14
    Kernel \r on an \m (\l)
  ```

5. ファイルのコピー、移動、削除

- [Copy](https://atmarkit.itmedia.co.jp/ait/articles/1605/31/news019.html)

  - 書式

  ```bash
    # file to file
    $ > cp コピー元ファイル名 コピー先ファイル名
    # file to dir
    $ > cp コピー元ファイル名 コピー先ディレクトリ
  ```

- [move](https://atmarkit.itmedia.co.jp/ait/articles/1606/13/news024.html)

  - 書式

  ```bash
    # file to file
    $ > mv コピー元ファイル名 コピー先ファイル名
    # file to dir
    $ > mv コピー元ファイル名 コピー先ディレクトリ
  ```

- [remove](https://atmarkit.itmedia.co.jp/ait/articles/1606/06/news013.html)

  - 書式

  ```bash
    $ > rm ファイル名
    $ > rm -r  ディレクトリ名
  ```

6. 圧縮と解凍

- gzip&gunzip

  - linux における圧縮としてよく使われる
  - gzip 形式の拡張子「.gz」
  - 圧縮

    - 書式

      ```bash
        $ > gzip ファイル名
      ```

    - example

      ```bash
        /tmp # ls test
        test.txt
        /tmp/test # gzip test.txt
        /tmp/test # ls -a
        . .. test.txt.gz
      ```

  - 解凍

    - 書式

      ```bash
        $ > gunzip 圧縮ファイル名
      ```

    - example

      ```bash
      /tmp/test # ls
      test.txt.gz
      /tmp/test # gunzip test.txt.gz
      /tmp/test # ls
      test.txt
      ```

- bzip2&xz

  - bzip2

    - 書式

      - 圧縮

        ```bash
          $ > bzip ファイル名
        ```

      - 解凍

        ```bash
          $ > bunzip 圧縮ファイル名
        ```

  - xz

    - install

      ```bash
        $ > apk add xz
      ```

    - 書式

      - 圧縮

        ```bash
          $ > xz ファイル名
        ```

      - 解凍

        ```bash
          $ > unxz 圧縮ファイル名
        ```

- zip&unzip
  - install
    ```bash
    / # apk add zip
    (1/2) Installing unzip (6.0-r9)
    (2/2) Installing zip (3.0-r9)
    Executing busybox-1.33.1-r3.trigger
    OK: 9 MiB in 20 packages
    ```
  - zip
    - gzip コマンドとは異なり元のファイルは削除されない
    - ディレクトリを圧縮するときは「-r」オプションを設定
    - 書式
      ```bash
        $> zip [-r] "圧縮ファイル名" "ファイル名 または ディレクトリ名"
      ```
    - example
      ```bash
        /tmp # ls
        test      test.txt
        /tmp # zip -r test.zip test
          adding: test/ (stored 0%)
          adding: test/test.txt (stored 0%)
      ```
  - unzip
    - gunzip とはことなり圧縮ファイルは削除されない
    - 書式
      ```bash
        $> unzip "圧縮ファイル名"
      ```
    - example
      ```bash
        /tmp # unzip test.zip
        Archive:  test.zip
          creating: test/
        extracting: test/test.txt
        /tmp # ls
        test      test.txt  test.zip
      ```
- tar
  - gzip/bzip2/xz コマンドはディレクトリの圧縮対応していません tar コマンドを使用して１ファイルにまとめる
  - cvf
    - アーカイブ
    - 書式
      ```bash
        $> tar cvf "アーカイブファイル名" "ディレクトリ名"
      ```
      - c オプション：アーカイブの作成
      - v オプション：処理内容の情報出力
      - f オプション：アーカイブファイル名指定
    - example
      ```bash
        /tmp # ls
        test      test.txt  test.zip
        /tmp # tar cvf test.tar test
        test/
        test/yamazaki.md
        test/test.txt
        /tmp #
      ```
  - xvf
    - アーカイブファイル展開
    - 書式
      ```bash
        $> tar xvf "アーカイブファイル名"
      ```
    - example
      ```bash
        /tmp # tar xvf test.tar
        test/
        test/yamazaki.md
        test/test.txt
        /tmp # ls
        test      test.tar  test.txt  test.zip
        /tmp # ls test
        test.txt     yamazaki.md
      ```
  - オプション
    | オプション | 説明 |
    | --- | ---|
    |c| アーカイブ作成|
    |x|アーカイブ展開|
    |v|詳細表示|
    |f|アーカイブファイル指定する|
    |z|gzip 圧縮利用|
    |j|bzip2 圧縮利用|
    |J|xzs 圧縮利用|
    |t|アーカイブの内容を表示|

7. ハードリックとシンボリックリンク
   ハードリンクやシンボリックリンクを使うと、１津のファイルに複数のファイルをつけることができる

- i ノード
  - ファイルサイズ・最終更新日・アクセス権情報・デスク上格納場所など記録している
  - メタデータを管理している
    - ファイルは、データ・メタデータで構成されている
  - すべてのファイル・ディレクトリにはメタデータを管理する i ノードが存在する
  - i ノードには連番がつけられている
    - ls コマンド-i オプションをつけると連番がわかる
  - i ノードとファイルの対応づけをリンクという
- ハードリンク

  - 1 つの i ノードに複数のファイル名をリンクさせること
  - ハードリンクを使うと１つのファイルに複数のファイル名でアクセスできる
  - ディレクトリのハードリンクを作成できない
  - コマンド

    - 所有者の直前にある「２」はハードリンクを表す

    ```bash
      $ ln 元のファイル 作成するリンクファイル

      / # ln tmp/test/test.txt tmp/link1.hard
      / # ls tmp/ -li
      total 12
      342788 -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
      248784 drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
      342789 -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
      248916 -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
      248913 -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
    ```

- シンボリックリンク

  - ファイルに別名を付けす仕組み
  - windows ショートカット/mac エイリアスと同じ
  - コマンド

    ```bash
      $ ln -s 元ファイル 作成するリンク

        / # ln -s  tmp/test/test.txt tmp/link2.sym
        / # ls tmp/ -li
        total 12
        342788 -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        248917 lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        248784 drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        342789 -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
        248916 -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        248913 -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
        / #
    ```

  - シンボリックはファイルへのポインタ（ファイル参照先）を格納している
    - 元ファイルが削除された場合、シンボリックファイルへのアクセスはエラーとなる
    - ハードリンクは、すべてのハードリンクを削除されない限りアクセスできる

- パーミッション

  - 所有者と所有グループ

    - ファイルやディレクトリへのアクセスコントロールするために使う
    - 所有者・・・ファイルやディレクトリを作成したユーザー
    - 所有グループ・・・所有者のプライマリーグループ
    - 所有者と所有グループの確認コマンド
      ```bash
        / # ls -l tmp/
        total 12
        -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
        -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
        / #
      ```

  - 所有者変更

    - 変更できるのは「root」ユーザーのみ
    - chown : CHange OWNer
    - コマンド

      - 指定したディレクトリ以下すべて所有者変更する場合「-R」オプション

      ```bash
        chown [-R] 所有者名 ファイル名またはディレクトリ

        / # ls -l tmp/
        total 12
        -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
        -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
        / # chown  alpine tmp/test.tar
        / # ls -l tmp/
        total 12
        -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        -rw-r--r--    1 alpine   root          2560 Aug 20 14:07 test.tar
        -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
      ```

  - 所有グループ変更

    - 一般ユーザーが変更する場合、一般ユーザーが所臆しているグループのみ
    - root ユーザーは制限なし
    - chgrp・・・CHange GRouP
    - コマンド

      - 指定したディレクトリ以下すべて所有グループ変更する場合「-R」オプション

      ```bash
        $ chgrp [-R] グループ名 ファイル名またはディレクトリ名

        / # ls -l tmp/
        total 12
        -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        -rw-r--r--    1 alpine   root          2560 Aug 20 14:07 test.tar
        -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
        / # chgrp alpine tmp/test.tar
        / # ls -l tmp/
        total 12
        -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
        lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
        drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
        -rw-r--r--    1 alpine   alpine        2560 Aug 20 14:07 test.tar
        -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
        -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
      ```

  - 所有者とグループ同時変更

    ```bash
    $ chown [user]:[group] [targetfile]

      / # ls -l tmp/
      total 12
      -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
      lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
      drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
      -rw-r--r--    1 alpine   alpine        2560 Aug 20 14:07 test.tar
      -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
      -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
      / # chown root:root tmp/test.tar
      / # ls -l tmp/
      total 12
      -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
      lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
      drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
      -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
      -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
      -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
    ```

  - アクセス権

    - 「読出し」「書込み」「実行」の３種類ある
    - アクセス権は、以下のそれぞれに対して設定

      - 所有者ユーザー
      - 所有グループに属するユーザー
      - その他のユーザー
      - 設定例
        - 所有者はファイルの「読出し」「書込み」可能
        - 所有グループはファイルの「読出し」
        - その他はファイルに対して何もできない
      - アクセス権の記号

        | 権限       | 記号        | 数字 | 権限説明（ファイルの場合）         | 権限説明（ディレクトリの場合）                              |
        | ---------- | ----------- | ---- | ---------------------------------- | ----------------------------------------------------------- |
        | 読み取り権 | r（read）   | 4    | cat コマンドなどで読出し可能となる | ディレクトリ内のファイル名一覧を表示することができる        |
        | 書き込み権 | w（write）  | 2    | ファイルの内容の変更が可能となる   | ファイル作成や削除することが可能                            |
        | 実行権     | x（eXecute) | 1    | プログラムとして実行が可能となる   | cd コマンドで移動やディレクトリ名のファイルにアクセスできる |
        | 権限無し   | -           | 0    |                                    |                                                             |

      - パーミッション確認コマンド

        ```bash
          $ ls -l ファイル名またはディレクトリ

          / # ls -l tmp/
          total 12
          -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
          lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
          drwxr-xr-x    2 root     root          4096 Aug 20 14:10 test
          -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
          -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
          -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
        ```

        「-rw-r--r--」・・・先頭の一文字はファイルモードの左端の１文字目はファイルの種類
        ↓ 実質
        「rw-r--r--」・・・左から３文字ずつ「所有者」「所有グループ」「その他」のアクセス権を表す
        ↓ 数字表記
        「644」・・・左から３文字ずつ数字変換して足し合わせたもの。

      - パーミッション変更コマンド

        - 実行できるのは、所有者と root ユーザーのみ
        - chmod・・・Change MODe (change mode)
        - アクセス権の設定はいずれか
          - 3 桁の数字をしている（アクセス権の記号を参照）
          - 記号
            | 種別 | 記号 | 説明 |
            | ---------- | ---- | ---------------- |
            | 対象 | u | 所有者 |
            | 対象 | g | 所有グループ |
            | 対象 | o | その他ユーザ |
            | 対象 | a | すべてのユーザー |
            | 操作 | + | 権限の追加 |
            | 操作 | - | 権限の削除 |
            | 操作 | = | 権限の指定 |
            | アクセス権 | r | 読み取り許可 |
            | アクセス権 | w | 書き込み許可 |
            | アクセス権 | x | 実行許可 |
        - コマンド

          ```bash
            $ chmod [-R] アクセス権 ファイル名またはディレクトリ
          ```

          - 例

            - 「rw-r--r--」の設定
              ```bash
                /tmp/access # ls -l
                total 0
                ----------    1 root     root             0 Aug 31 12:45 sample.txt
                /tmp/access # chmod 644 sample.txt
                /tmp/access # ls -l
                total 0
                -rw-r--r--    1 root     root             0 Aug 31 12:45 sample.txt
              ```
            - 「rwxr-xr-x」の設定
              ```bash
                /tmp/access # ls -l
                total 0
                -rw-r--r--    1 root     root             0 Aug 31 12:45 sample.txt
                /tmp/access # chmod 755 sample.txt
                /tmp/access # ls -l
                total 0
                -rwxr-xr-x    1 root     root             0 Aug 31 12:45 sample.txt
              ```
            - 「rwxr-xr-x」から「rwxrwxr-x」に変更

              ```bash
                /tmp/access # ls -l
                total 0
                -rwxr-xr-x    1 root     root             0 Aug 31 12:45 sample.txt
                /tmp/access # chmod g+w sample.txt
                /tmp/access # ls -l
                total 0
                -rwxrwxr-x    1 root     root             0 Aug 31 12:45 sample.txt
              ```

            - 所有者以外実行権限削除

              ```bash
                tmp/access # ls -l
                total 0
                -rwxrwxr-x    1 root     root             0 Aug 31 12:45 sample.txt
                /tmp/access # chmod go-x sample.txt
                /tmp/access # ls -l
                total 0
                -rwxrw-r--    1 root     root             0 Aug 31 12:45 sample.txt
              ```

  - シェル

    - ユーザーが入力したコマンドをカーネルに実行させる翻訳者（インタープリンタ）役割を果たす
    - システムで利用可能なシェルは/etc/shells ファイルで確認可能

      ```bash
        / # cat etc/shells
        # valid login shells
        /bin/sh
        /bin/ash
        /bin/bash
      ```

    - 種類
      | シェル | 意味 |説明 |
      | ------ | ---- |---- |
      | sh| Bourne Shell|機能がシンプルな UNIX 標準シェル|
      | ash| Almquist Shell |sh を拡張しつつ軽量なシェル。Alpine 補油順|
      | bash| Bourne Again Shell |sh を拡張して多機能にしたシェル。多くのディストリビューション標準|
      | csh| C shell|C |
      | tcsh| |csh を確証したシェル|
      | ksh| Korn shell|sh を拡張したシェル|
      | zsh| |ksh に bash や tcsh の機能を取り込んだ多機能シェル。macos で標準|
      | fish| friendly interactive shell |わかりやすさに重点をおいたシェル|

  - メタキャラクタ

    - シェルにとって特別な意味を持つ文字
    - 一覧
      |メタキャラクタ|説明|
      |---|---|
      |\*|0 文字以上の任意の文字列|
      |?|任意の１文字|
      |\[\]|\[\]内の任意の１文字|
      |\\|メタキャラクタの打消し(エスケープ文字)|
    - コマンド例

      ```bash
      /tmp/meta # for i in $(seq 10 $END); do  echo $i".txt" ; done
      1.txt
      2.txt
      3.txt
      4.txt
      5.txt
      6.txt
      7.txt
      8.txt
      9.txt
      10.txt
      /tmp/meta # for i in $(seq 10 $END); do  touch $i".txt" ; done
      /tmp/meta # ls
      1.txt   10.txt  2.txt   3.txt   4.txt   5.txt   6.txt   7.txt   8.txt   9.txt
      /tmp/meta # ls *.txt
      1.txt   10.txt  2.txt   3.txt   4.txt   5.txt   6.txt   7.txt   8.txt   9.txt
      /tmp/meta # ls ?.txt
      1.txt  2.txt  3.txt  4.txt  5.txt  6.txt  7.txt  8.txt  9.txt
      /tmp/meta # ls [1]*.txt
      1.txt   10.txt
      /tmp/meta # ls *[0]*.txt
      10.txt
      /tmp/meta #
      ```

  - 標準入出力

    - Linux
      - 標準入力
      - 標準出力
      - 標準エラー出力
    - リダイレクト（リダイレクション）

      - 標準出力を端末画面ではなく、任意のファイルに切り替える
      - 出力ファイルがない場合、自動で新規作成される
      - ファイル記述コマンド
        - 「>」上書き
        - 「>>」追記
      - コマンド

        ```bash
          $ コマンド > ファイル名

          /tmp # ls -l > ls.log
          /tmp # ls
          access      link1.hard  link2.sym   ls.log      meta        test        test.tar    test.txt    test.zip
          /tmp # cat ls.log
          total 20
          drwxr-xr-x    2 root     root          4096 Aug 31 12:45 access
          -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
          lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
          -rw-r--r--    1 root     root             0 Sep  2 13:21 ls.log
          drwxr-xr-x    2 root     root          4096 Aug 31 13:15 meta
          drwxr-xr-x    2 guest    abuild        4096 Aug 20 14:10 test
          -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
          -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
          -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
          /tmp #
        ```

      - 標準エラー出力
      - ファイル記述コマンド
        - 「2>」上書き
        - 「2>>」追記
      - コマンド

        ```bash
          $ コマンド 2> ファイル名
          /tmp # ls nonefile 2> ls2.log
          /tmp # cat ls2.log
          ls: nonefile: No such file or directory
        ```

  - パイプ

    - 「|」パイプ
    - パイプを使うと、ある子面戸の実行結果を別のコマンドで処理できる
    - コマンド

      ```bash
        $ コマンド | コマンド

        /tmp # history | less
        # 1画面に収まりきらない出力をlessコマンドで受けて表示できます
        /tmp # ls | wc -l
          10
        # lsコマンドの実行結果を、文字数や行数をカウントするwcコマンドで受けて、行数を表示
      ```

  - コマンド履歴
    - コマンド
      ```bash
        $ history
      ```
    - 数字は履歴番号
    - 「!履歴番号」を実行すると、指定した履歴番号のコマンドが実行される
    - コマンドの履歴は、ホームディレクトリ内の「.ash_history」ファイルに記録されている
  - エイリアス

    - よく使うコマンドに短縮名をつける
    - コマンドとオプションをまとめて１つのコマンドとして扱う
    - コマンド

      ```bash
        $ alias [別名[=コマンド]]]

          /tmp # alias la='ls -l'
          /tmp # la
          total 28
          drwxr-xr-x    2 root     root          4096 Aug 31 12:45 access
          -rw-r--r--    2 root     root             0 Aug 20 13:47 link1.hard
          lrwxrwxrwx    1 root     root            17 Aug 29 13:02 link2.sym -> tmp/test/test.txt
          -rw-r--r--    1 root     root           687 Sep  2 13:25 ls.log
          -rw-r--r--    1 root     root            40 Sep  2 13:27 ls2.log
          drwxr-xr-x    2 root     root          4096 Aug 31 13:15 meta
          drwxr-xr-x    2 guest    abuild        4096 Aug 20 14:10 test
          -rw-r--r--    1 root     root          2560 Aug 20 14:07 test.tar
          -rw-r--r--    1 root     root             0 Aug 18 14:56 test.txt
          -rw-r--r--    1 root     root           314 Aug 20 13:56 test.zip
      ```

    - alias 確認コマンド

      ```bash
        /tmp # alias
        la='ls -l'
        /tmp #

      ```

8. シェル変数と環境変数

   - シェル変数

     - 変数名は大文字小文字区別
     - 「＝」の前後にスペースは入れない
     - 変数の有効範囲は、変数を定義したシェルの中のみ

     - command

       ```bash
        $　変数名=値
          / # testvar=yamazaki
          / # echo $testvar
          yamazaki
       ```

   - 環境変数

     - 定義したシェルから起動したシェルや実行したコマンから変数の値を参照できる
     - 環境変数は、すべてのシェルで共有している変数ではない
       - 新しいシェルやコマンドを起動したときに、その隊をコピーされデフォルトで利用できるようになっている
     - command

       ```bash
        $　export [変数名[=値]]
        / # var1="Alpine"
        / # export var1
        / # export var2="centos"
        / # echo $var1
        Alpine
        / # echo $var2
        centos
       ```

     - 環境変数確認コマンド

       ```bash
        / # printenv
        HOSTNAME=d02147933e8e
        var1=Alpine
        SHLVL=1
        var2=centos
        HOME=/root
        TERM=xterm
        PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
        PWD=/
       ```

     - 環境変数&シェル変数確認コマンド

       ```bash
          / # set
          BB_ASH_VERSION='1.33.1'
          HISTFILE='/root/.ash_history'
          HOME='/root'
          HOSTNAME='d02147933e8e'
          IFS='
          '
          LINENO=''
          OPTIND='1'
          PATH='/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin'
          PPID='0'
          PS1='\w \$ '
          PS2='> '
          PS4='+ '
          PWD='/'
          SHLVL='1'
          TERM='xterm'
          _='printenv'
          testvar='yamazaki'
          var1='Alpine'
          var2='centos'
       ```

## docker alpine apache

_install_

- openrc

```bash
/ # apk add openrc --no-cache
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/main/x86_64/APKINDEX.tar.gz
fetch https://dl-cdn.alpinelinux.org/alpine/v3.14/community/x86_64/APKINDEX.tar.gz
(1/2) Installing ifupdown-ng (0.11.3-r0)
(2/2) Installing openrc (0.43.3-r1)
Executing openrc-0.43.3-r1.post-install
Executing busybox-1.33.1-r3.trigger
OK: 12 MiB in 22 packages

/ # apk info openrc
openrc-0.43.3-r1 description:
OpenRC manages the services, startup and shutdown of a host

openrc-0.43.3-r1 webpage:
https://github.com/OpenRC/openrc

openrc-0.43.3-r1 installed size:
2452 KiB

```

- apache2

```bash
/ # apk -U add apache2
/ # apk info apache2
apache2-2.4.48-r0 description:
A high performance Unix-based HTTP server
f
apache2-2.4.48-r0 webpage:
https://httpd.apache.org/

apache2-2.4.48-r0 installed size:
2920 KiB

```

_start_

```bash
/ # rc-service apache2 start
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/blkio/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpu/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpuacct/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpuset/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/devices/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/freezer/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/hugetlb/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/memory/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/net_cls/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/net_prio/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/perf_event/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/pids/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/rdma/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/systemd/tasks: Read-only file system
 * Starting apache2 ...
AH00558: httpd: Could not reliably determine the server's fully qualified domain name, using 172.17.0.2. Set the 'ServerName' directive globally to suppress this message
```

※エラーがでた

```bash
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/blkio/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpu/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpuacct/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/cpuset/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/devices/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/freezer/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/hugetlb/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/memory/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/net_cls/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/net_prio/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/perf_event/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/pids/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/rdma/tasks: Read-only file system
/lib/rc/sh/openrc-run.sh: line 108: can't create /sys/fs/cgroup/systemd/tasks: Read-only file system
 * You are attempting to run an openrc service on a
 * system which openrc did not boot.
 * You may be inside a chroot or you may have used
 * another initialization system to boot this system.
 * In this situation, you will get unpredictable results!
 * If you really want to do this, issue the following command:
 * touch /run/openrc/softlevel
 * ERROR: apache2 failed to start
```

↓ 対処方法

```bash
touch /run/openrc/softlevel

```
