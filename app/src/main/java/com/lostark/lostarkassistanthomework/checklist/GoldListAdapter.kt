package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.GoldList
import com.lostark.lostarkassistanthomework.databinding.ItemFamilyChecklistBinding
import com.lostark.lostarkassistanthomework.databinding.ItemGoldChracterBinding
import de.hdodenhof.circleimageview.CircleImageView

class GoldListAdapter(
    private val items: ArrayList<GoldList>,
    private val context: Context
) : RecyclerView.Adapter<GoldListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gold_chracter, parent, false)
        return GoldListAdapter.ViewHolder(view)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGoldChracterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.apply {
            bind(item, context)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(val binding: ItemGoldChracterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GoldList,
            context: Context
        ) {
            val jobs = context.resources.getStringArray(R.array.job)
            val pos = jobs.indexOf(item.job)+1;
            with(binding) {
                goldlist = item
                imgJob.setImageResource(context.resources.getIdentifier("jbi${pos}", "drawable", context.packageName))
                /*txtName.text = item.name
                txtServer.text = item.server
                txtLevel.text = "Lv.${item.level}"
                txtJob.text = item.job*/
                if (item.content == "") {
                    txtContent.text = "획득한 골드가 없습니다."
                } else {
                    txtContent.text = item.content
                }
                //txtGold.text = item.gold.toString()
            }
        }
    }
}