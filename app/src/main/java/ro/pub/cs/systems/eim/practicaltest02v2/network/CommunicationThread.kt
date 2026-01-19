package ro.pub.cs.systems.eim.practicaltest02v2.network

import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02v2.general.Constants
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class CommunicationThread(private val serverThread: ServerThread, private val socket: Socket) : Thread() {

    override fun run() {
        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(socket.getOutputStream(), true)

            val request = reader.readLine()
            Log.i(Constants.TAG, "[COMM] Processing: $request")

            if (request.isNullOrEmpty()) {
                writer.println("Error: Empty request")
                return
            }

            val parts = request.split(",")
            if (parts.size != 3) {
                writer.println("Error: Invalid format")
                return
            }

            val operation = parts[0].trim()
            val op1 = parts[1].trim().toIntOrNull()
            val op2 = parts[2].trim().toIntOrNull()

            if (op1 == null || op2 == null) {
                writer.println("Error: Invalid operands")
                return
            }

            var result = ""

            when (operation) {
                "add" -> {
                    val sum = op1.toLong() + op2.toLong()
                    if (sum > Int.MAX_VALUE || sum < Int.MIN_VALUE) {
                        result = "overflow"
                    } else {
                        result = sum.toString()
                    }
                }
                "mul" -> {
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }

                    val prod = op1.toLong() * op2.toLong()
                    if (prod > Int.MAX_VALUE || prod < Int.MIN_VALUE) {
                        result = "overflow"
                    } else {
                        result = prod.toString()
                    }
                }
                else -> result = "Error: Unknown operation"
            }

            writer.println(result)

        } catch (e: Exception) {
            Log.e(Constants.TAG, "[COMM] Error: " + e.message)
        } finally {
            socket.close()
        }
    }
}