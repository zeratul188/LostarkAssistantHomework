package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.MeasuredViewPager
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework

class ChracterRecylerAdapter(private val items: ArrayList<Homework>, private val context: Context, private val activity: FragmentActivity) : RecyclerView.Adapter<ChracterRecylerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chracter, parent, false)
        return ChracterRecylerAdapter.ViewHolder(view, activity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            bind(item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(v : View, private val activity: FragmentActivity) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var imgJob: ImageView
        lateinit var txtServer: TextView
        lateinit var txtName: TextView
        lateinit var txtLevel: TextView
        lateinit var txtJob: TextView
        lateinit var btnSetting: ImageButton
        lateinit var bottomNavigationView: BottomNavigationView
        lateinit var pagerMain: MeasuredViewPager

        fun bind(item: Homework, context: Context) {
            imgJob = view.findViewById(R.id.imgJob)
            txtName = view.findViewById(R.id.txtName)
            txtServer = view.findViewById(R.id.txtServer)
            txtLevel = view.findViewById(R.id.txtLevel)
            txtJob = view.findViewById(R.id.txtJob)
            btnSetting = view.findViewById(R.id.btnSetting)
            bottomNavigationView = view.findViewById(R.id.bottomNavigationView)
            pagerMain = view.findViewById(R.id.pagerMain)

            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
            txtName.text = item.name
            txtServer.text = item.server
            txtLevel.text = "Lv.${item.level}"
            txtJob.text = item.job

            val fragments = ArrayList<Fragment>()
            val dayFragment = DayHomeworkFragment(item)
            val weekFragment = WeekHomeworkFragment(item)
            fragments.add(dayFragment)
            fragments.add(weekFragment)
            val fm = activity.supportFragmentManager
            val pagerAdapter = HomeworkPagerAdapter(fragments, fm)
            pagerMain.adapter = pagerAdapter

            pagerMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    bottomNavigationView.menu.getItem(position).setChecked(true)
                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.action_day -> {
                        pagerMain.setCurrentItem(0)
                    }
                    R.id.action_week -> {
                        pagerMain.setCurrentItem(1)
                    }
                }
                return@setOnItemSelectedListener true
            }
        }
    }
}