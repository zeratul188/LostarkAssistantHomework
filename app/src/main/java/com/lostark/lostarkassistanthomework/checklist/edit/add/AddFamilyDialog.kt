package com.lostark.lostarkassistanthomework.checklist.edit.add

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.databinding.DialogAddFamilyHomeworkBinding
import com.lostark.lostarkassistanthomework.objects.EditData
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddFamilyDialog(
    context: Context,
    type: String,
    private val myCompositeDisposable: CompositeDisposable
): DialogFragment() {
    private lateinit var onClickListener: OnDialogClickListener

    private val selfFragment = AddSelfHomeworkFragment(myCompositeDisposable)
    private val presetFragment = AddPresetFamilyFragment(type)

    private lateinit var binding: DialogAddFamilyHomeworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_family_homework, null, false)

        AddFamilyDialog@this.childFragmentManager.beginTransaction().replace(R.id.layoutFrame, selfFragment)?.commit()

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

        return binding.root
    }

    fun dialogDismiss() = dismiss()

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

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