package com.govarthanan.baseflix

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

object BaseflixApi {
    fun getVideos(server: String): List<Video> {
        val base = server.trim().trimEnd('/')
        val connection = URL("$base/api/videos").openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 10000

        if (connection.responseCode !in 200..299) {
            throw Exception("Server returned HTTP ${connection.responseCode}")
        }

        val body = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val array = JSONArray(body)
        return buildList {
            for (i in 0 until array.length()) {
                val item = array.getJSONObject(i)
                add(
                    Video(
                        id = item.optLong("id", i.toLong()),
                        name = item.optString("name", "Untitled"),
                        folder = item.optString("folder", "Home"),
                        streamUrl = item.optString("stream_url", null)
                            ?: item.optString("streamUrl", null)
                    )
                )
            }
        }
    }
}
