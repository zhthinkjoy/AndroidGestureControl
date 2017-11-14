# 简介
本软件是基于习悦离线手势sdk来进行手势识别，通过识别不同的手势来产生不同事件，最终实现对android系统的简单控制。

## 使用方法
### 下载demo
```bash
git clone https://github.com/zhthinkjoy/AndroidGestureControl.git
```

### 下载sdk
进入[习悦开发者平台](https://dev.zhthinkjoy.com/SDK)，下载离线人脸识别sdk
[![](https://github.com/zhthinkjoy/git_resource/raw/master/ZHThinkjoyGesture_Android_SDK/gesture_sdk_download.png)](https://dev.zhthinkjoy.com/SDK)

## 将sdk放到指定目录
解压sdk，在ap目录下创建libs，将arm64-v8a,armeabi-v7a,和ZHTJgesture_v1.1 放在libs目录下
![](https://github.com/zhthinkjoy/git_resource/raw/master/ZHThinkjoyGesture_Android_SDK/add_gesture_library.png)

## 添加jar库为library
点击jar包，如果可以看见包里面的内容，则可以直接编译运行。若不能查看，右键点击ZHTJface_v1.0.jar，选择`add as library`。


