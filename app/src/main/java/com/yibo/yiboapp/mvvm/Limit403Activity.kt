package com.yibo.yiboapp.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson
import com.yibo.yiboapp.R
import com.yibo.yiboapp.entify.ServerTimeResponse

class Limit403Activity : BaseActivityKotlin() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limit403)

        val json = intent.getStringExtra("json")
        val response = Gson().fromJson(json, ServerTimeResponse::class.java)
        if(response != null){
            val textMessage = findViewById<TextView>(R.id.textMessage)
            textMessage.text = response.msg.replace("<br/>", "\n")
        }
    }
}