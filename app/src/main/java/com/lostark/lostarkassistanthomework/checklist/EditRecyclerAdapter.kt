package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.MotionEventCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.CheckDialog
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.EditData
import java.util.*
import kotlin.collections.ArrayList

class EditRecyclerAdapter(
    private val items: ArrayList<EditData>,
    private val context: Context,
    private val homework: Homework,
    private val startDragListener: OnStartDragListener
) : RecyclerView.Adapter<EditRecyclerAdapter.ViewHolder>(), EditItemTouchHelperCallback.OnItemMoveListener {
    val homeworkDB = HomeworkDatabase.getInstance(context)!!

    interface OnStartDragListener {
        fun onStartDrag(holder: ViewHolder)
    }

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
        val iconListener = View.OnClickListener {
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
        val touchListener = View.OnTouchListener { v, event ->
            if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                startDragListener.onStartDrag(holder)
            }
            return@OnTouchListener false
        }
        holder.apply {
            bind(item, context, listener, iconListener, touchListener)
            itemView.tag = item
        }
        holder.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                item.name = holder.edtName.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        holder.edtNow.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (holder.edtNow.text.toString() == "") {
                    item.now = 0
                } else {
                    var value = holder.edtNow.text.toString().toInt()
                    if (value > item.max) {
                        value = item.max
                    }
                    item.now = value
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        holder.edtMax.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (holder.edtMax.text.toString() == "") {
                    item.max = 1
                } else {
                    var value = holder.edtMax.text.toString().toInt()
                    if (value == 0) {
                        holder.edtMax.setText("1")
                        value = 1
                    }
                    item.max = value
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        holder.sprEnd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val ends = context.resources.getStringArray(R.array.ends)
                item.end = ends[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
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
        lateinit var imgHandle: ImageView

        fun bind(
            item: EditData,
            context: Context,
            listener: View.OnClickListener,
            iconListener: View.OnClickListener,
            touchListener: View.OnTouchListener
        ) {
            imgIcon = view.findViewById(R.id.imgIcon)
            edtName = view.findViewById(R.id.edtName)
            edtNow = view.findViewById(R.id.edtNow)
            edtMax = view.findViewById(R.id.edtMax)
            sprEnd = view.findViewById(R.id.sprEnd)
            btnDelete = view.findViewById(R.id.btnDelete)
            imgHandle = view.findViewById(R.id.imgHandle)

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
            imgIcon.setOnClickListener(iconListener)
            imgHandle.setOnTouchListener(touchListener)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}