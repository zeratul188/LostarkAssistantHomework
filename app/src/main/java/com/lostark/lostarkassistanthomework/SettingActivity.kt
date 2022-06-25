package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class SettingActivity : AppCompatActivity() {
    lateinit var btnDelete: Button
    lateinit var btnFamilyInit: Button
    lateinit var btnReset: Button

    lateinit var toolBar: Toolbar

    lateinit var homeworkDB: HomeworkDatabase
    lateinit var familyDB: FamilyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("설정")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        homeworkDB = HomeworkDatabase.getInstance(App.context())!!
        familyDB = FamilyDatabase.getInstance(App.context())!!

        btnDelete = findViewById(R.id.btnDelete)
        btnFamilyInit = findViewById(R.id.btnFamilyInit)
        btnReset = findViewById(R.id.btnReset)

        btnFamilyInit.setOnClickListener {
            val checkDialog = CheckDialog(this)
            checkDialog.setData("원정대 체크리스트를 초기값으로 설정하시겠습니까?", "초기화", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {

                    val customToast = CustomToast(App.context())
                    customToast.createToast("원정대 체크리스트를 초기값으로 설정되었습니다.", false)
                    customToast.show()
                }
            })
            checkDialog.show(true)
        }

        btnReset.setOnClickListener {
            val resetDialog = ResetDialog(this)
            resetDialog.setData()
            resetDialog.setOnClickListener(object : ResetDialog.OnDialogClickListener {
                override fun onCLicked() {
                    reset(resetDialog.chkWeek.isChecked)
                    val customToast = CustomToast(App.context())
                    customToast.createToast("체크리스트가 초기화되었습니다.", false)
                    customToast.show()
                }
            })
            resetDialog.show(true)
        }

        btnDelete.setOnClickListener {
            val checkDialog = CheckDialog(this)
            checkDialog.setData("모든 캐릭터 데이터가 삭제됩니다. 정말로 삭제하시겠습니까?", "삭제", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    var list = homeworkDB.homeworkDao().getAll()
                    list.forEach { item ->
                        homeworkDB.homeworkDao().delete(item)
                    }
                    val customToast = CustomToast(App.context())
                    customToast.createToast("모든 캐릭터 데이터가 삭제되었습니다.", false)
                    customToast.show()
                }
            })
            checkDialog.show(true)
        }
    }

    fun reset(isWeeked: Boolean) {
        var list = homeworkDB.homeworkDao().getAll()
        list.forEach { item ->
            val days = item.daylist.split(",")
            val daymaxs = item.daymaxs.split(",")
            val daynows = item.daynows.split(",")
            var daynowstr = ""
            for (i in days.indices) {
                when (days[i]) {
                    "카오스 던전" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.dungeonrest += diff
                        if (item.dungeonrest > 100) {
                            item.dungeonrest = 100
                        }
                    }
                    "가디언 토벌" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.bossrest += diff
                        if (item.bossrest > 100) {
                            item.bossrest = 100
                        }
                    }
                    "에포나 의뢰" -> {
                        var diff = daymaxs[i].toInt() - daynows[i].toInt()
                        if (diff < 0) {
                            diff = 0
                        }
                        diff *= 10
                        item.questrest += diff
                        if (item.questrest > 100) {
                            item.questrest = 100
                        }
                    }
                }
                if (daynowstr != "") daynowstr += ","
                daynowstr += "0"
            }
            if (isWeeked) {
                val weeklength = item.weeknows.split(",").size
                var weeknowstr = ""
                for (i in 0 until weeklength) {
                    if (weeknowstr != "") weeknowstr += ","
                    weeknowstr += "0"
                }
                item.weeknows = weeknowstr
            }
            item.daynows = daynowstr
            item.dungeonlost = 0
            item.bosslost = 0
            item.questlost = 0
            homeworkDB.homeworkDao().update(item)
        }

        val familys = familyDB.familyDao().getAll()
        familys.forEach { family ->
            if (family.type == "주간") {
                if (isWeeked) {
                    family.now = 0
                    familyDB.familyDao().update(family)
                }
            } else {
                family.now = 0
                familyDB.familyDao().update(family)
            }
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