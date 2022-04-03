FROM alpine:3.6

# create user & group
# -D :  Don't assign a password
#RUN addgroup -S nginx_group && adduser -D -S nginx_user -G nginx_group

# alpine packages 
RUN apk update && apk upgrade
# install nginx
RUN apk add --no-cache  nginx
# nginx home dir
RUN mkdir -p /run/nginx
RUN chown -R nginx:nginx  /run/nginx
RUN chmod -R 775 /run/nginx

#COPY nginx.conf /etc/nginx/nginx.conf

# change owner & group
# -R:sud dir Recurse
RUN mkdir -p /var/www/html
COPY html /var/www/html
RUN chown -R nginx:nginx  /var/www/html
RUN chmod -R 775 /var/www/html

ADD default.conf /etc/nginx/conf.d/default.conf
#ADD nginx.conf /etc/nginx/nginx.conf
# ポート設定
EXPOSE 80
# フォアグラウンドでnginx実行
CMD nginx -g "daemon off;"

