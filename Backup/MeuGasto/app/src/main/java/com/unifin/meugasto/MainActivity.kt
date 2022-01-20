package com.unifin.meugasto

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.view.*
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val format = NumberFormat.getCurrencyInstance()
        val retornoObter:Gasto = this.Obter()
        var saldo=retornoObter.nodeSaldoAtual
        var SaldoFormat = (saldo)!!.toDouble()!!.div(100)

        val mainSaldoAtual: TextView = findViewById<TextView>(R.id.mainSaldoAtual)
        mainSaldoAtual.text = format.format(SaldoFormat).toString()

    }

    override fun onRestart() {
        super.onRestart()
        setContentView(R.layout.activity_main)

        val format = NumberFormat.getCurrencyInstance()
        val retornoObter:Gasto = this.Obter()
        var saldo=retornoObter.nodeSaldoAtual
        var SaldoFormat = (saldo)!!.toDouble()!!.div(100)

        val mainSaldoAtual: TextView = findViewById<TextView>(R.id.mainSaldoAtual)
        mainSaldoAtual.text = format.format(SaldoFormat).toString()
    }

    fun OpenIncluirSaque(view: View){
        var intent= Intent(this, IncluirActivity::class.java)
        intent.putExtra("saque", true)
        intent.putExtra("saldoAtual", findViewById<TextView>(R.id.mainSaldoAtual).text)
        startActivity(intent)
    }

    fun OpenIncluirDespesa(view: View){
        var intent= Intent(this, IncluirActivity::class.java)
        intent.putExtra("saque", false)
        intent.putExtra("saldoAtual", findViewById<TextView>(R.id.mainSaldoAtual).text)
        startActivity(intent)

    }

    fun OpenListar(view: View){
        var intent= Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    fun Obter(): Gasto{
        var database=Database(this)
        val projections= arrayOf("ID","SaldoAtual","DataAtu")
        val selectionArgs= arrayOf("%")

        if(database.Count()){
            val cursor=database.Query(projections,"SaldoAtual like ?",selectionArgs,"ID DESC, DataAtu DESC")

            cursor.moveToFirst()

            val SaldoAtual=cursor.getInt(cursor.getColumnIndex("SaldoAtual"))

            val gasto = Gasto(0,0,SaldoAtual,"","")
            return gasto
        }else{
            val gasto = Gasto(0,0,0,"","")
            return gasto
        }
    }
}
