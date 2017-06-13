# PermissionCheck
Android M permission check

Android M权限检查，绝对好用，用法简单，通俗易懂，再也不用担心23了
# 使用方法：
第一步：
```java
PermissionCheck
                .with(MainActivity.this)
                .setRequestCodeAndisCue(requestCode, true)//requestCode是code码,true表示弹窗提示用户,false表示不弹窗
                .showText("我是标题", "我是内容1", "我是内容2", "按钮1", "按钮2")
                .needPermission("需要申请的权限")
                .callback(MainActivity.this)
                .check();                        
```
第二步：
```java
/**
*Activity需要实现PermissionCallBack接口
*接收权限申请结果接口回调方法，可以使用requestCode判断对应的申请，resultCode对应申请结果
*/
@Override
    public void applyResult(int requestCode, int resultCode) {
    }
```
第三步：
```java
/**
*重写该方法，用于接收结果
*/
@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionCheck.onRequestPermissionsResult(MainActivity.this, requestCode, permissions, grantResults);
    }
```
***
以上就可以使用了，简单方便快捷
</br>ps：设置弹窗与否针对于单个必须权限的检查
# 效果图：


***
# Using library in your application

Gradle dependency:
```java
compile 'com.github.jsmeli:permissioncheck:0.0.8'
```
or

Maven dependency:
```java
<dependency>
  <groupId>com.github.jsmeli</groupId>
  <artifactId>permissioncheck</artifactId>
  <version>0.0.8</version>
  <type>pom</type>
</dependency>
```
