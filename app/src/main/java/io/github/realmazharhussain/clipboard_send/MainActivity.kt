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

        val text = clipboardManager.primaryClip?.takeIf { it.itemCount > 0 }?.getItemAt(0)?.coerceToText(this)?.toString() ?: run {
            showToast("Clipboard is empty")
            return
        }

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
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