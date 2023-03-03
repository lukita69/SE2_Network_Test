package com.example.networktest

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class Server(private val port: Int) : Thread() {

    override fun run() {
        super.run()
        try {
            val serverSocket = ServerSocket(port)
            println("Server started and listening to port $port")
            while (true) {
                val clientSocket = serverSocket.accept()
                val clientThread = ClientThread(clientSocket)
                clientThread.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*
    I want to use a custom name, which can be used to connect to the server, instead of using localhost. the custom name should be "banane". how to modify the ServerSocket() method call to achieve this behaviour?
     */

    inner class ClientThread(private val clientSocket: Socket) : Thread() {
        //val bufferSize: Int = 1024

        override fun run() {
            super.run()
            try {
                val inputStream: DataInputStream = DataInputStream(clientSocket.getInputStream())
                val outputStream: DataOutputStream =
                    DataOutputStream(clientSocket.getOutputStream())

                println("New client connected!")

                //val receivedBytes:ByteArray = ByteArray(bufferSize)

                val matrikelNummer: Long =
                    inputStream.readLong() //ByteBuffer.wrap(receivedBytes).long
                val ergebnis: String = calcMatNr(matrikelNummer);

                outputStream.writeUTF(ergebnis)
                outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                clientSocket.close();
            }
        }
    }

    private fun calcMatNr(matNr: Long): String {
        var res: String = ""
        for(i in matNr.toString().indices){
            if(i%2 != 0){
                res += matNr.toString()[i].code.toChar()
                continue
            }
            res += matNr.toString()[i]
        }
        return res;
    }
}