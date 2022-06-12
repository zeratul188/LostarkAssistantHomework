package com.lostark.lostarkassistanthomework.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class WeekHomeworkFragment(private val homework: Homework) : Fragment() {
    lateinit var homeworkDB: HomeworkDatabase
    lateinit var listView: RecyclerView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_list_day, container, false)

        listView = view.findViewById(R.id.listView)

        homeworkDB = HomeworkDatabase.getInstance(requireContext())!!
        val items = ArrayList<Checklist>()

        var names = homework.weeklist.split(",")
        var nows = homework.weeknows.split(",")
        var maxs = homework.weekmaxs.split(",")
        var icons = homework.weekicons.split(",")
        var ends = homework.weekends.split(",")

        for (i in 0..(names.size-1)) {
            items.add(Checklist(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
        }

        val homeworkAdapter = HomeworkRecylerAdapter(items, requireContext(), homework, homeworkDB, "주간")
        listView.adapter = homeworkAdapter
        listView.addItemDecoration(RecyclerViewDecoration(0, 10))
        
        return view;
    }
}