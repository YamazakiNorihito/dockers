## Sambaのインストール

```bash
$yum update
$yum install -y samba
$useradd sambauser
#samba1234
$passwd sambauser

$pdbedit -a sambauser
$cd /home/sambauser/
$mkdir samba
$chown sambauser:sambauser samba
```

## 公開フォルダ

```bash
#設定は別途見てください
$vi /etc/samba/smb.conf

# status みてNoactiveの場合
$systemctl start smb
$systemctl start nmb
# status みてactiveの場合
$systemctl reload smb
$systemctl reload nmb
```

*windows 共有フォルダアクセス*

1. コントロールパネル
2. システム
3. 詳細情報
4. システムの詳細設定
5. タブ「コンピュータ名」
   1. 変更
   2. ワークグループ「NYGROUP」
6. PC再起動
7. ディレクトリ「\\\\{sambaServerIp}]
   1. 例 \\\\192.168.1.30

*smb.conf*
```yml
[global]
        # 変更箇所
        workgroup = NYGROUP
        security = user
        # 変更箇所
        guest account = sambauser
        # 変更箇所
        dos charset = CP932
        # 変更箇所
        unix charset = UTF-8
        # 変更箇所
        map to guest = Bad Password
        # 変更箇所
        #passdb backend = tdbsam

        printing = cups
        printcap name = cups
        load printers = yes
        cups options = raw

[homes]
        comment = Home Directories
        # 変更箇所
        path = %H/samba
        valid users = %S, %D%w%S
        browseable = No
        read only = No
        inherit acls = Yes

[printers]
        comment = All Printers
        path = /var/tmp
        printable = Yes
        create mask = 0600
        browseable = No

[print$]
        comment = Printer Drivers
        path = /var/lib/samba/drivers
        write list = @printadmin root
        force group = @printadmin
        create mask = 0664
        directory mask = 0775

# 以下丸っと追加
#==========================================================
[share]
        #共有フォルダ
        path = /home/sambauser/samba
        #マイネットワークに表示の有無
        browsable = yes
        #書き込み許可
        writable = yes
        #ゲストユーザのログイン許可
        guest ok = yes
        #読込みの許可
        read only = no
        #ファイル属性
        force create mode = 0777
        #ディレクトリ属性
        force directory mode = 0777
        #一般公開
        public = yes
```


## 共有フォルダ
アクセス権

```bash
$vi /etc/samba/smb.conf
```

*smb.conf*
```yaml
# See smb.conf.example for a more detailed config file or
# read the smb.conf manpage.
# Run 'testparm' to verify the config is correct after
# you modified it.

[global]
        workgroup = NYGROUP
        security = user
        guest account = sambauser
        dos charset = CP932
        unix charset = UTF-8

        #変更箇所
        map to guest = Never
        #変更箇所
        passdb backend = tdbsam

        printing = cups
        printcap name = cups
        load printers = yes
        cups options = raw

[homes]
        comment = Home Directories
        path = %H/samba
        valid users = %S, %D%w%S
        browseable = No
        read only = No
        inherit acls = Yes

[printers]
        comment = All Printers
        path = /var/tmp
        printable = Yes
        create mask = 0600
        browseable = No

[print$]
        comment = Printer Drivers
        path = /var/lib/samba/drivers
        write list = @printadmin root
        force group = @printadmin
        create mask = 0664
        directory mask = 0775
#==========================================================
[share]
        #共有フォルダ
        path = /home/sambauser/samba
        #マイネットワークに表示の有無
        browsable = yes
        #書き込み許可
        writable = yes
        #ゲストユーザのログイン許可
        guest ok = yes
        #読込みの許可
        read only = no
        #ファイル属性
        force create mode = 0777
        #ディレクトリ属性
        force directory mode = 0777
        #一般公開
        #変更箇所
        public = no

        #変更箇所
        valid users = sambauser
        #ファイル作成
        create mask = 0664
        #ディレクトリアクセス
        directory mask = 0775
```





