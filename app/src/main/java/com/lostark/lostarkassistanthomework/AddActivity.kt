package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar

class AddActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var btnAdd: Button
    lateinit var toolBar: Toolbar
    lateinit var rgAuto: RadioGroup
    lateinit var layoutFrame: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("캐릭터 추가")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)

        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        rgAuto = findViewById(R.id.rgAuto)
        layoutFrame = findViewById(R.id.layoutFrame)

        val autoFragment = AutoFragment()
        autoFragment.setEditName(edtName)
        val selfFragment = SelfFragment()

        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, autoFragment).commit()
        rgAuto.setOnCheckedChangeListener { radioGroup, id ->
            when (id) {
                R.id.rdoAuto -> {
                    supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, autoFragment).commit()
                }
                R.id.rdoSelf -> {
                    supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment).commit()
                }
            }
        }

        btnAdd.setOnClickListener {

        }

    }
}

