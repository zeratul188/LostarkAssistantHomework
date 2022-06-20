package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.button.MaterialButton
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.EditData

class EditPagerAdapter(private val homework: Homework, private val context: Context, private val fm: FragmentManager) : PagerAdapter() {
    //var homeworkDB: HomeworkDatabase = HomeworkDatabase.getInstance(App.context())!!
    lateinit var dayAdapter: EditRecyclerAdapter
    lateinit var weekAdapter: EditRecyclerAdapter

    private val days = ArrayList<EditData>()
    private val weeks = ArrayList<EditData>()

    var dungeon = 0
    var boss = 0
    var quest = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = null

        if (position != 2) {
            view = LayoutInflater.from(container.context).inflate(R.layout.fragment_edit_homework, container, false)

            val listView = view.findViewById<RecyclerView>(R.id.listView)
            listView.addItemDecoration(RecyclerViewDecoration(0, 10))
            val btnAdd = view.findViewById<MaterialButton>(R.id.btnAdd)
            btnAdd.setOnClickListener {
                var type = ""
                when (position) {
                    0 -> type = "일일"
                    1 -> type = "주간"
                }
                val addDialog = AddDialog(context, type)
                addDialog.setOnClickListener(object : AddDialog.OnDialogClickListener {
                    override fun onClicked() {
                        val customToast = CustomToast(context)
                        val data = addDialog.getItem()
                        if (data.name != "") {
                            var isFind = false
                            when (position) {
                                0 -> {
                                    days.forEach { day ->
                                        if (day.name == data.name) {
                                            isFind = true
                                        }
                                    }
                                    if (isFind) {
                                        customToast.createToast("이미 같은 이름의 숙제가 존재합니다.", false)
                                    } else {
                                        days.add(data)
                                        customToast.createToast("${data.name} 숙제를 추가하였습니다.", false)
                                        addDialog.dialogDismiss()
                                    }
                                }
                                1 -> {
                                    weeks.forEach { week ->
                                        if (week.name == data.name) {
                                            isFind = true
                                        }
                                    }
                                    if (isFind) {
                                        customToast.createToast("이미 같은 이름의 숙제가 존재합니다.", false)
                                    } else {
                                        weeks.add(data)
                                        customToast.createToast("${data.name} 숙제를 추가하였습니다.", false)
                                        addDialog.dialogDismiss()
                                    }
                                }
                            }
                            dayAdapter.notifyDataSetChanged()
                            weekAdapter.notifyDataSetChanged()
                        } else {
                            customToast.createToast("이름이 비어있습니다.", false)
                        }
                        customToast.show()
                    }
                })
                addDialog.show(fm, "addDialog")
            }
            
            when (position) {
                0 -> {
                    val names = homework.daylist.split(",")
                    val nows = homework.daynows.split(",")
                    val maxs = homework.daymaxs.split(",")
                    val icons = homework.dayicons.split(",")
                    val ends = homework.dayends.split(",")
                    for (i in names.indices) {
                        days.add(EditData(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
                    }
                    dayAdapter = EditRecyclerAdapter(days, context, homework)
                    listView.adapter = dayAdapter
                }
                1 -> {
                    val names = homework.weeklist.split(",")
                    val nows = homework.weeknows.split(",")
                    val maxs = homework.weekmaxs.split(",")
                    val icons = homework.weekicons.split(",")
                    val ends = homework.weekends.split(",")
                    for (i in names.indices) {
                        weeks.add(EditData(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
                    }
                    weekAdapter = EditRecyclerAdapter(weeks, context, homework)
                    listView.adapter = weekAdapter
                }
            }
        } else {
            view = LayoutInflater.from(container.context).inflate(R.layout.fragment_edit_rest, container, false)

            val txtDungeon = view.findViewById<TextView>(R.id.txtDungeon)
            val seekDungeon = view.findViewById<SeekBar>(R.id.seekDungeon)
            val txtBoss = view.findViewById<TextView>(R.id.txtBoss)
            val seekBoss = view.findViewById<SeekBar>(R.id.seekBoss)
            val txtQuest = view.findViewById<TextView>(R.id.txtQuest)
            val seekQuest = view.findViewById<SeekBar>(R.id.seekQuest)

            txtDungeon.text = homework.dungeonrest.toString()
            txtBoss.text = homework.bossrest.toString()
            txtQuest.text = homework.questrest.toString()
            seekDungeon.progress = homework.dungeonrest/10
            seekBoss.progress = homework.bossrest/10
            seekQuest.progress = homework.questrest/10

            dungeon = homework.dungeonrest
            boss = homework.bossrest
            quest = homework.questrest

            seekDungeon.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtDungeon.text = (p1*10).toString()
                    dungeon = p1*10
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
            seekBoss.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtBoss.text = (p1*10).toString()
                    boss = p1*10
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
            seekQuest.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtQuest.text = (p1*10).toString()
                    quest = p1*10
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = 3

    fun getNames(type: String): String {
        var result = ""
        if (type == "일일") {
            days.forEach { day ->
                if (result != "") {
                    result += ","
                }
                result += day.name
            }
        } else {
            weeks.forEach { week ->
                if (result != "") {
                    result += ","
                }
                result += week.name
            }
        }
        return result
    }

    fun getNows(type: String): String {
        var result = ""
        if (type == "일일") {
            days.forEach { day ->
                if (result != "") {
                    result += ","
                }
                result += day.now.toString()
            }
        } else {
            weeks.forEach { week ->
                if (result != "") {
                    result += ","
                }
                result += week.now.toString()
            }
        }
        return result
    }

    fun getMaxs(type: String): String {
        var result = ""
        if (type == "일일") {
            days.forEach { day ->
                if (result != "") {
                    result += ","
                }
                result += day.max.toString()
            }
        } else {
            weeks.forEach { week ->
                if (result != "") {
                    result += ","
                }
                result += week.max.toString()
            }
        }
        return result
    }

    fun getIcons(type: String): String {
        var result = ""
        if (type == "일일") {
            days.forEach { day ->
                if (result != "") {
                    result += ","
                }
                result += day.icon
            }
        } else {
            weeks.forEach { week ->
                if (result != "") {
                    result += ","
                }
                result += week.icon
            }
        }
        return result
    }

    fun getEnds(type: String): String {
        var result = ""
        if (type == "일일") {
            days.forEach { day ->
                if (result != "") {
                    result += ","
                }
                result += day.end
            }
        } else {
            weeks.forEach { week ->
                if (result != "") {
                    result += ","
                }
                result += week.end
            }
        }
        return result
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}