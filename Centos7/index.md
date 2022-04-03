
参考サイト
[CentOs7自宅](http://centossrv.com/)

## utile index
- [Linuxでインストール済みのパッケージを確認する](https://qiita.com/ichi_404/items/c9615f141d92d8e9722e)
- [markdown font color](https://qiita.com/twipg/items/d8043cd4681a2780c160)
- [systemctl](https://milestone-of-se.nesuke.com/sv-basic/linux-basic/systemctl/)
- [似たようなサイト](https://kajuhome.com/)
- [CentOSでグループの一覧を表示する](https://qiita.com/pugiemonn/items/e5bfdf25a500a0376a57)
- [CentOSでユーザーの一覧を確認する](https://qiita.com/pugiemonn/items/aca73990736f26c9cded)

## vmware install

※2022/2/4時点
[Installer](https://customerconnect.vmware.com/en/downloads/info/slug/desktop_end_user_computing/vmware_workstation_player/16_0)

## [CentOs7](http://centossrv.com/centos7.shtml)
### iso file download

- [Download](https://www.centos.org/download/)
    - x86_64
  - ftpはどこでもいい
    - 本当はServerの距離が近いところがよい
    - http://ftp.riken.jp/Linux/centos/7.9.2009/isos/x86_64/　
  - minimall を選択
    - [CentOS 7のISOイメージの種類まとめ](https://linuxfan.info/centos-7-iso-image-comparison)
- Installは参考サイト参照
  - Installや設定はサイト通りでOK
  - VMWARE外部アダプター
    SSHが接続ができない場合、真っ先に疑いたいもの
    - ネットワークアダプターをブリッジにする
    - [NATの使用](https://qiita.com/trkrcafeaulate/items/3dfa8d5ec1075848f1e9)
## [CentOS7初期設定](http://centossrv.com/centos7-init.shtml)

- getentコマンドでグループ一覧
```bash
getent group
```
- [suの利用を制限する方法](https://qiita.com/KEy2376/items/35794b0c416d67a5b25e)
- [yum-cron を使うと日に1回、インストール済みパッケージを自動的にアップデート](https://www.pletk.com/yum-cron.html)
  - [yum-cronを使おう](https://qiita.com/shimano_equipped/items/ff65ace63399435107f0)
- [yum groupinstall "Development tools" で入るパッケージ一覧](https://qiita.com/old_/items/6f9da09b9af795c11b71)
- [Linuxコマンド「sed」](https://atmarkit.itmedia.co.jp/ait/articles/1610/18/news008.html#opt)
  ```bash
    # -i option : /etc/aliasesファイルを直接編集
    # :/d option : /^root に一致する行を削除
    sed -i '/^root:/d' /etc/aliases

  ```
- root宛メールを転送する
  - [gmail](https://kshigeyama.dreampresenter.info/2015/11/20/3303/)
- [SELinux とは](https://www.redhat.com/ja/topics/linux/what-is-selinux)
  - [Selinux無効化理由](http://www.b-tm.co.jp/blog/detail/27.html)
- nkf command :Network Kanji Filter

## 仮想マシン構築(KVM)

- [KVM導入に必要なパッケージをインストール](https://qiita.com/moriba/items/ed02a264368845e53e28#2-kvm%E5%B0%8E%E5%85%A5%E3%81%AB%E5%BF%85%E8%A6%81%E3%81%AA%E3%83%91%E3%83%83%E3%82%B1%E3%83%BC%E3%82%B8%E3%82%92%E3%82%A4%E3%83%B3%E3%82%B9%E3%83%88%E3%83%BC%E3%83%AB%E3%81%99%E3%82%8B)

### [vmware on kvm](http://centossrv.com/kvm.shtml)

- VMWARE の設定
  - [プロセッサ　Intel VT-x/EPT またはAMD-V/RVIを仮想化ONにする](https://www.server-world.info/query?os=CentOS_7&p=vmware_player&f=3)
- CentOs7(VMWARE上)
  - centos7 iso file download
```bash
  ### OSイメージのダウンロード (CentOS7 -  minimal ISO)
  cd ~
  wget http://ftp.riken.jp/Linux/centos/7.9.2009/isos/x86_64/CentOS-7-x86_64-Minimal-2009.iso
  mv CentOS-7-x86_64-Minimal-2009.iso /tmp/CentOS-7-x86_64-Minimal-2009.iso
``` 
### [KVM Centos7 instance](http://centossrv.com/kvm-centos7.shtml)

```bash
  ### ゲスト作成&OSインストール
  virt-install \
    --name CentOS7 \
    --ram 1024 \
    --vcpus=1 \
    --file=/var/lib/libvirt/images/centos7.img  \
    --file-size=15 \
    --cdrom=/tmp/CentOS-7-x86_64-Minimal-2009.iso  \
    --os-variant=rhel7 \
    --network bridge=virbr0 \
    --accelerate \
    --graphics spice,listen=0.0.0.0 --channel spicevmc \
    --video qxl \
    --autostart \
    --noautoconsole \
    --noreboot

[root@localhost ~]# virsh dumpxml CentOS7-1|grep spice|grep port
    <graphics type='spice' port='5901' autoport='yes' listen='0.0.0.0'>
``` 

- port
  - [解放](https://www.server-world.info/query?os=CentOS_Stream_9&p=kvm&f=6)
     
     ```bash
       firewall-cmd --add-port=5900-5999/tcp
     ``` 
  - [確認](https://www.linuxmaster.jp/linux_skill/2009/02/linux-4.html#chapter-1)

    ```bash
      ss -atn
    ```

*KVM ゲストOSで絶対にやること*

- [kernel bug](https://serverfault.com/questions/772753/dev-centos-root-does-not-exist-after-migrating-centos7-from-vmware-to-xen)
```bash
  yum update
```

- [kvm 削除](https://qiita.com/TsutomuNakamura/items/db8624ad3f6d19a053e5)
  ```bash
    [root@host ~]# virsh autostart --disable CentOS7　←　ゲスト自動起動設定解除
    ドメイン VM01 の自動起動設定が解除されました

    [root@host ~]# virsh undefine CentOS7　←　ゲスト削除
    ドメイン VM01 の定義が削除されました

    [root@host ~]# rm -f /var/lib/libvirt/images/CentOS7.img　←　ゲスト割当てディスク削除（ゲスト削除）
  ```
- [acpid](https://wa3.i-3-i.info/word14320.html)

### [ゲスト環境変更](https://centossrv.com/kvm-env-chg.shtml)

- [ゲストOS変更](https://centossrv.com/kvm-env-chg.shtml#disk-centos)
```bash
 virsh console CetOS7

  # guestkvmで操作
 $　fdisk -l /dev/vda
```

- [fdisk command](https://atmarkit.itmedia.co.jp/ait/articles/1803/01/news034.html)


### サーバー公開前のセキュリティ強化
*<font color="Salmon">Linuxインストール後の初期の段階で導入しておくのが望ましい。</font>*
#### [ファイル改竄検知システム導入](https://centossrv.com/tripwire.shtml)

- [yum installで"パッケージは使用できません"と出る際の対応](https://qiita.com/simapaso/items/8a3ff77ae29ac041be0d)
  
  ``bash
    # tripwire install

    # repository search 
    yum repolist all | grep epel

    # not found case repository install
    yum install epel-release

    # search "tripwire" is found
    yum search tripwire

    # insatll!!
    yum install -y tripwire
  ```

- tripwire checker start
  ```bash
    #checker
    tripwire -m c -s -c /etc/tripwire/tw.cfg

    # checked summary report files
    ls /var/lib/tripwire/report/

    # report print
    twprint -m r --report-level 1 -c /etc/tripwire/tw.cfg -r /var/lib/tripwire/report/localhost.localdomain-20220207-225216.twr
  ```

- Tripwire定期自動実行設定
  - [/etc/crontabと/etc/cron.d設定ファイルの書き方](https://www.server-memo.net/tips/etc-crontab.html)


#### [rootkit検知ツール導入(chkrootkit)](https://centossrv.com/chkrootkit.shtml)

#### [アンチウイルスソフト導入(Clam AntiVirus)](https://centossrv.com/clamav.shtml)

#### [ファイアウォール構築(iptables)](https://centossrv.com/iptables.shtml)
- [現在の設定表示](https://www.server-memo.net/centos-settings/centos7/timedatectl.html)

### クライアント(Windows)からLinuxサーバーをリモート操作する
#### [SSHサーバー構築(OpenSSH)](https://centossrv.com/openssh.shtml)

[openSsh構築](./ssh/ssh.md)

### [DNSサーバー構築(BIND)](https://centossrv.com/bind.shtml)

- 参考のものでよくわからないければ下記を一度試してから　[本編](https://centossrv.com/bind.shtml)　の挑戦
```bash
 $su -
 $yum install -y bind
 $vi /etc/named.conf

 $cd /var/named
 $vi example.com.zone
 $TTL     1D
    @        IN    SOA    ns1.example.com.  root.example.com.(
                                  2022021622 ; serial
                                  1D         ; refresh
                                  1H         ; retry
                                  1W         ; expire
                                  3H      )  ;minimum

              IN    NS     ns1.example.com.
    mail1     IN    A      192.168.1.2
    ns1       IN    A      192.168.1.30

    @         IN    MX     10  mail1.example.com.

 $vi example.com.rev
    $TTL     1D
    @        IN    SOA    ns1.example.com.  root.example.com.(
                                  2022021622 ; serial
                                  1D         ; refresh
                                  1H         ; retry
                                  1W         ; expire
                                  3H         ;minimum
                                  )
              IN    NS     ns1.example.com.
    2         IN    PTR    mail1.example.com.
    3         IN    PTR    ns1.example.com.
 $vi /etc/resolv.conf
    # Generated by NetworkManager
    nameserver 127.0.0.1
    nameserver 192.168.1.1
    #nameserver 240d:1a:7dd:6100:6eeb:b6ff:fea4:ec33
    #search localdomain
 
 #port 待ち受けてね
 $firewall-cmd --permanent --add-port=53/udp
 $firewall-cmd --reload

  # windows cli
  $nslookup mail1.example.com 192.168.1.30
    サーバー:  UnKnown
    Address:  192.168.1.30

    名前:    mail1.example.com
    Address:  192.168.1.2
```

### [Webサーバーを公開する](https://www.server-memo.net/memo/wordpress/nginx-install.html)

WebサーバーとしてRedmineを構築します。
[redmine構築](./redmine構築.md)


### [Windowsファイルサーバー構築(Samba)](https://centossrv.com/samba.shtml)
- [`command > /dev/null 2>&1`](https://qiita.com/ritukiii/items/b3d91e97b71ecd41d4ea)
- [1>/dev/null 2>&1と2>&1 1>/dev/nullの違い](https://qiita.com/hasegit/items/2598963393e0395685f6)
- [bracket [ ]](https://qiita.com/Ping/items/f9b5175085026462b082)

sambaとして共有ディレクトリを構築します。
[sambaCommanDirectory構築](./samba.md)

