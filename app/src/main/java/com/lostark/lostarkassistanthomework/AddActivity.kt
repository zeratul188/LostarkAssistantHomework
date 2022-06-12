package com.lostark.lostarkassistanthomework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                            if (!savedFrameHomeworks.isEmpty()) {
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
                            val homework = Homework(0, item.name, item.level, item.server, item.job, daylist, daynows, daymaxs, dayicons, dayends, weeklist, weeknows, weekmaxs, weekicons, weekends, 0, 0, 0)
                            homeworkDB.homeworkDao().insertAll(homework)
                            println("${item.name} is inserted")
                        }
                    }
                    finish()
                }
                R.id.rdoSelf -> {

                }
            }
        }

    }
}

