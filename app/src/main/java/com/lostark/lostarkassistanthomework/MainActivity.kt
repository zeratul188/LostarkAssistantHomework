package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.checklist.ChecklistFragment
import com.lostark.lostarkassistanthomework.gold.GoldFragment

var progress : Int = 0

class MainActivity : AppCompatActivity() {
    lateinit var toolBar : Toolbar
    lateinit var layoutFrame : FrameLayout
    lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("로스트아크 어시스턴트 체크")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)

        layoutFrame = findViewById(R.id.layoutFrame)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val checklistFragment : ChecklistFragment = ChecklistFragment()
        val goldFragment : GoldFragment = GoldFragment()

        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_checklist -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
                R.id.action_gold -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, goldFragment).commit()
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
}