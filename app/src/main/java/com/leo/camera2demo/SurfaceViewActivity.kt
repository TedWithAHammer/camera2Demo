package com.leo.camera2demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.view.SurfaceHolder
import android.view.TextureView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_surfaceview.*

class SurfaceViewActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraList: Array<String>? = null
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private lateinit var cameraDevice: CameraDevice
    private lateinit var cameraCaptureSession: CameraCaptureSession

    companion object {
        const val REQUEST_CAMERA_CODE = 10001
    }

    private lateinit var holder: SurfaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surfaceview)
        initWidget()
    }

    private fun initWidget() {
        //        surfaceTexture = tv_camera.surfaceTexture
        holder = sv_camera.holder
        if (!checkAndRequestCameraPermission()) {
            return
        }
        initCamera()
    }

    private fun checkAndRequestCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return if (ActivityCompat.checkSelfPermission(this,
                                                      Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                                              REQUEST_CAMERA_CODE)
            false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_CODE) {
            if (!checkAndRequestCameraPermission()) {
                return
            }
            initCamera()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initCamera() {
        backgroundThread = HandlerThread("camera handler")
        backgroundThread.start()
        backgroundHandler = Handler(backgroundThread.looper)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        //get camera list
        cameraList = cameraManager.cameraIdList
        val cameraId = CameraCharacteristics.LENS_FACING_BACK
        val characteristics = cameraManager.getCameraCharacteristics(cameraId.toString())
        val streamConfigurationMap = characteristics.get(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val outputSize = streamConfigurationMap?.getOutputSizes(SurfaceTexture::class.java)
        val fpsRanges = characteristics.get(
            CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)
        //open camera
        cameraManager.openCamera(cameraId.toString(), stateCallBack, backgroundHandler)
    }

    private lateinit var builder: CaptureRequest.Builder
    private lateinit var request: CaptureRequest


    private fun createCaptureSession() {
        try {
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //            surfaceTexture.setDefaultBufferSize(800, 600)
            //            val previewSurface = Surface(surfaceTexture)
            builder.addTarget(holder.surface)
            cameraDevice.createCaptureSession(listOf(holder.surface), captureSessionCallback,
                                              Handler())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun createCameraRequest() {
        try {
            builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            request = builder.build()
            cameraCaptureSession.setRepeatingRequest(request, captureCallback, Handler())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private val stateCallBack = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice?) {
            if (camera != null) {
                cameraDevice = camera
                createCaptureSession()
            }
        }

        override fun onDisconnected(camera: CameraDevice?) {
            cameraDevice.close()
        }

        override fun onError(camera: CameraDevice?, error: Int) {
            cameraDevice.close()
        }

    }
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(session: CameraCaptureSession?,
                                         request: CaptureRequest?,
                                         partialResult: CaptureResult?) {
        }

        override fun onCaptureCompleted(session: CameraCaptureSession?,
                                        request: CaptureRequest?,
                                        result: TotalCaptureResult?) {
        }
    }

    private val captureSessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession?) {
            Toast.makeText(this@SurfaceViewActivity, "camera config failed",
                           Toast.LENGTH_SHORT).show()
        }

        override fun onConfigured(session: CameraCaptureSession?) {
            if (session != null) {
                cameraCaptureSession = session
                createCameraRequest()
            }
        }

    }

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?,
                                                 width: Int,
                                                 height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return true
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        }

    }
}
