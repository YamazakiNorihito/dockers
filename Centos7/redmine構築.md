
## 構築手順
### 必要なパッケージのインストール
```bash
$yum -y groupinstall 'Development Tools'
$yum -y install openssl-devel readline-devel zlib-devel curl-devel libyaml-devel libffi-devel
$yum -y install ImageMagick ImageMagick-devel ipa-pgothic-fonts
$yum install -y openssl-devel readline-devel zlib-devel

```

### Rubyをインストールする

1. rbenv install
```bash
#rbenvインストール
$git clone https://github.com/rbenv/rbenv.git /usr/share/rbenv

$/usr/share/rbenv/bin/rbenv init

$echo 'eval "$(rbenv init -)"' >> ~/.bash_profile
$vi ~/.bash_profile
# .bash_profile
# Get the aliases and functions
if [ -f ~/.bashrc ]; then
        . ~/.bashrc
fi
# User specific environment and startup programs
PATH=$PATH:$HOME/bin
export PATH
#rbenv/bin Pathを追加
export PATH="/usr/share/rbenv/bin/:$PATH"
eval "$(/usr/share/rbenv/bin/rbenv init -)"

$source ~/.bash_profile

# path確認
$rbenv -v
```

1. ruby-build install

```bash
$git clone https://github.com/rbenv/ruby-build.git /usr/share/rbenv/plugins/ruby-build

$/usr/share/rbenv/plugins/ruby-build/install.sh

#ruby version list
$rbenv install -l

# ruby ver.2.7.5 install 
$rbenv install 2.7.5
$rbenv global 2.7.5

#ruby version 確認
$ruby -v

```

### MySQLインストール 
#### パッケージのインストール
- [MySQL Yum Repository](https://dev.mysql.com/downloads/repo/yum/)

```bash
$yum localinstall -y https://dev.mysql.com/get/mysql80-community-release-el7-5.noarch.rpm
$yum install -y mysql-community-server	mysql-community-devel
```

#### mysql start

```bash
# ※1
$systemctl start mysqld
$systemctl enable mysqld
```

※1 not empty dir
mysql start でディレクトリ初期化ができなくエラーになりました
対処方法は以下の通り
```bash
#datadirを確認
$cat /etc/my.cnf
[mysqld]
#こいつ
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock

log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid

$cp -R /var/lib/mysql  /var/lib/mysql_22025
# data dir change dir
$cd /var/lib/mysql
#mysql data file and dir all delete
$rm -fR *

# これで解消！！！
$systemctl start mysqld
```

#### MySQLの初期設定

```bash
#rootアカウントの初期パスワードを確認
$grep 'temporary password' /var/log/mysqld.log

# all「Yes」 新パスワードは覚えておきましょう
$mysql_secure_installation
```

#### MySQLのconfファイルを設定
```bash
$cat /etc/my.cnf
[mysqld]
datadir=/var/lib/mysql
socket=/var/lib/mysql/mysql.sock

# 文字コードをUTF8に設定(Append!!!)
character-set-server = utf8

$systemctl reload mysqld
```

### Redmine
#### Redmine用のテーブルとアカウントを追加

```bash
＃P@ssword1234
$mysql -u root -p

$mysql> CREATE DATABASE redmine;

$mysql> CREATE USER 'redmine'@'localhost' IDENTIFIED WITH $mysql_native_password BY 'Redmine@1234';

$mysql> GRANT ALL PRIVILEGES ON redmine.* TO 'redmine'@'localhost' WITH GRANT OPTION;

$mysql>quit;

#確認
$mysql -u redmine -p
$mysql>quit;

```

#### 最新の安定版バージョンをダウンロード
- [redmineとRubyバージョン](https://redmine.org/projects/redmine/wiki/RedmineInstall) 
- [redmine tar](https://www.redmine.org/projects/redmine/wiki/Download)


```bash
$svn co https://svn.redmine.org/redmine/branches/4.2-stable /usr/share/redmine
$cd /usr/share/redmine/config/
$cp database.yml.example database.yml
$cat database.yml
production:
  adapter: mysql2
  database: redmine
  host: localhost
  username: redmine #変更
  password: "Redmine@1234" #変更
  # Use "utf8" instead of "utfmb4" for MySQL prior to 5.7.7
  encoding: utf8mb4

development:
  adapter: mysql2
  database: redmine_development
  host: localhost
  username: redmine
  password: "Redmine@1234"
  # Use "utf8" instead of "utfmb4" for MySQL prior to 5.7.7
  encoding: utf8mb4
```

#### Rubyのパッケージインストール
##### インストール

```bash
$cd /usr/share/redmine/
#必要なgemをインストール
$bundle install --without development test
#セッションデータ暗号化
$bundle exec rake generate_secret_token

#データベースのマイグレーション
$bundle exec rake db:migrate RAILS_ENV=production
```

### Unicorn
#### インストール
```bash
$cd /usr/share/redmine
$vi Gemfile.local
gem "unicorn"

$cd /usr/share/redmine
$bundle update

$vi /usr/lib/systemd/system/redmine-unicorn.service
[Unit]
Description=Redmine Unicorn Server
After=mysqld.service

[Service]
WorkingDirectory=/usr/share/redmine
Environment=RAILS_ENV=production
SyslogIdentifier=redmine-unicorn
PIDFile=/var/lib/redmine/tmp/pids/unicorn.pid

#bundle　に関してはよしなにに変更してください
#whereis bundle で出力されたPathを設定
ExecStart= /root/.rbenv/shims/bundle exec "unicorn_rails -c config/unicorn.rb -E production"
ExecStop=/usr/bin/kill -QUIT $MAINPID
ExecReload=/bin/kill -USR2 $MAINPID

[Install]
WantedBy=multi-user.target

```

#### RedmineにUnicornの設定ファイル追加

```bash
$vi /usr/share/redmine/config/unicorn.rb
worker_processes 2

# ここPath重要
#app_path = "/var/lib/redmine"
app_path="/usr/share/redmine"

listen  File.expand_path('tmp/unicorn_redmine.sock', app_path)
pid File.expand_path('tmp/unicorn.pid', app_path)
stderr_path File.expand_path('log/unicorn.stderr.log', app_path)
stdout_path File.expand_path('log/unicorn.stdout.log', app_path)

preload_app true

timeout 30

if GC.respond_to?(:copy_on_write_friendly=)
  GC.copy_on_write_friendly = true
end

before_fork do |server, worker|
  defined?(ActiveRecord::Base) and ActiveRecord::Base.connection.disconnect!

  old_pid = "#{server.config[:pid]}.oldbin"
  if old_pid != server.pid
    begin
      sig = (worker.nr + 1) >= server.worker_processes ? :QUIT : :TTOU
      Process.kill(sig, File.read(old_pid).to_i)
    rescue Errno::ENOENT, Errno::ESRCH
    end
  end
end

after_fork do |server, worker|
  defined?(ActiveRecord::Base) and ActiveRecord::Base.establish_connection
end

```

#### Service start

```bash
#※1
$systemctl start redmine-unicorn
$systemctl enabel redmine-unicorn
```

※1 start err
多分権限問題
```bash
$chmod 755 /usr/share/redmine
```

## Nginxのインストール

#### install
```bash
$vi /etc/yum.repos.d/nginx.repo
  [nginx] # リポジトリID（なんのリポジトリなのかを表す、ユニークで任意な名前）
  name=nginx repo # リポジトリ名(yum repolistで表示される)
  baseurl=http://nginx.org/packages/mainline/centos/7/$basearch/ # リポジトリの場所(このurlの先のリモートにリポジトリがあ>る)
  gpgcheck=1 # GPG署名確認を有効にするか否か（0 = しない, 1 = する）
  enabled=1 # yum コマンド利用時にこのリポジトリを使用するかどうか（0 = しない, 1 = する）
  gpgkey=http://nginx.org/keys/nginx_signing.key

  [nginx-source]
  name=nginx source
  baseurl=http://nginx.org/packages/mainline/centos/7/SRPMS/
  gpgcheck=1
  enabled=0
  gpgkey=http://nginx.org/keys/nginx_signing.key

# install
$yum install -y nginx
$rpm -qi nginx

$firewall-cmd --add-service http
$firewall-cmd --add-service http --permanent
$firewall-cmd --add-port 8080-8089/tcp
$firewall-cmd --add-port 8080-8089/tcp --permanent

# http://{serverIp}:80
# http://192.168.1.30:80

```

#### config

```bash

$cd /etc/nginx/conf.d/
#backupfile
$cp default.conf default.conf_backup
#redmine config file
$mv default.conf redmine.conf
#edit
$vi redmine.conf
server {
    listen       80;
    server_name  localhost;

    root /usr/share/redmine/public;

    # SSL証明書（Letsencrypt）の連携
    #location ^~ /.well-known/ {
    #   root /usr/share/nginx/html;
    #}

    # Unicornを連携
    location / {
       try_files $uri/index.html $uri.html $uri @app;
    }

    location @app {
       proxy_redirect off;
       proxy_set_header X-Real-IP $remote_addr;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header Host $http_host;
       proxy_connect_timeout 60;
       proxy_read_timeout 60;
       proxy_send_timeout 600;
       proxy_pass http://unicorn-redmine;
    }

   error_page 500 502 503 504 /500.html;
}

upstream unicorn-redmine {
  server unix:/usr/share/redmine/tmp/unicorn_redmine.sock;
}
```

#### Nginxの起動
```bash
$systemctl start nginx
$systemctl enable nginx

```

## Redmine表示
- http://{centos7ip}:80
- 初期設定
  - ID:admin
  - PW:admin


### MySQLデータベース自動バックアップ運用

```bash
#root user
$su -
$cd ~
$cd bin
$vi mysql-backup.sh
#!/bin/bash
# バックアップ先ディレクトリ
BACKDIR=/var/db/backup/mysql
# MySQLrootパスワード
ROOTPASS=P@ssword1234
# バックアップ先ディレクトリ再作成
rm  -rf $BACKDIR
mkdir -p $BACKDIR
# データベース名取得
DBLIST=`ls -p /var/lib/mysql | grep / | tr -d /`
# データベースごとにバックアップ
for dbname in $DBLIST
do
    [ $dbname = "performance_schema" ] && continue
    table_count=`mysql -u root -p$ROOTPASS -B -e "show tables" $dbname|wc -l`
    [ $table_count -ne 0 ] &&
    mysqldump --events -u root -p$ROOTPASS $dbname > $BACKDIR/$dbname.sql | logger -t mysqldump
done
#バックアップ定期自動実行設定
$echo "0 5 * * * root /root/bin/mysql-backup.sh" > /etc/cron.d/backup
```


mysql backup 復元テスト
```bash
$mysql -u root -p
$mysql> create database test; 
$mysql> use test
$mysql> create table test(num int, name varchar(50)); 
$mysql> insert into test values(1,'山田太郎');
$mysql> quit; 
$/root/bin/mysql-backup.sh
$mysqladmin -uroot -p -f drop test
$mysqladmin -uroot -p create test
$mysql -u root -p test < /backup/mysql/test.sql
$mysql -u root -p
$mysql> show databases;
$mysql> use test
$mysql> show tables;
$mysql> select * from test;

```






### 参考サイト
- [CentOS7にRedmine4を導入する](https://rin-ka.net/redmine-install-centos/#toc22)
- [redmine](http://guide.redmine.jp/RedmineInstall/) 
- [ruby bundler](https://www.sejuku.net/blog/19426)