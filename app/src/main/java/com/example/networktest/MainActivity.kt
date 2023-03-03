package com.example.networktest

import io.reactivex.rxjava3.core.Observable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.networktest.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private lateinit var bnd: ActivityMainBinding

    private val port: Int = 53212
    private val domainname: String = "se2-isys.aau.at"
    private lateinit var server: Server
    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bnd = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bnd.root)

        server = Server(port)
        server.start()

        val responseObservable = Observable.create<String> { emitter ->
            try {
                socket = Socket(domainname, port)
                val inputStream: DataInputStream = DataInputStream(socket.getInputStream())
                val response: String = inputStream.readUTF()

                emitter.onNext(response)

                inputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            emitter.onComplete()
        }

        responseObservable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { res -> bnd.lblServerAnswer.text = res.toString() },
                { err -> err.printStackTrace() })

        bnd.btnSend.setOnClickListener {
            if (bnd.txtMatrikelnummer.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number!", Toast.LENGTH_SHORT).show()
            } else {
                socket = Socket(domainname, port)
                val matNr: Long = bnd.txtMatrikelnummer.text.toString().toLong()

                bnd.btnSend.isEnabled = false

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val outputStream: DataOutputStream =
                            DataOutputStream(socket.getOutputStream())

                        outputStream.writeLong(matNr)

                        outputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!socket.isClosed) {
            socket.close()
        }
    }
}