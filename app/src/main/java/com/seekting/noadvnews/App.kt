package com.seekting.noadvnews

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.seekting.noadvnews.dao.DaoMaster
import com.seekting.noadvnews.dao.DaoSession


/**
 * Created by Administrator on 2017/10/10.
 */
class App : Application() {
     lateinit var mHelper: DaoMaster.DevOpenHelper
     lateinit var db: SQLiteDatabase
     lateinit var mDaoMaster: DaoMaster
     lateinit var mDaoSession: DaoSession
    override fun onCreate() {
        super.onCreate()
        App.app = this
        setDataBase()

    }

    companion object {
        lateinit var app: App
    }

    private fun setDataBase() {
        mHelper = DaoMaster.DevOpenHelper(this, "news-db")
        db = mHelper.writableDatabase
        mDaoMaster = DaoMaster(db)
        mDaoSession = mDaoMaster.newSession()

    }


}