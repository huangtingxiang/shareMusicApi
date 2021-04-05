#! /bin/bash

gnome-terminal --window -x bash -c "docker-compose build --no-cache;docker-compose up;exec bash;"

gnome-terminal --window -x bash -c "node ./lib/NeteaseCloudMusicApi/app.js;exec bash;"
sleep 5
gnome-terminal --window -x bash -c "python3.7 ./spider/main.py;exec bash;"
sleep 10
gnome-terminal --window -x bash -c "cd shareMusicApi;./mvnw spring-boot:run;exec bash;"
exit
