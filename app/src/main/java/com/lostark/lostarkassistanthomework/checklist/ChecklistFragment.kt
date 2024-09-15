package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.LoadingDialog
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.FamilyEditActivity
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.databinding.FragmentChecklistBinding
import com.lostark.lostarkassistanthomework.dbs.FamilyDBAdapter
import com.lostark.lostarkassistanthomework.dbs.GoldDBAdapter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.jsoup.Jsoup

class ChecklistFragment : Fragment() {
    private lateinit var viewModel: ChecklistViewModel
    private lateinit var binding: FragmentChecklistBinding

    lateinit var dayAdapter: DayRecyclerAdapter
    lateinit var weekAdapter: DayRecyclerAdapter

    lateinit var familyDBAdapter: FamilyDBAdapter
    lateinit var dayFamilys: ArrayList<Family>
    lateinit var weekFamilys: ArrayList<Family>

    lateinit var familyDB: FamilyDatabase

    lateinit var chracterAdapter: ChracterRecylerAdapter
    var homeworks: ArrayList<Homework> = ArrayList()
    lateinit var homeworkDB: HomeworkDatabase

    lateinit var goldDBAdapter: GoldDBAdapter
    private var myCompositeDisposable = CompositeDisposable()

    val NOTIFYED = 1

    fun syncProgress() {
        var max_progress = 0
        var progress = 0
        homeworks.forEach { homework ->
            val nows = homework.daynows.split(",")
            val maxs = homework.daymaxs.split(",")
            nows.forEach { now ->
                if (now != "") {
                    progress += now.toInt()
                }
            }
            maxs.forEach { max ->
                if (max != "") {
                    max_progress += max.toInt()
                }
            }
        }
        with(binding) {
            viewModel.maxProgress.value = max_progress
            viewModel.progress.value = progress
            viewModel.percent.value = (progress.toDouble()/max_progress.toDouble()*100).toInt()
            //progressAll.max = max_progress
            //progressAll.progress = progress
            //txtAll.text = "${(progress.toDouble()/max_progress.toDouble()*100).toInt()}%"
        }
        syncGold()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view : View = inflater.inflate(R.layout.fragment_checklist, container, false)
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_checklist, null, false)
        viewModel = ViewModelProvider(this).get(ChecklistViewModel::class.java)
        binding.checklistViewModel = viewModel

        binding.btnSetting.setOnClickListener {
            val intent = Intent(context, FamilyEditActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.btnList.setOnClickListener {
            val dialog = GoldDialog(requireContext())
            dialog.show(true)
        }

        val goldObserver = Observer<Int> { data ->
            binding.txtGold.text = data.toString()
        }
        val allGoldObserver = Observer<Int> { data ->
            binding.txtAllGold.text = data.toString()
        }
        val progressObserver = Observer<Int> { data ->
            binding.progressAll.progress = data
        }
        val maxProgressObserver = Observer<Int> { data ->
            binding.progressAll.max = data
        }
        val percentObserver = Observer<Int> { data ->
            binding.txtAll.text = "$data%"
        }
        viewModel.gold.observe(viewLifecycleOwner, goldObserver)
        viewModel.allGold.observe(viewLifecycleOwner, allGoldObserver)
        viewModel.progress.observe(viewLifecycleOwner, progressObserver)
        viewModel.maxProgress.observe(viewLifecycleOwner, maxProgressObserver)
        viewModel.percent.observe(viewLifecycleOwner, percentObserver)

        familyDBAdapter = FamilyDBAdapter(requireContext())
        goldDBAdapter = GoldDBAdapter(requireContext())
        familyDB = FamilyDatabase.getInstance(requireContext())!!

        dayFamilys = ArrayList()
        weekFamilys = ArrayList()

        dayAdapter = DayRecyclerAdapter(dayFamilys, requireContext(), familyDB)
        weekAdapter = DayRecyclerAdapter(weekFamilys, requireContext(), familyDB)

        val saveFamilyData = familyDB.familyDao().getAll()
        if (saveFamilyData.isEmpty()) {
            familyDBAdapter.open()
            initFamilys(familyDBAdapter.getItems("일일"), "일일")
            initFamilys(familyDBAdapter.getItems("주간"), "주간")
            familyDBAdapter.close()
        } else {
            asyncFamilyData(saveFamilyData)
        }

        with(binding) {
            dayListView.adapter = dayAdapter
            //dayListView.layoutManager = GridLayoutManager(requireContext(), 2)
            dayListView.addItemDecoration(RecyclerViewDecoration(10, 10))

            weekListView.adapter = weekAdapter
            //weekListView.layoutManager = GridLayoutManager(requireContext(), 2)
            weekListView.addItemDecoration(RecyclerViewDecoration(10, 10))
        }

        homeworkDB = HomeworkDatabase.getInstance(requireContext())!!
        chracterAdapter = ChracterRecylerAdapter(homeworks, requireContext(), requireActivity(), this)
        binding.chracterListView.adapter = chracterAdapter
        binding.chracterListView.addItemDecoration(RecyclerViewDecoration(0, 30))

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //resume()
        /*if (App.prefs.isBoolean("showgold", true)) {
            layoutGold.visibility = View.VISIBLE
        } else {
            layoutGold.visibility = View.GONE
        }*/
    }

    fun initFamilys(list: ArrayList<Family>, type: String) {
        list.forEach { item ->
            familyDB.familyDao().insertAll(item)
            if (type == "일일") {
                dayFamilys.add(item)
            } else {
                weekFamilys.add(item)
            }
        }
    }

    fun asyncFamilyData(list: List<Family>) {
        list.forEach { item ->
            if (item.type == "일일") {
                dayFamilys.add(item)
            } else {
                weekFamilys.add(item)
            }
        }
    }

    fun asyncChracterData(list: List<Homework>) {
        list.forEach { item ->
            homeworks.add(item)
        }
    }

    fun syncData() {
        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setCancelable(false)
        val handler = HomeworkHandler()
        loadingDialog.show()
        val thread = CloringThread(requireContext(), loadingDialog, homeworks, handler, homeworkDB)
        thread.start()
    }

    fun resume() {
        homeworks.clear()
        dayFamilys.clear()
        weekFamilys.clear()
        val saveFamilyData = familyDB.familyDao().getAll()
        asyncFamilyData(saveFamilyData)
        val saveChracterData = homeworkDB.homeworkDao().getAll()
        asyncChracterData(saveChracterData)
        homeworks.sort();
        dayFamilys.sort()
        weekFamilys.sort()
        syncProgress()
        dayAdapter.notifyDataSetChanged()
        weekAdapter.notifyDataSetChanged()
        chracterAdapter.notifyDataSetChanged()
        syncGold()
        syncGoldAll()
    }

    fun syncGoldAll() {
        var all = 0
        goldDBAdapter.open()
        val golds = goldDBAdapter.getItems()
        goldDBAdapter.close()
        homeworks.forEach { homework ->
            if (homework.isGold) {
                val items = homework.weeklist.split(",")
                for (i in items.indices) {
                    golds.forEach { gold ->
                        if (gold.name == items[i]) {
                            if (homework.level >= gold.min && homework.level < gold.max) {
                                all += gold.gold
                            }
                        }
                    }
                }
            }
        }
        viewModel.allGold.value = all
        //binding.txtAllGold.text = all.toString()
    }

    fun syncGold() {
        var all = 0
        goldDBAdapter.open()
        val golds = goldDBAdapter.getItems()
        goldDBAdapter.close()
        homeworks.forEach { homework ->
            if (homework.isGold) {
                val items = homework.weeklist.split(",")
                val nows = homework.weeknows.split(",")
                for (i in items.indices) {
                    golds.forEach { gold ->
                        if (gold.name == items[i]) {
                            if (homework.level >= gold.min && homework.level < gold.max && nows[i].toInt() >= gold.position) {
                                all += gold.gold
                            }
                        }
                    }
                }
            }
        }
        viewModel.gold.value = all
        //binding.txtGold.text = all.toString()
    }

    override fun onDestroy() {
        myCompositeDisposable.clear()
        super.onDestroy()
    }

    inner class HomeworkHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                NOTIFYED -> {
                    resume()
                    chracterAdapter.notifyDataSetChanged()
                }
                else -> {

                }
            }
        }
    }
}

class CloringThread(
    private val context: Context,
    private val loadingDialog: LoadingDialog,
    private val homeworks: ArrayList<Homework>,
    private val handler: Handler,
    private val homeworkDB: HomeworkDatabase
) : Thread() {
    override fun run() {
        try {
            homeworks.forEach { homework ->
                if (homework.auto) {
                    var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/${homework.name}").get()
                    var level_element = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)")
                    if (level_element.text() != "") {
                        var level_str = level_element.text()
                        level_str = level_str.replace("Lv.", "")
                        level_str = level_str.replace(",", "")
                        var level = level_str.toDouble()
                        var server_element = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > span.profile-character-info__server")
                        var server = server_element.text()
                        server = server.replace("@", "")
                        var job_element = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > img")
                        var job = job_element.attr("alt")
                        homework.level = level
                        homework.server = server
                        homework.job = job
                        homeworkDB.homeworkDao().update(homework)
                    }
                }
            }
            loadingDialog.dismiss()
            var message = Message.obtain()
            message.what = 1
            handler.sendMessage(message)
        } catch (e: Exception) {
            e.printStackTrace()
            loadingDialog.dismiss()
        }
    }
}