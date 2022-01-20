package com.unifin.meugasto

import java.util.*

class Gasto {
    var nodeID:Int?= null
    var nodeSaldoInicial:Int?= null
    var nodeSaldoAtual:Int?= null
    var nodeDescricao:String?= null
    var nodeDataAtu:String?= null


    constructor(ID:Int, SaldoInicial:Int, SaldoAtual:Int, Descricao:String, DataAtu:String){
        this.nodeID= ID
        this.nodeSaldoInicial= SaldoInicial
        this.nodeSaldoAtual= SaldoAtual
        this.nodeDescricao= Descricao
        this.nodeDataAtu= DataAtu
    }
}