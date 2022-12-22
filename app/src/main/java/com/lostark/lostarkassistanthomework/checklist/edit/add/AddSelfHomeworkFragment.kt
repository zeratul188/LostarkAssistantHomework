package com.lostark.lostarkassistanthomework.checklist.edit.add

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding4.widget.textChanges
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.IconSelectDialog
import com.lostark.lostarkassistanthomework.databinding.FragmentAddSelfBinding
import com.lostark.lostarkassistanthomework.objects.EditData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class AddSelfHomeworkFragment(
    private val myCompositeDisposable: CompositeDisposable
) : Fragment() {
    private lateinit var binding: FragmentAddSelfBinding
    private var edtNameSubscription: Disposable? = null

    private var ic = "hw1"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_add_homework, null, false)
        //val view : View = inflater.inflate(R.layout.fragment_add_self, container, false)

        val ends = requireActivity().resources.getStringArray(R.array.ends)
        val endAdapter = ArrayAdapter(requireContext(), R.layout.txt_item_end, ends)
        binding.sprEnds.adapter = endAdapter

        val edtNameChangeObservable = binding.edtName.textChanges()
        edtNameSubscription = edtNameChangeObservable
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = {
                    with(binding) {
                        if (edtName.text.toString().indexOf(",") != -1) {
                            edtName.setText(edtName.text.toString().replace(",", ""))
                            val toast = CustomToast(requireContext())
                            toast.createToast("\",\"기호는 사용하실 수 없습니다.", false)
                            toast.show()
                        }
                    }
                },
                onComplete = {

                },
                onError = {
                    Log.d("RX(edtName)", "Error : $it")
                }
            )
        myCompositeDisposable.add(edtNameSubscription)

        binding.imgIcon.setOnClickListener {
            val iconDialog = IconSelectDialog(requireContext())
            iconDialog.setOnClickListener(object : IconSelectDialog.OnDialogClickListener {
                override fun onClicked() {
                    val icon = iconDialog.getResult()
                    binding.imgIcon.setImageResource(requireActivity().resources.getIdentifier(icon.icon, "drawable", requireActivity().packageName))
                    ic = icon.icon
                }
            })
            iconDialog.show(true)
        }

        return binding.root
    }

    fun isEmpty(): Boolean = binding.edtName.text.toString() == ""

    fun getItem(): EditData {
        with(binding) {
            val name = edtName.text.toString()
            var max = 1
            if (edtMax.text.toString() != "") {
                max = edtMax.text.toString().toInt()
            }
            val end = sprEnds.selectedItem.toString()
            return EditData(name, 0, max, ic, end)
        }
    }
}