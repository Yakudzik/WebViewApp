package com.example.wvtestapp

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.webkit.URLUtil
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.marginTop
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var web: WebView
    lateinit var refresh: ImageButton
    lateinit var go2NewSite: ImageButton
    lateinit var adressLine: EditText

    lateinit var myLayout: ConstraintLayout
    var defaultAdress = "https://play-asia.com/"
    var pageKey = "PAGE_KEY"

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        createWebView(savedInstanceState.getString(pageKey).toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        refresh = findViewById<ImageButton>(R.id.refresh_button_ID)
        go2NewSite = findViewById<ImageButton>(R.id.upload_button_ID)
        adressLine = findViewById<EditText>(R.id.link_text_ID)
        adressLine.setScroller(Scroller(baseContext))
//        adressLine.isVerticalScrollBarEnabled = true
//        adressLine.movementMethod = ScrollingMovementMethod()

        myLayout = findViewById<ConstraintLayout>(R.id.mainAct_ID)
        web = findViewById<WebView>(R.id.wv_ID)

        if(savedInstanceState==null)
        createWebView(defaultAdress)

        refresh.apply {
           setOnClickListener {
               var url = web.url
               createWebView(url.toString())
           }
        }
        go2NewSite.apply {
            setOnClickListener {
                var url = adressLine.text
               if ( URLUtil.isValidUrl(url.toString()))
                createWebView(url.toString())
                else
                    createSnack("Wrong URL try again")
            }
        }


    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView(adress: String) {
        web.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                createSnack(web.url.toString())
                adressLine.text = Editable.Factory.getInstance().newEditable(web.url)
                super.onPageFinished(view, url)
            }
        }

        web.apply {
            loadUrl(adress)
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.allowContentAccess = true
            // settings.safeBrowsingEnabled = true
            settings.loadsImagesAutomatically = true
            settings.cacheMode = LOAD_DEFAULT
            // settings.mixedContentMode = 0
            settings.domStorageEnabled = true
        }

    }

    //кнопка назад, возвращает на прошлую страницу  
    override fun onBackPressed() {
        if (web.canGoBack())
            web.goBack()
        else
            super.onBackPressed()
    }

    //делаю снэк
    fun createSnack(string: String) {

        Snackbar.make(myLayout, string, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#00E2FF"))
            .setBackgroundTint(Color.parseColor("#3C3C3C"))
            .setAnchorView(R.id.interface_ID)
            .show()

//        var t = Toast.makeText(baseContext, string, Toast.LENGTH_SHORT)
//        t.setGravity(Gravity.TOP, 0, 0)
//        t.show()
    }

    //сохраняю адрес сайта при повороте
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var link = web.url
        outState.putString(pageKey,link)
        //outState.putBundle(pageKey, webState)
    }
}