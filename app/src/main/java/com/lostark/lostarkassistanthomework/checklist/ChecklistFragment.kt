package com.lostark.lostarkassistanthomework.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.dbs.FamilyDBAdapter

class ChecklistFragment : Fragment() {
    lateinit var txtAll: TextView
    lateinit var progressAll: ProgressBar
    lateinit var dayListView: RecyclerView
    lateinit var weekListView: RecyclerView

    lateinit var dayAdapter: DayRecyclerAdapter
    lateinit var weekAdapter: DayRecyclerAdapter

    lateinit var familyDBAdapter: FamilyDBAdapter
    lateinit var dayFamilys: ArrayList<Family>
    lateinit var weekFamilys: ArrayList<Family>

    lateinit var familyDB: FamilyDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_checklist, container, false)

        txtAll = view.findViewById(R.id.txtAll)
        progressAll = view.findViewById(R.id.progressAll)
        dayListView = view.findViewById(R.id.dayListView)
        weekListView = view.findViewById(R.id.weekListView)

        familyDBAdapter = FamilyDBAdapter(requireContext())
        familyDB = FamilyDatabase.getInstance(requireContext())!!

        dayFamilys = ArrayList()
        weekFamilys = ArrayList()

        val saveFamilyData = familyDB.familyDao().getAll()
        if (saveFamilyData.isEmpty()) {
            familyDBAdapter.open()
            initFamilys(familyDBAdapter.getItems("일일"), "일일")
            initFamilys(familyDBAdapter.getItems("주간"), "주간")
            familyDBAdapter.close()
        } else {
            asyncFamilyData(saveFamilyData)
        }

        dayAdapter = DayRecyclerAdapter(dayFamilys, requireContext(), familyDB)
        dayListView.adapter = dayAdapter
        dayListView.layoutManager = GridLayoutManager(requireContext(), 2)
        dayListView.addItemDecoration(RecyclerViewDecoration(10, 10))

        weekAdapter = DayRecyclerAdapter(weekFamilys, requireContext(), familyDB)
        weekListView.adapter = weekAdapter
        weekListView.layoutManager = GridLayoutManager(requireContext(), 2)
        weekListView.addItemDecoration(RecyclerViewDecoration(10, 10))

        return view
    }

    fun initFamilys(list: ArrayList<Family>, type: String) {
        list.forEach { item ->
            familyDB.familyDao().insertAll(item)
            if (type == "일일") {
                dayFamilys.add(item)
            } else {
                weekFamilys.add(item)
            }
            println("added file(name : ${item.name})")
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
}