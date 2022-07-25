package es.ipow.sqlite

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import es.ipow.sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainBinding
    private lateinit var friendsDBHelper : mySQLiteHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        friendsDBHelper = mySQLiteHelper(this)

        b.btnSave.setOnClickListener {
            if (b.etName.text.isNotBlank() &&
                b.etMail.text.isNotBlank()){
                friendsDBHelper.addData(b.etName.text.toString(),
                                        b.etMail.text.toString())
                b.etName.text.clear()
                b.etMail.text.clear()
                hideKeyBoard()
                toast("¡Guardado!")

            } else {
                toast("No se ha podido guardar", Toast.LENGTH_LONG)
            }
        }

        b.btnQuery.setOnClickListener {
            b.tvRequest.text = ""
            // Abro la base de datos en modo LECTURA
            val db : SQLiteDatabase = friendsDBHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM friends", null)

            // Compruebo si hay algún registro
            if (cursor.moveToFirst()) {
                do {
                    b.tvRequest.append(cursor.getInt(0).toString() + ": ")
                    b.tvRequest.append(cursor.getString(1).toString() + ", ")
                    b.tvRequest.append(cursor.getString(2).toString() + "\n ")
                } while (cursor.moveToNext())
            }
            hideKeyBoard()
            cursor.close()
        }

        b.btnDelete.setOnClickListener {
            var affected = 0
            if (b.etId.text.isNotBlank()){
                affected = friendsDBHelper.deleteData(b.etId.text.toString().toInt())
                b.etId.text.clear()
                b.etName.text.clear()
                b.etMail.text.clear()
                hideKeyBoard()
                toast("¡Borrado!")
            }
            toast("Datos borrados: $affected", Toast.LENGTH_LONG)
        }

        b.btnUpdate.setOnClickListener {
            if (b.etId.text.isNotBlank() &&
                b.etName.text.isNotBlank() &&
                b.etMail.text.isNotBlank()){
                friendsDBHelper.updateData(
                    b.etId.text.toString().toInt(),
                    b.etName.text.toString(),
                    b.etMail.text.toString())
                b.etId.text.clear()
                b.etName.text.clear()
                b.etMail.text.clear()
                hideKeyBoard()
                toast("¡Modificado!")

            } else {
                toast("No permitido campos vacíos", Toast.LENGTH_LONG)
            }
        }
    }

    fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(b.constraintLayoutMain.windowToken, 0)
    }
}