package com.example.networktest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.OutputStream
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val port: Int = 53212;
        try {
            ServerSocket(port).use { serverSocket ->
                System.out.println("Server is listening on port $port")
                while (true) {
                    val socket: Socket = serverSocket.accept()
                    println("New client connected")
                    val output: OutputStream = socket.getOutputStream()
                    val writer = PrintWriter(output, true)
                    writer.println(Date().toString())
                }
            }
        } catch (ex: IOException) {
            System.out.println("Server exception: " + ex.localizedMessage.toString())
            ex.printStackTrace()
        }
    }
}