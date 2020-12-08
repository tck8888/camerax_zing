package com.tck.camerax.zing

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.collection.arrayMapOf
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer

/**
 *<p>description:</p>
 *<p>created on: 2020/12/8 12:42</p>
 * @author tck
 *
 */
class BarcodeAnalyzer : ImageAnalysis.Analyzer {

    private var needBarcodeAnalyzer = true
    private var rowData: ByteArray? = null
    private var skipRowData: ByteArray? = null

    /**
     * zxing支持的yuv格式
     */
    private val yuvFormats = arrayListOf(
        ImageFormat.YUV_420_888,
        ImageFormat.YUV_422_888
    )

    private val multiFormatReader = MultiFormatReader().apply {
        val barcodeFormatList = arrayListOf(BarcodeFormat.QR_CODE)
        val hints = arrayMapOf(DecodeHintType.POSSIBLE_FORMATS to barcodeFormatList)
        setHints(hints)
    }

    private var onBarcodeAnalyzerListener: ((code: String) -> Unit)? = null

    fun setOnBarcodeAnalyzerListener(onBarcodeAnalyzerListener: ((code: String) -> Unit)?) {
        this.onBarcodeAnalyzerListener = onBarcodeAnalyzerListener
    }

    fun controlBarcodeAnalyzer(needBarcodeAnalyzer: Boolean) {
        this.needBarcodeAnalyzer = needBarcodeAnalyzer
    }

    override fun analyze(image: ImageProxy) {
        YLogger.d("get data from ${image.format}")

        if (!needBarcodeAnalyzer) {
            YLogger.d("BarcodeAnalyzer needBarcodeAnalyzer state:$needBarcodeAnalyzer")
            image.close()
            return
        }

        if (image.format !in yuvFormats) {
            image.close()
            return
        }

        val planes = image.planes

        if (planes.isEmpty()) {
            YLogger.d("BarcodeAnalyzer ImageProxy.PlaneProxy[] length = 0")
            image.close()
            return
        }

        val width = image.width
        val height = image.height

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
                val tempData = ByteBuffer.allocate(width * height)
                val row = if (rowData == null) {
                    rowData = ByteArray(width)
                    rowData
                } else {
                    rowData
                }
                val skipRow = if (skipRowData == null) {
                    skipRowData = ByteArray(yRowStride - width)
                    skipRowData
                } else {
                    skipRowData
                }
                for (i in 0 until height) {
                    yBuffer.get(row!!)
                    tempData.put(row)
                    if (i < height-1) {
                        yBuffer.get(skipRow!!)
                    }
                }
                tempData.toByteArray()
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
                    onBarcodeAnalyzerListener?.invoke(qrCode)
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

fun ByteBuffer.toByteArray(): ByteArray {
    rewind()
    val data = ByteArray(remaining())
    get(data)
    return data
}