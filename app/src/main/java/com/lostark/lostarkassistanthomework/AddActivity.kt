package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.dbs.HomeworkDBAdapter
import com.lostark.lostarkassistanthomework.objects.Chracter

class AddActivity : AppCompatActivity() {
    lateinit var edtName: EditText
    lateinit var btnAdd: Button
    lateinit var toolBar: Toolbar
    lateinit var rgAuto: RadioGroup
    lateinit var layoutFrame: FrameLayout

    var chracters = ArrayList<Chracter>()
    val homeworkDBAdapter: HomeworkDBAdapter = HomeworkDBAdapter(App.context())
    lateinit var homeworkDB: HomeworkDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("캐릭터 추가")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        edtName = findViewById(R.id.edtName)
        btnAdd = findViewById(R.id.btnAdd)
        rgAuto = findViewById(R.id.rgAuto)
        layoutFrame = findViewById(R.id.layoutFrame)

        homeworkDB = HomeworkDatabase.getInstance(this)!!

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
            when (rgAuto.checkedRadioButtonId) {
                R.id.rdoAuto -> {
                    chracters = autoFragment.chracters
                    chracters.forEach { item ->
                        if (item.isChecked) {
                            var daylist = ""
                            var daynows = ""
                            var daymaxs = ""
                            var dayicons = ""
                            var dayends = ""
                            var weeklist = ""
                            var weeknows = ""
                            var weekmaxs = ""
                            var weekicons = ""
                            var weekends = ""
                            homeworkDBAdapter.open()
                            val savedFrameHomeworks = homeworkDBAdapter.getItems()
                            if (savedFrameHomeworks.isNotEmpty()) {
                                savedFrameHomeworks.forEach { frameHomework ->
                                    if (frameHomework.type == "일일") {
                                        if (item.level >= frameHomework.min) {
                                            if (daylist != "") daylist += ","
                                            if (daynows != "") daynows += ","
                                            if (daymaxs != "") daymaxs += ","
                                            if (dayicons != "") dayicons += ","
                                            if (dayends != "") dayends += ","
                                            daylist += frameHomework.name
                                            daynows += "0"
                                            daymaxs += frameHomework.max
                                            dayicons += frameHomework.icon
                                            dayends += frameHomework.end
                                        }
                                    } else {
                                        if (item.level >= frameHomework.min) {
                                            if (weeklist != "") weeklist += ","
                                            if (weeknows != "") weeknows += ","
                                            if (weekmaxs != "") weekmaxs += ","
                                            if (weekicons != "") weekicons += ","
                                            if (weekends != "") weekends += ","
                                            weeklist += frameHomework.name
                                            weeknows += "0"
                                            weekmaxs += frameHomework.max
                                            weekicons += frameHomework.icon
                                            weekends += frameHomework.end
                                        }
                                    }
                                }
                            }
                            homeworkDBAdapter.close()
                            val homework = Homework(0, item.name, item.level, item.server, item.job, daylist, daynows, daymaxs, dayicons, dayends, weeklist, weeknows, weekmaxs, weekicons, weekends, 0, 0, 0, 0, 0, 0, true, 9999)
                            homeworkDB.homeworkDao().insertAll(homework)
                        }
                    }
                    finish()
                }
                R.id.rdoSelf -> {
                    if (edtName.text.toString() == "") {
                        val toast = CustomToast(App.context())
                        toast.createToast("캐릭터 이름을 입력해주세요.", false)
                        toast.show()
                        return@setOnClickListener
                    }
                    if (isOverlap(edtName.text.toString())) {
                        val toast = CustomToast(App.context())
                        toast.createToast("이미 ${edtName.text.toString()}의 캐릭터가 추가되어 있습니다.", false)
                        toast.show()
                        return@setOnClickListener
                    }
                    val preset = selfFragment.getPreset()
                    var daylist = ""
                    var daynows = ""
                    var daymaxs = ""
                    var dayicons = ""
                    var dayends = ""
                    var weeklist = ""
                    var weeknows = ""
                    var weekmaxs = ""
                    var weekicons = ""
                    var weekends = ""
                    homeworkDBAdapter.open()
                    val savedFrameHomeworks = homeworkDBAdapter.getItems()
                    if (!savedFrameHomeworks.isEmpty()) {
                        savedFrameHomeworks.forEach { frameHomework ->
                            if (frameHomework.type == "일일") {
                                if (preset.level >= frameHomework.min) {
                                    if (daylist != "") daylist += ","
                                    if (daynows != "") daynows += ","
                                    if (daymaxs != "") daymaxs += ","
                                    if (dayicons != "") dayicons += ","
                                    if (dayends != "") dayends += ","
                                    daylist += frameHomework.name
                                    daynows += "0"
                                    daymaxs += frameHomework.max
                                    dayicons += frameHomework.icon
                                    dayends += frameHomework.end
                                }
                            } else {
                                if (preset.level >= frameHomework.min) {
                                    if (weeklist != "") weeklist += ","
                                    if (weeknows != "") weeknows += ","
                                    if (weekmaxs != "") weekmaxs += ","
                                    if (weekicons != "") weekicons += ","
                                    if (weekends != "") weekends += ","
                                    weeklist += frameHomework.name
                                    weeknows += "0"
                                    weekmaxs += frameHomework.max
                                    weekicons += frameHomework.icon
                                    weekends += frameHomework.end
                                }
                            }
                        }
                    }
                    homeworkDBAdapter.close()
                    val homework = Homework(0, edtName.text.toString(), preset.level, preset.server, preset.job, daylist, daynows, daymaxs, dayicons, dayends, weeklist, weeknows, weekmaxs, weekicons, weekends, 0, 0, 0, 0, 0, 0, false, 9999)
                    homeworkDB.homeworkDao().insertAll(homework)
                    finish()
                }
            }
        }

    }

    fun isOverlap(name: String): Boolean {
        var isOverlap = false
        val datas = homeworkDB.homeworkDao().getAll()
        datas.forEach { data ->
            if (data.name == name) {
                isOverlap = true
            }
        }
        return isOverlap
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

