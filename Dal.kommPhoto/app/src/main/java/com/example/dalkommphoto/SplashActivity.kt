package com.example.dalkommphoto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.startActivity
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        try {
            Thread.sleep(2000)
            startActivity<LoginActivity>()
            finish()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }
}
