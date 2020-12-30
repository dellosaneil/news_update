package com.example.newstracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newstracker.Constants.Companion.URL_LINK_EXTRA
import com.example.newstracker.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val urlLink = intent.getStringExtra(URL_LINK_EXTRA)
        binding.webView.apply {
            urlLink?.let { loadUrl(it) } ?: Toast.makeText(
                context,
                resources.getString(R.string.web_view_error),
                Toast.LENGTH_LONG
            ).show()
            settings.javaScriptEnabled = true
            settings.displayZoomControls = true
            settings.loadsImagesAutomatically = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                settings.safeBrowsingEnabled = true
            }
        }
    }

}


