package com.leo.camera2demo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.support.v4.content.ContextCompat

/**
 * Created by leo on 2019/1/6.
 * Description:
 */
class CameraHelper {
    //    private var
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraDevice: CameraDevice

    companion object {
        fun checkPermission(context: Context) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermission()
            }
        }

        private fun requestPermission(){

        }
    }
}