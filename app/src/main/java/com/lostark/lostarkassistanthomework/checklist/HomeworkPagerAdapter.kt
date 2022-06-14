package com.lostark.lostarkassistanthomework.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase

class HomeworkPagerAdapter(private val checklists: ArrayList<ArrayList<Checklist>>, private val homework: Homework) :
    PagerAdapter() {

    lateinit var listView: RecyclerView
    var homeworkDB: HomeworkDatabase = HomeworkDatabase.getInstance(App.context())!!
    val heights = arrayListOf(0, 0)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.fragment_list_day, container, false)

        listView = view.findViewById(R.id.listView)

        var type = "null"
        when (position) {
            0 -> type = "일일"
            1 -> type = "주간"
        }
        val homeworkAdapter = HomeworkRecylerAdapter(checklists[position], App.context(), homework, homeworkDB, type)
        listView.adapter = homeworkAdapter
        listView.addItemDecoration(RecyclerViewDecoration(10, 10))

        heights[position] = view.height

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = checklists.size



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

}