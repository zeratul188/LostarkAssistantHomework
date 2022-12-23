package com.lostark.lostarkassistanthomework.checklist.edit

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.widget.*
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.lostark.lostarkassistanthomework.*
import com.lostark.lostarkassistanthomework.checklist.objects.SearchData
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.databinding.ActivityEditBinding
import com.lostark.lostarkassistanthomework.settings.CheckDialog
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.jsoup.Jsoup

class EditActivity : AppCompatActivity() {
    private val viewModel: EditViewModel by viewModels()
    private var myCompositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityEditBinding
    private val context = this

    lateinit var homework: Homework
    lateinit var data: SearchData
    val homeworkDB = HomeworkDatabase.getInstance(this)!!
    val NOTIFYED = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //setContentView(R.layout.activity_edit)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.editViewModel = viewModel

        homework = intent.getSerializableExtra("homework") as Homework
        viewModel.homework.value = homework

        val jobs = resources.getStringArray(R.array.job)
        val jobAdapter = ArrayAdapter(this, R.layout.txt_item_job, jobs)
        val servers = resources.getStringArray(R.array.servers)
        val serverAdapter = ArrayAdapter(this, R.layout.txt_item_job, servers)

        with(binding) {
            toolBar.title = "${homework.name}의 정보 수정"
            toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
            //toolBar.setNavigationIcon(R.drawable.icon_resize)
            setSupportActionBar(toolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            sprJobs.adapter = jobAdapter
            sprServers.adapter = serverAdapter

            swtAuto.isChecked = homework.auto
            try {
                sprServers.setSelection(servers.indexOf(homework.server))
            } catch (e: Exception) {
                sprServers.setSelection(0)
                e.printStackTrace()
            }
            try {
                sprJobs.setSelection(jobs.indexOf(homework.job))
            } catch (e: Exception) {
                sprJobs.setSelection(0)
                e.printStackTrace()
            }

            val pagerAdapter = EditPagerAdapter(homework, context, supportFragmentManager, myCompositeDisposable)
            pagerMain.adapter = pagerAdapter

            pagerMain.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    bottomNavigationView.menu.getItem(position).isChecked = true
                }

                override fun onPageSelected(position: Int) {
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

            bottomNavigationView.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.action_day -> {
                        pagerMain.currentItem = 0
                    }
                    R.id.action_week -> {
                        pagerMain.currentItem = 1
                    }
                    R.id.action_rest -> {
                        pagerMain.currentItem = 2
                    }
                }
                return@setOnItemSelectedListener true
            }

            btnSearch.setOnClickListener {
                val loadingDialog = LoadingDialog(context)
                loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                loadingDialog.setCancelable(false)
                loadingDialog.show()
                val handler = MyHandler()
                data = SearchData(0.0, "", "")
                val thread = EditCloringThread(context, loadingDialog, data, handler, edtName.text.toString())
                thread.start()
            }

            btnDelete.setOnClickListener {
                val dialog = CheckDialog(context)
                dialog.setData("${homework.name}의 정보를 삭제하시겠습니까?", "삭제", true)
                dialog.setOnClickListener(object : CheckDialog.OnDialogClickListener {
                    override fun onClicked() {
                        val toast = CustomToast(App.context())
                        toast.createToast("${homework.name}의 정보를 삭제하였습니다.", false)
                        toast.show()
                        homeworkDB.homeworkDao().delete(homework)
                        finish()
                    }
                })
                dialog.show(true)
            }

            btnApply.setOnClickListener {
                val toast = CustomToast(App.context())
                if (edtLevel.text.toString() == "") {
                    toast.createToast("아이템 레벨을 입력해주세요.", false)
                    toast.show()
                    return@setOnClickListener
                }
                if (edtName.text.toString() == "") {
                    toast.createToast("캐릭터 이름을 입력해주세요.", false)
                    toast.show()
                    return@setOnClickListener
                }
                homework.name = edtName.text.toString()
                homework.level = edtLevel.text.toString().toDouble()
                homework.server = sprServers.selectedItem.toString()
                homework.job = sprJobs.selectedItem.toString()
                homework.auto = swtAuto.isChecked
                homework.dungeonrest = pagerAdapter.dungeon
                homework.bossrest = pagerAdapter.boss
                homework.questrest = pagerAdapter.quest
                homework.dungeonlost = pagerAdapter.lostDungeon
                homework.bosslost = pagerAdapter.lostBoss
                homework.questlost = pagerAdapter.lostQuest
                homework.daylist = pagerAdapter.getNames("일일")
                homework.daynows = pagerAdapter.getNows("일일")
                homework.daymaxs = pagerAdapter.getMaxs("일일")
                homework.dayicons = pagerAdapter.getIcons("일일")
                homework.dayends = pagerAdapter.getEnds("일일")
                homework.weeklist = pagerAdapter.getNames("주간")
                homework.weeknows = pagerAdapter.getNows("주간")
                homework.weekmaxs = pagerAdapter.getMaxs("주간")
                homework.weekicons = pagerAdapter.getIcons("주간")
                homework.weekends = pagerAdapter.getEnds("주간")
                homeworkDB.homeworkDao().update(homework)
                toast.createToast("${homework.name}의 정보를 수정하였습니다.", false)
                toast.show()
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        myCompositeDisposable.clear()
        super.onDestroy()
    }

    inner class MyHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                NOTIFYED -> {
                    with(binding) {
                        val jobs = resources.getStringArray(R.array.job)
                        val servers = resources.getStringArray(R.array.servers)
                        edtLevel.setText(data.level.toString())
                        try {
                            sprServers.setSelection(servers.indexOf(data.server))
                        } catch (e: Exception) {
                            sprServers.setSelection(0)
                            e.printStackTrace()
                        }
                        try {
                            sprJobs.setSelection(jobs.indexOf(data.job))
                        } catch (e: Exception) {
                            sprJobs.setSelection(0)
                            e.printStackTrace()
                        }
                    }
                }
                else -> {

                }
            }
        }
    }
}

class EditCloringThread(
    private val context: Context,
    private val loadingDialog: LoadingDialog,
    private val data: SearchData,
    private val handler: Handler,
    private val homework_name: String
) : Thread() {
    override fun run() {
        try {
            var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/${homework_name}").get()
            var level_element = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)")
            if (level_element.text() != "") {
                var level_str = level_element.text()
                level_str = level_str.replace("Lv.", "")
                level_str = level_str.replace(",", "")
                var level = level_str.toDouble()
                var server_element = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > span.profile-character-info__server")
                var server = server_element.text()
                server = server.replace("@", "")
                var job_element = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > img")
                var job = job_element.attr("alt")
                data.level = level
                data.server = server
                data.job = job
            }
            loadingDialog.dismiss()
            var message = Message.obtain()
            message.what = 1
            handler.sendMessage(message)
        } catch (e: Exception) {
            e.printStackTrace()
            loadingDialog.dismiss()
        }
    }
}