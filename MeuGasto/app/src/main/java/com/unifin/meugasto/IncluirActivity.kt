package com.unifin.meugasto

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_incluir.*
import java.text.NumberFormat

class IncluirActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incluir)

        val valor: EditText = findViewById(R.id.Valor)

        valor.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun afterTextChanged(s: Editable) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString() != this.current) {
                    valor.removeTextChangedListener(this)
                    val cleanString = s.toString().replace("[,.]".toRegex(), "")
                    val parsed = java.lang.Double.parseDouble(cleanString)
                    val formatted = NumberFormat.getCurrencyInstance().format(parsed / 100)
                    current = formatted.replace("[R$]".toRegex(), "")
                    valor.setText(current)
                    valor.setSelection(current.length)
                    valor.addTextChangedListener(this)
                }
            }

        })

        val desc = findViewById<TextView>(R.id.Descricao)
        if(intent.getBooleanExtra("saque", true)){
            desc.setText("Registro de Saque");
            desc.isEnabled = false
        }
    }

    fun Obter(): Gasto{
        var database=Database(this)
        val projections= arrayOf("ID","SaldoInicial","SaldoAtual","Descricao","DataAtu")
        val selectionArgs= arrayOf("%")

        if(database.Count()){
            val cursor=database.Query(projections,"SaldoAtual like ?",selectionArgs,"ID DESC, DataAtu DESC")

            cursor.moveToFirst()

            val ID=cursor.getInt(cursor.getColumnIndex("ID"))
            val SaldoInicial=cursor.getInt(cursor.getColumnIndex("SaldoInicial"))
            val SaldoAtual=cursor.getInt(cursor.getColumnIndex("SaldoAtual"))
            val Descricao=cursor.getString(cursor.getColumnIndex("Descricao"))
            val DataAtu=cursor.getString(cursor.getColumnIndex("DataAtu"))

            val gasto = Gasto(ID,SaldoInicial,SaldoAtual,Descricao,DataAtu)
            return gasto
        }else{
            val gasto = Gasto(0,0,0,"","")
            return gasto
        }
    }

    fun incluir(view: View){
        val valor: String = Valor.text.toString().replace("[.,]".toRegex(), "")
        var database=Database(this)
        val retornoObter:Gasto = this.Obter()
        val SaldoAtual=retornoObter.nodeSaldoAtual
        var valorOperacao:Int
        var retornoID:Long
        val valorTemConteudo:Boolean = Valor.text.toString().isNotEmpty()

        if(valorTemConteudo){
            if(intent.getBooleanExtra("saque", true)){
                valorOperacao = SaldoAtual !!+ valor.toInt()
            }else{

                valorOperacao = SaldoAtual !!- valor.toInt()
            }

            var values= ContentValues()

            values.put("SaldoInicial", SaldoAtual.toString())
            values.put("SaldoAtual", valorOperacao.toString())
            if(Descricao.text.toString().isNotEmpty()){
                values.put("Descricao", Descricao.text.toString())
            }else{
                values.put("Descricao","Sem Descrição")
            }

            retornoID = database.Insert(values)

        }else{
            Valor.error = "Campo Obrigatório"
            retornoID = 0
        }

        if (retornoID > 0) {
            Toast.makeText(this, " Operação Registrada ", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, " Erro na Operação ", Toast.LENGTH_LONG).show()
        }
    }
}

