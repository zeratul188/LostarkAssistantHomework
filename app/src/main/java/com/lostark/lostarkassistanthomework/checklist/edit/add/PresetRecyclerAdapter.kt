package com.lostark.lostarkassistanthomework.checklist.edit.add

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Preset
import com.lostark.lostarkassistanthomework.databinding.ItemAddPresetBinding
import com.lostark.lostarkassistanthomework.objects.EditData

class PresetRecyclerAdapter (
    private val items: ArrayList<Preset>,
    private val context: Context
) : RecyclerView.Adapter<PresetRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAddPresetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {
            for (i in items.indices) {
                items[i].isChecked = position == i
            }
            notifyDataSetChanged()
        }
        holder.apply {
            bind(item, context, listener)
            itemView.tag = item
        }
    }

    override fun getItemCount(): Int = items.size

    fun getItem(): EditData {
        var preset: Preset = Preset("null", 0, "null", "null", false)
        items.forEach { item ->
            if (item.isChecked) {
                preset = item
            }
        }
        return EditData(preset.name, 0, preset.max, preset.icon, preset.end)
    }

    class ViewHolder(
        private val binding: ItemAddPresetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Preset, context: Context, listener: View.OnClickListener) {
            with(binding) {
                imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
                /*txtName.text = item.name
                txtMax.text = item.max.toString()
                txtEnd.text = item.end*/

                if (item.isChecked) {
                    layoutBackground.setBackgroundResource(R.drawable.selected_style)
                } else {
                    layoutBackground.setBackgroundResource(R.drawable.unselected_style)
                }

                root.setOnClickListener(listener)
            }
        }
    }
}