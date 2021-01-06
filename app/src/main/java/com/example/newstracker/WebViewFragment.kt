package com.example.newstracker

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import com.example.newstracker.Constants.Companion.URL_LINK_EXTRA
import com.example.newstracker.databinding.FragmentWebViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class WebViewFragment : FragmentLifecycleLogging() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        val urlLink = arguments?.getString(URL_LINK_EXTRA)
        displayWebsiteContent(urlLink)

        return binding.root
    }

    private val TAG = "WebViewFragment"

    @SuppressLint("SetJavaScriptEnabled")
    private fun displayWebsiteContent(urlLink: String?) {
        binding.webView.webViewClient = WebViewClient()
        CoroutineScope(Main).launch {
            binding.webView.apply {
                urlLink?.let {
                    loadUrl(it)
                } ?: Toast.makeText(
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
        Log.i(TAG, "displayWebsiteContent: ${binding.webView.progress}")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
        _binding = null
    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return false
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            binding.webViewProgressBar.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
        }
    }
}


