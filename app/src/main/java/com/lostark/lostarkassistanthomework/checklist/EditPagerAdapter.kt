package com.lostark.lostarkassistanthomework.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.EditData

class EditPagerAdapter(private val homework: Homework) : PagerAdapter() {
    var homeworkDB: HomeworkDatabase = HomeworkDatabase.getInstance(App.context())!!
    lateinit var dayAdapter: EditRecyclerAdapter
    lateinit var weekAdapter: EditRecyclerAdapter

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = null

        if (position != 2) {
            view = LayoutInflater.from(container.context).inflate(R.layout.fragment_edit_homework, container, false)

            val listView = view.findViewById<RecyclerView>(R.id.listView)
            listView.addItemDecoration(RecyclerViewDecoration(0, 10))
            
            when (position) {
                0 -> {
                    val edits = ArrayList<EditData>()
                    val names = homework.daylist.split(",")
                    val nows = homework.daynows.split(",")
                    val maxs = homework.daymaxs.split(",")
                    val icons = homework.dayicons.split(",")
                    val ends = homework.dayends.split(",")
                    for (i in names.indices) {
                        edits.add(EditData(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
                    }
                    dayAdapter = EditRecyclerAdapter(edits, App.context(), homework)
                    listView.adapter = dayAdapter
                }
                1 -> {
                    val edits = ArrayList<EditData>()
                    val names = homework.weeklist.split(",")
                    val nows = homework.weeknows.split(",")
                    val maxs = homework.weekmaxs.split(",")
                    val icons = homework.weekicons.split(",")
                    val ends = homework.weekends.split(",")
                    for (i in names.indices) {
                        edits.add(EditData(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
                    }
                    weekAdapter = EditRecyclerAdapter(edits, App.context(), homework)
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

            seekDungeon.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtDungeon.text = (p1*10).toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
            seekBoss.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtBoss.text = (p1*10).toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }
            })
            seekQuest.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    txtQuest.text = (p1*10).toString()
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



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}