package com.tck.camerax.zing

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.tck.camerax.zing.databinding.ActivityCameraBinding
import java.nio.ByteBuffer
import androidx.collection.arrayMapOf

/**
 *
 * description:

 * @date 2020/12/5 16:40

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var barcodeAnalyzer: BarcodeAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.previewView.post {
            initCamera()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val tempPreview = Preview.Builder()
                .build()
            val imageAnalysis = ImageAnalysis.Builder().build()
            barcodeAnalyzer = BarcodeAnalyzer()
            barcodeAnalyzer.setOnBarcodeAnalyzerListener {
                barcodeAnalyzer.controlBarcodeAnalyzer(false)
                intent.putExtra("qr_code", it)
                setResult(RESULT_OK, intent)
                finish()
            }
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), barcodeAnalyzer)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    tempPreview,
                    imageAnalysis
                )
                tempPreview.setSurfaceProvider(binding.previewView.surfaceProvider)
            } catch (e: Exception) {
                YLogger.d("initCamera error:${e.message}")
            }

        }, ContextCompat.getMainExecutor(this))

    }

    //一加
    //
    /*2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: get data from 35
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: get data from 0
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: width:800,height:600
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:1,planeProxy.rowStride:832,planeProxy.buffer.capacity():499168
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: =====================================================================
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: get data from 1
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: width:800,height:600
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:832,planeProxy.buffer.capacity():249567
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: =====================================================================
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: get data from 2
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: width:800,height:600
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:832,planeProxy.buffer.capacity():249567
2020-12-08 09:57:13.295 3350-3350/com.tck.opecv.train D/my_opencv: =====================================================================*/

    //小米
    /*2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: get data from 35
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: get data from 0
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: width:640,height:480
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:1,planeProxy.rowStride:640,planeProxy.buffer.capacity():307200
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: =====================================================================
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: get data from 1
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: width:640,height:480
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:640,planeProxy.buffer.capacity():153599
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: =====================================================================
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: get data from 2
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: width:640,height:480
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:640,planeProxy.buffer.capacity():153599
    2020-12-08 10:09:01.937 32424-32424/com.tck.opecv.train D/my_opencv: =====================================================================*/

    //华为
//    2020-12-08 10:11:10.165 7343-7343/com.tck.opecv.train D/my_opencv: get data from 35
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: get data from 0
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: width:640,height:480
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:1,planeProxy.rowStride:640,planeProxy.buffer.capacity():307200
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: =====================================================================
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: get data from 1
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: width:640,height:480
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:640,planeProxy.buffer.capacity():153599
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: =====================================================================
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: get data from 2
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: width:640,height:480
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: planeProxy.pixelStride:2,planeProxy.rowStride:640,planeProxy.buffer.capacity():153599
//    2020-12-08 10:11:10.166 7343-7343/com.tck.opecv.train D/my_opencv: =====================================================================

}

