## 文件传输客户端

## 环境
- JDK8+
- Maven3.6.1+

进入项目根路径后，执行以下命令，以安装
 [`teclan-jwt`](https://github.com/teclan/teclan-jwt) 、
 [`teclean-flyway`](https://github.com/teclan/teclan-flyway)
 [teclan-netty](https://github.com/teclan/teclan-netty) 
 依赖

``` 
git clone https://github.com/teclan/teclan-jwt
cd teclan-jwt
mvn install -D maven.test.skip=true

git clone https://github.com/teclan/teclan-flyway
cd teclan-jwt
mvn install -D maven.test.skip=true

git clone https://github.com/teclan/teclan-netty
cd teclan-netty
mvn install -D maven.test.skip=true
```