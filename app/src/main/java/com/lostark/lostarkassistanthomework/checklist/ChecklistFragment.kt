package com.lostark.lostarkassistanthomework.checklist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.ShareChecklist

class ChecklistFragment : Fragment() {
    lateinit var txtAll: TextView
    lateinit var progressAll: ProgressBar
    lateinit var dayListView: RecyclerView

    lateinit var dayAdapter: DayRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_checklist, container, false)

        txtAll = view.findViewById(R.id.txtAll)
        progressAll = view.findViewById(R.id.progressAll)
        dayListView = view.findViewById(R.id.dayListView)

        val list = ArrayList<ShareChecklist>()
        val drawable: Drawable = resources.getDrawable(R.drawable.hwid1)
        for (i in 1..6) {
            list.add(ShareChecklist("item(${i})", drawable, 0, i+2, "회"))
        }

        dayAdapter = DayRecyclerAdapter(list)
        dayListView.adapter = dayAdapter
        dayListView.addItemDecoration(RecyclerViewDecoration(10))

        return view
    }
}