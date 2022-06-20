package com.lostark.lostarkassistanthomework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.checklist.ChecklistFragment
import com.lostark.lostarkassistanthomework.gold.GoldFragment

class MainActivity : AppCompatActivity() {
    lateinit var toolBar : Toolbar
    lateinit var layoutFrame : FrameLayout
    lateinit var bottomNavigationView : BottomNavigationView

    private val checklistFragment = ChecklistFragment()
    private val goldFragment = GoldFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("LAA 체크리스트")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)

        layoutFrame = findViewById(R.id.layoutFrame)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_checklist -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, checklistFragment).commit()
                R.id.action_gold -> supportFragmentManager.beginTransaction().replace(R.id.layoutFrame, goldFragment).commit()
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onResume() {
        super.onResume()
        checklistFragment.resume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
            R.id.action_setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.action_refresh -> {
                checklistFragment.syncData()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}