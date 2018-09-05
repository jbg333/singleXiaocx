
https使用详见https://blog.csdn.net/u012702547/article/details/53790722
生成证书
keytool -genkey -alias tomcat  -storetype PKCS12 -keyalg RSA -keysize 2048  -keystore D:\keystore.p12 -validity 3650


server.ssl.key-store=keystore.p12
server.ssl.key-store-password=111111
key-store-password: 111111
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias:tomcat