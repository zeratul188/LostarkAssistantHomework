package com.lostark.lostarkassistanthomework.checklist.edit

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.viewpager.widget.PagerAdapter
import com.jakewharton.rxbinding4.widget.changeEvents
import com.jakewharton.rxbinding4.widget.textChanges
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.add.AddDialog
import com.lostark.lostarkassistanthomework.checklist.RecyclerViewDecoration
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.databinding.FragmentEditHomeworkBinding
import com.lostark.lostarkassistanthomework.databinding.FragmentEditRestBinding
import com.lostark.lostarkassistanthomework.objects.EditData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class EditPagerAdapter(
    private val homework: Homework,
    private val context: Context,
    private val fm: FragmentManager,
    private val myCompositeDisposable: CompositeDisposable
) : PagerAdapter(), EditRecyclerAdapter.OnStartDragListener {
    //var homeworkDB: HomeworkDatabase = HomeworkDatabase.getInstance(App.context())!!
    var dayAdapter: EditRecyclerAdapter
    var weekAdapter: EditRecyclerAdapter

    private val days = ArrayList<EditData>()
    private val weeks = ArrayList<EditData>()
    lateinit var dayHelper : ItemTouchHelper
    lateinit var weekHelper : ItemTouchHelper

    var dungeon = 0
    var boss = 0
    var quest = 0
    var lostDungeon =  0
    var lostBoss = 0
    var lostQuest = 0

    init {
        dungeon = homework.dungeonrest
        boss = homework.bossrest
        quest = homework.questrest

        lostDungeon =  homework.dungeonlost
        lostBoss = homework.bosslost
        lostQuest = homework.questlost

        val names = homework.daylist.split(",")
        val nows = homework.daynows.split(",")
        val maxs = homework.daymaxs.split(",")
        val icons = homework.dayicons.split(",")
        val ends = homework.dayends.split(",")
        for (i in names.indices) {
            if (nows[i] != "" && maxs[i] != "") {
                days.add(EditData(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
            }
        }
        dayAdapter = EditRecyclerAdapter(days, context, homework, this)

        val names2 = homework.weeklist.split(",")
        val nows2 = homework.weeknows.split(",")
        val maxs2 = homework.weekmaxs.split(",")
        val icons2 = homework.weekicons.split(",")
        val ends2 = homework.weekends.split(",")
        for (i in names2.indices) {
            if (nows2[i] != "" && maxs2[i] != "") {
                weeks.add(EditData(names2[i], nows2[i].toInt(), maxs2[i].toInt(), icons2[i], ends2[i]))
            }
        }
        weekAdapter = EditRecyclerAdapter(weeks, context, homework, this)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position != 2) {
            val binding: FragmentEditHomeworkBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_edit_homework, null, false)
            //view = LayoutInflater.from(container.context).inflate(R.layout.fragment_edit_homework, container, false)

            with(binding) {
                listView.addItemDecoration(RecyclerViewDecoration(0, 10))
                btnAdd.setOnClickListener {
                    var type = ""
                    when (position) {
                        0 -> type = "일일"
                        1 -> type = "주간"
                    }
                    val addDialog = AddDialog(context, type, myCompositeDisposable)
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
                                        dayAdapter.notifyDataSetChanged()
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
                                        weekAdapter.notifyDataSetChanged()
                                        printList(weeks)
                                    }
                                }
                            } else {
                                customToast.createToast("이름이 비어있습니다.", false)
                            }
                            customToast.show()
                        }
                    })
                    addDialog.show(fm, "addDialog")
                }

                val callback = EditItemTouchHelperCallback()

                when (position) {
                    0 -> {
                        callback.setOnItemMoveListener(dayAdapter)
                        dayHelper = ItemTouchHelper(callback)
                        dayHelper.attachToRecyclerView(listView)
                        listView.adapter = dayAdapter
                    }
                    1 -> {
                        callback.setOnItemMoveListener(weekAdapter)
                        weekHelper = ItemTouchHelper(callback)
                        weekHelper.attachToRecyclerView(listView)
                        listView.adapter = weekAdapter
                    }
                }
            }

            container.addView(binding.root)
            return binding.root
        } else {
            val binding: FragmentEditRestBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_edit_rest, null, false)
            //view = LayoutInflater.from(container.context).inflate(R.layout.fragment_edit_rest, container, false)

            with(binding) {
                seekDungeon.progress = homework!!.dungeonrest/10
                seekBoss.progress = homework!!.bossrest/10
                seekQuest.progress = homework!!.questrest/10
                dungeon = homework!!.dungeonrest
                boss = homework!!.bossrest
                quest = homework!!.questrest

                lostDungeon =  homework!!.dungeonlost
                lostBoss = homework!!.bosslost
                lostQuest = homework!!.questlost

                edtDungeon.setText(lostDungeon.toString())
                edtBoss.setText(lostBoss.toString())
                edtQuest.setText(lostQuest.toString())

                val edtDungeonChangeObservable = edtDungeon.textChanges()
                val edtDungeonSubscription: Disposable? = edtDungeonChangeObservable
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onNext = {
                            lostDungeon = if (edtDungeon.text.toString() == "") {
                                0
                            } else {
                                if (edtDungeon.text.toString().toInt() > 100) {
                                    edtDungeon.setText("100")
                                    100
                                } else {
                                    edtDungeon.text.toString().toInt()
                                }
                            }
                        },
                        onComplete = {

                        },
                        onError = {
                            Log.d("RXError(edtDungeon)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(edtDungeonSubscription)

                val edtBossObservable = edtBoss.textChanges()
                val edtBossSubscription: Disposable? = edtBossObservable
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onNext = {
                            lostBoss = if (edtBoss.text.toString() == "") {
                                0
                            } else {
                                if (edtBoss.text.toString().toInt() > 100) {
                                    edtBoss.setText("100")
                                    100
                                } else {
                                    edtBoss.text.toString().toInt()
                                }
                            }
                        },
                        onComplete = {

                        },
                        onError = {
                            Log.d("RXError(edtBoss)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(edtBossSubscription)

                val edtQuestObservable = edtQuest.textChanges()
                val edtQuestSubscription: Disposable? = edtQuestObservable
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                        onNext = {
                            lostQuest = if (edtQuest.text.toString() == "") {
                                0
                            } else {
                                if (edtQuest.text.toString().toInt() > 100) {
                                    edtQuest.setText("100")
                                    100
                                } else {
                                    edtQuest.text.toString().toInt()
                                }
                            }
                        },
                        onComplete = {

                        },
                        onError = {
                                Log.d("RXError(edtQeust)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(edtQuestSubscription)

                val seekDungeonObservable = seekDungeon.changeEvents()
                val seekDungeonSubscription = seekDungeonObservable
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onNext = {
                            txtDungeon.text = (seekDungeon.progress*10).toString()
                            dungeon = seekDungeon.progress*10
                        },
                        onComplete = {

                        },
                        onError = {
                            Log.d("RXError(seekDungeon)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(seekDungeonSubscription)
                val seekBossObservable = seekBoss.changeEvents()
                val seekBossSubscription: Disposable = seekBossObservable
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onNext = {
                            txtBoss.text = (seekBoss.progress*10).toString()
                            boss = seekBoss.progress*10
                        },
                        onComplete = {

                        },
                        onError = {
                            Log.d("RXError(edtQeust)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(seekBossSubscription)
                val seekQuestObservable = seekQuest.changeEvents()
                val seekQuestSubscription = seekQuestObservable
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onNext = {
                            txtQuest.text = (seekQuest.progress*10).toString()
                            quest= seekQuest.progress*10
                        },
                        onComplete = {

                        },
                        onError = {
                            Log.d("RXError(edtQeust)", "Error : $it")
                        }
                    )
                myCompositeDisposable.add(seekQuestSubscription)
            }
            container.addView(binding.root)
            return binding.root
        }
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

    fun printList(list: ArrayList<EditData>) {
        println("======================================")
        list.forEach { item ->
            println(item.name)
        }
        println("======================================")
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun onStartDrag(holder: EditRecyclerAdapter.ViewHolder) {
        dayHelper.startDrag(holder)
        weekHelper.startDrag(holder)
    }

}