package com.tck.camerax.zing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt

/**
 *<p>description:</p>
 *<p>created on: 2020/12/24 13:23</p>
 * @author tck
 * @version v3.7.6
 *
 */
class BadgeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var iconPaint: Paint
    private val scanIconRect = Rect()
    private val scanIcon: Bitmap

    private var iconWidth = 0
    private var iconHeight = 0
    private var radius = 0

    private var textPaint: Paint
    private var bgPaint: Paint
    private var path = Path()
    private var path1 = Path()


    init {
        iconPaint = Paint().apply {
            isAntiAlias = true
        }

        scanIcon = BitmapFactory.decodeResource(resources, R.mipmap.xiaoxi)
        iconWidth = scanIcon.width
        iconHeight = scanIcon.height
        radius = 17f.dp2px()

        textPaint = Paint().apply {
            color = "#ffffffff".toColorInt()
            textSize = 10f.dp2pxFloat()
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        bgPaint = Paint().apply {
            color = "#ffed5824".toColorInt()
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (number == 0) {
            setMeasuredDimension(iconWidth, iconHeight)
        } else if (number in 1..98) {
            setMeasuredDimension(iconWidth + radius / 2, iconHeight)
        }

    }

    private var number = 99

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.parseColor("#ff6481F5"))

        scanIconRect.left = 0
        scanIconRect.top = 0
        scanIconRect.right = iconWidth
        scanIconRect.bottom = iconHeight
        canvas?.drawBitmap(
            scanIcon,
            null,
            scanIconRect,
            iconPaint
        )
        canvas?.save()
        val big = 2f.dp2px()

        path.addOval(
            iconWidth - radius +0f,
            0f,
            iconWidth + radius+0f,
            radius.toFloat(),
            Path.Direction.CCW
        )

        canvas?.clipPath(path)

        path1.addOval(
            iconWidth - radius/2f +0f,
            0f,
            iconWidth - radius/2f +0f,
            radius.toFloat(),
            Path.Direction.CCW
        )

        canvas?.clipPath(path,Region.Op.DIFFERENCE)


        canvas?.drawCircle(iconWidth.toFloat(), radius / 2f, radius / 2f, bgPaint)
        canvas?.restore()

      //  canvas?.drawCircle(iconWidth.toFloat(), radius / 2f, radius / 2f, bgPaint)
//
//        val fontMetrics = textPaint.fontMetrics
//
//        canvas?.drawText(
//            number.toString(),
//            iconWidth.toFloat(),
//            radius / 2f - (fontMetrics.bottom + fontMetrics.top) / 2,
//            textPaint
//        )

    }
}

