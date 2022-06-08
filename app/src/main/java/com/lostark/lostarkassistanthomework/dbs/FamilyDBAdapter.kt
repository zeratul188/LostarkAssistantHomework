package com.lostark.lostarkassistanthomework.dbs

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.drawable.Drawable
import com.lostark.lostarkassistanthomework.checklist.objects.ShareChecklist
import com.lostark.lostarkassistanthomework.dbs.sys.LoadDBAdapter
import java.sql.SQLException

class FamilyDBAdapter {
    val tag: String = "FamilyDBAdapter"
    val table_name: String = "FAMILY"

    var context: Context
    lateinit var db: SQLiteDatabase
    lateinit var loader: LoadDBAdapter
    var loadDBAdapter: LoadDBAdapter

    constructor(context: Context) {
        this.context = context
        loadDBAdapter = LoadDBAdapter("family", context)
    }

    fun open(): FamilyDBAdapter {
        try {
            loadDBAdapter.open()
            loadDBAdapter.close()
            db = loadDBAdapter.readableDatabase
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return this
    }

    fun close() {
        loadDBAdapter.close()
    }

    fun getItems(type: String): ArrayList<ShareChecklist> {
        val list: ArrayList<ShareChecklist> = ArrayList()
        try {
            val sql: String = "SELECT * FROM ${table_name}"

            val cursor: Cursor = db.rawQuery(sql, null)
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(5) == type) {
                        var name: String = cursor.getString(1)
                        var max: Int = cursor.getInt(2)
                        var end: String = cursor.getString(3)
                        var icon_str: String = cursor.getString(4)
                        var icon: Drawable = context.resources.getDrawable(context.resources.getIdentifier("${icon_str}", "drawable", context.packageName))

                        val checklist: ShareChecklist = ShareChecklist(name, icon, 0, max, end)
                        list.add(checklist)
                    }

                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return list
    }
}