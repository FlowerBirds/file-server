# file-server
服务间通过feign调用接口后端上传文件

- SpringBoot 2.x
- OpenFeign

参数列表：

1. server.target: 上传到哪个服务上，例如http://localhost:8080/
2. file.location: 被上传的文件所在的目录，上传时会将此目录下的文件上传，例如/opt/tomcat/lib



使用方法

同台服务器测试，打开一个shell窗口：

```bash
java -jar -Dserver.port=8080 -Dserver.target=http://localhost:8081/ -Dfile.location=/opt/tomcat/lib file-server-1.0-SNAPSHOT.jar
```

打开另外一个窗口：

```bash
java -jar -Dserver.port=8081 file-server-1.0-SNAPSHOT.jar
```

使用命令行触发：

```bash
curl http://localhost:8080/api/file-test/upload
```

就可以测试服务中间文件上传功能