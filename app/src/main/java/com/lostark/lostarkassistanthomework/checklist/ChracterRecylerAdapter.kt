package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.edit.EditActivity
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.databinding.ItemChracterBinding

class ChracterRecylerAdapter(
    private val items: ArrayList<Homework>,
    private val context: Context,
    private val activity: FragmentActivity,
    private val fragment: ChecklistFragment
) : RecyclerView.Adapter<ChracterRecylerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chracter, parent, false)
        return ViewHolder(view, activity, fragment)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChracterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, fragment)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            bind(item, context)
            itemView.tag = item
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(
        private val binding: ItemChracterBinding, private val fragment: ChecklistFragment) : RecyclerView.ViewHolder(binding.root) {
        private val holder = this

        fun bind(item: Homework, context: Context) {
            binding.homework = item

            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            with(binding) {
                imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
                /*txtName.text = item.name
                txtServer.text = item.server
                txtLevel.text = "Lv.${item.level}"
                txtJob.text = item.job*/

                if (item.isGold) {
                    imgGold.visibility = View.VISIBLE
                } else {
                    imgGold.visibility = View.GONE
                }
            }

            val lists = ArrayList<ArrayList<Checklist>>()
            lists.add(ArrayList())
            lists.add(ArrayList())
            
            var daynames = item.daylist.split(",")
            var daynows = item.daynows.split(",")
            var daymaxs = item.daymaxs.split(",")
            var dayicons = item.dayicons.split(",")
            var dayends = item.dayends.split(",")
            for (i in daynames.indices) {
                if (daynows[i] != "" && daymaxs[i] != "") {
                    lists[0].add(Checklist(daynames[i], daynows[i].toInt(), daymaxs[i].toInt(), dayicons[i], dayends[i]))
                }
            }
            var names = item.weeklist.split(",")
            var nows = item.weeknows.split(",")
            var maxs = item.weekmaxs.split(",")
            var icons = item.weekicons.split(",")
            var ends = item.weekends.split(",")
            for (i in names.indices) {
                if (nows[i] != "" && maxs[i] != "") {
                    lists[1].add(Checklist(names[i], nows[i].toInt(), maxs[i].toInt(), icons[i], ends[i]))
                }
            }

            var max = 0
            var progress = 0
            lists[0].forEach { item ->
                max += item.max
                progress += item.now
            }
            with(binding) {
                progressHomework.max = max
                progressHomework.progress = progress
                txtProgress.text = "${(progress.toDouble()/max.toDouble()*100).toInt()}"

                val pagerAdapter = HomeworkPagerAdapter(lists, item, holder, fragment, pagerMain, bottomNavigationView)
                pagerMain.adapter = pagerAdapter
            }

            //pagerMain.currentItem = App.prefs.getInt("dayorweek", 0)
            //bottomNavigationView.menu.getItem(App.prefs.getInt("dayorweek", 0)).setChecked(true)
            //resized(App.prefs.getInt("dayorweek", 0))

            binding.pagerMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    binding.bottomNavigationView.menu.getItem(position).isChecked = true
                    resized(position, item)
                }

                override fun onPageSelected(position: Int) {

                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            /*txtName.setOnClickListener {
                val intent = Intent(context, InformationActivity::class.java)
                intent.putExtra("name", item.name)
                context.startActivity(intent)
            }*/

            binding.btnSetting.setOnClickListener {
                val intent = Intent(context, EditActivity::class.java)
                intent.putExtra("homework", item)
                context.startActivity(intent)
            }

            binding.bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.action_day -> {
                        binding.pagerMain.currentItem = 0
                    }
                    R.id.action_week -> {
                        binding.pagerMain.currentItem = 1
                    }
                }
                return@setOnItemSelectedListener true
            }
        }

        fun syncProgress(progress: Int, max: Int) {
            with(binding) {
                progressHomework.max = max
                progressHomework.progress = progress
                txtProgress.text = "${(progress.toDouble()/max.toDouble()*100).toInt()}"
            }
        }

        fun resized(position: Int, item: Homework) {
            var pos = position
            if (App.prefs.getInt("dayorweek", 0) == 1) {
                pos = 1-position
            }
            val view = binding.pagerMain[pos]
            if (view != null) {
                view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                //val width = view.measuredWidth
                val height = view.measuredHeight
                val params = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                params.topToBottom = binding.bottomNavigationView.id
                binding.pagerMain.layoutParams = params
            }
        }
    }
}