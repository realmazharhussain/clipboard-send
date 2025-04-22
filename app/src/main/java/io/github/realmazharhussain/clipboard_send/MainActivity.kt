package io.github.realmazharhussain.clipboard_send

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = LinearLayout(this).apply {
            setBackgroundColor(Color.TRANSPARENT)
            post {
                onViewRendered()
                finish()
            }
        }
        setContentView(view)
    }

    private fun onViewRendered() {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager ?: run {
            showToast("Clipboard service not available")
            return
        }

        val clip = clipboardManager.primaryClip?.takeIf { it.itemCount > 0 } ?: run {
            showToast("Clipboard is empty")
            return
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            val item = clip.getItemAt(0)
            type = clip.description.getMimeType(0)
            if (type == "text/plain") {
                putExtra(Intent.EXTRA_TEXT, item.text)
            } else {
                putExtra(Intent.EXTRA_STREAM, item.uri)
            }
        }

        val chooser: Intent = Intent.createChooser(intent, "Share via")

        try {
            startActivity(chooser)
        } catch (_: ActivityNotFoundException) {
            showToast("An internal error occured!")
        }
    }

    private fun showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }
}