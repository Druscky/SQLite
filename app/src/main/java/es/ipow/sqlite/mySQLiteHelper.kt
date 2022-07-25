package es.ipow.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class mySQLiteHelper(context: Context) : SQLiteOpenHelper(context, "AddressBook.db", null, 1) {

    // Creo una tabla utilizando un comando SQL
    override fun onCreate(db: SQLiteDatabase?) {
        // Esto debe hacerse dentro de un bloque try-catch
        val commandCreate = "CREATE TABLE friends " +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT)"
        db!!.execSQL(commandCreate)
    }
    // Se llamará a esta función cuando se actualice la versión de la DB
    // Por ejemplo cuando hay que añadir más campos
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Ejemplo
        val commandDelete = "DROP TABLE IF EXISTS friends"
        db!!.execSQL(commandDelete)
        onCreate(db)
    }

    // Creamos una función para AÑADIR datos
    fun addData (name: String, email: String) {
        // ContentValues tiene una estructura de tipo Map()
        val data = ContentValues()
        data.put("name", name)
        data.put("email", email)
        // Abro la DB en modo ESCRITURA
        val db = this.writableDatabase
        db.insert("friends", null,data)
        db.close()
    }

    // Creamos una función para BORRAR datos
    fun deleteData (id: Int) : Int {
        val args = arrayOf(id.toString())

        val db = this.writableDatabase
        // La ejecución de este comando devuelve el número de registros afectados
        val affectedRows = db.delete("friends", "_id = ?",args)
        // Alternativamente. Pero esta forma no devuelve nada
        // db.execSQL("DELETE FROM friends WHERE _id = ?", args)
        db.close()
        return affectedRows
    }

    // Creamos una función para MODIFICAR datos
    fun updateData (id:Int, name: String, email: String) {
        val args = arrayOf(id.toString())

        // ContentValues tiene una estructura de tipo Map()
        val data = ContentValues()
        data.put("name", name)
        data.put("email", email)
        // Abro la DB en modo ESCRITURA
        val db = this.writableDatabase
        // La ejecución de este comando devuelve el número de registros afectados
        db.update("friends", data, "_id = ?",args)
        db.close()
    }

}