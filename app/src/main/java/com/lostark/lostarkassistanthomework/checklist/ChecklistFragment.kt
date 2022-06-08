package com.lostark.lostarkassistanthomework.checklist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.ShareChecklist
import com.lostark.lostarkassistanthomework.dbs.FamilyDBAdapter
import com.lostark.lostarkassistanthomework.dbs.sys.LoadDBAdapter

class ChecklistFragment : Fragment() {
    lateinit var txtAll: TextView
    lateinit var progressAll: ProgressBar
    lateinit var dayListView: RecyclerView
    lateinit var weekListView: RecyclerView

    lateinit var dayAdapter: DayRecyclerAdapter
    lateinit var weekAdapter: DayRecyclerAdapter

    lateinit var familyDBAdapter: FamilyDBAdapter
    lateinit var dayFamilys: ArrayList<ShareChecklist>
    lateinit var weekFamilys: ArrayList<ShareChecklist>

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

        familyDBAdapter.open()
        dayFamilys = familyDBAdapter.getItems("일일")
        weekFamilys = familyDBAdapter.getItems("주간")
        familyDBAdapter.close()

        dayAdapter = DayRecyclerAdapter(dayFamilys)
        dayListView.adapter = dayAdapter
        dayListView.addItemDecoration(RecyclerViewDecoration(10))

        weekAdapter = DayRecyclerAdapter(weekFamilys)
        weekListView.adapter = weekAdapter
        weekListView.addItemDecoration(RecyclerViewDecoration(10))

        return view
    }
}