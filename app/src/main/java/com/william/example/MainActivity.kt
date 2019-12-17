package com.william.example

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.william.fastpermisssion.FastPermission
import com.william.fastpermisssion.OnPermissionCallback
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermission = {
            val permissionList = arrayListOf<String>(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )

            FastPermission.getInstance()
                .start(this, permissionList, object :
                    OnPermissionCallback {
                    override fun onAllGranted() {
                        Toast.makeText(this@MainActivity, "所有请求通过", Toast.LENGTH_SHORT).show()
                    }

                    override fun onGranted(grantedPermissions: ArrayList<String>?) {
                        Log.i(MainActivity::javaClass.name, "部分权限被允许：$grantedPermissions")
                    }

                    override fun onDenied(deniedPermissions: ArrayList<String>?) {
                        deniedPermissions?.also {
                            var formattedText = ""
                            it.forEach { p ->
                                formattedText = formattedText.plus(p).plus("\n")
                            }

                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("提示")
                                .setMessage("如下权限未通过：\n $formattedText")
                                .create()
                                .show()
                        }
                    }

                    override fun onDeniedForever(deniedForeverP: ArrayList<String>?) {
                        deniedForeverP?.also {
                            var deniedForeverText = ""
                            it.forEach { p ->
                                deniedForeverText = deniedForeverText.plus(p).plus("\n")
                            }
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("提示")
                                .setMessage("如下权限被永久禁用:\n $deniedForeverText \n 请到系统设置中打开权限，否则影响后续功能正常使用")
                                .setPositiveButton("跳转到该app的系统设置") { _, _ ->
                                    FastPermission.getInstance()
                                        .jump2Settings(this@MainActivity)
                                }
                                .create()
                                .show()
                        }
                    }
                })
        }

        (findViewById<View>(android.R.id.content) as ViewGroup)
            .addView(
                Button(this).apply {
                    setOnClickListener {
                        requestPermission()
                    }
                },
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )


    }
}
