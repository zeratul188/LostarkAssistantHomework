package com.lostark.lostarkassistanthomework.checklist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.lostark.lostarkassistanthomework.R

class InformationActivity : AppCompatActivity() {
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
    private lateinit var progressEquip: Array<ProgressBar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)
    }
}