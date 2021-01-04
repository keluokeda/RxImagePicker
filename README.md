# RxImagePicker
## 导入方式
#### 将JitPack存储库添加到您的构建文件中(项目根目录下build.gradle文件)
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### 添加依赖项
```
dependencies {
	        implementation 'com.github.keluokeda:ApkInstaller:1.0.0'
	}
```

## 使用
```
 RxImagePicker(this)
                .pick(RxImagePicker.SOURCE_CAMERA)
                .subscribe {
                    imageView.setImageURI(it.uri)
                }
 ```
