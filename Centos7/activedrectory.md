
## 事前準備

```bash
$echo centosdc01 > /etc/hostname

#※
$vi /etc/sysconfig/network
HOSTNAME=centosdc01

```

*/etc/sysconfig/network*
```yml
HOSTNAME=centosdc01
```

## Sambaインストール
### Sambaインストール用パッケージの導入

*Samba 4をmakeする際に必要となるパッケージ*
```bash
$yum -y install perl gcc libacl-devel libblkid-devel gnutls-devel \
readline-devel python-devel gdb pkgconfig krb5-workstation \
zlib-devel setroubleshoot-server libaio-devel \
setroubleshoot-plugins policycoreutils-python \
libsemanage-python setools-libs-python setools-libs \
popt-devel libpcap-devel sqlite-devel libidn-devel \
libxml2-devel libacl-devel libsepol-devel libattr-devel \
keyutils-libs-devel cyrus-sasl-devel cups-devel bind-utils \
libxslt docbook-style-xsl openldap-devel

#python3系
$yum install -y https://repo.ius.io/ius-release-el7.rpm
$yum install -y python36u python36u-libs python36u-devel python36u-pip

$yum install -y libpqxx-devel
$yum install -y lmdb-devel
$yum install -y flex
$yum install -y bison

```

*GnuTLS install ver3.6.9*
```bash
$cd /tmp
$wget https://www.gnupg.org/ftp/gcrypt/gnutls/v3.6/gnutls-3.6.9.tar.xz
$tar xvf gnutls-3.6.9.tar.xz
$cd gnutls-3.6.9*

```

### Sambaインストール

```bash
$mkdir -p /tmp/samba
$cd /tmp/samba

$wget http://www.samba.org/samba/ftp/samba-4.7.12.tar.gz
$tar zxvf samba-4.7.12.tar.gz
$cd samba-*
#※
$vi configure

#Sambaインストール
$./configure && make && make install

# ※1 S --------------------------------
# ./configure でエラーが出た場合以下の手順
$./configure --without-pam
$make && make install
# ※1 E --------------------------------

```

*/tmp/samba/samba-4.15.5/configure*
```bash
#!/bin/sh

PREVPATH=`dirname $0`

WAF=./buildtools/bin/waf

# using JOBS=1 gives maximum compatibility with
# systems like AIX which have broken threading in python
JOBS=1
export JOBS

# Make sure we don't have any library preloaded.
unset LD_PRELOAD

cd . || exit 1
${PYTHON:=python} $WAF configure "$@" || exit 1
cd $PREVPATH
```

## ドメイン準備

```bash
# Administrator → Admin1234
$/usr/local/samba/bin/samba-tool domain provision --use-rfc2307 --interactive --function-level=2008_R2
Realm : CENTOS.LOCAL　←　レルム（例:CENTOS.LOCAL）を応答
 Domain [CENTOS]:　←　空ENTER（ドメイン名はレルムをピリオドで分割した最左端）
 Server Role (dc, member, standalone) [dc]:　←　空ENTER（サーバー役割はドメインコントローラー）
 DNS backend (SAMBA_INTERNAL, BIND9_FLATFILE, BIND9_DLZ, NONE) [SAMBA_INTERNAL]:　←　空ENTER（DNSはSAMBA内蔵DNS）
 DNS forwarder IP address (write 'none' to disable forwarding) [192.168.1.1]:　←　空ENTER（DNS転送先は現在DNS）
Administrator password:　←　任意のドメイン管理者パスワードを応答（注）パスワードは英数字記号混在で8文字以上であること
Retype password:　←　ドメイン管理者パスワードを再応答（確認）
```


<span style="color: red; ">※ドメイン準備をやり直す場合</span>
```bash
$rm -f /usr/local/samba/etc/smb.conf　←　ドメイン準備で生成されたファイルを削除
$rm -rf /usr/local/samba/private/*　←　ドメイン準備で生成されたファイルを削除
$rm -rf /usr/local/samba/var/locks/sysvol/*　←　ドメイン準備で生成されたファイルを削除
```

## Samba起動

```bash
#※　Samba起動スクリプト作成
$vi /etc/rc.d/init.d/samba
$chmod 755  /etc/rc.d/init.d/samba
$chmod +x /etc/rc.d/init.d/samba
$ln -s /etc/rc.d/init.d/samba /etc/rc3.d/S80samba
$chkconfig samba on
$systemctl start samba
#※
$vi /etc/sysconfig/network-scripts/ifcfg-ens33

#重要かも
$systemctl stop NetworkManager
$systemctl disable NetworkManager

$systemctl restart network

#疎通確認
$/usr/local/samba/bin/samba-tool dns zonelist 127.0.0.1 -U Administrator
  2 zone(s) found
  pszZoneName                 : centos.local
  Flags                       : DNS_RPC_ZONE_DSINTEGRATED DNS_RPC_ZONE_UPDATE_SECURE
  ZoneType                    : DNS_ZONE_TYPE_PRIMARY
  Version                     : 50
  dwDpFlags                   : DNS_DP_AUTOCREATED DNS_DP_DOMAIN_DEFAULT DNS_DP_ENLISTED
  pszDpFqdn                   : DomainDnsZones.centos.local

  pszZoneName                 : _msdcs.centos.local
  Flags                       : DNS_RPC_ZONE_DSINTEGRATED DNS_RPC_ZONE_UPDATE_SECURE
  ZoneType                    : DNS_ZONE_TYPE_PRIMARY
  Version                     : 50
  dwDpFlags                   : DNS_DP_AUTOCREATED DNS_DP_FOREST_DEFAULT DNS_DP_ENLISTED
  pszDpFqdn                   : ForestDnsZones.centos.local

$cp /usr/local/samba/private/krb5.conf /etc/krb5.conf

#firewall 
$firewall-cmd --add-port=53/tcp --permanent;firewall-cmd --add-port=53/udp --permanent; \
firewall-cmd --add-port=88/tcp --permanent;firewall-cmd --add-port=88/udp --permanent; \
firewall-cmd --add-port=135/tcp --permanent;firewall-cmd --add-port=137-138/udp --permanent; \
firewall-cmd --add-port=139/tcp --permanent;firewall-cmd --add-port=389/tcp --permanent; \
firewall-cmd --add-port=389/udp --permanent;firewall-cmd --add-port=445/tcp --permanent; \
firewall-cmd --add-port=464/tcp --permanent;firewall-cmd --add-port=464/udp --permanent; \
firewall-cmd --add-port=636/tcp --permanent;firewall-cmd --add-port=1024-5000/tcp --permanent;\
firewall-cmd --add-port=3268-3269/tcp --permanent;firewall-cmd --add-port=49152-65535/tcp --permanent

$firewall-cmd --reload

```

*/etc/rc.d/init.d/samba*
```yml
#!/bin/bash
#
# samba4        This shell script takes care of starting and stopping
#               samba4 daemons.
#
# chkconfig: - 58 74
# description: Samba 4.0 will be the next version of the Samba suite
# and incorporates all the technology found in both the Samba4 alpha
# series and the stable 3.x series. The primary additional features
# over Samba 3.6 are support for the Active Directory logon protocols
# used by Windows 2000 and above.

### BEGIN INIT INFO
# Provides: samba4
# Required-Start: $network $local_fs $remote_fs
# Required-Stop: $network $local_fs $remote_fs
# Should-Start: $syslog $named
# Should-Stop: $syslog $named
# Short-Description: start and stop samba4
# Description: Samba 4.0 will be the next version of the Samba suite
# and incorporates all the technology found in both the Samba4 alpha
# series and the stable 3.x series. The primary additional features
# over Samba 3.6 are support for the Active Directory logon protocols
# used by Windows 2000 and above.
### END INIT INFO

# Source function library.
. /etc/init.d/functions


# Source networking configuration.
. /etc/sysconfig/network


prog=samba
prog_dir=/usr/local/samba/sbin/
lockfile=/var/lock/subsys/$prog


start() {
        [ "$NETWORKING" = "no" ] && exit 1
#       [ -x /usr/sbin/ntpd ] || exit 5

                # Start daemons.
                echo -n $"Starting samba4: "
                daemon $prog_dir/$prog -D
        RETVAL=$?
                echo
        [ $RETVAL -eq 0 ] && touch $lockfile
        return $RETVAL
}


stop() {
        [ "$EUID" != "0" ] && exit 4
                echo -n $"Shutting down samba4: "
        killproc $prog_dir/$prog
        RETVAL=$?
                echo
        [ $RETVAL -eq 0 ] && rm -f $lockfile
        return $RETVAL
}


# See how we were called.
case "$1" in
start)
        start
        ;;
stop)
        stop
        ;;
status)
        status $prog
        ;;
restart)
        stop
        start
        ;;
reload)
        echo "Not implemented yet."
        exit 3
        ;;
*)
        echo $"Usage: $0 {start|stop|status|restart|reload}"
        exit 2
esac
```

*/etc/sysconfig/network-scripts/ifcfg-ens33*
```yml
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
BOOTPROTO="none"
DEFROUTE="yes"
IPADDR=192.168.1.33
NETMASK=255.255.255.0
GATEWAY=192.168.1.1
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="f706f2d4-2f8a-433e-ad57-b8c107b7f796"
DEVICE="ens33"
ONBOOT="yes"

#追加
DNS1="127.0.0.1"

```

## Windows PC Domain参加

wifi接続の場合
1. コントロール パネル\ネットワークとインターネット\ネットワーク接続
2. Wifi
   1. プロパティ
      1. IPv4 
         1. DNS:192.168.1.33
      2. IPv6無効
疎通確認
```bash
$nslookup centosdc01.centos.local
168.192.in-addr.arpa
        primary name server = prisoner.iana.org
        responsible mail addr = hostmaster.root-servers.org
        serial  = 2002040800
        refresh = 1800 (30 mins)
        retry   = 900 (15 mins)
        expire  = 604800 (7 days)
        default TTL = 604800 (7 days)
(root)  ??? unknown type 41 ???
サーバー:  UnKnown
Address:  192.168.1.33

名前:    centosdc01.centos.local
Addresses:  240d:1a:7dd:6100:20c:29ff:fe7d:c745
          192.168.1.33
```

ドメイン参加
1. コントロール パネル\すべてのコントロール パネル項目\システム
2. システム詳細
3. コンピュータ名
   1. 所属するグループ
      1. ドメイン：centos.local
4. PC再起動
5. ログイン
   1. centos\administrator でログインできること

## リモートサーバー管理ツール導入
*windows active directory manager*

1. コンピュータ管理
2. アプリと機能
   1. オプション機能
   2. 機能追加
      1. RSATのActiveDirectory連携来そうな機能Install
      2. [パッチ適応](https://www.microsoft.com/ja-JP/download/details.aspx?id=45520)
3. 「Active Directory ユーザーとコンピューター」でActive Directoryのドメイン、コンピューター、ユーザーが確認できること


## Samba4 ユーザー管理
```bash
#add
$/usr/local/samba/bin/samba-tool user create norihito password@1234
User 'norihito' created successfully

#dsable
$/usr/local/samba/bin/samba-tool user disable norihito

#status
$/usr/local/samba/bin/pdbedit -u norihito -v

#enable
$/usr/local/samba/bin/samba-tool user enable norihito

#list
$/usr/local/samba/bin/samba-tool user list

#pass change
$/usr/local/samba/bin/samba-tool user setpassword norihito --newpassword=password456@

```

*移動ユーザープロファイル設定*

どの端末からドメインへログオンしてもデスクトップ設定やマイドキュメント等のユーザー固有の環境（ユーザープロファイル）を利用できるようにする。
```bash
$mkdir /usr/local/samba/var/profiles
$chmod 1777 /usr/local/samba/var/profiles
$vi /usr/local/samba/etc/smb.conf
# Global parameters
[global]
        dns forwarder = 192.168.1.1
        netbios name = CENTOSDC01
        realm = CENTOS.LOCAL
        server role = active directory domain controller
        workgroup = CENTOS
        idmap_ldb:use rfc2307 = yes

[netlogon]
        path = /usr/local/samba/var/locks/sysvol/centos.local/scripts
        read only = No

[sysvol]
        path = /usr/local/samba/var/locks/sysvol
        read only = No
[Profiles]
        path = /usr/local/samba/var/profiles
        read only = No
        guest ok = Yes
        browseable = No

# new user
$ /usr/local/samba/bin/samba-tool user create centos01 pass123@ --profile-path="\\\192.168.1.33\pro
files\%USERNAME%"
User 'centos01' created successfully

#exists user
$/usr/local/samba/bin/pdbedit -u norihito --profile="\\\192.168.1.33\profiles\%USERNAME%"
Unix username:        norihito
NT username:
Account Flags:        [U          ]
User SID:             S-1-5-21-626912993-2191104437-2377732376-1104
Primary Group SID:    S-1-5-21-626912993-2191104437-2377732376-513
Full Name:
Home Directory:
HomeDir Drive:        (null)
Logon Script:
Profile Path:         \\192.168.1.33\profiles\%USERNAME%
Domain:
Account desc:
Workstations:
Munged dial:
Logon time:           金, 04  3月 2022 14:41:25 JST
Logoff time:          0
Kickoff time:         木, 14  9月 30828 11:48:05 JST
Password last set:    金, 04  3月 2022 14:31:41 JST
Password can change:  金, 04  3月 2022 14:31:41 JST
Password must change: never
Last bad password   : 0
Bad password count  : 0
Logon hours         : FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

```

- [CentOS 7とSamba4で自宅用Active Directory Domain Controller (AD DC)を構築する① インストール編](https://orebibou.com/ja/home/201503/20150316_001/)
- [Samba 4.8.5 から 4.9.1 にアップデートしてみた](https://kusoneko.blogspot.com/2018/10/samba-update-4.8.5-to-4.9.1.html)
- [Sambaサーバをソースからインストールする](https://www7390uo.sakura.ne.jp/wordpress/archives/768)
- [Active Directoryドメインコントローラー構築](https://centossrv.com/activedirectory-samba.shtml)
- [network.serviceはLSBの開始に失敗しました：ネットワークの起動/停止-Centos 7](https://qastack.jp/unix/278155/network-service-failed-to-start-lsb-bring-up-down-networking-centos-7)
- [EC2インスタンスでnetworkが起動しなかった](http://www.4doku.com/2019/09/ec2network.html)
- https://www.rem-system.com/samba-ad/#12_PC
- https://syachiku.net/linux-activedirectory-jpin/
- [SAMBAWIKI](https://wiki.samba.org/index.php/Package_Dependencies_Required_to_Build_Samba#Red_Hat_Enterprise_Linux_7_.2F_CentOS_7_.2F_Scientific_Linux_7)