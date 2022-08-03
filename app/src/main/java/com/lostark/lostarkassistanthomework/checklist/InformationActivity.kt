package com.lostark.lostarkassistanthomework.checklist

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.CustomToast
import com.lostark.lostarkassistanthomework.LoadingDialog
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.Chracter
import org.jsoup.Jsoup

class InformationActivity : AppCompatActivity() {
    private lateinit var toolBar: Toolbar

    private val equipLength = 6
    private val itemLength = 5
    private val statLength = 6

    private var familyLevel = ""
    private var battleLevel = ""
    private var server = ""
    private var level = ""
    private var objectLevel = ""
    private var job = ""
    private var title = ""
    private var guild = ""
    private var pvp = ""
    private var levelGround = ""
    private var ground = ""
    private var attact = ""
    private var health = ""
    private var spiecialTool = ""
    //Equip Image
    private val equipLevels = Array(equipLength) { "" }
    private val equipStatues = Array(equipLength) { 0 }
    private val equipNames = Array(equipLength) { "" }
    //Item Image
    private val itemStatues = Array(itemLength) { 0 }
    private val itemNames = Array(itemLength) { "" }
    private val itemEffects = Array(itemLength) { "" }
    private val itemStamps = Array(itemLength) { "" }
    //Arm Image
    private var arm = ""
    private var armEffect = ""
    private var stone = ""
    private var stoneStamp = ""
    private val stats = Array(statLength) { "" }
    private var stamps = ""

    private lateinit var txtFamilyLevel: TextView
    private lateinit var txtBattleLevel: TextView
    private lateinit var txtServer: TextView
    private lateinit var txtLevel: TextView
    private lateinit var txtObjectLevel: TextView
    private lateinit var txtJob: TextView
    private lateinit var txtTitle: TextView
    private lateinit var txtGuild: TextView
    private lateinit var txtPvP: TextView
    private lateinit var txtLevelGround: TextView
    private lateinit var txtGround: TextView
    private lateinit var txtAttack: TextView
    private lateinit var txtHealth: TextView
    private lateinit var txtSpecialTool: TextView

    private lateinit var imgEquips: Array<ImageView>
    private lateinit var txtLevelEquips: Array<TextView>
    private lateinit var txtStatueEquips: Array<TextView>
    private lateinit var progressEquips: Array<ProgressBar>
    private lateinit var txtNameEquips: Array<TextView>

    private lateinit var imgItems: Array<ImageView>
    private lateinit var txtStatueItems: Array<TextView>
    private lateinit var progressItems: Array<ProgressBar>
    private lateinit var txtItems: Array<TextView>
    private lateinit var txtEffectItems: Array<TextView>
    private lateinit var txtStampItems: Array<TextView>
    private lateinit var imgArm: ImageView
    private lateinit var txtArm: TextView
    private lateinit var txtEffectArm: TextView
    private lateinit var imgStone: ImageView
    private lateinit var txtStone: TextView
    private lateinit var txtStampStone: TextView

    private lateinit var txtStats: Array<TextView>
    private lateinit var txtStamp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        val name = intent.getStringExtra("name")

        toolBar = findViewById(R.id.toolBar)
        toolBar.setTitle("${name}의 정보")
        toolBar.setTitleTextColor(resources.getColor(R.color.main_font))
        //toolBar.setNavigationIcon(R.drawable.icon_resize)
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        txtFamilyLevel = findViewById(R.id.txtFamilyLevel)
        txtBattleLevel = findViewById(R.id.txtBattleLevel)
        txtServer = findViewById(R.id.txtServer)
        txtLevel = findViewById(R.id.txtLevel)
        txtObjectLevel = findViewById(R.id.txtObjectLevel)
        txtJob = findViewById(R.id.txtJob)
        txtTitle = findViewById(R.id.txtTitle)
        txtGuild = findViewById(R.id.txtGuild)
        txtPvP = findViewById(R.id.txtPvP)
        txtLevelGround = findViewById(R.id.txtLevelGround)
        txtGround = findViewById(R.id.txtGround)
        txtAttack = findViewById(R.id.txtAttack)
        txtHealth = findViewById(R.id.txtHealth)
        txtSpecialTool = findViewById(R.id.txtSpecialTool)
        imgArm = findViewById(R.id.imgArm)
        txtArm = findViewById(R.id.txtArm)
        txtEffectArm = findViewById(R.id.txtEffectArm)
        imgStone = findViewById(R.id.imgStone)
        txtStone = findViewById(R.id.txtStone)
        txtStampStone = findViewById(R.id.txtStampStone)
        txtStamp = findViewById(R.id.txtStamp)
        txtStats = Array(statLength) { i ->
            findViewById(resources.getIdentifier("txtStat${i+1}", "id", packageName))
        }
        imgEquips = Array(equipLength) { i ->
            findViewById(resources.getIdentifier("imgEquip${i+1}", "id", packageName))
        }
        txtLevelEquips = Array(equipLength) { i ->
            findViewById(resources.getIdentifier("txtLevelEquip${i+1}", "id", packageName))
        }
        txtStatueEquips = Array(equipLength) { i ->
            findViewById(resources.getIdentifier("txtStatueEquip${i+1}", "id", packageName))
        }
        progressEquips = Array(equipLength) { i ->
            findViewById(resources.getIdentifier("progressEquip${i+1}", "id", packageName))
        }
        txtNameEquips = Array(equipLength) { i ->
            findViewById(resources.getIdentifier("txtNameEquip${i+1}", "id", packageName))
        }
        imgItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("imgItem${i+1}", "id", packageName))
        }
        txtStatueItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("txtStatueItem${i+1}", "id", packageName))
        }
        progressItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("progressItem${i+1}", "id", packageName))
        }
        txtItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("txtItem${i+1}", "id", packageName))
        }
        txtEffectItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("txtEffectItem${i+1}", "id", packageName))
        }
        txtStampItems = Array(itemLength) { i ->
            findViewById(resources.getIdentifier("txtStampItem${i+1}", "id", packageName))
        }

        setData()
        val loadingDialog = LoadingDialog(this)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.setCancelable(false)
        val handler = ChracterHandler()
        loadingDialog.show()
        if (name != null) {
            val thread = CloringThread(name, this, loadingDialog, handler)
            thread.start()
        }
    }

    private fun setData() {
        txtFamilyLevel.text = familyLevel
        txtBattleLevel.text = battleLevel
        txtServer.text = server
        txtLevel.text = level
        txtObjectLevel.text = objectLevel
        txtJob.text = job
        txtTitle.text = title
        txtGuild.text = guild
        txtPvP.text = pvp
        txtLevelGround.text = levelGround
        txtGround.text = ground
        txtAttack.text = attact
        txtHealth.text = health
        txtSpecialTool.text = spiecialTool
        txtArm.text = arm
        txtEffectArm.text = armEffect
        txtStone.text = stone
        txtStampStone.text = stoneStamp
        txtStamp.text = stamps

        for (i in 0 until equipLength) {
            txtLevelEquips[i].text = equipLevels[i]
            txtStatueEquips[i].text = equipStatues[i].toString()
            progressEquips[i].progress = equipStatues[i]
            txtNameEquips[i].text = equipNames[i]
        }
        for (i in 0 until itemLength) {
            txtStatueItems[i].text = itemStatues[i].toString()
            progressItems[i].progress = itemStatues[i]
            txtItems[i].text = itemNames[i]
            txtEffectItems[i].text = itemEffects[i]
            txtStampItems[i].text = itemStamps[i]
        }
        for (i in 0 until statLength) {
            txtStats[i].text = stats[i]
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

    inner class CloringThread(
        private val chracterName: String,
        private val context: Context,
        private val loadingDialog: LoadingDialog,
        private val handler: Handler
    ) : Thread() {
        override fun run() {
            try {
                var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/$chracterName").get()

                familyLevel = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info > div.level-info__expedition > span:nth-child(2)").text()
                battleLevel = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info > div.level-info__item > span:nth-child(2)").text()
                server = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > span.profile-character-info__server").text().replace("@", "")
                level = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)").text()
                objectLevel = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__item > span:nth-child(2)").text()
                job = doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > img").attr("alt")
                title = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.game-info > div.game-info__title > span:nth-child(2)").text()
                guild = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.game-info > div.game-info__guild > span:nth-child(2)").text()
                pvp = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.game-info > div.level-info__pvp > span:nth-child(2)").text()
                levelGround = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.game-info > div.game-info__wisdom > span:nth-child(2)").text()
                ground = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.game-info > div.game-info__wisdom > span:nth-child(3)").text()
                var item1 = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.special-info > div > ul > li:nth-child(1) > div > span > p > font").text()
                var item2 = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.special-info > div > ul > li:nth-child(2) > div > span > p > font").text()
                var item3 = doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.special-info > div > ul > li:nth-child(3) > div > span > p > font").text()
                if (item1 == "") {
                    item1 = "-"
                }
                if (item2 == "") {
                    item2 = "-"
                }
                if (item3 == "") {
                    item3 = "-"
                }
                spiecialTool = "${item1}\n${item2}\n${item3}"
                attact = doc.select("#profile-ability > div.profile-ability-basic > ul > li:nth-child(1) > span:nth-child(2)").text()
                health = doc.select("#profile-ability > div.profile-ability-basic > ul > li:nth-child(2) > span:nth-child(2)").text()

                var headDoc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/BT%EB%B0%94%EB%93%A0%EC%89%90%ED%8D%BC").get()
                equipNames[0] = headDoc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)").text()


                loadingDialog.dismiss()
                var message = Message.obtain()
                message.what = 1
                handler.sendMessage(message)
            } catch (e: Exception) {
                e.printStackTrace()
                loadingDialog.dismiss()
                var message = Message.obtain()
                message.what = 2
                handler.sendMessage(message)
            }
        }
    }

    inner class ChracterHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                1 -> {
                    setData()
                }
                2 -> {
                    val toast = CustomToast(this@InformationActivity)
                    toast.createToast("정보를 불러오는데 문제가 발생하였습니다.", false)
                    toast.show()
                }
            }
        }
    }
}