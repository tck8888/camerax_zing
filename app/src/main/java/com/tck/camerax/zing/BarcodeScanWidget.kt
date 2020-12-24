package com.tck.my.opengl.base

import android.animation.ValueAnimator
import android.animation.ValueAnimator.RESTART
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import com.tck.camerax.zing.R
import com.tck.camerax.zing.dp2pxFloat

/**
 *
 * description:

 * @date 2020/12/23 20:53

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class BarcodeScanWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var paint: Paint
    private var paint2: Paint
    private var iconPaint: Paint
    private var radius = 0F
    private var cornerWidth = 0F
    private var cornerLength = 0F
    private var borderWidth = 0F

    private val scanIcon: Bitmap
    private val scanIconRect = Rect()

    init {
        radius = 4f.dp2pxFloat()
        cornerWidth = 3f.dp2pxFloat()
        cornerLength = 21.5f.dp2pxFloat()
        borderWidth = 1.5f.dp2pxFloat()

        paint = Paint().apply {
            color = "#FF07C160".toColorInt()
            strokeWidth = cornerWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        paint2 = Paint().apply {
            color = "#6607C160".toColorInt()
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        iconPaint = Paint().apply {
            isAntiAlias = true
        }

        scanIcon = BitmapFactory.decodeResource(resources, R.mipmap.saomiao)


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawBg(canvas)


        scanIconRect.left = borderWidth.toInt()
        scanIconRect.top = scanDistance
        scanIconRect.right = width - borderWidth.toInt()
        scanIconRect.bottom =
            scanDistance + ((scanIcon.height.toFloat() / scanIcon.width.toFloat()) * width).toInt()

        canvas?.drawBitmap(scanIcon, null, scanIconRect, iconPaint)

    }

    private fun drawBg(canvas: Canvas?) {
        canvas?.save()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas?.clipOutRect(
                cornerLength,
                0f,
                width - cornerLength,
                height.toFloat()
            )
            canvas?.clipOutRect(
                0f,
                cornerLength,
                width.toFloat(),
                height.toFloat() - cornerLength
            )

        } else {
            canvas?.clipRect(
                cornerLength,
                0f,
                width - cornerLength,
                height.toFloat(),
                Region.Op.DIFFERENCE
            )
            canvas?.clipRect(
                0f,
                cornerLength,
                width.toFloat(),
                height.toFloat() - cornerLength,
                Region.Op.DIFFERENCE
            )
        }


        val center = cornerWidth / 2f

        canvas?.drawRoundRect(
            center,
            center,
            width - center,
            height - center,
            radius,
            radius,
            paint
        )
        canvas?.restore()

        canvas?.drawLine(cornerLength, center, width - cornerLength, center, paint2)
        canvas?.drawLine(
            cornerLength,
            height - center,
            width - cornerLength,
            height - center,
            paint2
        )
        canvas?.drawLine(center, cornerLength, center, height - cornerLength, paint2)
        canvas?.drawLine(
            width - center,
            cornerLength,
            width - center,
            height - cornerLength,
            paint2
        )
    }

    private var scanDistance = 0

    private var started: Boolean = false

    private var valueAnimator: ValueAnimator? = null

    fun startScan() {
        if (started) {
            return
        }

        val start = 0
        val end = height - ((scanIcon.height.toFloat() / scanIcon.width.toFloat()) * width).toInt()
        val startAlpha = end - cornerLength

        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(start, end)
        }

        valueAnimator?.let {
            it.addUpdateListener { animator ->
                scanDistance = animator.animatedValue as Int

                if (scanDistance >= startAlpha) {
                    var alpha = (((end - scanDistance) / cornerLength) * 255).toInt()
                    alpha = when {
                        alpha < 0 -> {
                            0
                        }
                        alpha > 255 -> {
                            255
                        }
                        else -> {
                            alpha
                        }
                    }
                    iconPaint.alpha = alpha
                } else {
                    iconPaint.alpha = 255
                }

                invalidate()
            }
            it.repeatMode = RESTART
            it.repeatCount = -1
            it.duration = 2000L
            it.start()
            started = true
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        started = false
        valueAnimator?.cancel()
        valueAnimator = null
    }
}

