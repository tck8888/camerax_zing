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
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), myAnalyzer);

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

    private val yuvFormats = arrayListOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888,
        ImageFormat.YUV_444_888
    )

    private val multiFormatReader = MultiFormatReader().apply {
        val barcodeFormatList = arrayListOf(BarcodeFormat.QR_CODE)
        val hints = arrayMapOf(DecodeHintType.POSSIBLE_FORMATS to barcodeFormatList)
        setHints(hints)
    }

    private var myAnalyzer = ImageAnalysis.Analyzer { image ->

        YLogger.d("get data from ${image.format}")

        if (image.format !in yuvFormats) {
            image.close()
            return@Analyzer
        }

        val width = image.width
        val height = image.height
        val planes = image.planes

        if(planes.isEmpty()){
            YLogger.d("BarcodeAnalyzer ImageProxy.PlaneProxy[] length = 0")
            image.close()
        }

        planes.forEachIndexed { index, planeProxy ->
            YLogger.d("get data from: $index")
            YLogger.d("image.imageInfo.rotationDegrees: ${image.imageInfo.rotationDegrees}")
            YLogger.d("width: ${width},height: ${height}")
            YLogger.d("planeProxy.pixelStride: ${planeProxy.pixelStride},planeProxy.rowStride: ${planeProxy.rowStride},planeProxy.buffer.capacity(): ${planeProxy.buffer.capacity()}")
            YLogger.d("=====================================================================")
        }
        //width:800,height:600
        //planeProxy.pixelStride:2,planeProxy.rowStride:832,planeProxy.buffer.capacity():249567
        //y数据
        try {
            val yRowStride = planes[0].rowStride
            val yBuffer = planes[0].buffer
            val data = if (yRowStride > width) {
                val data = ByteBuffer.allocate(width * height)
                val row = ByteArray(width)
                val skipRow = ByteArray(yRowStride - width)
                for (i in 0..height) {
                    yBuffer.get(row)
                    data.put(row)
                    if (i < height - 1) {
                        yBuffer.get(skipRow)
                    }
                }
                data.toByteArray()
            } else {
                yBuffer.toByteArray()
            }

            val source = PlanarYUVLuminanceSource(
                data,
                image.width,
                image.height,
                0,
                0,
                image.width,
                image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            val result = multiFormatReader.decode(binaryBitmap)
            if (result != null) {
                val qrCode = result.toString()
                if (qrCode.isNotEmpty()) {
                    YLogger.d("BarcodeAnalyzer,qrCode is $qrCode")
                } else {
                    YLogger.d("BarcodeAnalyzer,qrCode isEmpty")
                }
            } else {
                YLogger.d("BarcodeAnalyzer,result = null")
            }
        } catch (e: Exception) {
            YLogger.d("ImageAnalysis.Analyzer error:${e.message}")
        }

        image.close()
    }
}

private fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}