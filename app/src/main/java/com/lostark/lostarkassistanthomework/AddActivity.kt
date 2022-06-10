package com.lostark.lostarkassistanthomework

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.jsoup.Jsoup

class AddActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val thread = CloringThread(edtName, this)
            thread.start()
        }

    }
}

class CloringThread(private val edtName: EditText, private val context: Context) : Thread() {
    override fun run() {
        var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+edtName.text.toString()).get()
        var level = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2):nth-child(2)")
        println("Level : ${level.text()}")
        var result = level.text()
        result = result.replace("Lv.", "")
        result = result.replace(",", "")
        println("Result : ${result}")

        var elements = doc.select("#expand-character-list > ul")
        for (element in elements) {
            val result = element.select("span > button > span")
            println("result : ${result.text()}")
            println(element.text())
            val lists = result.text().split(" ")
            var cnt = 1
            for (chracter in lists) {
                println("${cnt}번째 캐릭터 이름 : ${chracter}")
                cnt++
            }
        }
    }
}