package com.ssafy.finalpass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            // 홈 화면(HomeActivity)으로 이동
            startActivity(Intent(this, MainActivity::class.java))
            finish() // SplashActivity 종료
        }, 2000) // 2초 후 전환
    }
}