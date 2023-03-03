package com.example.networktest

import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int): Thread() {

    override fun run() {
        super.run()
        try {
            val serverSocket = ServerSocket(port)
            while (true) {
                val clientSocket = serverSocket.accept()
                val clientThread = ClientThread(clientSocket)
                clientThread.start()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    inner class ClientThread(private val clientSocket: Socket):Thread(){
        override fun run() {
            super.run()
        }
    }
}