package com.yibo.yiboapp.mvvm

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.yibo.yiboapp.manager.ManagerFactory
import com.yibo.yiboapp.network.HttpResult
import com.yibo.yiboapp.network.httpRequest
import com.yibo.yiboapp.utils.Utils
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

abstract class VeryBaseActivity: AppCompatActivity() {

    @JvmField
    protected val TAG = this.javaClass.simpleName

    @JvmField
    protected val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.logAll(TAG, "onCreate()")
        Utils.useTransformBar(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onStart() {
        super.onStart()
        if(this !is Limit403Activity){
            checkBlackList()
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun checkBlackList(){
        Utils.logAll(TAG, "checkBlackList()")
        lifecycleScope.launch {
            val result = httpRequest { ManagerFactory.generalManager.fetchServerTimeAsync() }
            if(result is HttpResult.Success){
                val response = result.data
                if(response.code == 403){
                    val intent = Intent(this@VeryBaseActivity, Limit403Activity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("json", Gson().toJson(response))
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}