### 下载

[ ![Download](https://api.bintray.com/packages/huangwilliam33333/maven/fastpermission-x/images/download.svg) ](https://bintray.com/huangwilliam33333/maven/fastpermission-x/_latestVersion)

``` groovy
//for androidx
implementation "com.hwilliamgo:fastpermission-x:1.0.0"

//for support lib
implementation 'com.hwilliamgo:fastpermission-support:1.0.1'
```



### 使用

Android一行代码完成危险权限请求：

``` kotlin
//声明权限数组
val permissionList = arrayListOf<String>(
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

//请求权限，通过回调判断请求结果。
FastPermission.getInstance()
    .start(this, permissionList, object :
        OnPermissionCallback {
        override fun onAllGranted() {
        }
        override fun onGranted(grantedPermissions: ArrayList<String>?) {
        }
        override fun onDenied(deniedPermissions: ArrayList<String>?) {
        }
        override fun onDeniedForever(deniedForeverP: ArrayList<String>?) {
            FastPermission.getInstance()
                            .jump2Settings(this@MainActivity)
        }
    })
```

