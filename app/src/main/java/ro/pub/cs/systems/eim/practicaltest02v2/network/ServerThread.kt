package ro.pub.cs.systems.eim.practicaltest02v2.network

import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02v2.general.Constants
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class ServerThread(private val port: Int) : Thread() {
    var serverSocket: ServerSocket? = null

    init {
        try {
            serverSocket = ServerSocket(port)
        } catch (e: IOException) {
            Log.e(Constants.TAG, "Error creating server socket: " + e.message)
        }
    }

    override fun run() {
        while (!Thread.currentThread().isInterrupted) {
            try {
                if (serverSocket == null) break

                Log.i(Constants.TAG, "[SERVER] Waiting for connections...")
                val socket = serverSocket!!.accept()

                CommunicationThread(this, socket).start()

            } catch (e: IOException) {
                Log.e(Constants.TAG, "Error in server run: " + e.message)
            }
        }
    }

    fun stopThread() {
        interrupt()
        serverSocket?.close()
    }
}