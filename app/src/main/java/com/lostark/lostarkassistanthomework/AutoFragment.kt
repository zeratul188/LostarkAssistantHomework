package com.lostark.lostarkassistanthomework

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.checklist.RecyclerViewDecoration
import com.lostark.lostarkassistanthomework.objects.Chracter
import org.jsoup.Jsoup

class AutoFragment : Fragment() {
    lateinit var listAuto: RecyclerView
    lateinit var btnSearch: Button
    lateinit var edtName: EditText

    val chracters = ArrayList<Chracter>()
    lateinit var chracterAdapter: ChracterRecylerAdapter

    val NOTIFYED = 1

    fun setEditName(edtName: EditText) {
        this.edtName = edtName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_auto, container, false)

        listAuto = view.findViewById(R.id.listAuto)
        btnSearch = view.findViewById(R.id.btnSearch)

        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        loadingDialog.setCancelable(false)

        val handler = ChracterHandler()

        btnSearch.setOnClickListener {
            chracters.clear()
            loadingDialog.show()
            val thread = CloringThread(edtName, requireContext(), loadingDialog, chracters, handler)
            thread.start()
        }

        chracterAdapter = ChracterRecylerAdapter(chracters, requireContext())
        listAuto.adapter = chracterAdapter
        listAuto.addItemDecoration(RecyclerViewDecoration(0, 10))

        return view
    }

    inner class ChracterHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                NOTIFYED -> {
                    chracterAdapter.notifyDataSetChanged()
                }
                else -> {

                }
            }
        }
    }
}

class CloringThread(
    private val edtName: EditText,
    private val context: Context,
    private val loadingDialog: LoadingDialog,
    private val chracters: ArrayList<Chracter>,
    private val handler: Handler
) : Thread() {
    override fun run() {
        var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+edtName.text.toString()).get()

        var elements = doc.select("#expand-character-list > ul")
        for (element in elements) {
            val result = element.select("span > button > span")
            val lists = result.text().split(" ")
            for (name in lists) {
                var single_doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/${name}").get()
                var level_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)")
                var level_str = level_element.text()
                level_str = level_str.replace("Lv.", "")
                level_str = level_str.replace(",", "")
                var level = level_str.toDouble()
                var server_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > span.profile-character-info__server")
                var server = server_element.text()
                server = server.replace("@", "")
                var job_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > img")
                var job = job_element.attr("alt")
                /*println("================================================================")
                println("Name : ${name}\nLevel : ${level}\nServer : ${server}\nJob : ${job}")
                println("================================================================")*/
                val jobs = context.resources.getStringArray(R.array.job)
                if (jobs.indexOf(job) != -1) {
                    chracters.add(Chracter(name, level, server, job, false))
                }
            }
        }
        chracters.sort()
        loadingDialog.dismiss()
        var message = Message.obtain()
        message.what = 1
        handler.sendMessage(message)
    }
}