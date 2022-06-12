package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class SettingActivity : AppCompatActivity() {
    lateinit var btnDelete: Button
    lateinit var toolBar: Toolbar

    lateinit var homeworkDB: HomeworkDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("설정")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)

        homeworkDB = HomeworkDatabase.getInstance(App.context())!!

        btnDelete = findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            var list = homeworkDB.homeworkDao().getAll()
            list.forEach { item ->
                homeworkDB.homeworkDao().delete(item)
            }
            Toast.makeText(App.context(), "item is deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}