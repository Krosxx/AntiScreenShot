package cn.vove7.antiscreenshot

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import de.robv.android.xposed.XSharedPreferences

@SuppressLint("WorldReadableFiles")
class MainActivity : AppCompatActivity() {

    val sv by lazy {
        findViewById<TextView>(R.id.status_tv)
    }
    val targetView by lazy {
        findViewById<EditText>(R.id.target_et)
    }

    val sp by lazy {
        this.getSharedPreferences(packageName, Context.MODE_WORLD_READABLE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val target = sp.getString("target", "com.chaoxing.mobile")!!

        targetView.setText(target)
        targetView.addTextChangedListener(afterTextChanged = {
            sp.edit().putString("target", it.toString()).apply()
        })

    }

    private val statusText
        get() = (if (enabled()) "模块已生效" else "模块未启用").let { s ->
            SpannableString(s).also {
                it.setSpan(
                    ForegroundColorSpan(if (enabled()) Color.GREEN else Color.RED),
                    0,
                    s.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(statusText)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        sv.text = statusText
    }

    private fun enabled() = false


}