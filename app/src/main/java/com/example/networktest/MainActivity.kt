package com.example.networktest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.networktest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityMainBinding

    private val port: Int = 53212
    private val localDomainname: String = "localhost"
    private val globalDomainname: String = "se2-isys.aau.at"
    private lateinit var server: Server
    private lateinit var localClient: Client
    private lateinit var globalClient: Client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        server = Server(port)
        server.startServer()
        server.waitForConnection()

        localClient = Client(localDomainname, port)
        globalClient = Client(globalDomainname, port)


        bnd.btnSendGlobal.setOnClickListener {
            if (bnd.txtMatrikelnummer.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number!", Toast.LENGTH_SHORT).show()
            } else {
                val matNr: String = bnd.txtMatrikelnummer.text.toString()
                globalClient.sendRequest(matNr, object: Client.ResponseListener{
                    override fun onResponse(response: String) {
                        runOnUiThread {
                            bnd.lblServerAnswer.text = response
                        }
                    }
                })
            }
        }

        bnd.btnSendLocal.setOnClickListener {
            if (bnd.txtMatrikelnummer.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number!", Toast.LENGTH_SHORT).show()
            } else {
                val matNr: String = bnd.txtMatrikelnummer.text.toString()
                localClient.sendRequest(matNr, object: Client.ResponseListener{
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