package com.example.networktest

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CountDownLatch

class Server(private val port: Int) {

    private lateinit var serverSocket: ServerSocket
    private var isRunning = false
    private var conLatch: CountDownLatch = CountDownLatch(1)
    fun startServer() {
        if (isRunning) {
            println("Server already running and listening to ${serverSocket.inetAddress.hostAddress}:$port")
            return
        }
        Thread {
            try {
                serverSocket = ServerSocket(port, 0, InetAddress.getByName("192.168.200.2"))
                isRunning = true
                println("Server started and listening to ${serverSocket.inetAddress.hostAddress}:$port")
                conLatch.countDown()
                while (isRunning) {
                    val clientSocket = serverSocket.accept()
                    ClientThread(clientSocket).start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stopServer()
            }
        }.start()
    }

    fun stopServer() {
        if (isRunning) {
            isRunning = false
            serverSocket.close()
            println("Server closed...")
        }
    }

    private inner class ClientThread(private val clientSocket: Socket) : Thread() {
        //val bufferSize: Int = 1024

        override fun run() {
            super.run()
            clientSocket.let { socket ->
                try {
                    val inputStream: BufferedReader =
                        BufferedReader(InputStreamReader(socket.getInputStream()))
                    val outputStream: PrintWriter = PrintWriter(socket.getOutputStream(), true)
                    val clientAddr: String? = socket.inetAddress.hostAddress

                    println("Client $clientAddr connected to server")

                    while (!socket.isClosed) {
                        try {
                            val req: String = inputStream.readLine()
                            println("Received request from client $clientAddr: $req")
                            val result: String = calcMatNr(req)
                            outputStream.println(result)
                            println("Sent result to client $clientAddr: $result")
                        } catch (e: java.lang.NullPointerException) {
                            break
                        }
                    }
                    //val receivedBytes:ByteArray = ByteArray(bufferSize)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    clientSocket.close()
                    println("Client ${socket.inetAddress.hostAddress} disconnected from server..")
                }
            }
        }
    }

    fun waitForConnection() {
        try {
            conLatch.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun calcMatNr(matNr: String): String {
        var res: String = ""
        for (i in matNr.indices) {
            if (i % 2 != 0 && matNr[i].code != 48) {
                res += (matNr[i] - 49) + 97
                continue
            }
            res += matNr[i]
        }
        return res
    }
}