# shareMusicApi
咪咕音乐，QQ音乐，网易云音乐搜索接口

# 项目要求环境
* jdk8
* python3.7(及pip)
* nodejs
* docker-compose
* ubuntu18.0(或16.0)
* 项目占用端口:数据库3306,网易云3000,爬虫8080,nginx9000,spring-boot8081。确保端口不被占用。

# 启动步骤
* 进入lib/NeteaseCloudMusicApi目录下，使用npm install。
* 进入spider目录，使用python3 -m pip install aiohttp(python版本3.7以上)。
* 进入shareMucisApi使用mvn install。
* 命令行下运行./start.sh。(建议运行前先用docker-compose构建，否则第一次运行可能会失败)。
* 修改安卓网络配置,res/xm/netword_secutiry_config.xml 加入当前电脑ip,BaseHttpService修改BASE_URL为当前电脑ip。
* 数据库使用mysql 3306端口，初始化用户root,密码123456。
