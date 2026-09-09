package com.govarthanan.baseflix

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var serverInput: EditText
    private lateinit var status: TextView
    private lateinit var list: LinearLayout
    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serverInput = findViewById(R.id.serverInput)
        status = findViewById(R.id.status)
        list = findViewById(R.id.videoList)
        val connect = findViewById<Button>(R.id.connectButton)

        serverInput.setText(getPreferences(MODE_PRIVATE).getString("server", "http://192.168.1.100:4000"))
        connect.setOnClickListener { connectToServer() }
    }

    private fun connectToServer() {
        val server = serverInput.text.toString().trim().trimEnd('/')
        if (server.isBlank()) return

        getPreferences(MODE_PRIVATE).edit().putString("server", server).apply()
        status.text = "Connecting..."
        list.removeAllViews()

        executor.execute {
            try {
                val videos = BaseflixApi.getVideos(server)
                runOnUiThread {
                    status.text = "${videos.size} videos"
                    videos.groupBy { it.folder.ifBlank { "Home" } }.forEach { (folder, items) ->
                        val heading = TextView(this).apply {
                            text = folder
                            textSize = 22f
                            setPadding(8, 24, 8, 12)
                        }
                        list.addView(heading)

                        items.forEach { video ->
                            val row = TextView(this).apply {
                                text = video.name
                                textSize = 18f
                                setPadding(16, 18, 16, 18)
                                isFocusable = true
                                isClickable = true
                                setOnClickListener {
                                    val url = video.streamUrl
                                    if (url.isNullOrBlank()) {
                                        status.text = "No stream_url for ${video.name}. We need to connect the player route next."
                                    } else {
                                        startActivity(Intent(this@MainActivity, PlayerActivity::class.java).apply {
                                            putExtra("stream_url", if (url.startsWith("http")) url else server + "/" + url.trimStart('/'))
                                            putExtra("title", video.name)
                                        })
                                    }
                                }
                            }
                            list.addView(row)
                        }
                    }
                }
            } catch (e: Exception) {
                runOnUiThread { status.text = "Connection failed: ${e.message}" }
            }
        }
    }

    override fun onDestroy() {
        executor.shutdownNow()
        super.onDestroy()
    }
}
