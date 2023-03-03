package com.example.networktest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.networktest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityMainBinding

    private val port: Int = 53212
    private val domainname: String = "localhost"
    private lateinit var server: Server
    private lateinit var client: Client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        server = Server(port)
        server.startServer()
        server.waitForConnection()

        client = Client(domainname, port)

        bnd.btnSend.setOnClickListener {
            if (bnd.txtMatrikelnummer.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number!", Toast.LENGTH_SHORT).show()
            } else {
                val matNr: String = bnd.txtMatrikelnummer.text.toString()
                client.sendRequest(matNr, object: Client.ResponseListener{
                    override fun onResponse(response: String) {
                        runOnUiThread {
                            bnd.lblServerAnswer.text = response
                        }
                    }
                })
            }
        }
    }
}