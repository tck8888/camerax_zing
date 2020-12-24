package com.tck.camerax.zing

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt

/**
 *
 * description:

 * @date 2020/12/24 21:19

 * @author tck88
 *
 * @version v1.0.0
 *
 */
class SmallBellWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        val defaultText = "99+"
    }

    private val iconPaint: Paint
    private val iconRect = Rect()
    private val iconBitmap: Bitmap

    private val path = Path()
    private val bgPaint: Paint

    private var outerCircleLeft = 0F
    private var outerCircleTop = 0F
    private var defaultIconSize = 0F
    private var outerCircleRadius = 0F
    private var innerCircleRadius = 0F

    private var textPaint: Paint

    private var number = 7

    init {
        defaultIconSize = 29f.dp2pxFloat()
        outerCircleLeft = 17.5f.dp2pxFloat()
        outerCircleTop = 1.5f.dp2pxFloat()
        outerCircleRadius = 7.5f.dp2pxFloat()
        innerCircleRadius = 6.5f.dp2pxFloat()

        iconPaint = Paint().apply {
            isAntiAlias = true
        }
        bgPaint = Paint().apply {
            isAntiAlias = true
            color = "#FFED5824".toColorInt()
            style = Paint.Style.FILL
        }

        textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 10f.dp2pxFloat()
        }

        iconBitmap = BitmapFactory.decodeResource(resources, R.mipmap.xiaoxi)

    }


    fun showNum(num: Int) {
        if (num < 0) {
            this.number = 0
        } else {
            this.number = num
        }

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (number == 0) {
            setMeasuredDimension(defaultIconSize.toInt(), defaultIconSize.toInt())
        } else if (number in 1..99) {
            setMeasuredDimension(
                (outerCircleLeft + outerCircleRadius * 2).toInt(),
                defaultIconSize.toInt()
            )
        } else {
            setMeasuredDimension(
                (outerCircleLeft + outerCircleRadius * 2 + textPaint.measureText(defaultText)).toInt(),
                defaultIconSize.toInt()
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        iconRect.left = 0
        iconRect.top = 0
        iconRect.right = defaultIconSize.toInt()
        iconRect.bottom = defaultIconSize.toInt()

        if (number == 0) {
            canvas?.drawBitmap(iconBitmap, null, iconRect, iconPaint)
        } else {
            val circleX = outerCircleRadius + outerCircleLeft
            val circleY = outerCircleRadius + outerCircleTop

            canvas?.save()

            path.addCircle(
                circleX,
                circleY,
                outerCircleRadius,
                Path.Direction.CCW
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                canvas?.clipOutPath(path)
            } else {
                canvas?.clipPath(path, Region.Op.DIFFERENCE)
            }
            canvas?.drawBitmap(iconBitmap, null, iconRect, iconPaint)
            canvas?.restore()

            val fontMetrics = textPaint.fontMetrics

            if (number < 100) {
                canvas?.drawCircle(
                    circleX,
                    circleY,
                    innerCircleRadius,
                    bgPaint
                )

                canvas?.drawText(
                    number.toString(),
                    circleX,
                    circleY + (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom,
                    textPaint
                )
            } else {
                canvas?.drawRoundRect(
                    circleX - innerCircleRadius,
                    circleY - innerCircleRadius,
                    circleX + innerCircleRadius + textPaint.measureText(defaultText),
                    circleY + innerCircleRadius,
                    innerCircleRadius,
                    innerCircleRadius,
                    bgPaint
                )

                canvas?.drawText(
                    defaultText,
                    circleX + textPaint.measureText(defaultText) / 2f,
                    circleY + (fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.bottom,
                    textPaint
                )
            }
        }


    }
}