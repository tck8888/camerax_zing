package com.tck.camerax.zing

import android.content.res.Resources
import android.util.Log

/**
 *
 * description:

 * @date 2020/12/5 20:12

 * @author tck88
 *
 * @version v1.0.0
 *
 */
object YLogger {

    const val TAG = "my_camerax_zxing"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }
}

fun Float.dp2pxFloat(): Float = (Resources.getSystem().displayMetrics.density * this + 0.5f)


fun Float.dp2px(): Int = (Resources.getSystem().displayMetrics.density * this + 0.5f).toInt()