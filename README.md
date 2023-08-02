# optional-face-recognition

<p>
  <a href="https://www.oracle.com/java/technologies/javase/17u-relnotes.html"><img src="https://img.shields.io/badge/jdk-%3E=17.0.0-blue.svg" alt="jdk compatility"></a>
  <a href="https://spring.io/projects/spring-boot"><img src="https://img.shields.io/badge/springboot-%3E=3.0.0-green.svg" alt="springboot compatility"></a>
  <a href="https://cloud.baidu.com/product/face"><img src="https://img.shields.io/badge/Baidu_Face_Offline_SDK_Windows_Java-=8.3-orange.svg" alt="Baidu_Face_Offline_SDK_Windows_Java compatility"></a>
  <a href="https://ai.arcsoft.com.cn/product/arcface.html"><img src="https://img.shields.io/badge/ArcFaceSDK-=4.1-red.svg" alt="ArcFaceSDK compatility"></a>
  <a href="https://www.faceplusplus.com.cn/"><img src="https://img.shields.io/badge/MegviiFaceSDK-=3.0-violet.svg" alt="MegviiFaceSDK compatility"></a>
</p>

## 介绍

项目基于 [springboot@3.0.5](https://spring.io/projects/spring-boot) 系列开发，开发环境使用 [jdk@17.x](https://www.oracle.com/java/technologies/downloads/#java17)。

适配百度、虹软、旷视人脸识别SDK

## 预览

## 项目结构说明

## 接口文档

http://localhost:8080/doc.html

## 使用

1、打包

2、修改配置文件

3、部署启动

### 不同SDK介绍（版本、价格）

#### 百度

分为**在线版**和**离线版**。

在线版是通过百度云的URL识别检测的，无需本地SDK，图片可保存在百度云人脸库，每月（天）有调用次数的限制，不过在开发测试阶段无需担心，因为每个账号的调用次数还是非常给力的，完全够用。

免费调用次数：

![baidu-04](https://github.com/yushuishu/optional-face-recognition/assets/50919172/0964bbd7-e527-4a03-b301-e475d7e88a85)

购买价格：

![baidu-03](https://github.com/yushuishu/optional-face-recognition/assets/50919172/68a5afc2-3fb7-4a3a-a79f-36315562d29c)

<br>

离线版启动项目需要SDK，并且是单台设备激活，多台设备激活需要购买多个激活码。离线版的购买，购买数量是100个起购，如果想要购买一个（一台设备）激活码，比如在开发阶段，想要在本机使用，或者在个人电脑上做Demo，不需要很多激活码，就只能通过活动来购买。

百度离线SDK购买（普通方式）：

![badiu-02](https://github.com/yushuishu/optional-face-recognition/assets/50919172/f8192839-1c59-466b-808f-ec02c60f3dbc)


百度离线SDK购买（活动方式）：https://cloud.baidu.com/campaign/PromotionActivity/index.html?track=navigationA

![baidu-01](https://github.com/yushuishu/optional-face-recognition/assets/50919172/a20df648-3984-4fd1-bb20-531c3461c7fb)


#### 虹软

https://ai.arcsoft.com.cn/manual/docs#/211

![hongruan-01](https://github.com/yushuishu/optional-face-recognition/assets/50919172/0a760eec-6f6e-4970-bf77-9095ea15ba94)



#### 旷视




## 引用

