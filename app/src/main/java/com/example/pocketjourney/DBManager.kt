package com.example.pocketjourney

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DBManager (val context: Context) {
    private lateinit var helper: DbHelper
    private lateinit var db:SQLiteDatabase

    fun open():DBManager{
        helper= DbHelper(context)
        db=helper.writableDatabase
        return this
    }

    fun close(){
        helper.close()
    }

    fun insert_utente(nome:String,cognome:String,email:String,pw:String,telefono:String){
        val values=ContentValues().apply {
            put(DbHelper.NOME,nome)
            put(DbHelper.COGNOME,cognome)
            put(DbHelper.EMAIL,email)
            put(DbHelper.PASSWORD,pw)
            put(DbHelper.NUMEROCELLULARE,telefono)
        }
        val newRowId=db.insert(DbHelper.TABLE_NAME,null,values)
    }

    fun updateUtente(_id:Long,nome:String,cognome:String,email:String,pw:String,cell:String):Int{
        val selezione= "${DbHelper._ID}=?"
        val selectionArgs=arrayOf(_id.toString())

        val values=ContentValues().apply {
            put(DbHelper.EMAIL,email)
            put(DbHelper.PASSWORD,pw)
            put(DbHelper.NUMEROCELLULARE,cell)
        }
        val rit=db.update(DbHelper.TABLE_NAME,values,selezione,selectionArgs)
        return rit
    }

    fun updateEmailUtente(email:String):Int{
        val selezione= "${DbHelper.EMAIL}=?"
        val selectionArgs=arrayOf(email.toString())

        val values=ContentValues().apply {
            put(DbHelper.EMAIL,email)
        }
        val rit=db.update(DbHelper.TABLE_NAME,values,selezione,selectionArgs)
        return rit
    }

    fun deleteUtente(_id: Long) {
        val selection = "${DbHelper._ID}=?"
        val selectionArgs = arrayOf(_id.toString())
        db.delete(DbHelper.TABLE_NAME, selection, selectionArgs)
    }

    //con la fetchAll ottengo tutte le risposte di una query
    fun fetchAll():Cursor {
        val projection = arrayOf(DbHelper._ID, DbHelper.NOME, DbHelper.COGNOME,DbHelper.EMAIL,DbHelper.PASSWORD,DbHelper.NUMEROCELLULARE,DbHelper.REF_DATI_PAGAMENTO,DbHelper.REF_RECENSISCE,DbHelper.REF_PRENOTA)
        val cursor = db.query(
            DbHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }
}