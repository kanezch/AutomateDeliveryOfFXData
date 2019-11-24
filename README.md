#### My local environment

```shell
OS: macOS Mojave 10.14.6
Docker: 18.03.1-ce-mac65 (24312)
IDE: IDEA
JAVA: 1.8
```



#### Process

![process](https://github.com/kanezch/AutomateDeliveryOfFXData/blob/master/process.png)

#### Step1 Create Docker images and network

```shell
#APITOKEN and EMAIL_ADDRESS are input through environment variables and I assume that the host for creating docker images is securely enough
cd FXDataMonitor
./gradlew jibDockerBuild -Djib.container.environment=APITOKEN="5dd1e36b7af744.86050871" --image=kanezheng/fxdatamonitor:v1

cd FXDataReceiver
./gradlew jibDockerBuild -Djib.container.environment=EMAIL_ADDRESS=kanezhengsydneytest@gmail.com --image=kanezheng/fxdatareceiver:v1

docker network create fxdata_net
```



#### Step2 Run containers

```shell
#Run rabbitmq
docker run --network fxdata_net --hostname rabbitmqhost --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:management

#Run fxdatamonitor
docker run -d --network fxdata_net --hostname fxdatamonitorhost --name fxdatamonitor kanezheng/fxdatamonitor:v1

#Run fxdatareceiver
docker run -d --network fxdata_net --hostname fxdatareceiverhost --name fxdatareceiver kanezheng/fxdatareceiver:v1
```