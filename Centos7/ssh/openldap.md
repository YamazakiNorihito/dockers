


```bash
$rpm -qa | grep openldap

```

```bash
$yum install -y openldap openldap-servers openldap-clients

#Copy パーミッションと所有者とタイムスタンプを保持する
$cp -p /usr/share/openldap-servers/DB_CONFIG.example /var/lib/ldap/DB_CONFIG

#所有者、アクセス権
$chown ldap. /var/lib/ldap/DB_CONFIG

$systemctl start slapd
$systemctl enable slapd

#ファイルディスクリプタ上限値の設定
#確認
$grep "Max open files" /proc/`pidof slapd`/limits
# 設定
$vi /usr/lib/systemd/system/slapd.service
# デーモン読み込み、再起動
$systemctl daemon-reload
$systemctl restart slapd

#ログ設定
$vi /etc/rsyslog.conf
$systemctl restart rsyslog

#ログローテート設定
$vi /etc/logrotate.d/syslog

# -----------------------------
#設定管理者の設定
# -----------------------------
# 設定管理者パスワード生成
$slappasswd
New password:password
Re-enter new password:password
{SSHA}kW5UgdwAe68Sbu8TuW+UpcNWGTSNoSzT

{SSHA}RwrMSA8iJNaigtN7+eqgbWw4oo+E4oSx

password1234
{SSHA}x+LGeQqH2i9UZwbJyf/QZ3MQVdHH+A26

# LDIF作業用ディレクトリ作成
$mkdir /root/ldif
# 設定管理者追加用LDIF作成※olcRootPWに先ほどメモしたパスワードを記載
$vi /root/ldif/rootdn.ldif
# 設定管理者設定追加
$ldapmodify -Y EXTERNAL -H ldapi:/// -f /root/ldif/rootdn.ldif
SASL/EXTERNAL authentication started
SASL username: gidNumber=0+uidNumber=0,cn=peercred,cn=external,cn=auth
SASL SSF: 0

#標準スキーマ追加
$ldapadd -x -W -D cn=admin,cn=config -f /etc/openldap/schema/cosine.ldif
$ldapadd -x -W -D cn=admin,cn=config -f /etc/openldap/schema/nis.ldif
$ldapadd -x -W -D cn=admin,cn=config -f /etc/openldap/schema/inetorgperson.ldif
$ldapadd -x -W -D cn=admin,cn=config -f /etc/openldap/schema/ppolicy.ldif




```


*/usr/lib/systemd/system/slapd.service*
```yml
#追記
LimitNOFILE=65536
#追記
LimitNPROC=65536
```

*/etc/rsyslog.conf*
```yaml
#追記
local4.* /var/log/slapd/slapd.log
```

*/etc/logrotate.d/syslog*
```yaml
#追記
/var/log/slapd/slapd.log
```

*/root/ldif/rootdn.ldif*
```yaml
angetype: modify
add: olcRootDN
olcRootDN: cn=admin,cn=config
-
add: olcRootPW
olcRootPW: {SSHA}kW5UgdwAe68Sbu8TuW+UpcNWGTSNoSzT
```