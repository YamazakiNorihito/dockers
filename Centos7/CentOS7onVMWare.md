
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

### vmware 上にCentos7構築
1. 新規仮想マシン作成
2. インストーラーディスクイメージファイル選択
3. 仮想マシンの名前任意
4. 仮想マシンウィザード
   1. ハードウェア
      1. ネットワークアダプタ
         1. ブリッジ選択
         2. 物理ネットワーク接続の状態を複製
*CentOS7　Start！*
1. 設定
   1. 言語日本語
   2. ネットワーク接続enth ON
   3. アカウント
      1. root
         1. PW:root
      2. sshuser
         1. PW:sshuser
2. ssh status check
   ```bash
    #問題ないこと確認
    $systemctl status sshd
   ``` 
3. ip show
   ```bash
    #ssh 接続するときのIP表示
    $ip address show 
   ``` 

*windows cli*
1. ssh 接続
   ```bash
    $ssh root@192.168.1.33
   ``` 
2. centos7 設定
   1. SELinuxの無効化
      ```bash
        #SELinuxの無効化
        $getenforce
          Enforcing
        $setenforce 0
        $getenforce
          Permissive
        $vi /etc/sysconfig/selinux
      ``` 
      */etc/sysconfig/selinux*
      ```yml
        # This file controls the state of SELinux on the system.
        # SELINUX= can take one of these three values:
        #     enforcing - SELinux security policy is enforced.
        #     permissive - SELinux prints warnings instead of enforcing.
        #     disabled - No SELinux policy is loaded.
        # 変更
        #SELINUX=enforcing
        SELINUX=disabled
        # SELINUXTYPE= can take one of three values:
        #     targeted - Targeted processes are protected,
        #     minimum - Modification of targeted policy. Only selected processes are protected.
        #     mls - Multi Level Security protection.
        SELINUXTYPE=targeted
      ```
    1. NTPサーバー構築
       ```bash
        $yum -y install chrony
        $vi /etc/chrony.conf
        $chronyc makestep
        $systemctl restart chronyd
        $systemctl enable chronyd
        $chronyc sources　←　NTPサーバーへの時刻同期状態確認
       ```
       */etc/chrony.conf*
       ```yml
        # Use public servers from the pool.ntp.org project.
        # Please consider joining the pool (http://www.pool.ntp.org/join.html).
        #時刻同期対象サーバーを日本国内に限定する
        server 0.jp.pool.ntp.org iburst
        server 1.jp.pool.ntp.org iburst
        server 2.jp.pool.ntp.org iburst
        server 3.jp.pool.ntp.org iburst

        # Record the rate at which the system clock gains/losses time.
        driftfile /var/lib/chrony/drift

        # Allow the system clock to be stepped in the first three updates
        # if its offset is larger than 1 second.
        makestep 1.0 3

        # Enable kernel synchronization of the real-time clock (RTC).
        rtcsync

        # Enable hardware timestamping on all interfaces that support it.
        #hwtimestamp *

        # Increase the minimum number of selectable sources required to adjust
        # the system clock.
        #時刻同期サーバーの候補が2個以上の場合のみ、時刻同期を行う
        minsources 2

        # Allow NTP client access from local network.
        #allow 192.168.0.0/16
        #アクセスを許可するNTPクライアントのIPアドレス or サブネットを指定
        #複数指定する場合は複数行に分ける
        allow 192.168.1.0/24
        allow 10.0.0.2

        #NTPサーバとして使用しない場合は以下のように指定する
        port 0


        # Serve time even if not synchronized to a time source.
        #local stratum 10

        # Specify file containing keys for NTP authentication.
        #keyfile /etc/chrony.keys

        # Specify directory for log files.
        logdir /var/log/chrony

        # Select which information is logged.
        #log measurements statistics tracking
       `` 


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
