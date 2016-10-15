call_go_from_java
====

## dependency

* java 1.7+
* golang 1.6+
* maven

## usage


```
git clone git@github.com:flowbehappy/call_go_from_java.git
cd call_go_from_java
sh build_go.sh
# mvn jnaerator:generate
```
* `build_go.sh` - compiles the `test.go` into a c dynamic lib, put int `lib` folder.
* `mvn jnaerator:generate `- generates the jna Java classes by jnaerator. Note that somehow in Greate China [nativelibs4java.sourceforge.net](nativelibs4java.sourceforge.net) is likely blocked, this script may fail to download the `maven-jnaerator-plugin`. Consider using an VPN. And you don't need to run this script if you haven't modified the export methods in test.go.

It is a maven project. Import this project into your favourite dev environment. Compile and run `TestCallGolang`.
