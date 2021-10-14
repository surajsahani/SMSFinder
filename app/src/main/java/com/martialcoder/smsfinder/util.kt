package com.martialcoder.smsfinder

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

fun Context.showToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}

fun Context.showLongToast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}


fun UI(view: View, context: Context) {
    if (view !is EditText) {
        view.setOnTouchListener { view, motionEvent ->
            hide(context)
            false
        }
    }

    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            UI(innerView, context)
        }
    }
}

fun hide(context: Context) {
    try {
        (context as AppCompatActivity).window.setSoftInputMode((WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN))
        if (context.currentFocus != null && context.currentFocus!!.windowToken != null) {
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                context.currentFocus!!.windowToken,
                0
            )
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}