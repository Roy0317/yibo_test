package com.yibo.yiboapp.mvvm

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast


fun Activity.isShowingLoading(): Boolean{
    var rootView = findViewById<ViewGroup>(android.R.id.content)
    if(rootView == null){
        rootView = window.decorView.findViewById(android.R.id.content)
    }

    return rootView?.findViewWithTag<RelativeLayout>("progress_relative") != null
}

fun Activity.showLoading(){
    runOnUiThread {
        var rootView = findViewById<ViewGroup>(android.R.id.content)
        if(rootView == null){
            rootView = window.decorView.findViewById(android.R.id.content)
        }
        if(rootView == null) return@runOnUiThread

        var relativeLayout = rootView.findViewWithTag<RelativeLayout>("progress_relative")
        if(relativeLayout == null){
            relativeLayout = RelativeLayout(this)
            relativeLayout.tag = "progress_relative"
            relativeLayout.gravity = Gravity.CENTER
            relativeLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            relativeLayout.setOnClickListener {  }
            rootView.addView(relativeLayout)

            val view = View(this)
            view.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
            view.setBackgroundColor(Color.BLACK)
            view.alpha = 0.5f
            relativeLayout.addView(view)

            val progressBar = ProgressBar(this)
            val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            lp.addRule(RelativeLayout.CENTER_IN_PARENT)
            progressBar.layoutParams = lp
            relativeLayout.addView(progressBar)
        }

        relativeLayout.visibility = View.VISIBLE
    }
}

fun Activity.hideLoading(){
    runOnUiThread {
        var rootView = findViewById<ViewGroup>(android.R.id.content)
        if(rootView == null){
            rootView = window.decorView.findViewById(android.R.id.content)
        }
        if(rootView == null) return@runOnUiThread

        val relativeLayout = rootView.findViewWithTag<RelativeLayout>("progress_relative")
        if(relativeLayout != null){
            relativeLayout.visibility = View.GONE
        }
    }
}

fun Activity.hideSoftInput() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showSoftInput() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }
}


fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(resID: Int){
    Toast.makeText(this, resID, Toast.LENGTH_SHORT).show()
}

fun String?.isOn(): Boolean{
    return this?.equals("on", true) ?: false
}

fun String?.ifNullOrEmpty(defaultValue: () -> String): String =
    if (isNullOrEmpty()) defaultValue() else this

fun View.setNewOnClickedListener(listener: (View) -> Unit) {
    //紀錄最後點擊時間
    var lastClicked: Long = 0
    this.setOnClickListener{ view ->
        //計算連點時間 < 1sec 不callback
        if (SystemClock.elapsedRealtime() - lastClicked < 1000) return@setOnClickListener
        lastClicked = SystemClock.elapsedRealtime()
        listener(view)
    }
}