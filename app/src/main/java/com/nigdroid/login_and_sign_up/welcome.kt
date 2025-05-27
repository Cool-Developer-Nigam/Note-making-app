package com.nigdroid.login_and_sign_up

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nigdroid.login_and_sign_up.databinding.ActivityWelcomeBinding

class welcome : AppCompatActivity() {
    private val binding : ActivityWelcomeBinding by lazy{
        ActivityWelcomeBinding.inflate(layoutInflater)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //this code is for splash screeen

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        },3000)


        //this code is to make the text with 2 colors
        val welcomeTxt="Welcome"
val spannableString= SpannableString(welcomeTxt)
        spannableString.setSpan(ForegroundColorSpan(Color.RED),0,5,0)
        spannableString.setSpan(ForegroundColorSpan(Color.GRAY),5,welcomeTxt.length,0)



        binding.welTxt.text=spannableString

    }
}