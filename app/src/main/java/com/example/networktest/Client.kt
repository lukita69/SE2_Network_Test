package com.example.networktest

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket

class Client(private val domainname: String, private val port: Int) {

    interface ResponseListener {
        fun onResponse(response: String)
    }

    fun sendRequest(req: String, listener: ResponseListener) {
        Thread {
            try {
                val socket: Socket = Socket(
                    InetAddress.getByName(domainname), port
                )
                val outputStream: PrintWriter = PrintWriter(socket.getOutputStream(), true)
                val inputStream: BufferedReader =
                    BufferedReader(InputStreamReader(socket.getInputStream()))
                outputStream.println(req)
                val response = inputStream.readLine()
                listener.onResponse(response)
                socket.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}