

```bash
# ------------------------------------
# Windows Command
# ------------------------------------
#windows → linux
$windows>ssh root@192.168.1.36

# ------------------------------------
# Linux Command
# ------------------------------------
$root>cat /etc/centos-release
CentOS Linux release 7.9.2009 (Core)

#初期からInstallされています
$root>yum list installed openssh-server openssh-clients

$root>su - sshuser

$sshuser>mkdir ~/.ssh
$sshuser>chmod 700 ~/.ssh

$sshuser>cd ~/
#.ssh ディレクトリ内に作成
$sshuser>ssh-keygen -t ecdsa -b 521

#公開鍵、秘密鍵を生成
$sshuser>ssh-keygen -t ecdsa -b 521
$sshuser>cd ~/.ssh
$sshuser>ls
    id_rsa     このファイルは、windows　clientへこぴーをする、ssh接続時使用します 
    id_rsa.pub
$sshuser>cp id_rsa.pub authorized_keys
$sshuser>chmod 600 .ssh/authorized_keys
$sshuser>cat id_ecdsa.pub
    ・・・・hogehoge
    ・・・・= sshuser@localhost.localdomain  ←sshしたいユーザー名が出ていること重要！

$sshuser>exit

$root>usermod -G wheel sshuser
#※同階層のファイルを参照　【★】ついているものが変更点
$root>cat /etc/ssh/ssh_config
#※同階層のファイルを参照　【★】ついているものが変更点
$root>cat /etc/ssh/sshd_config

#サーバー自身からのsshへのアクセスを許可
$root>echo "sshd:127.0.0.1" >> /etc/hosts.allow
#内部(例:192.168.1.XXXからのsshアクセスを許可)
$root>echo "sshd: 192.168.1." >> /etc/hosts.allow

#必要であれば実施今回は不要
#$root>echo "sshd: .ppp.asahi-net.or.jp"  >> /etc/hosts.allow　←　外部(例:xxx.ppp.asahi-net.or.jpからのsshアクセスを許可)

#sshへの全てのアクセスを禁止
$root>echo "sshd: ALL" >> /etc/hosts.deny

$root>systemctl reload sshd

# ------------------------------------
# Windows Command
# ------------------------------------
#windows → linux
$windows>ssh -i id_ecdsa sshuser@192.168.1.36
```

*秘密鍵をclientに取得する方法*

以下方法があります。
1. 秘密鍵(id_ecdsa)中身を全コピーして、windows テキストに貼り付け同じ名前で保存
2. vsftpd　サービスを使用してftpで取得

    ```bash
      # -------------------------
      # linux
      # -------------------------
      $root>yum -y install vsftpd
      $root>systemctl start vsftpd.service
      # -------------------------
      # windows
      # -------------------------
      $ftp 192.168.1.36
      > ftp user : sshuser
      $sshuser>cd .ssh
      $sshuser>get id_ecdsa
      $sshuser>exit
      # -------------------------
      # linux
      # -------------------------
      $root>systemctl stop vsftpd.service

    ```

*参考サイト*
- [SSH暗号設定の強化](https://qiita.com/qiitamatumoto/items/f2a39ea93bf6741228ba)
- [CentOS 7.0 - SSHサーバ OpenSSH 設定！](https://www.mk-mode.com/blog/2014/08/12/centos-7-0-installation-of-openssh/)
- [SSHサーバの構築手順（鍵認証設定、CentOS7）](https://www.purpledice.jp/ssh%e3%82%b5%e3%83%bc%e3%83%90%e3%81%ae%e6%a7%8b%e7%af%89%e6%89%8b%e9%a0%86%ef%bc%88%e9%8d%b5%e8%aa%8d%e8%a8%bc%e8%a8%ad%e5%ae%9a%e3%80%81centos7%ef%bc%89/)
- [CentOS7.3でSSH接続(公開鍵認証)する方法](https://qiita.com/uhooi/items/137de4578534c8e7e7f2)
- [VS Code Remote Development SSHセットアップ中にハマったこと](https://qiita.com/igrep/items/3a3ba8e9089885c3c9f6)