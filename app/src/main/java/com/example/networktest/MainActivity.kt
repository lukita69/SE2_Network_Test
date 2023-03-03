package com.example.networktest

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val port: Int = 53212;
    private lateinit var server: Server

    private lateinit var txtMatNr: EditText
    private lateinit var lblServerResponse: TextView
    private lateinit var btnSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        server = Server(port)
        server.start()

        txtMatNr = findViewById(R.id.txtMatrikelnummer)
        lblServerResponse = findViewById(R.id.lblServerAnswer)
        btnSend
    }
}