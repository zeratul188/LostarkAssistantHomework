package com.lostark.lostarkassistanthomework.checklist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.objects.EditData

class AddSelfHomeworkFragment : Fragment() {
    private lateinit var imgIcon: ImageView
    private lateinit var edtName: EditText
    private lateinit var edtMax: EditText
    private lateinit var sprEnds: Spinner

    private var ic = "hw1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_add_self, container, false)

        imgIcon = view.findViewById(R.id.imgIcon)
        edtName = view.findViewById(R.id.edtName)
        edtMax = view.findViewById(R.id.edtMax)
        sprEnds = view.findViewById(R.id.sprEnds)

        val ends = requireActivity().resources.getStringArray(R.array.ends)
        val endAdapter = ArrayAdapter(requireContext(), R.layout.txt_item_end, ends)
        sprEnds.adapter = endAdapter

        edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (edtName.text.toString().indexOf(",") != -1) {
                    edtName.setText(edtName.text.toString().replace(",", ""))
                    val toast = CustomToast(requireContext())
                    toast.createToast("\",\"기호는 사용하실 수 없습니다.", false)
                    toast.show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        imgIcon.setOnClickListener {
            val iconDialog = IconSelectDialog(requireContext())
            iconDialog.setOnClickListener(object : IconSelectDialog.OnDialogClickListener {
                override fun onClicked() {
                    val icon = iconDialog.getResult()
                    imgIcon.setImageResource(requireActivity().resources.getIdentifier(icon.icon, "drawable", requireActivity().packageName))
                    ic = icon.icon
                }
            })
            iconDialog.show(true)
        }

        return view
    }

    fun isEmpty(): Boolean = edtName.text.toString() == ""

    fun getItem(): EditData {
        val name = edtName.text.toString()
        var max = 1
        if (edtMax.text.toString() != "") {
            max = edtMax.text.toString().toInt()
        }
        val end = sprEnds.selectedItem.toString()
        return EditData(name, 0, max, ic, end)
    }
}