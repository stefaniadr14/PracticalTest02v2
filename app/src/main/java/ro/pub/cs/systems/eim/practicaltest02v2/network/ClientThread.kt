package ro.pub.cs.systems.eim.practicaltest02v2.network

import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ClientThread(
    private val address: String,
    private val port: Int,
    private val command: String,
    private val resultTextView: TextView
) : Thread() {

    override fun run() {
        try {
            val socket = Socket(address, port)
            val writer = PrintWriter(socket.getOutputStream(), true)
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            writer.println(command)

            val response = reader.readLine()

            if (response != null) {
                resultTextView.post {
                    resultTextView.text = response
                }
            }
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
            resultTextView.post {
                resultTextView.text = "Error: connection failed"
            }
        }
    }
}