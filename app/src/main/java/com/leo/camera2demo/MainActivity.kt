package com.leo.camera2demo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraList: Array<String>? = null
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private lateinit var cameraDevice: CameraDevice
    private lateinit var surfaceTexture: SurfaceTexture
    private lateinit var cameraCaptureSession: CameraCaptureSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidget()
    }

    private fun initWidget() {
        surfaceTexture = tv_camera.surfaceTexture
        initCamera()
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
        val streamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val outputSize = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
        val fpsRanges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES)
        //open camera
        cameraManager.openCamera(cameraId.toString(), stateCallBack, backgroundHandler)

    }

    private lateinit var builder: CaptureRequest.Builder
    private lateinit var request: CaptureRequest


    private fun initCameraRequest() {
        try {
            builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val surfaces = ArrayList<Surface>()
            surfaceTexture.setDefaultBufferSize(800, 600)
            val previewSurface = Surface(surfaceTexture)
            surfaces.add(previewSurface)
            builder.addTarget(previewSurface)
            cameraDevice.createCaptureSession(surfaces, stateCallback, Handler())
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
            camera?.apply {
                cameraDevice = this
                initCameraRequest()
            }
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
            session?.apply {
                cameraCaptureSession = session
                createCameraRequest()
            }
        }

    }

    private val listener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
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
