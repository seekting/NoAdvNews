package com.seekting.noadvnews

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("seekting", "hello")
        var appid = "46647"
        var appkey = "f8226efcfa85460a8f3d703eb08a1689"
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onStart() {
        super.onStart()
    }
}
