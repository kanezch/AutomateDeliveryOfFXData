# AutomateDeliveryOfFXData

FXDataMonitor

./gradlew jibDockerBuild -Djib.container.environment=APITOKEN="5dd1e36b7af744.86050871" --image=kanezheng/fxdatamonitor


docker network create fxdata_net


docker run --network fxdata_net --hostname rabbitmqhost --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:management

spring.rabbitmq.host=rabbitmq

docker run -d --network fxdata_net --hostname fxdatamonitorhost --name fxdatamonitor kanezheng/fxdatamonitor:v1



FXDataReceiver

./gradlew jibDockerBuild -Djib.container.environment=EMAIL_ADDRESS=kanezhengsydneytest@gmail.com --image=kanezheng/fxdatareceiver


spring.rabbitmq.host=rabbitmq

distributionUrl=https\://services.gradle.org/distributions/gradle-4.10-all.zip

docker run -d --network fxdata_net --hostname fxdatareceiverhost --name fxdatareceiver kanezheng/fxdatareceiver:v1

