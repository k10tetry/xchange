package com.k10tetry.xchange

import java.io.File
import java.io.IOException

object FileProvider {

    const val rootPath = "src/test/resources/"

    fun getJsonString(fileName: String): String? {
        return try {
            val bufferedReader = File(rootPath.plus(fileName)).inputStream().bufferedReader()
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()

            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            stringBuilder.toString()
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

}