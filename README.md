# shareMusicApi
咪咕音乐，QQ音乐，网易云音乐搜索接口


# 启动步骤
* 启动第三方接口
  * 进入lib目录,进入NeteaseCloudMusicApi,使用node app.js启动网易云接口，监听3000端口.
* 爬虫接口
  * 进入spider目录，使用python3 main.py 启动项目爬虫接口，监听8080端口
* 修改安卓网络配置,res/xm/netword_secutiry_config.xml 加入当前电脑ip。
* 数据库使用mysql 3306端口，初始化用户admin
* 启动api接口,idea导入shareApi启动spring-boot项目
* 导入nginx配置,监听9000端口,/spider转发爬虫端口，/api转发spring-boot 端口
