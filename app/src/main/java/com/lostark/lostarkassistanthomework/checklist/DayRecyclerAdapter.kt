package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Family
import com.lostark.lostarkassistanthomework.checklist.rooms.FamilyDatabase
import com.lostark.lostarkassistanthomework.databinding.ItemChracterBinding
import com.lostark.lostarkassistanthomework.databinding.ItemFamilyChecklistBinding

class DayRecyclerAdapter(private val items : ArrayList<Family>, private val context: Context, private val db: FamilyDatabase) : RecyclerView.Adapter<DayRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.item_family_checklist, parent, false)
        return DayRecyclerAdapter.ViewHolder(view)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFamilyChecklistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val longListener = View.OnLongClickListener { it ->
            item.now = item.max
            with(holder.binding) {
                txtCount.text = "${item.now}/${item.max}"
                layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                layoutComplete.visibility = View.VISIBLE
            }
            db.familyDao().update(item)
            notifyDataSetChanged()
            return@OnLongClickListener true
        }
        val listener = View.OnClickListener { it ->
            //click event
            if (item.now < item.max) {
                item.now++;
            } else {
                item.now = 0
            }
            with(holder.binding) {
                txtCount.text = "${item.now}/${item.max}"
                if (item.now >= item.max) {
                    layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                    layoutComplete.visibility = View.VISIBLE
                } else {
                    layoutBackground.setBackgroundResource(R.drawable.item_checklist_background)
                    layoutComplete.visibility = View.GONE
                }
            }
            db.familyDao().update(item)
            notifyDataSetChanged()
        }
        holder.apply {
            bind(longListener, listener, item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ItemFamilyChecklistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(longListener: View.OnLongClickListener, listener: View.OnClickListener, item: Family, context: Context) {
            with(binding) {
                family = item
                imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
                txtName.text = item.name
                txtCount.text = "${item.now}/${item.max}"
                txtEnd.text = item.end
                if (item.now >= item.max) {
                    layoutBackground.setBackgroundResource(R.drawable.item_checklist_disabled_background)
                    layoutComplete.visibility = View.VISIBLE
                } else {
                    layoutBackground.setBackgroundResource(R.drawable.item_checklist_background)
                    layoutComplete.visibility = View.GONE
                }
            }

            binding.root.setOnClickListener(listener)
            binding.root.setOnLongClickListener(longListener)
        }
    }
}