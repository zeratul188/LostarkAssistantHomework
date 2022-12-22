package com.lostark.lostarkassistanthomework.checklist.edit.add

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.databinding.DialogAddHomeworkBinding
import com.lostark.lostarkassistanthomework.objects.EditData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddDialog(
    context: Context,
    type: String,
    private val myCompositeDisposable: CompositeDisposable
) : DialogFragment(){
    private lateinit var onClickListener: OnDialogClickListener

    private val selfFragment = AddSelfHomeworkFragment(myCompositeDisposable)
    private val presetFragment = AddPresetHomeworkFragment(type)

    private lateinit var binding: DialogAddHomeworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*activity?.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)*/
        //activity?.window!!.setGravity(Gravity.CENTER)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_homework, null, false)

        AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment)?.commit()

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_self -> AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment).commit()
                R.id.action_preset -> AddDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, presetFragment).commit()
            }
            return@setOnItemSelectedListener true
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnAdd.setOnClickListener {
            onClickListener.onClicked()
        }

        return view
    }

    fun dialogDismiss() = dismiss()

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    //fun isNameEmpty(): Boolean = selfFragment.isEmpty()

    fun getItem(): EditData {
        return when (binding.bottomNavigationView.selectedItemId) {
            R.id.action_self -> selfFragment.getItem()
            else -> presetFragment.getItem()
        }
    }

    interface OnDialogClickListener {
        fun onClicked()
    }
}