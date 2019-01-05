package com.leo.camera2demo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.TextureView

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraList: Array<String>? = null
    private lateinit var backgroudThread: HandlerThread
    private lateinit var backgroudHandler: Handler
    private lateinit var cameraDevice: CameraDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @SuppressLint("MissingPermission")
    private fun initCamera() {
        backgroudThread = HandlerThread("camera handler")
        backgroudThread.start()
        backgroudHandler = Handler(backgroudThread.looper)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        //get camera list
        cameraList = cameraManager.cameraIdList

        val characteristics = cameraManager.getCameraCharacteristics(cameraList!![0])
        //open camera
        cameraManager.openCamera(cameraList!![0], stateCallBack, backgroudHandler)

    }

    private val stateCallBack = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {

        }

        override fun onDisconnected(camera: CameraDevice?) {
        }

        override fun onError(camera: CameraDevice?, error: Int) {
        }

    }
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(session: CameraCaptureSession?, request: CaptureRequest?, partialResult: CaptureResult?) {
        }

        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
        }
    }

    private val stateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession?) {
        }

        override fun onConfigured(session: CameraCaptureSession?) {
        }

    }

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        }

    }
}
