[![Build Status](https://travis-ci.org/toulezu/jee-utils.svg?branch=master-github)](https://travis-ci.org/toulezu/jee-utils) [![codecov](https://codecov.io/gh/toulezu/jee-utils/branch/master-github/graph/badge.svg)](https://codecov.io/gh/toulezu/jee-utils)



# jee-utils
- Java EE 开发中常用的工具类

# 如何使用

- 在需要依赖此项目的 pom.xml 中添加如下配置后，再添加相应的依赖关系即可
```xml
<!-- github 仓库地址 -->
<repositories>
	<repository>
	   	<id>jee-utils-mvn-repo</id>
		<url>https://raw.github.com/toulezu/jee-utils/mvn-repo/</url>
	</repository>
</repositories>

<!-- maven依赖 -->
<dependencies>
	<dependency>
		<groupId>com.ckjava</groupId>
		<artifactId>jee-utils</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```

## 方法列表

### ObjectUtils 

- isEmptyObject : 对象的所有字段都为空才返回true
- isNotEmptyObject : 对象的所有字段都不为空才返回 true
- getObjectString : 将一个对象中不为空的字段拼接成 key1=value1&key2=value2
- fillMapWithString : 将 key1=value1&key2=value2的字符串存入Map中
- objectToBytes : 将Java对象Object转换成Byte字节数组
- bytesToObject : 将 Byte字节数组 转成 Java 对象
