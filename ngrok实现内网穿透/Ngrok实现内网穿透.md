# Ngrok实现内网穿透

https://dashboard.ngrok.com/  

进入到ngrock的官网,可以使用github的帐号进行免费注册

ngrok有免费的隧道ID(ngrok给生成固定的域名)也有收费的(可以自定义域名)

本案例为windows系统下的操作,操作流程如下:

```
1. 下载windows版本的ngrok,解压到任意的目录下
```

###### ![1590464509514](.\typro-img\1590464509514.png)

```
2. 进入到cmd界面,并进入到存有ngrok.exe文件的目录中执行以下命令
ngrok authtoken 1cQYg10gtnxRVnauDNIU33GiHEd_qGyvMRYggxqZAQsqcvKi
(免费的隧道ID,每个人的不同,登录进去之后就有了,这个是我的ID)
```

![1590464642570](.\typro-img\1590464642570.png)

```
3. 如果启动的本地的项目的端口是8080,就执行ngrok http 8080
会生成http和htpps的外网地址,访问即可
```

![1590464789432](.\typro-img\1590464789432.png)

