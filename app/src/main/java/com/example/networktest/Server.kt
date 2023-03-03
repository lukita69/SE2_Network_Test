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
            val serverSocket = ServerSocket(port, 0, InetAddress.getByName("se2-isys.aau.at"))
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
                val ergebnis: Long = calcMatNr(matrikelNummer);

                outputStream.writeLong(ergebnis)
                outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                clientSocket.close();
            }
        }
    }

    private fun calcMatNr(matNr: Long): Long {

        return matNr;
    }
}