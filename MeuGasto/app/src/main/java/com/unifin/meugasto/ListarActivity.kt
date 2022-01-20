package com.unifin.meugasto

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_listar.*
import kotlinx.android.synthetic.main.detalhe_item.view.*
import java.text.NumberFormat
import java.util.*

class ListarActivity : AppCompatActivity() {
    var ListGastos= ArrayList<Gasto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        LoadQuery("%") //parâmetro para filtrar
    }

    fun LoadQuery(filtro:String){
        var database=Database(this)
        val projections= arrayOf("ID","SaldoInicial","SaldoAtual","Descricao","DataAtu")
        val selectionArgs= arrayOf(filtro)
        val cursor=database.Query(projections,"SaldoAtual like ?",selectionArgs,"ID, DataAtu")

        ListGastos.clear()
        if(cursor.moveToFirst()){
            do{
                val ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val SaldoInicial=cursor.getInt(cursor.getColumnIndex("SaldoInicial"))
                val SaldoAtual=cursor.getInt(cursor.getColumnIndex("SaldoAtual"))
                val Descricao=cursor.getString(cursor.getColumnIndex("Descricao"))
                val DataAtu=cursor.getString(cursor.getColumnIndex("DataAtu"))

                ListGastos.add(Gasto(ID,SaldoInicial,SaldoAtual,Descricao,DataAtu))

            }while (cursor.moveToNext())
        }

        var gastosAdapter= GastosAdpater(this, ListGastos)
        lvListarGastos.adapter= gastosAdapter
    }

    inner class  GastosAdpater: BaseAdapter {
        var ListGastosAdpater= ArrayList<Gasto>()
        var context: Context?= null

        constructor(context: Context, listGastosAdpater:ArrayList<Gasto>):super(){
            this.ListGastosAdpater= listGastosAdpater
            this.context= context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var myView= layoutInflater.inflate(R.layout.detalhe_item,null)
            var myListGastos= ListGastosAdpater[p0]
            val format = NumberFormat.getCurrencyInstance()

            val saldoInicial = myListGastos.nodeSaldoInicial
            val saldoAtual = myListGastos.nodeSaldoAtual
            val valorOperacao = saldoAtual!!.minus(saldoInicial!!.toInt())

            var saldoInicialFormat = (saldoInicial)!!.toDouble()!!.div(100)
            var saldoAtualFormat = (saldoAtual)!!.toDouble()!!.div(100)
            var valorOperacaoForamt = (valorOperacao)!!.toDouble()!!.div(100)

            myView.tvID.text= myListGastos.nodeID.toString()
            myView.tvSaldoInicial.text= format.format(saldoInicialFormat).toString()
            myView.tvSaldoAtual.text= format.format(saldoAtualFormat).toString()
            myView.tvValor.text= format.format(valorOperacaoForamt).toString()
            myView.tvDescricao.text= myListGastos.nodeDescricao.toString()
            myView.tvDataAtu.text= myListGastos.nodeDataAtu.toString()


            if(p0 %2 == 1){
                myView.setBackgroundColor(Color.parseColor("#E5E5E5"))
            }
            else{
                myView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            myView.ivDelete.setOnClickListener( View.OnClickListener {
                var database=Database(this.context!!)
                val selectionArgs= arrayOf(myListGastos.nodeID.toString())

                val builder = AlertDialog.Builder(this@ListarActivity)
                builder.setTitle("Excluir Registro")
                builder.setMessage("Deseja excluir o registro?")
                builder.setPositiveButton("SIM"){dialog, which ->
                    database.Delete("ID=?",selectionArgs)
                    LoadQuery("%")
                    Toast.makeText(applicationContext,"Registro Excluido",Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("NÃO"){dialog,which ->
                    Toast.makeText(applicationContext,"Operação Negada",Toast.LENGTH_SHORT).show()
                }
                builder.setNeutralButton("Cancelar"){_,_ ->
                    Toast.makeText(applicationContext,"Operação Cancelada",Toast.LENGTH_SHORT).show()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            })
            return myView
        }

        override fun getItem(p0: Int): Any {
            return ListGastosAdpater[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return ListGastosAdpater.size
        }
    }
}
