package com.example.helloworldkeyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button

import java.io.IOException;

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class KeyboardService : InputMethodService() {
    override fun onCreateInputView(): View {
        // Inflate your keyboard layout from the XML file
        val inflater = layoutInflater
        val inputView = inflater.inflate(R.layout.keyboard_layout, null)

        // Get references to your buttons
        val button_emoji_1 = inputView.findViewById<Button>(R.id.button_emoji_1)
        button_emoji_1.setOnClickListener { inputText("\uD83D\uDCCC") } //输入📌

        val button_emoji_2 = inputView.findViewById<Button>(R.id.button_emoji_2)
        button_emoji_2.setOnClickListener { inputText("\uD83D\uDCCC") } //输入📌

        val buttonHello = inputView.findViewById<Button>(R.id.button_hello)
        val buttonWorld = inputView.findViewById<Button>(R.id.button_world)
        val buttonKeyboard = inputView.findViewById<Button>(R.id.button_keyboard)
        val buttonx = inputView.findViewById<Button>(R.id.button_x)

        // Set click listeners for your buttons
        buttonHello.setOnClickListener { webInputText("我爱你") }//字符串输入
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
