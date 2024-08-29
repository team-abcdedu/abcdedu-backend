#!/bin/bash
IS_BLUE=$(docker ps | grep blue | wc -l)
if [ ${IS_BLUE} -eq 1 ]; then
  docker-compose up -d green
  while [ 1 = 1 ]; do
    sleep 3
    REQUEST=$(curl http://localhost:8082/api/v1/actuator/health)
    IS_ACTIVE=$(echo ${REQUEST} | grep 'UP' | wc -l)
    if [ ${IS_ACTIVE} -eq 1 ] ; then
      break;
    fi
  done;
  sleep 3
  sudo cp /etc/nginx/sites-available/green /etc/nginx/sites-enabled/default
  sudo nginx -s reload
  docker-compose stop blue
  docker-compose rm blue

else
  docker-compose up -d blue
  while [ 1 = 1 ]; do
    sleep 3
    REQUEST=$(curl http://localhost:8081/api/v1/actuator/health)
    IS_ACTIVE=$(echo ${REQUEST} | grep 'UP' | wc -l)
    if [ ${IS_ACTIVE} -eq 1 ] ; then
      break;
    fi
  done;
  sleep 3
  sudo cp /etc/nginx/sites-available/blue /etc/nginx/sites-enabled/default
  sudo nginx -s reload
  docker-compose stop green
  docker-compose rm green
fi
