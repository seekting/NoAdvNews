package com.seekting.noadvnews

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import android.util.Log
import com.bumptech.glide.Glide
import com.seekting.noadvnews.dao.DaoMaster
import com.seekting.noadvnews.dao.DaoSession
import java.io.File
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

const val ACTION_DUMP_DB = "com.seekting.noadvnews.dumpDB"
const val DATA_BASE_NAME = "news-db.db"
const val CORE_THREAD_SIZE = 10
const val MAX_THREAD_SIZE = 20
var mExecutor: ExecutorService? = null

/**
 * Created by Administrator on 2017/10/10.
 */
class App : Application() {
    lateinit var mHelper: DaoMaster.DevOpenHelper
    lateinit var db: SQLiteDatabase
    lateinit var mDaoMaster: DaoMaster
    lateinit var mDaoSession: DaoSession
    lateinit var dbOutputReceiver: BroadcastReceiver


    override fun onCreate() {
        super.onCreate()
        App.app = this
        setDataBase()
        Glide.get(this).register(NoUrlEncodeUrl::class.java, InputStream::class.java, NoUrlEncodeLoaderFactory())
        dbOutputReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent?.action ?: ""
                when (action) {
                    ACTION_DUMP_DB -> dumpDB()
                }
            }
        }
        val filter = IntentFilter()
        filter.addAction(ACTION_DUMP_DB)
        registerReceiver(dbOutputReceiver, filter)
        mExecutor = ThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE,
                0L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue())

    }

    private fun dumpDB() {
        val databases = getDatabasePath(DATA_BASE_NAME)
        val sdcard: File = Environment.getExternalStorageDirectory()
        val output = File(sdcard, DATA_BASE_NAME)
        databases.copyTo(output, overwrite = true)
        Log.d("seekting", "dumpDB:${output.exists()}")
    }

    companion object {
        lateinit var app: App
    }

    private fun setDataBase() {
        mHelper = DaoMaster.DevOpenHelper(this, DATA_BASE_NAME)
        db = mHelper.writableDatabase
        mDaoMaster = DaoMaster(db)
        mDaoSession = mDaoMaster.newSession()


    }


}

public fun threadpool(block: () -> Unit) {
    mExecutor?.execute(Runnable {
        block()
    })

}