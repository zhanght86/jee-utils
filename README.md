[![Build Status](https://travis-ci.org/toulezu/jee-utils.svg?branch=master)](https://travis-ci.org/toulezu/jee-utils)


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
