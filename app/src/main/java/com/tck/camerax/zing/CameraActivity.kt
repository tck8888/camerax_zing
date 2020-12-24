package com.tck.camerax.zing

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout

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
    private var barcodeAnalyzer: BarcodeAnalyzer? = null
    private var imageAnalysis: ImageAnalysis? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.previewView.post {
            initCamera()
        }

        val layoutParams = binding.barcodeScanWidget.layoutParams as FrameLayout.LayoutParams
        layoutParams.topMargin = resources.displayMetrics.heightPixels * 165 / 667
        layoutParams.width = resources.displayMetrics.widthPixels * 256 / 375
        layoutParams.height = resources.displayMetrics.widthPixels * 256 / 375
        binding.barcodeScanWidget.layoutParams = layoutParams
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

            val tempImageAnalysis = ImageAnalysis.Builder().build()
            imageAnalysis = tempImageAnalysis
            val tempBarcodeAnalyzer = BarcodeAnalyzer()
            barcodeAnalyzer = tempBarcodeAnalyzer
            tempBarcodeAnalyzer.setOnBarcodeAnalyzerListener {
                tempBarcodeAnalyzer.controlBarcodeAnalyzer(false)
                intent.putExtra("qr_code", it)
                setResult(RESULT_OK, intent)
                finish()
            }
            tempImageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), tempBarcodeAnalyzer)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    tempPreview,
                    tempImageAnalysis
                )
                tempPreview.setSurfaceProvider(binding.previewView.surfaceProvider)

                binding.barcodeScanWidget.visibility= View.VISIBLE
                binding.barcodeScanWidget.post{
                    binding.barcodeScanWidget.startScan()
                }


            } catch (e: Exception) {
                YLogger.d("initCamera error:${e.message}")
            }

        }, ContextCompat.getMainExecutor(this))

    }

    override fun onDestroy() {
        super.onDestroy()
        imageAnalysis?.clearAnalyzer()
        barcodeAnalyzer?.controlBarcodeAnalyzer(false)
        barcodeAnalyzer?.setOnBarcodeAnalyzerListener(null)
        barcodeAnalyzer = null
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

