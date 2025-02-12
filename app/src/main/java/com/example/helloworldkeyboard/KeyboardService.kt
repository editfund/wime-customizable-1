package com.example.helloworldkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.text.Editable; 
import android.text.TextWatcher; 

import android.os.Bundle
import android.webkit.*

import java.io.IOException;

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class KeyboardService : InputMethodService() {
    private lateinit var webView: WebView

    override fun onCreateInputView(savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        
        // Enable JavaScript (optional)
        webView.settings.javaScriptEnabled = true

        // Load a URL
        webView.loadUrl("https://www.example.com")

        // Handle link navigation within WebView (optional)
        webView.webViewClient = WebViewClient()


        // Inflate your keyboard layout from the XML file
        val inflater = layoutInflater
        val inputView = inflater.inflate(R.layout.keyboard_layout, null)

        //📌文本框
        val editText = inputView.findViewById<EditText>(R.id.editText)

        // 设置文本
        editText.setText("默认文本")

        // 获取文本
        val text = editText.text.toString()
        println("当前文本: $text")

        // 添加文本改变监听器
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 文本改变后调用
                println("文本已更改: ${s.toString()}")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("文本已更改: ${s.toString()}") //⚠️
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("文本已更改: ${s.toString()}") //⚠️
            }
        })

        // 📌Get references to your buttons
        val button_emoji_1 = inputView.findViewById<Button>(R.id.button_emoji_1)
        button_emoji_1.setOnClickListener { inputText("\uD83D\uDCCC") } //输入📌

        val button_emoji_2 = inputView.findViewById<Button>(R.id.button_emoji_2)
        button_emoji_2.setOnClickListener { inputText("\ud83c\udd98") } //输入🆘

        val button_emoji_3 = inputView.findViewById<Button>(R.id.button_emoji_3)
        button_emoji_3.setOnClickListener { inputText("\u26a0\ufe0f") } //输入⚠️

        val button_wime = inputView.findViewById<Button>(R.id.button_wime)
        button_wime.setOnClickListener { webInputText("") } //输入WIME      


        val buttonHello = inputView.findViewById<Button>(R.id.button_hello)
        val buttonWorld = inputView.findViewById<Button>(R.id.button_world)
        val buttonKeyboard = inputView.findViewById<Button>(R.id.button_keyboard)
        val buttonx = inputView.findViewById<Button>(R.id.button_x)

        // Set click listeners for your buttons
        buttonHello.setOnClickListener { inputText("我爱你") }//字符串输入
        buttonWorld.setOnClickListener { inputText("192.168.1.1") }
        buttonKeyboard.setOnClickListener { inputText("\uD83D\uDCCC") } //输入📌
        buttonx.setOnClickListener { inputText("⅓") }

        return inputView
    }

    private fun inputText(text: String) {
        val inputConnection = currentInputConnection
        inputConnection?.commitText(text, 1)
    }

    private fun webInputText(text: String) {
        val inputConnection = currentInputConnection
        //inputConnection?.commitText(text, 1)

		// 创建OkHttpClient实例
		val client = OkHttpClient()

        // 构建请求对象
        val request = Request.Builder()
            .url("https://jsonplaceholder.typicode.com/posts/1") // 替换为您的URL
            .build()
        
        // 执行请求并获取响应
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 请求失败时的处理逻辑
                e.printStackTrace()
            }
        
            override fun onResponse(call: okhttp3.Call, response: Response) {
                // 请求成功时的处理逻辑
                if (response.isSuccessful) {
                    // 获取响应体并处理
                    val responseBody = response.body?.string()
                    
                    println(responseBody)
                    inputConnection?.commitText(responseBody + text, 1)
                } else {
                    // 处理不成功的响应
                    println("Request failed with code: ${response.code}")
                }
            }
        })
    }
}
