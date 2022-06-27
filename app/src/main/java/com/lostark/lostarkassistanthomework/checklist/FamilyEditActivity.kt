package com.lostark.lostarkassistanthomework.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase

class FamilyEditActivity : AppCompatActivity(), EditFamilyRecyclerAdapter.OnStartDragListener {
    lateinit var listDays: RecyclerView
    lateinit var listWeeks: RecyclerView
    lateinit var btnApply: Button

    lateinit var toolBar: Toolbar

    private val days = ArrayList<Family>()
    private val weeks = ArrayList<Family>()
    private val familyDB = FamilyDatabase.getInstance(App.context())!!

    private lateinit var dayAdapter: EditFamilyRecyclerAdapter
    private lateinit var weekAdapter: EditFamilyRecyclerAdapter
    private lateinit var dayHelper: ItemTouchHelper
    private lateinit var weekHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_family_edit)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("원정대 체크리스트 편집")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listDays = findViewById(R.id.listDays)
        listWeeks = findViewById(R.id.listWeeks)
        btnApply = findViewById(R.id.btnApply)

        val list = familyDB.familyDao().getAll()
        list.forEach { item ->
            if (item.type == "일일") {
                days.add(item)
            } else {
                weeks.add(item)
            }
        }

        listWeeks.addItemDecoration(RecyclerViewDecoration(0, 10))
        listDays.addItemDecoration(RecyclerViewDecoration(0, 10))
        dayAdapter = EditFamilyRecyclerAdapter(days, this, this)
        weekAdapter = EditFamilyRecyclerAdapter(weeks, this, this)

        val dayCallback = EditItemTouchHelperCallback()
        val weekCallback = EditItemTouchHelperCallback()
        dayCallback.setOnItemMoveListener(dayAdapter)
        weekCallback.setOnItemMoveListener(weekAdapter)
        dayHelper = ItemTouchHelper(dayCallback)
        weekHelper = ItemTouchHelper(weekCallback)
        dayHelper.attachToRecyclerView(listDays)
        weekHelper.attachToRecyclerView(listWeeks)

        listDays.adapter = dayAdapter
        listWeeks.adapter = weekAdapter

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

    override fun onStartDrag(holder: EditFamilyRecyclerAdapter.ViewHolder) {
        dayHelper.startDrag(holder)
        weekHelper.startDrag(holder)
    }
}