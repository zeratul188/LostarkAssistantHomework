package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.CheckDialog
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.EditData

class EditRecyclerAdapter(
    private val items: ArrayList<EditData>,
    private val context: Context,
    private val homework: Homework
) : RecyclerView.Adapter<EditRecyclerAdapter.ViewHolder>() {
    val homeworkDB = HomeworkDatabase.getInstance(context)!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val pos = position
        val listener = View.OnClickListener {
            val checkDialog = CheckDialog(context)
            checkDialog.setData("${item.name}의 숙제를 삭제하시겠습니까?", "삭제", true)
            checkDialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                override fun onClicked() {
                    val customToast = CustomToast(App.context())
                    customToast.createToast("${item.name}의 숙제를 삭제하였습니다.", false)
                    customToast.show()
                    items.removeAt(pos)
                    notifyDataSetChanged()
                }
            })
            checkDialog.show(true)
        }
        val icon_listener = View.OnClickListener {
            val iconDialog = IconSelectDialog(context)
            iconDialog.setOnClickListener(object : IconSelectDialog.OnDialogClickListener {
                override fun onClicked() {
                    val icon = iconDialog.getResult()
                    item.icon = icon.icon
                    notifyDataSetChanged()
                }
            })
            iconDialog.show(true)
        }
        holder.apply {
            bind(item, context, homework, listener, icon_listener)
            itemView.tag = item
        }
    }

    fun getItems(): ArrayList<EditData> = items

    override fun getItemCount(): Int = items.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val view = v
        lateinit var imgIcon: ImageView
        lateinit var edtName: EditText
        lateinit var edtNow: EditText
        lateinit var edtMax: EditText
        lateinit var sprEnd: Spinner
        lateinit var btnDelete: ImageButton

        fun bind(item: EditData, context: Context, homework: Homework, listener: View.OnClickListener, icon_listener: View.OnClickListener) {
            imgIcon = view.findViewById(R.id.imgIcon)
            edtName = view.findViewById(R.id.edtName)
            edtNow = view.findViewById(R.id.edtNow)
            edtMax = view.findViewById(R.id.edtMax)
            sprEnd = view.findViewById(R.id.sprEnd)
            btnDelete = view.findViewById(R.id.btnDelete)

            val ends = context.resources.getStringArray(R.array.ends)
            val endAdapter = ArrayAdapter(context, R.layout.txt_item_end, ends)
            sprEnd.adapter = endAdapter
            val pos = ends.indexOf(item.end)
            if (pos != -1) {
                sprEnd.setSelection(pos)
            }
            imgIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            edtName.setText(item.name)
            edtNow.setText(item.now.toString())
            edtMax.setText(item.max.toString())

            btnDelete.setOnClickListener(listener)
            imgIcon.setOnClickListener(icon_listener)
        }
    }
}