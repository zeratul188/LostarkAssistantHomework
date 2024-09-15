package com.lostark.lostarkassistanthomework.checklist.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.add.AddFamilyDialog
import com.lostark.lostarkassistanthomework.checklist.RecyclerViewDecoration
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.databinding.ActivityFamilyEditBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class FamilyEditActivity : AppCompatActivity(), EditFamilyRecyclerAdapter.OnStartDragListener {
    private val viewModel: FamilyEditViewModel by viewModels()
    private lateinit var binding: ActivityFamilyEditBinding
    private var myCompositeDisposable = CompositeDisposable()

    private val days = ArrayList<Family>()
    private val weeks = ArrayList<Family>()
    private val familyDB = FamilyDatabase.getInstance(App.context())!!

    private lateinit var dayAdapter: EditFamilyRecyclerAdapter
    private lateinit var weekAdapter: EditFamilyRecyclerAdapter
    private lateinit var dayHelper: ItemTouchHelper
    private lateinit var weekHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_family_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_family_edit)
        binding.familyViewModel = viewModel

        with(binding) {
            toolBar.setTitle("원정대 체크리스트 편집")
            toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
            //toolBar.setNavigationIcon(R.drawable.icon_resize)
            setSupportActionBar(toolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val list = familyDB.familyDao().getAll()
        list.forEach { item ->
            if (item.type == "일일") {
                days.add(item)
            } else {
                weeks.add(item)
            }
        }
        days.sort()
        weeks.sort()

        binding.listWeeks.addItemDecoration(RecyclerViewDecoration(0, 10))
        binding.listDays.addItemDecoration(RecyclerViewDecoration(0, 10))
        dayAdapter = EditFamilyRecyclerAdapter(days, this, this)
        weekAdapter = EditFamilyRecyclerAdapter(weeks, this, this)

        val dayCallback = EditItemTouchHelperCallback()
        val weekCallback = EditItemTouchHelperCallback()
        dayCallback.setOnItemMoveListener(dayAdapter)
        weekCallback.setOnItemMoveListener(weekAdapter)
        dayHelper = ItemTouchHelper(dayCallback)
        weekHelper = ItemTouchHelper(weekCallback)
        dayHelper.attachToRecyclerView(binding.listDays)
        weekHelper.attachToRecyclerView(binding.listWeeks)

        binding.listDays.adapter = dayAdapter
        binding.listWeeks.adapter = weekAdapter

        binding.btnAddDay.setOnClickListener {
            val addDialog = AddFamilyDialog(this, "일일", myCompositeDisposable)
            addDialog.setOnClickListener(object : AddFamilyDialog.OnDialogClickListener {
                override fun onClicked() {
                    val customToast = CustomToast(this@FamilyEditActivity)
                    val data = addDialog.getItem()
                    if (data.name != "") {
                        var isFind = false
                        days.forEach { day ->
                            if (day.name == data.name) {
                                isFind = true
                            }
                        }
                        if (isFind) {
                            customToast.createToast("이미 같은 이름의 숙제가 존재합니다.", false)
                        } else {
                            days.add(Family(0, data.name, data.icon, data.now, data.max, data.end, 0, "일일"))
                            customToast.createToast("${data.name} 숙제를 추가하였습니다.", false)
                            addDialog.dialogDismiss()
                            dayAdapter.notifyDataSetChanged()
                        }
                    } else {
                        customToast.createToast("이름이 비어있습니다.", false)
                    }
                    customToast.show()
                }
            })
            addDialog.show(supportFragmentManager, "addDialog")
        }

        binding.btnAddWeek.setOnClickListener {
            val addDialog = AddFamilyDialog(this, "주간", myCompositeDisposable)
            addDialog.setOnClickListener(object : AddFamilyDialog.OnDialogClickListener {
                override fun onClicked() {
                    val customToast = CustomToast(this@FamilyEditActivity)
                    val data = addDialog.getItem()
                    if (data.name != "") {
                        var isFind = false
                        days.forEach { day ->
                            if (day.name == data.name) {
                                isFind = true
                            }
                        }
                        if (isFind) {
                            customToast.createToast("이미 같은 이름의 숙제가 존재합니다.", false)
                        } else {
                            weeks.add(Family(0, data.name, data.icon, data.now, data.max, data.end, 0, "주간"))
                            customToast.createToast("${data.name} 숙제를 추가하였습니다.", false)
                            addDialog.dialogDismiss()
                            weekAdapter.notifyDataSetChanged()
                        }
                    } else {
                        customToast.createToast("이름이 비어있습니다.", false)
                    }
                    customToast.show()
                }
            })
            addDialog.show(supportFragmentManager, "addDialog")
        }

        binding.btnApply.setOnClickListener {
            val list = familyDB.familyDao().getAll()
            list.forEach { item ->
                familyDB.familyDao().delete(item)
            }
            for (i in days.indices) {
                days[i].position = i
                familyDB.familyDao().insertAll(days[i])
            }
            for (i in weeks.indices) {
                weeks[i].position = i
                familyDB.familyDao().insertAll(weeks[i])
            }
            val customToast = CustomToast(this)
            customToast.createToast("원정대 체크리스트를 수정하였습니다.", false)
            customToast.show()
            finish()
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

    override fun onStartDrag(holder: EditFamilyRecyclerAdapter.ViewHolder) {
        dayHelper.startDrag(holder)
        weekHelper.startDrag(holder)
    }

    override fun onDestroy() {
        myCompositeDisposable.clear()
        super.onDestroy()
    }
}