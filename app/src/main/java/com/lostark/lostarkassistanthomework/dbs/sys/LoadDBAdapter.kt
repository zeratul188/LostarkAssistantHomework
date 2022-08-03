package com.lostark.lostarkassistanthomework.dbs.sys

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.*

class LoadDBAdapter: SQLiteOpenHelper{
    var name: String = "null"
    var context: Context
    val root_dir: String = "/data/data/com.lostark.lostarkassistanthomework/databases/"

    lateinit var mDatabase: SQLiteDatabase

    constructor(name: String, context: Context) : super(context, "${name}.db", null, 1) { //version
        this.name = name
        this.context = context
        databaseCheck()
    }

    fun databaseCheck() {
        val dbFile = File("${root_dir}${name}.db")
        if (!dbFile.exists()) {
            /*this.readableDatabase
            this.close()*/
            dbCopy()
        }
    }

    fun dbCopy() {
        try {
            val folder = File(root_dir)
            if (!folder.exists()) {
                folder.mkdir()
            }
            val inputStream: InputStream = context.assets.open("${name}.db")
            val filepath: String = "${root_dir}${name}.db"
            val outputStream: OutputStream = FileOutputStream(filepath)
            val buffer: ByteArray = ByteArray(1024)
            var length: Int = inputStream.read(buffer)
            while (length > 0) {
                outputStream.write(buffer, 0, length)
                length = inputStream.read(buffer)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun open(): Boolean {
        val path: String = "${root_dir}${name}.db"
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDatabase != null
    }

    override fun close() {
        if (mDatabase != null) {
            mDatabase.close()
        }
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            when (oldVersion) {
                1 -> {

                }
            }
        }
    }

    /*fun setDB(ctx: Context): Boolean {
        val folder :File = File(root_dir)
        if (!folder.exists()) {
            folder.mkdir()
        }
        val assetManager: AssetManager = ctx.resources.assets;
        val outfile: File = File("${root_dir}${name}.db")
        try {
            val inputStream: InputStream = assetManager.open("${name}.db", AssetManager.ACCESS_BUFFER)
            val filesize: Int = inputStream.available()
            if (outfile.length() <= 0) {
                val tempdata: ByteArray = ByteArray(filesize)
                inputStream.read(tempdata)
                inputStream.close()
                outfile.createNewFile()
                val outputStream: OutputStream = FileOutputStream(outfile)
                outputStream.write(tempdata)
                outputStream.close()
            } else {
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
        return true
    }*/
}