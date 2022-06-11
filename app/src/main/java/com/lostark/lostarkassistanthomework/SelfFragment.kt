package com.lostark.lostarkassistanthomework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment

class SelfFragment : Fragment() {
    lateinit var edtLevel: EditText
    lateinit var edtServer: EditText
    lateinit var sprJobs: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_self, container, false)

        edtLevel = view.findViewById(R.id.edtLevel)
        edtServer = view.findViewById(R.id.edtServer)
        sprJobs = view.findViewById(R.id.sprJobs)

        val jobs = requireActivity().resources.getStringArray(R.array.job)
        val jobAdapter = ArrayAdapter<String>(requireContext(), R.layout.txt_item_job, jobs)
        sprJobs.adapter = jobAdapter

        return view
    }
}