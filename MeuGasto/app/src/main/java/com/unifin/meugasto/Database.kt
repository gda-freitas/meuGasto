package com.unifin.meugasto

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class  Database{
    val DB_NAME="db_gasto"
    val TB_GASTOS="tb_Gasto"

    val COL_ID="ID"
    var COL_SALDO_INICIAL="SaldoInicial"
    var COL_SALDO_ATUAL="SaldoAtual"
    var COL_DESCRICAO="Descricao"
    var COL_DATA_ATU="DataAtu"

    val DB_VERSION=1
    var sqlDB:SQLiteDatabase?=null

    val sqlCreateTable="CREATE TABLE IF NOT EXISTS "+ TB_GASTOS +" ("+
            COL_ID + " INTEGER PRIMARY KEY," + COL_SALDO_INICIAL + " INTEGER," + COL_SALDO_ATUAL + " INTEGER," + COL_DESCRICAO + " TEXT, " +
            COL_DATA_ATU + " DATETIME DEFAULT CURRENT_TIMESTAMP);"
    val sqlInsertPrim="INSERT INTO " + TB_GASTOS + "(" + COL_SALDO_INICIAL + ", " + COL_SALDO_ATUAL + ", " + COL_DESCRICAO +
            ") VALUES(0, 0, 'Registro Inicial')"

    //"CREATE TABLE IF NOT EXISTS db_gasto (ID INTEGER PRIMARY KEY,SaldoInicial TEXT, SaldoAtual TEXT, Descricao TEXT, DataAtu DATETIME DEFAULT CURRENT_TIMESTAMP)"
    constructor(context:Context){
        var db=DatabaseHelper(context)

        sqlDB=db.writableDatabase
    }

    inner class  DatabaseHelper:SQLiteOpenHelper{
        var context:Context?=null

        constructor(context:Context):super(context,DB_NAME,null,DB_VERSION){
            this.context=context
        }
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            //p0!!.execSQL(sqlInsertPrim)
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("Drop table IF EXISTS " + TB_GASTOS)
        }
    }

    fun Insert(values:ContentValues):Long{
        val ID= sqlDB!!.insert(TB_GASTOS,"",values)
        return ID
    }

    fun Query(projection:Array<String>,selection:String,selectionArgs:Array<String>,sorOrder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables=TB_GASTOS
        val cursor=qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
        return cursor
    }

    fun Count():Boolean{
        //Checa se tabela esta vazia
        val ccursor:Cursor = sqlDB!!.rawQuery("SELECT count(*) FROM "+ TB_GASTOS+ "", null)
        if(ccursor != null && ccursor.moveToFirst()){
            val icount = ccursor.getInt(0)
            if(icount == 0)
                return false
        }
        return true
    }


    fun Delete(selection:String,selectionArgs:Array<String>):Int{
        val count=sqlDB!!.delete(TB_GASTOS,selection,selectionArgs)
        return  count
    }

    fun Update(values:ContentValues,selection:String,selectionargs:Array<String>):Int{
        val count=sqlDB!!.update(TB_GASTOS,values,selection,selectionargs)
        return count
    }

}