version avec buildpack.toml
## Approche JDK
```
Builder : Dockerfile multi-stage
Image builder : openjdk:17-jdk-alpine
Image Run : eclipse-temurin:17-jdk-alpine
Application : Mode FatJar
```
Construire l'image (90s-1.7s):
```shell
docker build -f Docker/jdk.Dockerfile -t beneficiaires:jdk .
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8580:8080 --name beneficiaires-jdk beneficiaires:jdk
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-jdk-cli beneficiaires:jdk
```


## Approche JRE
```
Builder : Dockerfile multi-stage
Image builder : openjdk:17-jdk-alpine
Image Run : eclipse-temurin:17-jre-alpine
Application : Mode FatJar
```
Construire l'image (1.3s):
```shell
docker build -f Docker/jre.Dockerfile -t beneficiaires:jre .
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8581:8080 --name beneficiaires-jre beneficiaires:jre
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-jre-cli beneficiaires:jre
```



## Approche Custom JRE avec Jlink
```
Builder : Dockerfile multi-stage
Image builder : openjdk:17-jdk-alpine
Image Run : alpine:latest
Application : Mode FatJar
Optimisation du JRE avec Jlink
```
Construire l'image (112.8s-1.4s):
```shell
docker build -f Docker/custom-jre.Dockerfile -t beneficiaires:custom-jre .
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8582:8080 --name beneficiaires-custom-jre beneficiaires:custom-jre
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-custom-jre-cli beneficiaires:custom-jre
```

## Approche Jib
```
Builder : Jib sur Maven
Application : Mode FatJar
Build sans Docker
```
ATTENTION pour jib il faut modifier le fichier $home/.docker/config.json
et enlever la partie credsStore a remettre apres.

Construire l'image (50.653,12.313 s):
```shell
./mvnw compile jib:dockerBuild -Dimage=beneficiaires:jib
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8583:8080 --name beneficiaires-jib beneficiaires:jib
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-jib-cli beneficiaires:jib
```

## Approche Spring-Boot CNB
```
Builder : Spring-boot:build-image sur Maven
Application : Mode FatJar
```
Prerequis:
* `Docker`

Construire l'image avec spring-boot CNB plugin (53.191 s-22.172 s): 
```shell
./mvnw clean spring-boot:build-image -Dspring-boot.build-image.imageName=beneficiaires:cnb -DskipTests
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8584:8080 --name beneficiaires-cnb beneficiaires:cnb
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-cnb-cli beneficiaires:cnb
```

## Approche Pack CNB
```
Builder : Pack
Application : Mode FatJar
```
Prerequis:
* `Docker`
* `Pack`

Construire l'image avec Pack (50s)
```shell
pack build --builder=paketobuildpacks/builder:tiny -eGOOGLE_RUNTIME_VERSION=17 beneficiaires:pack
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8585:8080 --name beneficiaires-pack beneficiaires:pack
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-pack-cli beneficiaires:pack
```

## Approche Spring-Boot CNB Native
```
Builder : Spring-boot:build-image avec native sur Maven
Application : Mode FatJar
```
prerequis: 
* `Docker (avec 4 CPU & 8Gb RAM min)`
* `GraalVM pour Java17 : java 22.3.1.r17-grl` 

Construire l'image avec spring-boot CNB plugin (09:27 min):
```shell
./mvnw clean spring-boot:build-image \
      -Pnative \
      -Dspring-boot.build-image.imageName=beneficiaires:cnb-native -DskipTests
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8586:8080 --name beneficiaires-cnb-native beneficiaires:cnb-native
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-cnb-native-cli beneficiaires:cnb-native
```


## Approche Pack CNB Native
```
Builder : Pack avec Native
Application : Mode FatJar
```
Construire l'application avec AOT processed Spring Boot executable jar (6m40):
```shell
./mvnw clean package
```
Builder avec pack (6m40s):
```shell
pack build --builder paketobuildpacks/builder:tiny \
    --path target/beneficiaires-1.0-exec.jar \
    --env 'BP_NATIVE_IMAGE=true' \
    beneficiaires:native-jar-cnb
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8587:8080 --name beneficiaires-native-jar-cnb beneficiaires:native-jar-cnb
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-native-jar-cnb-cli beneficiaires:native-jar-cnb
```

## Approche Spring-Boot CNB Docker Native
```
Builder : Dockerfile multi-stage
Image builder : ghcr.io/graalvm/native-image:ol8-java17-22
Image Run : registry.access.redhat.com/ubi8/ubi-minimal:8.7-1107
Application : Mode FatJar
```
Builder sur Dockerfile avec compil SB native (365.4s):
```shell
docker build -f Docker/native.Dockerfile -t beneficiaires:native .
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8588:8080 --name beneficiaires-native beneficiaires:native
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-native-cli beneficiaires:native
```

## Approche Spring-Boot CNB Docker Native Compressé
```
Builder : Dockerfile multi-stage
Image builder : ghcr.io/graalvm/native-image:ol8-java17-22
Image Run : registry.access.redhat.com/ubi8/ubi-minimal:8.7-1107
Application : Mode FatJar / Compressé par UPX
```
build from Dockerfile (479.7s):
```shell
docker build -f Docker/native-x.Dockerfile -t beneficiaires:native-x .
```
Lancer l'application:
```shell
docker run -it -ePORT=8080 -p8589:8080 --name beneficiaires-native-x beneficiaires:native-x
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-native-x-cli beneficiaires:native-x
```

## Approche Kpack CNB sur Kubernetes
```
Builder : cloudfoundry/cnb:bionic sur kpack
Application : Mode FatJar
```
build from Kubernetes ressources (3m26):
Dans le repertoire kpack:
installer les ressources :
```shell
./kpack_builder.sh install
```
lancer le build de l'image:
```shell
./kpack_builder.sh build
```
Lancer l'application:
```shell
docker run -it -p 8592:8079 --name beneficiaires-kpack synapsety/beneficiaires:kpack
```
ou Lancer l'application en mode client:
```shell
docker run -it -eBENEF_CLI=yes --name beneficiaires-kpack-cli synapsety/beneficiaires:kpack
```
pas de recherche d'update sur les images socles:
https://github.com/buildpacks-community/kpack/blob/main/docs/stack.md

prise ne charge des sides-cars:
https://github.com/buildpacks-community/kpack/blob/main/docs/injected_sidecars.md

c'est l'image qui surveille pour les changements:
https://github.com/buildpacks-community/kpack/blob/main/docs/image.md

