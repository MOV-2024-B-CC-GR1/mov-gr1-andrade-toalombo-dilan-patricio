package com.example.a011_android

class BBaseDatosMemoria {
    companion object {
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1,"Dilan","a@a.com"))
            arregloBEntrenador.add(BEntrenador(2,"Andrade","b@b.com"))
            arregloBEntrenador.add(BEntrenador(3,"Patricio","c@c.com"))
        }
    }
}